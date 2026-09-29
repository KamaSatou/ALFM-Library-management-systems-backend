package database.example.oracleapi.service;

import database.example.oracleapi.entity.Borrow;
import database.example.oracleapi.repository.BorrowRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;

    public BorrowService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }

    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    public Borrow getBorrowById(Long id) {
        return borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow not found: " + id));
    }

    public Borrow createBorrow(Borrow borrow) {

        if (borrow.getBorrowDate() == null) {
            borrow.setBorrowDate(LocalDate.now());
        }

        if (borrow.getStatus() == null || borrow.getStatus().isBlank()) {
            borrow.setStatus("BORROWED");
        }

        return borrowRepository.save(borrow);
    }

    public Borrow updateBorrow(Long id, Borrow updatedBorrow) {
        Borrow borrow = getBorrowById(id);

        borrow.setMember(updatedBorrow.getMember());
        borrow.setBorrowDate(updatedBorrow.getBorrowDate());
        borrow.setDueDate(updatedBorrow.getDueDate());
        borrow.setStatus(updatedBorrow.getStatus());

        return borrowRepository.save(borrow);
    }

    public void deleteBorrow(Long id) {
        Borrow borrow = getBorrowById(id);
        borrowRepository.delete(borrow);
    }
}