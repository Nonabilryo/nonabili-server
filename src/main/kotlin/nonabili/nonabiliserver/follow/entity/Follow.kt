package nonabili.nonabiliserver.follow.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import nonabili.nonabiliserver.user.entity.User
import java.util.Date
import java.util.UUID

@Entity
data class Follow(
        @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
        @Column(name = "date", unique = false, nullable = false)
    val date: Date = Date(),
        @ManyToOne
    val follower: User,  // follow하는 사람
        @ManyToOne
    val following: User   // follow받는 사람
)
