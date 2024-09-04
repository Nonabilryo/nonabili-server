package nonabili.nonabiliserver.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
data class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idx: Long = 0,
    val sender: UUID,
    val receiver: UUID,
    val contentType: ContentType = ContentType.MESSAGE,
    val content: String,
    val createdAt: Date = Date()
)
data class ChatSenderAndReceiver(
    val sender: UUID,
    val receiver: UUID
)