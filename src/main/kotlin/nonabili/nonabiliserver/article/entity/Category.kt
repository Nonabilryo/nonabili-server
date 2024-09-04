package nonabili.nonabiliserver.article.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.util.UUID


@Entity
@Table(name = "category")
data class Category (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID? = UUID.randomUUID(),

    val name: String,

    @ManyToOne
    @JoinColumn(name = "parent_idx")
    val parent: Category? = null,

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    val children: List<Category>? = null
) {}