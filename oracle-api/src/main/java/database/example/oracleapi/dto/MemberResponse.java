package database.example.oracleapi.dto;

import java.time.LocalDate;

import database.example.oracleapi.entity.Member;

public class MemberResponse {

    private Long memberId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate registerDate;
    private String status;

    public MemberResponse() {
    }

    public static MemberResponse fromEntity(Member member) {

        MemberResponse response = new MemberResponse();

        response.memberId = member.getMemberId();
        response.fullName = member.getFullName();
        response.email = member.getEmail();
        response.phone = member.getPhone();
        response.registerDate = member.getRegisterDate();
        response.status = member.getStatus();

        return response;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public String getStatus() {
        return status;
    }
}