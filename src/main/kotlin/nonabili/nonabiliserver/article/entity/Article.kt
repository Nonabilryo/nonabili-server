package nonabili.nonabiliserver.article.entity

import jakarta.persistence.*
import nonabili.nonabiliserver.common.entity.Image
import java.util.Date
import java.util.UUID

@Entity
data class Article( //todo / put column
        @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
        val title: String,
        val writer: UUID,
        val buyer: UUID? = null,
        @ManyToOne(fetch = FetchType.LAZY)  // todo 시발
    @JoinColumn(name = "category_idx")
    val category: Category? = null,
        val description: String,
        val price: Long,
        val rentalType: RentalType = RentalType.MONTH,
        @OneToMany(fetch = FetchType.LAZY)
    val images: List<Image>,
        val createdAt: Date = Date(),
        @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_idx")
    val like: List<Like> = listOf()
)
