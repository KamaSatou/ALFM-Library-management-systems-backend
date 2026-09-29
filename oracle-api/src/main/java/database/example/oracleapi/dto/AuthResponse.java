package database.example.oracleapi.dto;

public class AuthResponse {

    private Long accountId;
    private Long memberId;
    private String fullName;
    private String email;
    private String role;
    private String status;

    public AuthResponse() {
    }

    public AuthResponse(
            Long accountId,
            Long memberId,
            String fullName,
            String email,
            String role,
            String status) {

        this.accountId = accountId;
        this.memberId = memberId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}