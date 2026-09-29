package database.example.oracleapi.service;

import database.example.oracleapi.entity.BorrowDetail;
import database.example.oracleapi.entity.BorrowDetailId;
import database.example.oracleapi.repository.BorrowDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowDetailService {

    private final BorrowDetailRepository borrowDetailRepository;

    public BorrowDetailService(BorrowDetailRepository borrowDetailRepository) {
        this.borrowDetailRepository = borrowDetailRepository;
    }

    public List<BorrowDetail> getAllBorrowDetails() {
        return borrowDetailRepository.findAll();
    }

    public BorrowDetail getBorrowDetail(Long borrowId, Long bookId) {

        BorrowDetailId id = new BorrowDetailId(borrowId, bookId);

        return borrowDetailRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Borrow detail not found: borrowId="
                                        + borrowId
                                        + ", bookId="
                                        + bookId
                        )
                );
    }

    public BorrowDetail createBorrowDetail(BorrowDetail borrowDetail) {
        return borrowDetailRepository.save(borrowDetail);
    }

    public BorrowDetail updateBorrowDetail(
            Long borrowId,
            Long bookId,
            BorrowDetail updatedDetail) {

        BorrowDetail detail = getBorrowDetail(borrowId, bookId);

        detail.setReturnDate(updatedDetail.getReturnDate());

        return borrowDetailRepository.save(detail);
    }

    public void deleteBorrowDetail(Long borrowId, Long bookId) {

        BorrowDetailId id =
                new BorrowDetailId(borrowId, bookId);

        if (!borrowDetailRepository.existsById(id)) {
            throw new RuntimeException(
                    "Borrow detail not found: borrowId="
                            + borrowId
                            + ", bookId="
                            + bookId
            );
        }

        borrowDetailRepository.deleteById(id);
    }
}