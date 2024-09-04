package nonabili.nonabiliserver.review.service

import nonabili.nonabiliserver.review.dto.request.ReviewPostRequest
import nonabili.nonabiliserver.review.dto.response.AvgRatingResponse
import nonabili.nonabiliserver.review.entity.Review
import nonabili.nonabiliserver.review.repository.ReviewRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ReviewService(val reviewRepository: ReviewRepository, val reviewS3UploadService: ReviewS3UploadService) {
    fun postReview(articleIdx: String, images: List<MultipartFile>, request: ReviewPostRequest, userIdx: String) { // todo order와 연계
//        val userIdx = userClient.getUserIdxById(userId).idx ?: throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        reviewRepository.save(
            Review(
                writer = UUID.fromString(userIdx),
                article = UUID.fromString(articleIdx),
                rating = request.rating,
                title = request.title,
                description = request.description,
                imageUrls = images.map { reviewS3UploadService.saveFileAndGetUrl(it, "review_images") }
            )
        )
    }
    fun getReview(articleIdx: String, page: Int): Page<Review> {
        return reviewRepository.findReviewsByArticle(UUID.fromString(articleIdx), PageRequest.of(page, 30))
    }
    fun getAvgRating(articleIdx: String): AvgRatingResponse {
        return AvgRatingResponse(
            rating = reviewRepository.findAverageRatingByArticle(UUID.fromString(articleIdx))
        )
    }
    fun deleteReview(reviewIdx: String, userIdx: String) {
        val review = reviewRepository.findReviewByIdx(UUID.fromString(reviewIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_REVIEW)
        if (review.writer != UUID.fromString(userIdx)) throw CustomError(ErrorState.DIFFERENT_USER)
        reviewRepository.delete(review)
    }
}