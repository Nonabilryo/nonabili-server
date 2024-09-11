package nonabili.nonabiliserver.article.controller

import nonabili.nonabiliserver.article.dto.request.ArticlePostRequest
import nonabili.nonabiliserver.article.dto.response.ArticleInfoResponse
import nonabili.nonabiliserver.article.dto.response.ArticleSuggestResponse
import nonabili.nonabiliserver.article.dto.response.LikedResponse
import nonabili.nonabiliserver.article.service.ArticleService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/article")
class ArticleController(val articleService: ArticleService) {
    @GetMapping("/page/{page}")
    fun getSuggestArticle(@PathVariable page: Int): ResponseEntity<ResponseFormat<Page<ArticleSuggestResponse>>> {
        val result = articleService.getSuggestArticle(page)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @PostMapping(consumes = ["multipart/form-data"])
    fun postArticle(principal: Principal, @ModelAttribute request: ArticlePostRequest): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        articleService.postArticle(request, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @GetMapping("/{articleIdx}")  // todo 최근 본거 저장 gateway
    fun getArticleInfo(@PathVariable articleIdx: String): ResponseEntity<ResponseFormat<ArticleInfoResponse>> {
        val result = articleService.getArticleInfo(articleIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }

    @DeleteMapping("/{articleIdx}")
    fun deleteArticle(
        principal: Principal,
        @PathVariable articleIdx: String
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        articleService.deleteArticle(articleIdx, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

    @GetMapping("/{articleIdx}/liked")
    fun getLiked(@PathVariable articleIdx: String, principal: Principal): ResponseEntity<ResponseFormat<LikedResponse>> {
        val userIdx = principal.name
        val result = articleService.getLiked(articleIdx, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @PostMapping("/{articleIdx}/like")
    fun postLike(@PathVariable articleIdx: String, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        articleService.postLike(articleIdx, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @DeleteMapping("/{articleIdx}/like")
    fun deleteLike(@PathVariable articleIdx: String, principal: Principal): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        articleService.deleteLike(articleIdx, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }

//    @GetMapping("/articleIdxToWriterIdx/{articleIdx}")
//    fun getWriterIdxByArticleIdx(@PathVariable articleIdx: String): ResponseEntity<ResponseFormat<Any>> {
//        val result = articleService.getWriterIdxByArticleIdx(articleIdx)
//        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
//    }

}