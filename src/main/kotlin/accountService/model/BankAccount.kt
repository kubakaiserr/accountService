package accountService.model

import accountService.dto.BankAccountDTO
import jakarta.persistence.*

@Entity
data class BankAccount(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String,
    var balance: Double,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
)