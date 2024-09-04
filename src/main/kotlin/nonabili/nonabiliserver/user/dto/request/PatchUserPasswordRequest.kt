package nonabili.nonabiliserver.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class PatchUserPasswordRequest(
        @field:NotNull
        @field:NotBlank
        @field:Pattern(regexp = "[a-zA-Z0-9!@#\$%]{6,25}")
        val passwordNow: String,
        @field:NotNull
        @field:NotBlank
        @field:Pattern(regexp = "[a-zA-Z0-9!@#\$%]{6,25}")
        val passwordAfter: String
)
