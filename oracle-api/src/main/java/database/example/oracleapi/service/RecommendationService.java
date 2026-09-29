package database.example.oracleapi.service;

import database.example.oracleapi.dto.RecommendedBookResponse;
import database.example.oracleapi.entity.Book;
import database.example.oracleapi.entity.BorrowDetail;
import database.example.oracleapi.repository.BookRepository;
import database.example.oracleapi.repository.BorrowDetailRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final BookRepository bookRepository;
    private final BorrowDetailRepository borrowDetailRepository;

    public RecommendationService(
            BookRepository bookRepository,
            BorrowDetailRepository borrowDetailRepository
    ) {
        this.bookRepository = bookRepository;
        this.borrowDetailRepository = borrowDetailRepository;
    }

    public List<RecommendedBookResponse> getRecommendations(Long memberId) {

        List<Book> allBooks = bookRepository.findAll();
        List<BorrowDetail> allBorrowDetails = borrowDetailRepository.findAll();

        // Lấy lịch sử đọc/mượn của member
        List<Book> historyBooks = allBorrowDetails.stream()
                .filter(detail ->
                        detail.getBorrow() != null
                                && detail.getBorrow().getMember() != null
                                && detail.getBorrow().getMember().getMemberId() != null
                                && detail.getBorrow().getMember().getMemberId().equals(memberId)
                )
                .map(BorrowDetail::getBook)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Người dùng chưa có lịch sử -> gợi ý sách còn sẵn
        if (historyBooks.isEmpty()) {
            return allBooks.stream()
                    .filter(this::isAvailable)
                    .limit(10)
                    .map(book -> new RecommendedBookResponse(
                            book,
                            0,
                            "Gợi ý cho người dùng mới"
                    ))
                    .collect(Collectors.toList());
        }

        Map<Long, Integer> categoryScores = new HashMap<>();
        Map<Long, Integer> authorScores = new HashMap<>();
        Map<String, Integer> tagScores = new HashMap<>();

        Set<Long> readBookIds = new HashSet<>();

        // Phân tích sở thích từ lịch sử đọc
        for (Book book : historyBooks) {

            if (book.getBookId() != null) {
                readBookIds.add(book.getBookId());
            }

            if (book.getCategoryId() != null) {
                categoryScores.merge(
                        book.getCategoryId(),
                        1,
                        Integer::sum
                );
            }

            if (book.getAuthorId() != null) {
                authorScores.merge(
                        book.getAuthorId(),
                        1,
                        Integer::sum
                );
            }

            if (book.getTag() != null && !book.getTag().isBlank()) {
                String tag = book.getTag().trim().toLowerCase();

                tagScores.merge(
                        tag,
                        1,
                        Integer::sum
                );
            }
        }

        List<RecommendedBookResponse> recommendations = new ArrayList<>();

        // Chấm điểm sách
        for (Book book : allBooks) {

            if (book.getBookId() == null) {
                continue;
            }

            // Không đề xuất sách đã đọc
            if (readBookIds.contains(book.getBookId())) {
                continue;
            }

            // Không đề xuất sách hết
            if (!isAvailable(book)) {
                continue;
            }

            int score = 0;
            List<String> reasons = new ArrayList<>();

            // Cùng thể loại: +5
            if (book.getCategoryId() != null) {

                int count = categoryScores.getOrDefault(
                        book.getCategoryId(),
                        0
                );

                if (count > 0) {
                    score += count * 5;
                    reasons.add("Cùng thể loại với sách bạn từng đọc");
                }
            }

            // Cùng tác giả: +3
            if (book.getAuthorId() != null) {

                int count = authorScores.getOrDefault(
                        book.getAuthorId(),
                        0
                );

                if (count > 0) {
                    score += count * 3;
                    reasons.add("Tác giả bạn từng đọc");
                }
            }

            // Cùng tag: +2
            if (book.getTag() != null && !book.getTag().isBlank()) {

                String tag = book.getTag().trim().toLowerCase();

                int count = tagScores.getOrDefault(tag, 0);

                if (count > 0) {
                    score += count * 2;
                    reasons.add("Chủ đề phù hợp với sở thích của bạn");
                }
            }

            if (score > 0) {
                recommendations.add(
                        new RecommendedBookResponse(
                                book,
                                score,
                                String.join(", ", reasons)
                        )
                );
            }
        }

        // Điểm cao xếp trước
        recommendations.sort(
                Comparator.comparingInt(
                        RecommendedBookResponse::getScore
                ).reversed()
        );

        return recommendations.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    private boolean isAvailable(Book book) {
        return book.getAvailableQuantity() != null
                && book.getAvailableQuantity() > 0;
    }
}