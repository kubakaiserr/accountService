package accountService

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountServiceApplication

fun main(args: Array<String>) {
	runApplication<AccountServiceApplication>(*args)
}
