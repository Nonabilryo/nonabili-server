package nonabili.nonabiliserver.common.repository

import nonabili.nonabiliserver.common.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ImageRepository: JpaRepository<Image, UUID> {
}
