package accountService.dto

import jakarta.validation.constraints.*

data class UserCreationDTO(
    @field:NotBlank(message = "Name must not be blank")
    @field:Size(max = 50, message = "Name must not exceed 50 characters")
    val name: String
)