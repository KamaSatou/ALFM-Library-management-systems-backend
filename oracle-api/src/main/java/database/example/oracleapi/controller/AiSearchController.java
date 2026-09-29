package database.example.oracleapi.controller;

import database.example.oracleapi.dto.AiSearchIntent;
import database.example.oracleapi.entity.Book;
import database.example.oracleapi.service.AiSearchService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiSearchController {

    private final AiSearchService aiSearchService;

    public AiSearchController(
            AiSearchService aiSearchService
    ) {
        this.aiSearchService = aiSearchService;
    }

    // =====================================================
    // AI SEARCH THẬT
    // =====================================================

    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(
            @RequestParam("q") String query
    ) {

        return ResponseEntity.ok(
                aiSearchService.search(query)
        );
    }

    // =====================================================
    // ENDPOINT DEBUG
    // Xem Gemini hiểu câu người dùng như thế nào
    // =====================================================

    @GetMapping("/analyze")
    public ResponseEntity<AiSearchIntent> analyze(
            @RequestParam("q") String query
    ) {

        return ResponseEntity.ok(
                aiSearchService.analyze(query)
        );
    }
}