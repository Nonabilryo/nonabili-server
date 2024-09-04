package nonabili.nonabiliserver.common.service
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import nonabili.nonabiliserver.common.entity.Image
import nonabili.nonabiliserver.common.repository.ImageRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class S3UploadService(@Value("\${cloud.aws.s3.bucket}") val bucket: String, val amazonS3: AmazonS3, val imageRepository: ImageRepository) {
    val log = LoggerFactory.getLogger(javaClass)
    fun saveImage(image: MultipartFile, directory: String): Image {
        val metadata = ObjectMetadata()
        metadata.contentLength = image.size
        metadata.contentType = image.contentType

        val uuid = UUID.randomUUID()

        val key = "${directory}/${uuid}"

        image.inputStream.use { amazonS3.putObject(bucket, key, it, metadata) }

        log.warn(amazonS3.getUrl(bucket, key).toString())
        return imageRepository.save(Image(
            uuid,
            amazonS3.getUrl(bucket, key).toString()
        ))
    }
    fun saveImages(images: List<MultipartFile>, directory: String): List<Image> {
        return images.map {
            saveImage(it, directory)
        }
    }
}