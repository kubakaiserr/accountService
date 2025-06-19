package accountService.mapper

import accountService.model.User
import accountService.dto.UserDTO



    fun User.toDTO(): UserDTO {
        return UserDTO(
            id = this.id ?: throw IllegalStateException("User ID cannot be null"),
            name = this.name
        )
    }

    fun UserDTO.toEntity(): User {
        return User(
            id = this.id,
            name = this.name
        )
    }
