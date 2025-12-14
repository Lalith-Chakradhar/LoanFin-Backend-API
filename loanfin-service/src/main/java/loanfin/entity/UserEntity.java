package loanfin.entity;

import jakarta.persistence.*;
import loanfin.enums.UserRole;
import loanfin.util.UserId;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name="users",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})},
        indexes = {@Index(name = "idx_user_email_id", columnList = "email")}
)
public class UserEntity extends BaseEntity{

    @Id
    @UserId
    @Column(name="id", nullable = false, updatable = false)
    private String id;

    @Column(name="name")
    @Convert(converter = EncryptDecryptConverter.class)
    private String userName;

    @Column(name = "email_hash", unique = true, nullable = false)
    private String emailHash;

    @Column(name = "email", nullable = false)
    @Convert(converter = EncryptDecryptConverter.class)
    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
