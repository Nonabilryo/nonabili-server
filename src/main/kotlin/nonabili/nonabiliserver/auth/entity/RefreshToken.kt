package nonabili.nonabiliserver.auth.entity

import jakarta.persistence.*
import nonabili.nonabiliserver.user.entity.User

@Entity
data class RefreshToken(
    @Id
    val token: String,
    @OneToOne
    @JoinColumn(name = "user_idx")
    val user: User
)
