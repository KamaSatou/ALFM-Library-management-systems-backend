package database.example.oracleapi.repository;

import database.example.oracleapi.entity.BorrowDetail;
import database.example.oracleapi.entity.BorrowDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowDetailRepository
        extends JpaRepository<BorrowDetail, BorrowDetailId> {
}