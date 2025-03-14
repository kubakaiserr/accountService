package accountService.controller

import accountService.dto.UserCreationDTO
import accountService.dto.UserDTO
//import accountService.model.User
import accountService.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody userCreationDTO: UserCreationDTO): ResponseEntity<UserDTO> {
        val registeredUser = userService.registerUser(userCreationDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
    }

    @GetMapping
    fun getAllUsers(): List<UserDTO> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        val user = userService.getUserById(id) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}/name")
    fun updateUserName(@PathVariable id: Long, @RequestBody newName: UserName): ResponseEntity<UserDTO> {
        val updatedUser = userService.updateUserName(id, newName.name) ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    data class UserName(val name: String)
}