package nonabili.nonabiliserver.review.dto.response

import java.util.UUID

data class ReviewResponse(
        val idx: UUID,
        val writerIdx: UUID,
        val articleIdx: UUID,
        val rating: Float,
        val title: String,
        val description: String,
        val imageUrls: List<String>
)
