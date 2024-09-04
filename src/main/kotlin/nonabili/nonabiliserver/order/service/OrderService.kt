package nonabili.nonabiliserver.service

import com.siot.IamportRestClient.IamportClient
import com.siot.IamportRestClient.request.CancelData
import com.siot.IamportRestClient.request.PrepareData
import nonabili.nonabiliserver.common.repository.ArticleRepository
import nonabili.nonabiliserver.dto.request.AcceptPostRequest
import nonabili.nonabiliserver.dto.request.OrderPostRequest
import nonabili.nonabiliserver.dto.request.PayPostRequest
import nonabili.nonabiliserver.entity.Order
import nonabili.nonabiliserver.entity.OrderState
import nonabili.nonabiliserver.entity.RentalType
import nonabili.nonabiliserver.repository.OrderRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class OrderService(val orderRepository: OrderRepository,
    @Value("\${portOne.apiKey}") val apiKey: String,
    @Value("\${portOne.secretKey}") val secretKey: String,
    @Value("\${portOne.impCode}") val impCode: String,
        val articleRepository: ArticleRepository
) {
    fun postOrder(userIdx: String, request: OrderPostRequest) { //todo article check
        val order = Order(
            article = UUID.fromString(request.article),
            user = UUID.fromString(userIdx),
            comment = request.comment,
            rentalType = RentalType.fromInt(request.rentalType)!!,
            period = request.period
        )
        orderRepository.save(order)
        val prepareData = PrepareData(order.idx.toString(), BigDecimal(order.period))
        IamportClient(apiKey, secretKey).postPrepare(prepareData)
    }
    fun postPayOrder(userIdx: String, request: PayPostRequest) {
        val order = orderRepository.findOrderByIdx(UUID.fromString(request.order)) ?: throw CustomError(ErrorState.NOT_FOUND_ORDER)
        if (order.user.toString() != userIdx) throw CustomError(ErrorState.DIFFERENT_USER)
        val payment = IamportClient(apiKey, secretKey).paymentByImpUid(order.idx.toString())
        try {
            if (payment.code != 0) throw Error()
            if (payment.response.status != "paid") throw Error()
            if (payment.response.amount.toLong() != order.period)  throw Error()
            if (payment.response.currency != "KRW") throw Error()
//            if (payment.response.vbankNum != order.product.shop.seller.bankNum) throw Error()
        } catch (e: Error) {
            paymentCancel(payment.response.impUid)
            orderRepository.save(order.copy(state = OrderState.CANCEL))
            throw CustomError(ErrorState.UNAUTHENTICATED_PAYMENT)
        }

        orderRepository.save(order.copy(state = OrderState.PAID))
    }
    fun getOrders(userIdx: String, articleIdx: String, page: Int): Page<Order> {
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        if (article.writer.toString() != userIdx) throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        return orderRepository.findOrdersByArticle(UUID.fromString(articleIdx), PageRequest.of(page, 25))
    }
    fun deleteOrder(userIdx: String, orderIdx: String) {
        val order = orderRepository.findOrderByIdx(UUID.fromString(orderIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ORDER)
        if (order.user.toString() != userIdx) throw CustomError(ErrorState.DIFFERENT_USER)
        try {
            val payment = IamportClient(apiKey, secretKey).paymentByImpUid(order.idx.toString())
            paymentCancel(payment.response.impUid)
        } catch (e: Error) {}
        orderRepository.save(order.copy(state = OrderState.CANCEL))
    }
    fun postAcceptOrder(userIdx: String, request: AcceptPostRequest) {
        val order = orderRepository.findOrderByIdx(UUID.fromString(request.order)) ?: throw CustomError(ErrorState.NOT_FOUND_ORDER)
        val article = articleRepository.findArticleByIdx(order.article) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        if (article.writer.toString() != userIdx) throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        val calendar = Calendar.getInstance()
        when (order.rentalType.value) {
            0 -> calendar.add(Calendar.YEAR, order.period as Int)
            1 -> calendar.add(Calendar.MONTH, order.period as Int)
            2 -> calendar.add(Calendar.DAY_OF_MONTH, order.period as Int)
            3 -> calendar.add(Calendar.HOUR, order.period as Int)
        }
        orderRepository.save(order.copy(state = OrderState.USING, paidAt = Date(), closedAt = calendar.time))
    }
    fun paymentCancel(impUid: String) {
        val data = CancelData(impUid, true)
        IamportClient(apiKey, secretKey).cancelPaymentByImpUid(data)
    }
}