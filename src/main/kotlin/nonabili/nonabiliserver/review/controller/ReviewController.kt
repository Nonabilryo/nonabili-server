package nonabili.nonabiliserver.review.controller

import jakarta.validation.Valid
import nonabili.nonabiliserver.review.dto.request.ReviewPostRequest
import nonabili.nonabiliserver.review.service.ReviewService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import nonabili.nonabiliserver.review.dto.response.AvgRatingResponse
import nonabili.nonabiliserver.review.dto.response.ReviewResponse
import nonabili.nonabiliserver.review.entity.Review
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/article/{articleIdx}/review")
class ReviewController(val reviewService: ReviewService) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun postReview(
            principal: Principal,
            @PathVariable articleIdx: String,
            @RequestParam(required = false) images: List<MultipartFile>,
            @Valid request: ReviewPostRequest,
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        reviewService.postReview(articleIdx, images, request, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @GetMapping("/{page}")
    fun getReivew(
        @PathVariable articleIdx: String,
        @PathVariable page: Int
    ): ResponseEntity<ResponseFormat<Page<ReviewResponse>>> {
        val result = reviewService.getReview(articleIdx, page)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @GetMapping("/avgRating")
    fun getAvgRating(
        @PathVariable articleIdx: String,
    ): ResponseEntity<ResponseFormat<AvgRatingResponse>> {
        val result = reviewService.getAvgRating(articleIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @DeleteMapping("/{reviewIdx}")
    fun deleteReivew(
        principal: Principal,
        @PathVariable reviewIdx: String
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        reviewService.deleteReview(reviewIdx, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
}