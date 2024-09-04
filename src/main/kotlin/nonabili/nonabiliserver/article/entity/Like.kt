package nonabili.nonabiliserver.article.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "article_like")
data class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
    val user: UUID,
    @ManyToOne(fetch = FetchType.LAZY)
    val article: Article
)
