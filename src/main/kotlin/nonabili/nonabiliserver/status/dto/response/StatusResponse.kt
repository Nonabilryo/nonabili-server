package nonabili.nonabiliserver.status.dto.response

import java.util.Date
import java.util.UUID

data class StatusResponse(
        val idx: UUID,
        val imageUrls: List<String>,
        val videoUrl: String,
        val articleIdx: UUID,
        val title: String,
        val description: String,
        val createdAt: Date

)
