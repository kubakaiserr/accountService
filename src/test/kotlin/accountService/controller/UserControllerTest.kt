package accountService.controller

import accountService.dto.UserCreationDTO
import accountService.dto.UserDTO
import accountService.exception.UserNotFoundException
import accountService.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService


    @Test
    fun `getAllUsers should return list of users`() {
        val users = listOf(
            UserDTO(id = UUID.randomUUID(), name = "User 1"),
            UserDTO(id = UUID.randomUUID(), name = "User 2")
        )
        `when`(userService.getAllUsers()).thenReturn(users)

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("User 1"))
            .andExpect(jsonPath("$[1].name").value("User 2"))
    }

    @Test
    fun `getUserById should return user with given ID`() {
        val user = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "User 1")
        `when`(userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenReturn(user)

        mockMvc.perform(get("/users/00000000-0000-0000-0000-000000000001"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("User 1"))
    }

    @Test
    fun `getUserById should return 404 when user with given ID does not exist`() {
        `when`(userService.getUserById(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenThrow(UserNotFoundException("User with ID 00000000-0000-0000-0000-000000000001 not found"))

        mockMvc.perform(get("/users/00000000-0000-0000-0000-000000000001"))
            .andExpect(status().isNotFound)
            .andExpect(content().string("User with ID 00000000-0000-0000-0000-000000000001 not found"))
    }

    @Test
    fun `createUser should save and return UserDTO`() {
        val user = UserDTO(id = UUID.randomUUID(), name = "User 1")
        val newUser = UserCreationDTO(name = "User 1")
        `when`(userService.registerUser(newUser)).thenReturn(user)

        mockMvc.perform(post("/users/register")
            .contentType("application/json")
            .content("{\"name\": \"User 1\"}")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("User 1"))
    }

    @Test
    fun `updateUserName should return updated user`() {
        val user = UserDTO(id = UUID.fromString("00000000-0000-0000-0000-000000000001"), name = "User 1")
        `when`(userService.updateUserName(UUID.fromString("00000000-0000-0000-0000-000000000001"), "User 2")).thenReturn(user)

        mockMvc.perform(put("/users/00000000-0000-0000-0000-000000000001/name")
            .contentType("application/json")
            .content("{\"name\": \"User 2\"}")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("User 1"))
    }

    @Test
    fun `deleteUser should return 204`() {
        mockMvc.perform(delete("/users/00000000-0000-0000-0000-000000000001"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deleteUser should return 404 when user with given ID does not exist`() {
        `when`(userService.deleteUser(UUID.fromString("00000000-0000-0000-0000-000000000001"))).thenThrow(UserNotFoundException("User with ID 00000000-0000-0000-0000-000000000001 not found"))

        mockMvc.perform(delete("/users/00000000-0000-0000-0000-000000000001"))
            .andExpect(status().isNotFound)
            .andExpect(content().string("User with ID 00000000-0000-0000-0000-000000000001 not found"))
    }

    @Test
    fun `updateUserName should return 404 when user with given ID does not exist`() {
        `when`(userService.updateUserName(UUID.fromString("00000000-0000-0000-0000-000000000001"), "User 2")).thenThrow(UserNotFoundException("User with ID 00000000-0000-0000-0000-000000000001 not found"))

        mockMvc.perform(put("/users/00000000-0000-0000-0000-000000000001/name")
            .contentType("application/json")
            .content("{\"name\": \"User 2\"}")
        )
            .andExpect(status().isNotFound)
            .andExpect(content().string("User with ID 00000000-0000-0000-0000-000000000001 not found"))
    }

}