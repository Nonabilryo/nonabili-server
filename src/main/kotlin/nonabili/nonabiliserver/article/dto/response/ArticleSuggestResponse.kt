package nonabili.nonabiliserver.article.dto.response

import nonabili.nonabiliserver.common.entity.Image
import nonabili.nonabiliserver.article.entity.RentalType
import java.util.Date

data class ArticleSuggestResponse(
        val idx: String,
        val title: String,
        val price: Long,
        val rentalType: String,
        val image: Image,
        val createdAt: Date,
)
