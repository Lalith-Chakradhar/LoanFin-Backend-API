package loanfin.entity;

import jakarta.persistence.*;

@Entity
@Table(name="loans")
public class LoanEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // internal PK

    @Column(name="loan_account_number")
}
