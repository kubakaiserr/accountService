package accountService.integration

import accountService.dto.UserCreationDTO
import accountService.dto.UserDTO
import accountService.model.User
import accountService.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun `registerUser should create and return new user`() {
        val userCreationDTO = UserCreationDTO(name = "Integration Test User")
        val request = HttpEntity(userCreationDTO)
        val response: ResponseEntity<UserDTO> = restTemplate
            .postForEntity("http://localhost:$port/users/register", request, UserDTO::class.java)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals("Integration Test User", response.body?.name)
    }

    @Test
    fun `getAllUsers should return list of users`() {
        val user1 = userRepository.save(User(name = "User 1"))
        val user2 = userRepository.save(User(name = "User 2"))

        val response: ResponseEntity<List<UserDTO>> = restTemplate
            .getForEntity("http://localhost:$port/users", List::class.java) as ResponseEntity<List<UserDTO>>

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(2, response.body?.size)

    }

    @Test
    fun `getUserById should return user with given ID`() {
        val user = userRepository.save(User(name = "User 1"))

        val response: ResponseEntity<UserDTO> = restTemplate
            .getForEntity("http://localhost:$port/users/${user.id}", UserDTO::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("User 1", response.body?.name)
    }

    @Test
    fun `deleteUser should remove user`() {
        val user = userRepository.save(User(name = "User 1"))

        restTemplate.delete("http://localhost:$port/users/${user.id}")

        val foundUser = userRepository.findById(user.id!!)
        assertEquals(false, foundUser.isPresent)
    }
}