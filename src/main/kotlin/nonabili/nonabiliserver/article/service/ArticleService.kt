package nonabili.nonabiliserver.article.service

import nonabili.nonabiliserver.article.dto.request.ArticlePostRequest
import nonabili.nonabiliserver.article.dto.response.ArticleInfoResponse
import nonabili.nonabiliserver.article.dto.response.ArticleSuggestResponse
import nonabili.nonabiliserver.article.dto.response.LikedResponse
import nonabili.nonabiliserver.article.entity.Article
import nonabili.nonabiliserver.article.entity.Category
import nonabili.nonabiliserver.article.entity.Like
import nonabili.nonabiliserver.article.entity.RentalType
import nonabili.nonabiliserver.common.repository.ArticleRepository
import nonabili.nonabiliserver.article.repository.CategoryRepository
import nonabili.nonabiliserver.article.repository.LikeRepository
import nonabili.nonabiliserver.common.repository.UserRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import nonabili.nonabiliserver.common.service.S3UploadService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.Calendar
import java.util.UUID

@Service
class ArticleService(
        val articleRepository: ArticleRepository,
        val categoryRepository: CategoryRepository,
        val s3UploadService: S3UploadService,
        val likeRepository: LikeRepository,
        val userRepository: UserRepository
) {
    fun getSuggestArticle(page: Int): Page<ArticleSuggestResponse> {
        val cal = Calendar.getInstance()

//        cal.add(Calendar.DAY_OF_MONTH, -200)
//        val articles = articleRepository.findArticlesByCreatedAtAfterOrderByLikeDesc(createdAt = cal.time, PageRequest.of(page, 25))

//        val articles = articleRepository.findArticlesByCreatedAtAfterOrderByLikeDesc2(PageRequest.of(page, 25))

        val articles = articleRepository.findArticlesByCreatedAtAfterOrderByLikeDesc3(PageRequest.of(page, 25))


        return PageImpl(articles.content.map { ArticleSuggestResponse(
            idx = it.idx.toString(),
            title = it.title,
            price = it.price,
            rentalType = it.rentalType.toString(),
            image = it.images[0],
            createdAt = it.createdAt
        ) }, articles.pageable, articles.totalElements)
    }
    fun postArticle(request: ArticlePostRequest, userIdx: String) {  // todo validation
//        val userIdx = userClient.getUserIdxById(userId).idx
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val category = request.category?.let {
            categoryRepository.findCategoryByName(request.category) ?: categoryRepository.save(
                Category(
                    name = request.category
                )
            )
        }
        articleRepository.save(
            Article(
                title = request.title,
                writer = user,
                category = category,
                description = request.description,
                price = request.price,
                rentalType = RentalType.fromInt(request.rentalType) ?: throw CustomError(ErrorState.ILLEGAL_RENTALTYPE),
                images = s3UploadService.saveImages(request.images, "article_images")
            )
        )
    }
    fun getArticleInfo(articleIdx: String): ArticleInfoResponse {
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        return ArticleInfoResponse(
            title = article.title,
            images = article.images,
//            writer = userClient.getUserInfoByIdx(article.writer.toString()).userName ?: throw CustomError(ErrorState.ILLEGAL_ARTICLE),
            writer = article.writer.toString(),
            category = article.category?.name ?: "",
            description = article.description,
            price = article.price,
            rentalType = article.rentalType.toString(),
            createdAt = article.createdAt,
            like = article.like.size
        )
    }
    fun deleteArticle(articleIdx: String, userIdx: String) {
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        if (!article.writer.equals(user)) throw CustomError(ErrorState.DIFFERENT_USER)
        articleRepository.delete(article)
    }

    fun getLiked(articleIdx: String, userIdx: String): LikedResponse {
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        return LikedResponse(liked = likeRepository.existsLikeByUserAndArticle(UUID.fromString(userIdx), article))
    }
    fun postLike(articleIdx: String, userIdx: String) {
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
//        val userIdx = userClient.getUserIdxById(userId).idx
        likeRepository.findLikeByUserAndArticle(UUID.fromString(userIdx), article)?.let { throw CustomError(ErrorState.AREADY_LIKED) }
        val like = Like(user = UUID.fromString(userIdx), article = article)
        likeRepository.save(like)
    }

    fun deleteLike(articleIdx: String, userIdx: String) {
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
//        val userIdx = userClient.getUserIdxById(userId).idx
        val like = likeRepository.findLikeByUserAndArticle(UUID.fromString(userIdx), article)
        if (like == null) throw CustomError(ErrorState.NOT_FOUND_LIKE)
        likeRepository.delete(like)
    }

    fun getWriterIdxByArticleIdx(articleIdx: String): String {
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        return article.writer.toString()
    }
}