package accountService.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "`user`")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    var name: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val bankAccounts: List<BankAccount> = mutableListOf(),

    @Version
    val version: Long? = null
)
