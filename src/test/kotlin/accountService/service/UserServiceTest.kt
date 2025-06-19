package accountService.service

import accountService.dto.UserCreationDTO
import accountService.exception.UserNotFoundException
import accountService.model.User
import accountService.repository.BankAccountRepository
import accountService.repository.UserRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class UserServiceTest {

    private val userRepository = Mockito.mock(UserRepository::class.java)
    private val userService = UserService(userRepository)

    @Test
    fun `registerUser should save and return UserDTO`() {
        val userCreationDTO = UserCreationDTO(name = "Test User")
        val user = User(name = "Test User", id = UUID.fromString("00000000-0000-0000-0000-000000000001"))
//        Mockito.`when`(userRepository.save(user)).thenReturn(user)
        Mockito.`when`(userRepository.save(Mockito.any(User::class.java))).thenReturn(user)

        val result = userService.registerUser(userCreationDTO)

        assert(result.name == "Test User")
    }

    @Test
    fun `getAllUsers should return list of UserDTOs`() {
        val users = listOf(
            User(id = UUID.randomUUID(), name = "User 1"),
            User(id = UUID.randomUUID(), name = "User 2")
        )
        Mockito.`when`(userRepository.findAll()).thenReturn(users)

        val result = userService.getAllUsers()

        assert(result.size == 2)
        assert(result[0].name == "User 1")
        assert(result[1].name == "User 2")
    }

    @Test
    fun `getUserById should return UserDTO with given ID`() {
        val user = User(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "User 1")
        Mockito.`when`(userRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(java.util.Optional.of(user))

        val result = userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"))

        assert(result?.name == "User 1")
    }

    @Test
    fun `getUserById should throw UserNotFoundException when user with given ID does not exist`() {
        Mockito.`when`(userRepository.findById(UUID.randomUUID())).thenThrow(UserNotFoundException("User with ID 1 not found"))

        assertThrows<UserNotFoundException> {
            userService.getUserById(UUID.randomUUID())


        }
    }

    @Test
    fun `updateUserName should update and return UserDTO`() {
        val user = User(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "User 1")
        Mockito.`when`(userRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(java.util.Optional.of(user))
        Mockito.`when`(userRepository.save(user)).thenReturn(user)

        val result = userService.updateUserName(UUID.fromString("00000000-0000-0000-0000-000000000001"), "New Name")

        assert(result?.name == "New Name")
    }

    @Test
    fun `updateUserName should throw UserNotFoundException when user with given ID does not exist`() {
        Mockito.`when`(userRepository.findById(UUID.randomUUID())).thenThrow(UserNotFoundException("User with ID 1 not found"))

        assertThrows<UserNotFoundException> {
            userService.updateUserName(UUID.randomUUID(), "New Name")
        }
    }

    @Test
    fun `deleteUser should delete user with given ID`() {
        Mockito.doNothing().`when`(userRepository).deleteById(UUID.randomUUID())

        userService.deleteUser(UUID.randomUUID())
    }

    @Test
    fun `deleteUser should throw UserNotFoundException when user with given ID does not exist`() {
        Mockito.doThrow(UserNotFoundException("User with ID 00000000-0000-0000-0000-000000000001 not found")).`when`(userRepository).deleteById(UUID.fromString("00000000-0000-0000-0000-000000000001"))

        assertThrows<UserNotFoundException> {
            userService.deleteUser(UUID.fromString("00000000-0000-0000-0000-000000000001"))
        }
    }

    

}