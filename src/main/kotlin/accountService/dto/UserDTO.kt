package accountService.dto

import java.util.UUID
import io.swagger.v3.oas.annotations.media.Schema


@Schema(description = "User response DTO")
data class UserDTO(
    @Schema(description = "Unique identifier of the user", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID,
    @Schema(description = "Name of the user", example = "John Doe")
    val name: String
)