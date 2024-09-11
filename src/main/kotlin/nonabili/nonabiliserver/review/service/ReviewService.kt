package nonabili.nonabiliserver.review.service

import nonabili.nonabiliserver.common.repository.ArticleRepository
import nonabili.nonabiliserver.common.repository.UserRepository
import nonabili.nonabiliserver.review.dto.request.ReviewPostRequest
import nonabili.nonabiliserver.review.dto.response.AvgRatingResponse
import nonabili.nonabiliserver.review.entity.Review
import nonabili.nonabiliserver.review.repository.ReviewRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import nonabili.nonabiliserver.review.dto.response.ReviewResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ReviewService(
        val reviewRepository: ReviewRepository,
        val reviewS3UploadService: ReviewS3UploadService,
        val userRepository: UserRepository,
        val articleRepository: ArticleRepository
) {
    fun postReview(articleIdx: String, images: List<MultipartFile>, request: ReviewPostRequest, userIdx: String) { // todo order와 연계
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        reviewRepository.save(
            Review(
                writer = user,
                article = article,
                rating = request.rating,
                title = request.title,
                description = request.description,
                imageUrls = images.map { reviewS3UploadService.saveFileAndGetUrl(it, "review_images") }
            )
        )
    }
    fun getReview(articleIdx: String, page: Int): Page<ReviewResponse> {
        return reviewRepository.findReviewsByArticle(UUID.fromString(articleIdx), PageRequest.of(page, 30)).map { ReviewResponse(
                idx = it.idx,
                writerIdx = it.writer.idx,
                articleIdx = it.article.idx,
                rating = it.rating,
                title = it.title,
                description = it.description,
                imageUrls = it.imageUrls
        ) }
    }
    fun getAvgRating(articleIdx: String): AvgRatingResponse {
        return AvgRatingResponse(
            rating = reviewRepository.findAverageRatingByArticle(UUID.fromString(articleIdx))
        )
    }
    fun deleteReview(reviewIdx: String, userIdx: String) {
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val review = reviewRepository.findReviewByIdx(UUID.fromString(reviewIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_REVIEW)
        if (!review.writer.equals(user)) throw CustomError(ErrorState.DIFFERENT_USER)
        reviewRepository.delete(review)
    }
}