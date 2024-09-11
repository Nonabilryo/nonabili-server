package nonabili.nonabiliserver.service

import nonabili.nonabiliserver.common.repository.ArticleRepository
import nonabili.nonabiliserver.common.repository.UserRepository
import nonabili.nonabiliserver.dto.request.StatusPostRequest
import nonabili.nonabiliserver.entity.Status
import nonabili.nonabiliserver.repository.StatusRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import nonabili.nonabiliserver.status.dto.response.StatusResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class StatusService(
        val statusRepository: StatusRepository,
        val statusS3UploadService: StatusS3UploadService,
        val articleRepository: ArticleRepository,
        val userRepository: UserRepository
) {
    fun postStatus(articleIdx: String, images: List<MultipartFile>, video: MultipartFile?, request: StatusPostRequest, userIdx: String) {
        if (images.isEmpty() && video == null) throw CustomError(ErrorState.EMPTY_REQUEST)
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        val writerIdx = article.writer.toString()
        if (userIdx != writerIdx) throw CustomError(ErrorState.DIFFERENT_USER)
        try {
            statusRepository.save(
                Status(
                    imageUrls = images.map { statusS3UploadService.saveFileAndGetUrl(it, "status_images") },
                    videoUrl = video?.let { statusS3UploadService.saveFileAndGetUrl(video, "status_video")} ?: "",
                    article = article,
                    title = request.title,
                    description = request.description
                )
            )
        } catch (e: Exception) {
            throw CustomError(ErrorState.CANT_SAVE)
        }
    }
    fun getStatus(articleIdx: String, page: Int): Page<StatusResponse> {
        val article = articleRepository.findArticleByIdx(UUID.fromString(articleIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_ARTICLE)
        return statusRepository.findStatusesByArticle(article, PageRequest.of(page, 10)).map { StatusResponse(
                idx = it.idx,
                imageUrls = it.imageUrls,
                videoUrl = it.videoUrl,
                articleIdx = it.article.idx,
                title = it.title,
                description = it.description,
                createdAt = it.createdAt
        ) }
    }
    fun deleteStatus(statusIdx: String, userIdx: String) {
        val user = userRepository.findUserByIdx(UUID.fromString(userIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_USER)
        val status = statusRepository.findStatusByIdx(UUID.fromString(statusIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_STATUS)
        if (!user.equals(status.article.writer)) throw CustomError(ErrorState.DIFFERENT_USER)
        statusRepository.delete(status)
    }
}