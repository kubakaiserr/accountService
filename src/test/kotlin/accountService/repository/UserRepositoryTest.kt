package accountService.repository

import accountService.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `findById should return user when user exists`() {
        val user = userRepository.save(User(name = "Test User"))
        val foundUser = userRepository.findById(user.id!!).orElse(null)
        assertNotNull(foundUser)
        assertEquals("Test User", foundUser?.name)
    }

    @Test
    fun `findById should return empty when user does not exist`() {
        val foundUser = userRepository.findById(999L).orElse(null)
        assertNull(foundUser)
    }

    @Test
    fun `save should create new user`() {
        val user = User(name = "Test User")
        val savedUser = userRepository.save(user)
        assertNotNull(savedUser.id)
        assertEquals("Test User", savedUser.name)
    }

    @Test
    fun `delete should remove user`() {
        val user = userRepository.save(User(name = "Test User"))
        userRepository.delete(user)
        val foundUser = userRepository.findById(user.id!!).orElse(null)
        assertNull(foundUser)
    }
}