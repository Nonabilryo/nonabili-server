package nonabili.nonabiliserver.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class DeleteUserRequest(
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[a-zA-Z0-9!@#\$%]{6,25}")
    val password: String
)
