package nonabili.nonabiliserver.entity

import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Entity
data class Status(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
    @ElementCollection
    val imageUrls: List<String>,
    val videoUrl: String,
    val article: UUID,
    val title: String,
    val description: String,
    val createdAt: Date = Date()
) {}