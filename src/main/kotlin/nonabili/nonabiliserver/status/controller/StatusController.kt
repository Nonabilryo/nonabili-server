package nonabili.nonabiliserver.controller

import jakarta.validation.Valid
import nonabili.nonabiliserver.dto.request.StatusPostRequest
import nonabili.nonabiliserver.service.StatusService
import nonabili.nonabiliserver.common.util.ResponseFormat
import nonabili.nonabiliserver.common.util.ResponseFormatBuilder
import nonabili.nonabiliserver.entity.Status
import nonabili.nonabiliserver.status.dto.response.StatusResponse
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/article/{articleIdx}/status")
class StatusController(val statusService: StatusService) {
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun postStatus(
            @PathVariable articleIdx: String,
            @RequestParam(required = false) images: List<MultipartFile>,
            @RequestParam(required = false) video: MultipartFile?,
            principal: Principal,
            @Valid request: StatusPostRequest
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        statusService.postStatus(articleIdx, images, video, request, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
    @GetMapping()
    fun getStatus(
        principal: Principal,
        @RequestParam(required = false, defaultValue = "0", value = "page",) page: Int,
        @PathVariable articleIdx: String
        ): ResponseEntity<ResponseFormat<Page<StatusResponse>>> {
        val result = statusService.getStatus(articleIdx, page)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.build(result))
    }
    @DeleteMapping("/{statusIdx}")
    fun deleteStatus(
            principal: Principal,
            @PathVariable articleIdx: String,
            @PathVariable statusIdx: String
    ): ResponseEntity<ResponseFormat<Any>> {
        val userIdx = principal.name
        statusService.deleteStatus(statusIdx, userIdx)
        return ResponseEntity.ok(ResponseFormatBuilder { message = "success" }.noData())
    }
}