package nonabili.nonabiliserver

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "nonabili-api", version = "1.0", description = "안녕하세요"))
class NonabiliServerApplication

fun main(args: Array<String>) {
	runApplication<NonabiliServerApplication>(*args)
}
