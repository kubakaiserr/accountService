package accountService.service

import accountService.model.User
import accountService.dto.UserCreationDTO
import accountService.dto.UserDTO
import accountService.exception.UserNotFoundException
import accountService.mapper.toDTO
import accountService.mapper.toEntity
//import accountService.model.User
import accountService.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {

    fun registerUser(userCreationDTO: UserCreationDTO): UserDTO {
        val newUser = User(name = userCreationDTO.name)
        return repository.save(newUser).toDTO()
    }

    fun getAllUsers(): List<UserDTO> = repository.findAll().map { it.toDTO() }

    fun getUserById(id: Long): UserDTO? = repository.findById(id).orElseThrow {
        UserNotFoundException("User with ID $id not found") }.toDTO()

    fun updateUserName(id: Long, newName: String): UserDTO? {
        val user = repository.findById(id).orElseThrow {
            UserNotFoundException("User with ID $id not found")
        }
        user.name = newName
        return repository.save(user).toDTO()
    }

    fun deleteUser(id: Long) {
        try {
            repository.deleteById(id)
        } catch (e: Exception) {
            throw UserNotFoundException("User with ID $id not found")
        }
    }

}