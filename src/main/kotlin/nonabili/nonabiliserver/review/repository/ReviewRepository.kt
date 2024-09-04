package nonabili.nonabiliserver.review.repository

import nonabili.nonabiliserver.review.entity.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ReviewRepository: JpaRepository<Review, UUID> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.article = :articleIdx")
    fun findAverageRatingByArticle(@Param("articleIdx") articleIdx: UUID): Float
    fun findReviewByIdx(idx: UUID): Review?
    fun findReviewsByArticle(articleIdx: UUID, pageable: Pageable): Page<Review>
}