package nonabili.nonabiliserver.review.controller

import jakarta.validation.Valid
import nonabili.nonabiliserver.review.dto.request.ReviewPostRequest
import nonabili.nonabiliserver.review.service.ReviewService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/review")
class ReviewController(val reviewService: ReviewService) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun postReview(
            principal: Principal,
            @RequestParam(required = true) articleIdx: String,
            @RequestParam(required = false) images: List<MultipartFile>,
            @Valid request: ReviewPostRequest,
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        reviewService.postReview(articleIdx, images, request, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @GetMapping()
    fun getReivew(
        @RequestParam(required = true) articleIdx: String,
        @RequestParam(required = false, defaultValue = "0", value = "page",) page: Int
    ): ResponseEntity<ResponseFormat<Any>> {
        val result = reviewService.getReview(articleIdx, page)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @GetMapping("/avgRating")
    fun getAvgRating(
        @RequestParam(required = true) articleIdx: String,
        @RequestParam(required = false, defaultValue = "0", value = "page",) page: Int
    ): ResponseEntity<ResponseFormat<Any>> {
        val result = reviewService.getAvgRating(articleIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @DeleteMapping
    fun deleteReivew(
        principal: Principal,
        @RequestParam(required = true) reviewIdx: String
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        reviewService.deleteReview(reviewIdx, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
}