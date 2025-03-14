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

@SpringBootTest
class UserServiceTest {

    private val userRepository = Mockito.mock(UserRepository::class.java)
    private val userService = UserService(userRepository)

    @Test
    fun `registerUser should save and return UserDTO`() {
        val userCreationDTO = UserCreationDTO(name = "Test User")
        val user = User(name = "Test User")
        Mockito.`when`(userRepository.save(user)).thenReturn(user)

        val result = userService.registerUser(userCreationDTO)

        assert(result.name == "Test User")
    }

    @Test
    fun `getAllUsers should return list of UserDTOs`() {
        val users = listOf(
            User(id = 1L, name = "User 1"),
            User(id = 2L, name = "User 2")
        )
        Mockito.`when`(userRepository.findAll()).thenReturn(users)

        val result = userService.getAllUsers()

        assert(result.size == 2)
        assert(result[0].name == "User 1")
        assert(result[1].name == "User 2")
    }

    @Test
    fun `getUserById should return UserDTO with given ID`() {
        val user = User(id = 1L, name = "User 1")
        Mockito.`when`(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user))

        val result = userService.getUserById(1L)

        assert(result?.name == "User 1")
    }

    @Test
    fun `getUserById should throw UserNotFoundException when user with given ID does not exist`() {
        Mockito.`when`(userRepository.findById(1L)).thenThrow(UserNotFoundException("User with ID 1 not found"))

        assertThrows<UserNotFoundException> {
            userService.getUserById(1L)


        }
    }

    @Test
    fun `updateUserName should update and return UserDTO`() {
        val user = User(id = 1L, name = "User 1")
        Mockito.`when`(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user))
        Mockito.`when`(userRepository.save(user)).thenReturn(user)

        val result = userService.updateUserName(1L, "New Name")

        assert(result?.name == "New Name")
    }

    @Test
    fun `updateUserName should throw UserNotFoundException when user with given ID does not exist`() {
        Mockito.`when`(userRepository.findById(1L)).thenThrow(UserNotFoundException("User with ID 1 not found"))

        assertThrows<UserNotFoundException> {
            userService.updateUserName(1L, "New Name")
        }
    }

    @Test
    fun `deleteUser should delete user with given ID`() {
        Mockito.doNothing().`when`(userRepository).deleteById(1L)

        userService.deleteUser(1L)
    }

    @Test
    fun `deleteUser should throw UserNotFoundException when user with given ID does not exist`() {
        Mockito.doThrow(UserNotFoundException("User with ID 1 not found")).`when`(userRepository).deleteById(1L)

        assertThrows<UserNotFoundException> {
            userService.deleteUser(1L)
        }
    }

    

}