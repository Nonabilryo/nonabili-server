package nonabili.nonabiliserver.auth.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
