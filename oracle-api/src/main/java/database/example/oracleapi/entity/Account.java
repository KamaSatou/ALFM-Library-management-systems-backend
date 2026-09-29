package database.example.oracleapi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "LIB_ACCOUNTS")
public class Account {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "account_seq"
    )
    @jakarta.persistence.SequenceGenerator(
        name = "account_seq",
        sequenceName = "SEQ_LIB_ACCOUNTS",
        allocationSize = 1
    )
    @Column(name = "ACCOUNTID")
    private Long accountId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBERID", nullable = false)
    private Member member;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE", nullable = false)
    private String role;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "RESETOTP")
    private String resetOtp;

    @Column(name = "RESETOTPEXPIRES")
    private LocalDateTime resetOtpExpires;

    public Account() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResetOtp() {
    return resetOtp;
    }

    public void setResetOtp(String resetOtp) {
    this.resetOtp = resetOtp;
    }

    public LocalDateTime getResetOtpExpires() {
    return resetOtpExpires;
    }

    public void setResetOtpExpires(LocalDateTime resetOtpExpires) {
    this.resetOtpExpires = resetOtpExpires;
    }
}