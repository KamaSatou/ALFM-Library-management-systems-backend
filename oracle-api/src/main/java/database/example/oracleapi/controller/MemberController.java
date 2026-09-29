package database.example.oracleapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import database.example.oracleapi.dto.MemberRequest;
import database.example.oracleapi.dto.MemberResponse;
import database.example.oracleapi.service.MemberService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // =========================================================
    // GET ALL MEMBERS
    // GET /api/members
    // =========================================================
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers() {

        return ResponseEntity.ok(
                memberService.getAllMembers()
        );
    }

    // =========================================================
    // GET MEMBER BY ID
    // GET /api/members/{memberId}
    // =========================================================
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponse> getMemberById(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(
                memberService.getMemberById(memberId)
        );
    }

    // =========================================================
    // CREATE MEMBER
    // POST /api/members
    // =========================================================
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(
            @Valid @RequestBody MemberRequest request) {

        MemberResponse createdMember =
                memberService.createMember(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMember);
    }

    // =========================================================
    // UPDATE MEMBER
    // PUT /api/members/{memberId}
    // =========================================================
    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMember(
            @PathVariable Long memberId,
            @Valid @RequestBody MemberRequest request) {

        return ResponseEntity.ok(
                memberService.updateMember(
                        memberId,
                        request
                )
        );
    }

    // =========================================================
    // DELETE MEMBER
    // DELETE /api/members/{memberId}
    // =========================================================
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long memberId) {

        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }
}