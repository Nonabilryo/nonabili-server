package nonabili.nonabiliserver.user.dto.response

import java.util.Date

data class UserInfoResponse(
    val idx: String,
    val name: String,
    val imageUrl: String?,
    val description: String,
    val signed: Date
)
