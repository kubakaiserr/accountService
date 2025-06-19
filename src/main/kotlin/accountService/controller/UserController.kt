package accountService.controller

import accountService.dto.UserCreationDTO
import accountService.dto.UserDTO
//import accountService.model.User
import accountService.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid userCreationDTO: UserCreationDTO): ResponseEntity<UserDTO> {
        val registeredUser = userService.registerUser(userCreationDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
    }

    @GetMapping
    fun getAllUsers(): List<UserDTO> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: UUID): ResponseEntity<UserDTO> {
        val user = userService.getUserById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}/name")
    fun updateUserName(@PathVariable id: UUID, @RequestBody @Valid newName: UserName): ResponseEntity<UserDTO> {
        val updatedUser = userService.updateUserName(id, newName.name) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: UUID): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    data class UserName(
        @field:NotBlank(message = "Name must not be blank")
        @field:Size(max = 50, message = "Name must not exceed 50 characters")
        val name: String)
}