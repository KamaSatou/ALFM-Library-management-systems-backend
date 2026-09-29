package database.example.oracleapi.controller;

import database.example.oracleapi.entity.Borrow;
import database.example.oracleapi.service.BorrowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = "*")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        return ResponseEntity.ok(
                borrowService.getAllBorrows()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                borrowService.getBorrowById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Borrow> createBorrow(
            @RequestBody Borrow borrow) {

        Borrow created =
                borrowService.createBorrow(borrow);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borrow> updateBorrow(
            @PathVariable Long id,
            @RequestBody Borrow borrow) {

        return ResponseEntity.ok(
                borrowService.updateBorrow(id, borrow)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrow(
            @PathVariable Long id) {

        borrowService.deleteBorrow(id);

        return ResponseEntity.noContent().build();
    }
}