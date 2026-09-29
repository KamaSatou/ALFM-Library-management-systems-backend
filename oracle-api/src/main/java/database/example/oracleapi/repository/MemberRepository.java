package database.example.oracleapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import database.example.oracleapi.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}