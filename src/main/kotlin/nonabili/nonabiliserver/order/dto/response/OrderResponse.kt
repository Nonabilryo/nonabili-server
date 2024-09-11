package nonabili.nonabiliserver.order.dto.response

import java.util.Date
import java.util.UUID

data class OrderResponse(
        val idx: UUID,
        val userIdx: UUID,
        val articleIdx: UUID,
        val state: String,
        val comment: String,
        val rentalType: String,
        val period: Long,
        val paidAt: Date?,
        val closedAt: Date?,
        val createdAt: Date
)
