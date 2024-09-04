package nonabili.nonabiliserver.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import nonabili.nonabiliserver.repository.StatusRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class StatusS3UploadService(@Value("\${cloud.aws.s3.bucket}") val bucket: String, val amazonS3: AmazonS3, val statusRepository: StatusRepository) {
//    fun saveStatus(images: List<MultipartFile>, video: MultipartFile?, articleIdx: String){
//        try {
//            statusRepository.save(
//                Status(
//                    imageUrls = images.map { saveFileAndGetUrl(it, "status_images") },
//                    videoUrl = video?.let {saveFileAndGetUrl(video, "status_video")} ?: "",
//                    article = UUID.fromString(articleIdx),
//                )
//            )
//        } catch (e: Exception) {
//            throw CustomError(ErrorState.CANT_SAVE)
//        }
//    }
    fun saveFileAndGetUrl(image: MultipartFile, directory: String): String {  // return url
        val metadata = ObjectMetadata()
        metadata.contentLength = image.size
        metadata.contentType = image.contentType

        val uuid = UUID.randomUUID()

        val key = "${directory}/${uuid}"

        image.inputStream.use { amazonS3.putObject(bucket, key, it, metadata) }

        return amazonS3.getUrl(bucket, key).toString()
    }
}