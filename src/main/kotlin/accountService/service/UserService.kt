package accountService.service

import accountService.model.User
import accountService.dto.UserCreationDTO
import accountService.dto.UserDTO
import accountService.exception.UserNotFoundException
import accountService.mapper.toDTO
import accountService.mapper.toEntity
//import accountService.model.User
import accountService.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID
import org.slf4j.Logger
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(private val repository: UserRepository) {

    private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    fun registerUser(userCreationDTO: UserCreationDTO): UserDTO {
        logger.info("Registering a new user with name: ${userCreationDTO.name}")
        val newUser = User(name = userCreationDTO.name)
        val savedUser = repository.save(newUser)
        logger.info("User registered successfully with ID: ${savedUser.id}")
        return savedUser.toDTO()
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserDTO> {
        logger.info("Fetching all users")
        return repository.findAll().map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: UUID): UserDTO? {
        logger.info("Fetching user with ID: $id")
        return repository.findById(id).orElseThrow {
            logger.error("User with ID $id not found")
            UserNotFoundException("User with ID $id not found")
        }.toDTO()
    }

    @Transactional
    fun updateUserName(id: UUID, newName: String): UserDTO? {
        logger.info("Updating user with ID: $id to new name: $newName")
        val user = repository.findById(id).orElseThrow {
            logger.error("User with ID $id not found")
            UserNotFoundException("User with ID $id not found")
        }
        user.name = newName
        val updatedUser = repository.save(user)
        logger.info("User with ID: $id updated successfully")
        return updatedUser.toDTO()
    }

    @Transactional
    fun deleteUser(id: UUID) {
        logger.info("Deleting user with ID: $id")
        try {
            repository.deleteById(id)
            logger.info("User with ID: $id deleted successfully")
        } catch (e: Exception) {
            logger.error("Error deleting user with ID: $id", e)
            throw UserNotFoundException("User with ID $id not found")
        }
    }
}