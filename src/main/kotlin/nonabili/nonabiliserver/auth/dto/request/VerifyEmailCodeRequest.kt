package nonabili.nonabiliserver.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class VerifyEmailCodeRequest (
    @field:NotNull
    @field:NotBlank
    val emailVerifyCode: String
)