package nonabili.nonabiliserver.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[a-zA-Z0-9]{6,25}")
    val id: String,
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[a-zA-Z0-9!@#\$%]{6,25}")
    val password: String
)
