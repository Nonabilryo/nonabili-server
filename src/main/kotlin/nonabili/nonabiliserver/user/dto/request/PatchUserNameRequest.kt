package nonabili.nonabiliserver.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class PatchUserNameRequest(
        @field:NotNull
        @field:NotBlank
        @field:Pattern(regexp = "[가-힣a-zA-Z0-9]{2,12}")
        val name: String
)
