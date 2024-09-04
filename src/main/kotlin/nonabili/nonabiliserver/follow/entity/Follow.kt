package nonabili.nonabiliserver.follow.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.Date
import java.util.UUID

@Entity
data class Follow(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
    @Column(name = "date", unique = false, nullable = false)
    val date: Date = Date(),
    @Column(name = "follower", unique = false, nullable = false)
    val follower: UUID,  // follow하는 사람
    @Column(name = "following", unique = false, nullable = false)
    val following: UUID   // follow받는 사람
)
