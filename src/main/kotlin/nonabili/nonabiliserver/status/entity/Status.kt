package nonabili.nonabiliserver.entity

import jakarta.persistence.*
import nonabili.nonabiliserver.article.entity.Article
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
    @ManyToOne
    val article: Article,
    val title: String,
    val description: String,
    val createdAt: Date = Date()
) {}