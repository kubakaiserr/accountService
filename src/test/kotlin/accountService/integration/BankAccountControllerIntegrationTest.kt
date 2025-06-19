package accountService.integration

import accountService.dto.BankAccountCreationDTO
import accountService.dto.BankAccountDTO
import accountService.model.BankAccount
import accountService.model.User
import accountService.repository.BankAccountRepository
import accountService.repository.UserRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class BankAccountControllerIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var entityManager: EntityManager

    @Autowired
    private lateinit var bankAccountRepository: BankAccountRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var testUser: User

    @BeforeEach
    fun setup() {
        entityManager.clear()
        bankAccountRepository.deleteAll()
        userRepository.deleteAll()
        testUser = userRepository.saveAndFlush(User(name = "Test User"))
    }

    @Test
    fun `getAllAccounts should return list of accounts`() {
        val account1 = bankAccountRepository.saveAndFlush(BankAccount(name = "Account 1", balance = 100.0, user = testUser))
        val account2 = bankAccountRepository.saveAndFlush(BankAccount(name = "Account 2", balance = 200.0, user = testUser))

        val response: ResponseEntity<List<BankAccountDTO>> = restTemplate
            .getForEntity("http://localhost:$port/accounts", List::class.java) as ResponseEntity<List<BankAccountDTO>>

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.size)
    }

    @Test
    fun `getAccountById should return account with given ID`() {
        val account = bankAccountRepository.saveAndFlush(BankAccount(name = "Account 1", balance = 100.0, user = testUser))

        val response: ResponseEntity<BankAccountDTO> = restTemplate
            .getForEntity("http://localhost:$port/accounts/${account.id}", BankAccountDTO::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("Account 1", response.body?.name)
    }

    @Test
    fun `createAccount should create and return new account`() {
        val accountCreationDTO = BankAccountCreationDTO(
            name = "New Account",
            balance = 100.0,
            userId = testUser.id ?: throw IllegalStateException("User ID cannot be null"))
        val request = HttpEntity(accountCreationDTO)
        val response: ResponseEntity<BankAccountDTO> = restTemplate
            .postForEntity("http://localhost:$port/accounts", request, BankAccountDTO::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("New Account", response.body?.name)
    }

    @Test
    fun `deleteAccount should remove account`() {
        val account = bankAccountRepository.saveAndFlush(BankAccount(name = "Account 1", balance = 100.0, user = testUser))

        restTemplate.delete("http://localhost:$port/accounts/${account.id}")

        val foundAccount = bankAccountRepository.findById(account.id!!)
        assertEquals(false, foundAccount.isPresent)
    }
}