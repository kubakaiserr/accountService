package accountService.mapper

import accountService.dto.UserDTO
import accountService.model.User
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.mockito.Mockito

@SpringBootTest
class UserMapperTest {



    @Test
    fun `toDTO should map User to UserDTO`() {
        val user = User(id = 1L, name = "Test User")
        val userDTO = user.toDTO()

        assert(userDTO.id == 1L)
        assert(userDTO.name == "Test User")
    }

    @Test
    fun `toEntity should map UserDTO to User`() {
        val userDTO = UserDTO(id = 1L, name = "Test User")
        val user = userDTO.toEntity()

        assert(user.id == 1L)
        assert(user.name == "Test User")
    }
}