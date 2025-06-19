package accountService.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "DTO for updating the account name")
data class AccountName(
    @field:NotBlank(message = "Name must not be blank")
    @field:Size(max = 50, message = "Name must not exceed 50 characters")
    @Schema(description = "New account name", example = "Vacation Fund")
    val name: String
)
