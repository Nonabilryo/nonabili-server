package nonabili.nonabiliserver.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class VerifyTellCodeRequest(
    @field:NotNull
    @field:NotBlank
    val tellVerifyCode: String
)
