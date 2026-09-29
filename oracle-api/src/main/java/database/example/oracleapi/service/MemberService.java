package database.example.oracleapi.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import database.example.oracleapi.dto.MemberRequest;
import database.example.oracleapi.dto.MemberResponse;
import database.example.oracleapi.entity.Account;
import database.example.oracleapi.entity.Member;
import database.example.oracleapi.repository.AccountRepository;
import database.example.oracleapi.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    public MemberService(
            MemberRepository memberRepository,
            AccountRepository accountRepository) {

        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
    }

    // =========================================================
    // GET ALL MEMBERS
    // GET /api/members
    // =========================================================
    public List<MemberResponse> getAllMembers() {

        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::fromEntity)
                .toList();
    }

    // =========================================================
    // GET MEMBER BY ID
    // GET /api/members/{memberId}
    // =========================================================
    public MemberResponse getMemberById(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Member not found"
                        )
                );

        return MemberResponse.fromEntity(member);
    }

    // =========================================================
    // CREATE MEMBER
    // POST /api/members
    // =========================================================
    public MemberResponse createMember(MemberRequest request) {

        Member member = new Member();

        // Oracle automatically generates MEMBERID

        member.setFullName(request.getFullName());
        member.setEmail(request.getEmail());
        member.setPhone(request.getPhone());

        // REGISTERDATE is NOT NULL
        member.setRegisterDate(LocalDate.now());

        // STATUS is NOT NULL
        if (request.getStatus() == null ||
                request.getStatus().isBlank()) {

            member.setStatus("ACTIVE");

        } else {

            member.setStatus(request.getStatus());
        }

        Member savedMember =
                memberRepository.save(member);

        return MemberResponse.fromEntity(savedMember);
    }

    // =========================================================
    // UPDATE MEMBER
    // PUT /api/members/{memberId}
    // =========================================================
    @Transactional
    public MemberResponse updateMember(
            Long memberId,
            MemberRequest request) {

        // -----------------------------------------------------
        // 1. Tìm MEMBER
        // -----------------------------------------------------

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Member not found"
                        )
                );

        // -----------------------------------------------------
        // 2. Kiểm tra request
        // -----------------------------------------------------

        if (request == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Request không hợp lệ"
            );
        }

        if (request.getFullName() == null ||
                request.getFullName().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Họ tên không được để trống"
            );
        }

        if (request.getEmail() == null ||
                request.getEmail().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email không được để trống"
            );
        }

        String newEmail =
                request.getEmail().trim();

        // -----------------------------------------------------
        // 3. Kiểm tra email có bị tài khoản khác sử dụng không
        // -----------------------------------------------------

        Account accountWithEmail =
                accountRepository
                        .findByEmail(newEmail)
                        .orElse(null);

        if (accountWithEmail != null) {

            Long accountMemberId =
                    accountWithEmail
                            .getMember()
                            .getMemberId();

            if (!memberId.equals(accountMemberId)) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email đã được sử dụng"
                );
            }
        }

        // -----------------------------------------------------
        // 4. UPDATE LIB_MEMBERS
        // -----------------------------------------------------

        member.setFullName(
                request.getFullName().trim()
        );

        member.setEmail(newEmail);

        member.setPhone(
                request.getPhone()
        );

        // Không cho update STATUS nếu request không gửi
        if (request.getStatus() != null &&
                !request.getStatus().isBlank()) {

            member.setStatus(
                    request.getStatus()
            );
        }

        Member updatedMember =
                memberRepository.save(member);

        // -----------------------------------------------------
        // 5. UPDATE LIB_ACCOUNTS
        // -----------------------------------------------------
        // Nếu email Profile thay đổi,
        // email đăng nhập cũng được thay đổi.

        Account account =
                accountRepository
                        .findByMember_MemberId(memberId)
                        .orElse(null);

        if (account != null) {

            account.setEmail(newEmail);

            accountRepository.save(account);
        }

        // -----------------------------------------------------
        // 6. Trả dữ liệu mới về Android
        // -----------------------------------------------------

        return MemberResponse.fromEntity(
                updatedMember
        );
    }

    // =========================================================
    // DELETE MEMBER
    // DELETE /api/members/{memberId}
    // =========================================================
    public void deleteMember(Long memberId) {

        if (!memberRepository.existsById(memberId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Member not found"
            );
        }

        memberRepository.deleteById(memberId);
    }
}