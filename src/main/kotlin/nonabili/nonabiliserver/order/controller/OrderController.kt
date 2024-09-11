package nonabili.nonabiliserver.controller

import jakarta.validation.Valid
import nonabili.nonabiliserver.dto.request.AcceptPostRequest
import nonabili.nonabiliserver.dto.request.OrderPostRequest
import nonabili.nonabiliserver.dto.request.PayPostRequest
import nonabili.nonabiliserver.service.OrderService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import nonabili.nonabiliserver.order.dto.response.OrderResponse
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/article/{articleIdx}/order")
class OrderController(val orderService: OrderService) {
    @PostMapping()
    fun postOrder(
            principal: Principal,
            @PathVariable articleIdx: String,
            @Valid request: OrderPostRequest
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        orderService.postOrder(userIdx, request, articleIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/pay")
    fun postPayOrder(
            principal: Principal,
            @PathVariable articleIdx: String,
            @Valid request: PayPostRequest
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        orderService.postPayOrder(userIdx, request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/accept")
    fun postAcceptOrder(principal: Principal, @Valid acceptPostRequest: AcceptPostRequest): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        orderService.postAcceptOrder(userIdx, acceptPostRequest)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @GetMapping()
    fun getOrders(
            principal: Principal,
            @RequestParam(name = "articleIdx") articleIdx: String,
            @RequestParam(name = "page") page: Int
    ): ResponseEntity<ResponseFormat<Page<OrderResponse>>> {
        val userIdx = principal.name
        val result = orderService.getOrders(userIdx, articleIdx, page)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success"}.build(result))
    }
    @DeleteMapping
    fun deleteOrder(
            principal: Principal,
            @RequestParam(name = "orderIdx") orderIdx: String
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        orderService.deleteOrder(userIdx, orderIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success"}.noData())
    }
}