package nonabili.nonabiliserver.article.repository

import nonabili.nonabiliserver.article.entity.Article
import nonabili.nonabiliserver.article.entity.Like
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface LikeRepository: JpaRepository<Like, UUID> {
//    @Query("SELECT a FROM Like a WHERE a.user = :user")
    fun findLikeByUserAndArticle(user: UUID, article: Article): Like?
    fun existsLikeByUserAndArticle(user: UUID, article: Article): Boolean
    fun countLikesByArticle(article: Article): Int
//    fun countLikesByArticle(article: UUID): Long
//    fun existsLikeByUserAndArticle()
//    fun countLikesByArticle(article: UUID): Long
}