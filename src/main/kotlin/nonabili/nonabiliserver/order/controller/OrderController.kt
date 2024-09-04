package nonabili.nonabiliserver.controller

import jakarta.validation.Valid
import nonabili.nonabiliserver.dto.request.AcceptPostRequest
import nonabili.nonabiliserver.dto.request.OrderPostRequest
import nonabili.nonabiliserver.dto.request.PayPostRequest
import nonabili.nonabiliserver.service.OrderService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/order")
class OrderController(val orderService: OrderService) {
    @PostMapping()
    fun postOrder(principal: Principal, @Valid request: OrderPostRequest): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        orderService.postOrder(userIdx, request)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @PostMapping("/pay")
    fun postPayOrder(principal: Principal, @Valid request: PayPostRequest): ResponseEntity<ResponseFormat<Any>> {
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
    ): ResponseEntity<ResponseFormat<Any>> {
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