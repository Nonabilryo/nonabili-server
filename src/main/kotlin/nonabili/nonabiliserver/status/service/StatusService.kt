package nonabili.nonabiliserver.service

import nonabili.nonabiliserver.common.repository.ArticleRepository
import nonabili.nonabiliserver.dto.request.StatusPostRequest
import nonabili.nonabiliserver.entity.Status
import nonabili.nonabiliserver.repository.StatusRepository
import nonabili.nonabiliserver.common.util.error.CustomError
import nonabili.nonabiliserver.common.util.error.ErrorState
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class StatusService(val statusRepository: StatusRepository, val statusS3UploadService: StatusS3UploadService, val articleRepository: ArticleRepository) {
    fun postStatus(images: List<MultipartFile>, video: MultipartFile?, request: StatusPostRequest, userIdx: String) {
        if (images.isEmpty() && video == null) throw CustomError(ErrorState.EMPTY_REQUEST)
//        val userIdx = userClient.getUserIdxById(userId).idx ?: throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        val article = articleRepository.findArticleByIdx(UUID.fromString(request.articleIdx)) ?: throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        val writerIdx = article.writer.toString()
        if (userIdx != writerIdx) throw CustomError(ErrorState.DIFFERENT_USER)
        try {
            statusRepository.save(
                Status(
                    imageUrls = images.map { statusS3UploadService.saveFileAndGetUrl(it, "status_images") },
                    videoUrl = video?.let { statusS3UploadService.saveFileAndGetUrl(video, "status_video")} ?: "",
                    article = UUID.fromString(request.articleIdx),
                    title = request.title,
                    description = request.description
                )
            )
        } catch (e: Exception) {
            throw CustomError(ErrorState.CANT_SAVE)
        }
    }
    fun getStatus(articleIdx: String, page: Int): Page<Status> {
        return statusRepository.findStatusesByArticle(UUID.fromString(articleIdx), PageRequest.of(page, 10))
    }
    fun deleteStatus(statusIdx: String, userIdx: String) {
        val status = statusRepository.findStatusByIdx(UUID.fromString(statusIdx)) ?: throw CustomError(ErrorState.NOT_FOUND_STATUS)
//        val userIdx = userClient.getUserIdxById(userId).idx ?: throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        val article = articleRepository.findArticleByIdx(status.article) ?: throw CustomError(ErrorState.SERVER_UNAVAILABLE)
        val writerIdx = article.writer.toString()
        if (userIdx != writerIdx) throw CustomError(ErrorState.DIFFERENT_USER)
        statusRepository.delete(status)
    }
}