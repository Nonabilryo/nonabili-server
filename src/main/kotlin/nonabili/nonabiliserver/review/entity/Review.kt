package nonabili.nonabiliserver.review.entity

import jakarta.persistence.*
import nonabili.nonabiliserver.article.entity.Article
import nonabili.nonabiliserver.user.entity.User
import java.util.UUID

@Entity
data class Review(
        @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
        @ManyToOne
    val writer: User,
        @ManyToOne
    val article: Article,
        val rating: Float,
        val title: String,
        val description: String,
        @ElementCollection
    val imageUrls: List<String>
)
