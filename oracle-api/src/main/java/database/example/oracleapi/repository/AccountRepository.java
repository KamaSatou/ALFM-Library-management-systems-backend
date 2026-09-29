package database.example.oracleapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import database.example.oracleapi.entity.Account;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Account> findByMember_MemberId(Long memberId);
}