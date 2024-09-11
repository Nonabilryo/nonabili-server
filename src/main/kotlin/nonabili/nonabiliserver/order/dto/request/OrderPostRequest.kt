package nonabili.nonabiliserver.dto.request

import org.hibernate.validator.constraints.UUID

data class OrderPostRequest(
    val comment: String,
    val rentalType: Int,
    val period: Long,
)