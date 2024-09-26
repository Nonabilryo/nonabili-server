package nonabili.nonabiliserver.chat.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import nonabili.nonabiliserver.entity.Chat
import nonabili.nonabiliserver.user.entity.User
import java.util.Date
import java.util.UUID

@Entity
data class LatestChat(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val uuid: UUID = UUID.randomUUID(),
    val sender: UUID,
    val receiver: UUID,
    @OneToOne
    val chat: Chat,
    val updatedAt: Date = Date()
)
