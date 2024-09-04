package nonabili.nonabiliserver.article.dto.request

import org.springframework.web.multipart.MultipartFile

data class ArticlePostRequest( //todo validation
    val title: String,
    val category: String? = null,
    val description: String,
    val price: Long,
    val rentalType: Int,
    val images: List<MultipartFile>
)
