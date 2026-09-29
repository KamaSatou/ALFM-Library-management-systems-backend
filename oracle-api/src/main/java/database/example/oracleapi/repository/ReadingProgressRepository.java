package database.example.oracleapi.repository;

import database.example.oracleapi.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingProgressRepository
        extends JpaRepository<ReadingProgress, Long> {

    Optional<ReadingProgress> findByMemberIdAndBookId(
            Long memberId,
            Long bookId
    );

    List<ReadingProgress> findByMemberId(
            Long memberId
    );
}