package nonabili.nonabiliserver.common.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "image")
data class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val idx: UUID = UUID.randomUUID(),
    @Column(name = "url", unique = true, nullable = false, length = 255)
    val url: String
)
