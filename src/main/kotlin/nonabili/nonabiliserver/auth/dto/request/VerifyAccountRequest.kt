package nonabili.nonabiliserver.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class VerifyAccountRequest(
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[가-힣a-zA-Z0-9]{2,12}")
    val name: String,
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[a-zA-Z0-9]{6,25}")
    val id: String,
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[a-zA-Z0-9!@#$%]{6,25}")
    val password: String,
    @field:NotNull
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotNull
    @field:NotBlank
    @field:Pattern(regexp = "[0-9]{11}")
    val tell: String,
    @field:Pattern(regexp = "[가-힣\\s\\-]{6,25}")
    val adress: String?,
    @field:NotNull
    @field:NotBlank
    val tellVerifyCode: String
)
