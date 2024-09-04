package nonabili.nonabiliserver.review.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
data class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
    val writer: UUID,
    val article: UUID,
    val rating: Float,
    val title: String,
    val description: String,
    @ElementCollection
    val imageUrls: List<String>
)
