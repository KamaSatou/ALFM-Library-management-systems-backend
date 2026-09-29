package database.example.oracleapi.controller;

import database.example.oracleapi.dto.RecommendedBookResponse;
import database.example.oracleapi.service.RecommendationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService
    ) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations/{memberId}")
    public ResponseEntity<List<RecommendedBookResponse>> getRecommendations(
            @PathVariable Long memberId
    ) {
        return ResponseEntity.ok(
                recommendationService.getRecommendations(memberId)
        );
    }
}