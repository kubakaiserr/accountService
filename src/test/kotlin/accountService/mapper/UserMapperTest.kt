package accountService.mapper

import accountService.dto.UserDTO
import accountService.model.User
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.mockito.Mockito
import java.util.*

@SpringBootTest
class UserMapperTest {



    @Test
    fun `toDTO should map User to UserDTO`() {
        val user = User(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val userDTO = user.toDTO()

        assert(userDTO.id == UUID.fromString("00000000-0000-0000-0000-000000000001"))
        assert(userDTO.name == "Test User")
    }

    @Test
    fun `toEntity should map UserDTO to User`() {
        val userDTO = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "Test User")
        val user = userDTO.toEntity()

        assert(user.id == UUID.fromString("00000000-0000-0000-0000-000000000001"))
        assert(user.name == "Test User")
    }
}