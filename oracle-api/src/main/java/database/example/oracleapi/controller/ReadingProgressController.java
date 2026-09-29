package database.example.oracleapi.controller;

import database.example.oracleapi.entity.ReadingProgress;
import database.example.oracleapi.service.ReadingProgressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reading-progress")
@CrossOrigin(origins = "*")
public class ReadingProgressController {

    private final ReadingProgressService service;

    public ReadingProgressController(
            ReadingProgressService service
    ) {
        this.service = service;
    }


    @GetMapping("/member/{memberId}")
    public List<ReadingProgress> getByMember(
            @PathVariable Long memberId
    ) {

        return service.getByMember(memberId);
    }


    @GetMapping("/{memberId}/{bookId}")
    public ReadingProgress getProgress(
            @PathVariable Long memberId,
            @PathVariable Long bookId
    ) {

        return service.getProgress(
                memberId,
                bookId
        );
    }


    @PutMapping("/{memberId}/{bookId}")
    public ReadingProgress updateProgress(
            @PathVariable Long memberId,
            @PathVariable Long bookId,
            @RequestBody Map<String, Integer> request
    ) {

        Integer progress =
                request.getOrDefault(
                        "progress",
                        0
                );

        return service.updateProgress(
                memberId,
                bookId,
                progress
        );
    }


    @PutMapping("/{memberId}/{bookId}/read")
    public ReadingProgress markAsRead(
            @PathVariable Long memberId,
            @PathVariable Long bookId
    ) {

        return service.markAsRead(
                memberId,
                bookId
        );
    }
}