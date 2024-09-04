package nonabili.nonabiliserver.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class VerifyEmailRequest(
    @field:NotNull
    @field:NotBlank
    @field:Email
    val email: String
)
