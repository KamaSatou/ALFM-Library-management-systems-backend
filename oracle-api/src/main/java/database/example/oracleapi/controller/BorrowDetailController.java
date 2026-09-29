package database.example.oracleapi.controller;

import database.example.oracleapi.entity.BorrowDetail;
import database.example.oracleapi.service.BorrowDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-details")
@CrossOrigin(origins = "*")
public class BorrowDetailController {

    private final BorrowDetailService borrowDetailService;

    public BorrowDetailController(
            BorrowDetailService borrowDetailService) {

        this.borrowDetailService = borrowDetailService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowDetail>> getAllBorrowDetails() {

        return ResponseEntity.ok(
                borrowDetailService.getAllBorrowDetails()
        );
    }

    @GetMapping("/{borrowId}/{bookId}")
    public ResponseEntity<BorrowDetail> getBorrowDetail(
            @PathVariable Long borrowId,
            @PathVariable Long bookId) {

        return ResponseEntity.ok(
                borrowDetailService.getBorrowDetail(
                        borrowId,
                        bookId
                )
        );
    }

    @PostMapping
    public ResponseEntity<BorrowDetail> createBorrowDetail(
            @RequestBody BorrowDetail borrowDetail) {

        BorrowDetail created =
                borrowDetailService.createBorrowDetail(
                        borrowDetail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{borrowId}/{bookId}")
    public ResponseEntity<BorrowDetail> updateBorrowDetail(
            @PathVariable Long borrowId,
            @PathVariable Long bookId,
            @RequestBody BorrowDetail borrowDetail) {

        return ResponseEntity.ok(
                borrowDetailService.updateBorrowDetail(
                        borrowId,
                        bookId,
                        borrowDetail
                )
        );
    }

    @DeleteMapping("/{borrowId}/{bookId}")
    public ResponseEntity<Void> deleteBorrowDetail(
            @PathVariable Long borrowId,
            @PathVariable Long bookId) {

        borrowDetailService.deleteBorrowDetail(
                borrowId,
                bookId
        );

        return ResponseEntity.noContent().build();
    }
}