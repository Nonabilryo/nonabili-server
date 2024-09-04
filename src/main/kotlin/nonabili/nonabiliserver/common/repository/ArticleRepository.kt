package nonabili.nonabiliserver.common.repository

import nonabili.nonabiliserver.article.entity.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Date
import java.util.UUID

@Repository
interface ArticleRepository: JpaRepository<Article, UUID> {
    fun findArticleByIdx(idx: UUID): Article?

//    @Query("SELECT a FROM Article a WHERE a.createdAt >= :createdAt ORDER BY SIZE(a.like) DESC")
@Query("""
        SELECT DISTINCT a FROM Article a 
        LEFT JOIN FETCH a.category c
        LEFT JOIN FETCH a.images i
        WHERE a.createdAt >= :createdAt
        ORDER BY SIZE(a.like) DESC
    """)
    fun findArticlesByCreatedAtAfterOrderByLikeDesc(  // todo without category and like
        @Param("createdAt") createdAt: Date,
        pageable: Pageable
    ): Page<Article>

    @Query("SELECT a FROM Article a ORDER BY SIZE(a.like) DESC")
    fun findArticlesByCreatedAtAfterOrderByLikeDesc2(  // todo without category and like
        pageable: Pageable
    ): Page<Article>
    @Query("""
        SELECT DISTINCT a FROM Article a 
        LEFT JOIN FETCH a.category c
        LEFT JOIN FETCH a.images i
        ORDER BY SIZE(a.like) DESC
    """)
    fun findArticlesByCreatedAtAfterOrderByLikeDesc3(
        pageable: Pageable
    ): Page<Article>
}