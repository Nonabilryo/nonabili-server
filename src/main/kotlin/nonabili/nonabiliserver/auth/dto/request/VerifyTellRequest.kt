package nonabili.nonabiliserver.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class VerifyTellRequest(
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[0-9]{11}")
    val tell: String
)
