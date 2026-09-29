package database.example.oracleapi.service;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import database.example.oracleapi.dto.AuthResponse;
import database.example.oracleapi.dto.LoginRequest;
import database.example.oracleapi.dto.RegisterRequest;
import database.example.oracleapi.entity.Account;
import database.example.oracleapi.entity.Member;
import database.example.oracleapi.repository.AccountRepository;
import database.example.oracleapi.repository.MemberRepository;

@Service
public class AuthService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthService(
            AccountRepository accountRepository,
            MemberRepository memberRepository) {

        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
    }

    // ========================================
    // REGISTER
    // ========================================
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (request == null) {
            throw new RuntimeException("Request không hợp lệ");
        }

        if (request.getFullName() == null ||
                request.getFullName().isBlank()) {
            throw new RuntimeException("Họ tên không được để trống");
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {
            throw new RuntimeException("Email không được để trống");
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {
            throw new RuntimeException("Mật khẩu không được để trống");
        }

        String email = request.getEmail().trim();

        // Kiểm tra email đã tồn tại
        if (accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được đăng ký");
        }

        // ========================================
        // CREATE MEMBER
        // ========================================
        Member member = new Member();

        member.setFullName(request.getFullName().trim());
        member.setEmail(email);
        member.setPhone(request.getPhone());
        member.setRegisterDate(LocalDate.now());
        member.setStatus("ACTIVE");

        member = memberRepository.save(member);

        // ========================================
        // CREATE ACCOUNT
        // ========================================
        Account account = new Account();

        account.setMember(member);
        account.setEmail(email);

        // BCrypt mã hóa password
        account.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        account.setRole("MEMBER");
        account.setStatus("ACTIVE");

        account = accountRepository.save(account);

        // ========================================
        // RETURN RESPONSE
        // ========================================
        return new AuthResponse(
                account.getAccountId(),
                member.getMemberId(),
                member.getFullName(),
                account.getEmail(),
                account.getRole(),
                account.getStatus()
        );
    }

    // ========================================
    // LOGIN
    // ========================================
    public AuthResponse login(LoginRequest request) {

        if (request == null) {
            throw new RuntimeException("Request không hợp lệ");
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {
            throw new RuntimeException("Email không được để trống");
        }

        if (request.getPassword() == null ||
                request.getPassword().isBlank()) {
            throw new RuntimeException("Mật khẩu không được để trống");
        }

        String email = request.getEmail().trim();

        // ========================================
        // 1. FIND ACCOUNT BY EMAIL
        // ========================================
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("EMAIL_NOT_FOUND")
                );

        // ========================================
        // 2. CHECK ACCOUNT STATUS
        // ========================================
        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new RuntimeException("ACCOUNT_NOT_ACTIVE");
        }

        // ========================================
        // 3. CHECK PASSWORD
        // ========================================
        if (account.getPassword() == null ||
                account.getPassword().isBlank()) {

            throw new RuntimeException("PASSWORD_NOT_SET");
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                account.getPassword())) {

            throw new RuntimeException("PASSWORD_WRONG");
        }

        // ========================================
        // 4. GET MEMBER
        // ========================================
        Member member = account.getMember();

        if (member == null) {
            throw new RuntimeException("MEMBER_NOT_FOUND");
        }

        // ========================================
        // 5. LOGIN SUCCESS
        // ========================================
        return new AuthResponse(
                account.getAccountId(),
                member.getMemberId(),
                member.getFullName(),
                account.getEmail(),
                account.getRole(),
                account.getStatus()
        );
    }
}