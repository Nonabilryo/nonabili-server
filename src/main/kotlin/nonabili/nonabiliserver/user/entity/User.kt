package nonabili.nonabiliserver.user.entity

import jakarta.persistence.*
import nonabili.nonabiliserver.common.entity.Image
import nonabili.nonabiliserver.auth.entity.Role
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "user")
data class User(
        @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idx", unique = true, nullable = false, length = 255)
    val idx: UUID = UUID.randomUUID(),
        @Column(name = "name", unique = true, nullable = false, length = 255)
    val name: String,
        @Column(name = "id", unique = true, nullable = false, length = 255)
    val id: String,
        @Column(name = "password", unique = false, nullable = false, length = 255)
    val password: String,
        @Column(name = "email", unique = true, nullable = false, length = 255)
    val email: String,
        @Column(name = "tell", unique = true, nullable = true, length = 255)
    val tell: String? = null,
        @Column(name = "adress", unique = false, nullable = false, length = 255)
    val adress: String = "전국",
        @Column(name = "signed", unique = false, nullable = false, length = 255)
    val signed: Date = Date(),
        @OneToOne(cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "profile_idx")
    val image: Image? = null,
        @Column(name = "description", unique = false, nullable = false, length = 255)
    val description: String = "안녕하세요",
        @Column(name = "role", unique = false, nullable = false)
    val role: Role = Role.USER
)
