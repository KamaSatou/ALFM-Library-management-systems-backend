package database.example.oracleapi.service;

import database.example.oracleapi.dto.AiSearchIntent;
import database.example.oracleapi.entity.Book;
import database.example.oracleapi.entity.Category;
import database.example.oracleapi.repository.BookRepository;
import database.example.oracleapi.repository.CategoryRepository;

import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class AiSearchService {

    // Sách phải đạt ít nhất số điểm này mới được coi là phù hợp
    private static final int MIN_RELEVANCE_SCORE = 10;

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final GeminiAiService geminiAiService;

    public AiSearchService(
            BookRepository bookRepository,
            CategoryRepository categoryRepository,
            GeminiAiService geminiAiService
    ) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.geminiAiService = geminiAiService;
    }

    // ==========================================================
    // SEARCH
    // ==========================================================

    public List<Book> search(String query) {

        if (query == null || query.isBlank()) {
            return new ArrayList<>();
        }

        String cleanQuery = query.trim();

        AiSearchIntent intent;

        // ======================================================
        // THỬ GEMINI TRƯỚC
        // ======================================================

        try {

            System.out.println();
            System.out.println(
                    "===== DANG PHAN TICH BANG GEMINI ====="
            );

            intent = geminiAiService
                    .analyzeSearchQuery(cleanQuery);

            System.out.println(
                    "Gemini AI phan tich thanh cong."
            );

        } catch (Exception e) {

            // ==================================================
            // GEMINI LỖI -> FALLBACK
            // ==================================================

            System.out.println();
            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "Gemini khong kha dung."
            );

            System.out.println(
                    "Ly do: " + e.getMessage()
            );

            System.out.println(
                    "Chuyen sang FALLBACK SEARCH."
            );

            System.out.println(
                    "=========================================="
            );

            intent = createFallbackIntent(cleanQuery);
        }

        // ======================================================
        // LẤY SÁCH + CATEGORY TỪ ORACLE
        // ======================================================

        List<Book> books =
                bookRepository.findAll();

        List<Category> categories =
                categoryRepository.findAll();

        List<ScoredBook> scoredBooks =
                new ArrayList<>();

        // ======================================================
        // CHẤM ĐIỂM TỪNG SÁCH
        // ======================================================

        for (Book book : books) {

            // Không còn sách thì không đề xuất
            if (book.getAvailableQuantity() == null
                    || book.getAvailableQuantity() <= 0) {

                continue;
            }

            int score =
                    calculateScore(
                            book,
                            categories,
                            intent
                    );

            // Chấm thêm bằng query gốc
            score += calculateDirectQueryScore(
                    book,
                    categories,
                    cleanQuery
            );

            System.out.println(
                    "BOOK: "
                            + book.getTitle()
                            + " | SCORE: "
                            + score
            );

            // ==================================================
            // CHỈ NHẬN SÁCH ĐỦ LIÊN QUAN
            // ==================================================

            if (score >= MIN_RELEVANCE_SCORE) {

                scoredBooks.add(
                        new ScoredBook(
                                book,
                                score
                        )
                );
            }
        }

        // ======================================================
        // SẮP XẾP ĐIỂM CAO -> THẤP
        // ======================================================

        scoredBooks.sort(
                Comparator.comparingInt(
                        ScoredBook::getScore
                ).reversed()
        );

        List<Book> result =
                scoredBooks.stream()
                        .limit(10)
                        .map(ScoredBook::getBook)
                        .toList();

        // ======================================================
        // LOG
        // ======================================================

        if (result.isEmpty()) {

            System.out.println(
                    "AI Search: KHONG TIM THAY SACH PHU HOP."
            );

        } else {

            System.out.println(
                    "AI Search tim duoc "
                            + result.size()
                            + " sach phu hop."
            );
        }

        return result;
    }

    // ==========================================================
    // ANALYZE
    // ==========================================================

    public AiSearchIntent analyze(String query) {

        if (query == null || query.isBlank()) {
            return new AiSearchIntent();
        }

        try {

            return geminiAiService
                    .analyzeSearchQuery(query);

        } catch (Exception e) {

            System.out.println(
                    "Gemini analyze loi -> dung fallback."
            );

            return createFallbackIntent(query);
        }
    }

    // ==========================================================
    // FALLBACK INTENT
    // ==========================================================

    private AiSearchIntent createFallbackIntent(
            String query
    ) {

        AiSearchIntent intent =
                new AiSearchIntent();

        String normalizedQuery =
                normalize(query);

        List<String> keywords =
                new ArrayList<>();

        List<String> tags =
                new ArrayList<>();

        String category = "";

        // ======================================================
        // PRODUCTIVITY / FOCUS
        // ======================================================

        if (containsAny(
                normalizedQuery,
                "tap trung",
                "focus",
                "nang suat",
                "productivity",
                "lam viec",
                "hieu qua",
                "quan ly thoi gian",
                "time management",
                "deep work"
        )) {

            keywords.add("focus");
            keywords.add("productivity");
            keywords.add("deep work");
            keywords.add("time management");

            tags.add("focus");
            tags.add("productivity");

            category = "Productivity";
        }

        // ======================================================
        // HABIT / SELF DEVELOPMENT
        // ======================================================

        if (containsAny(
                normalizedQuery,
                "thoi quen",
                "habit",
                "phat trien ban than",
                "self development",
                "self improvement",
                "ky luat",
                "dong luc",
                "motivation"
        )) {

            keywords.add("habit");
            keywords.add("self improvement");
            keywords.add("self development");

            tags.add("habit");
            tags.add("self development");

            if (category.isBlank()) {
                category = "Self Development";
            }
        }

        // ======================================================
        // COMMUNICATION
        // ======================================================

        if (containsAny(
                normalizedQuery,
                "giao tiep",
                "communication",
                "noi chuyen",
                "thuyet phuc",
                "persuasion",
                "ung xu",
                "quan he",
                "relationship"
        )) {

            keywords.add("communication");
            keywords.add("persuasion");
            keywords.add("relationship");

            tags.add("communication");

            if (category.isBlank()) {
                category = "Communication";
            }
        }

        // ======================================================
        // NOVEL
        // ======================================================

        if (containsAny(
                normalizedQuery,
                "tieu thuyet",
                "novel",
                "truyen",
                "fiction",
                "van hoc"
        )) {

            keywords.add("novel");
            keywords.add("fiction");

            tags.add("novel");

            if (category.isBlank()) {
                category = "Novel";
            }
        }

        // ======================================================
        // KHÔNG NHẬN DIỆN ĐƯỢC CHỦ ĐỀ
        // ======================================================

        if (keywords.isEmpty()) {

            String[] words =
                    normalizedQuery.split("\\s+");

            for (String word : words) {

                if (word.length() >= 4
                        && !isStopWord(word)) {

                    keywords.add(word);
                }
            }
        }

        intent.setKeywords(keywords);
        intent.setTags(tags);
        intent.setCategory(category);

        intent.setExplanation(
                "Gemini tam thoi khong kha dung. "
                        + "He thong dang tim sach bang che do fallback."
        );

        return intent;
    }

    // ==========================================================
    // CHẤM ĐIỂM AI INTENT
    // ==========================================================

    private int calculateScore(
            Book book,
            List<Category> categories,
            AiSearchIntent intent
    ) {

        int score = 0;

        String title =
                normalize(book.getTitle());

        String tag =
                normalize(book.getTag());

        // ======================================================
        // KEYWORDS
        // ======================================================

        if (intent.getKeywords() != null) {

            for (String keyword :
                    intent.getKeywords()) {

                String k =
                        normalize(keyword);

                if (k.isBlank()) {
                    continue;
                }

                // Khớp nguyên keyword trong title
                if (title.contains(k)) {
                    score += 10;
                }

                // Khớp nguyên keyword trong tag
                if (tag.contains(k)) {
                    score += 8;
                }

                String[] words =
                        k.split("\\s+");

                for (String word : words) {

                    if (word.length() < 3
                            || isStopWord(word)) {

                        continue;
                    }

                    if (title.contains(word)) {
                        score += 3;
                    }

                    if (tag.contains(word)) {
                        score += 2;
                    }
                }
            }
        }

        // ======================================================
        // TAG
        // ==========================================================

        if (intent.getTags() != null) {

            for (String aiTag :
                    intent.getTags()) {

                String normalizedTag =
                        normalize(aiTag);

                if (!normalizedTag.isBlank()
                        && tag.contains(normalizedTag)) {

                    score += 15;
                }
            }
        }

        // ======================================================
        // CATEGORY
        // ==========================================================

        if (book.getCategoryId() != null
                && intent.getCategory() != null
                && !intent.getCategory().isBlank()) {

            String aiCategory =
                    normalize(
                            intent.getCategory()
                    );

            for (Category category :
                    categories) {

                if (category.getCategoryId() == null) {
                    continue;
                }

                if (!category
                        .getCategoryId()
                        .equals(
                                book.getCategoryId()
                        )) {

                    continue;
                }

                String categoryName =
                        normalize(
                                category.getCategoryName()
                        );

                if (categoryName.equals(aiCategory)
                        || categoryName.contains(aiCategory)
                        || aiCategory.contains(categoryName)) {

                    score += 20;
                }
            }
        }

        return score;
    }

    // ==========================================================
    // CHẤM QUERY GỐC
    // ==========================================================

    private int calculateDirectQueryScore(
            Book book,
            List<Category> categories,
            String query
    ) {

        int score = 0;

        String normalizedQuery =
                normalize(query);

        String title =
                normalize(book.getTitle());

        String tag =
                normalize(book.getTag());

        // ======================================================
        // KHỚP NGUYÊN QUERY
        // ==========================================================

        if (!normalizedQuery.isBlank()) {

            if (title.contains(normalizedQuery)) {
                score += 30;
            }

            if (tag.contains(normalizedQuery)) {
                score += 25;
            }
        }

        // ======================================================
        // KHỚP TỪ KHÓA QUERY
        // ==========================================================

        String[] queryWords =
                normalizedQuery.split("\\s+");

        for (String word : queryWords) {

            // Bỏ từ quá ngắn / từ vô nghĩa
            if (word.length() < 4
                    || isStopWord(word)) {

                continue;
            }

            if (title.contains(word)) {
                score += 4;
            }

            if (tag.contains(word)) {
                score += 3;
            }
        }

        // ======================================================
        // CATEGORY
        // ==========================================================

        if (book.getCategoryId() != null) {

            for (Category category :
                    categories) {

                if (category.getCategoryId() == null) {
                    continue;
                }

                if (!category
                        .getCategoryId()
                        .equals(
                                book.getCategoryId()
                        )) {

                    continue;
                }

                String categoryName =
                        normalize(
                                category.getCategoryName()
                        );

                if (!categoryName.isBlank()
                        && normalizedQuery.contains(categoryName)) {

                    score += 20;
                }
            }
        }

        return score;
    }

    // ==========================================================
    // STOP WORD
    // Loại những từ chung chung để tránh match sai
    // ==========================================================

    private boolean isStopWord(
            String word
    ) {

        return word.equals("toi")
                || word.equals("muon")
                || word.equals("sach")
                || word.equals("giup")
                || word.equals("cho")
                || word.equals("cua")
                || word.equals("nhung")
                || word.equals("mot")
                || word.equals("cac")
                || word.equals("tim")
                || word.equals("doc")
                || word.equals("ve")
                || word.equals("voi")
                || word.equals("trong")
                || word.equals("nay")
                || word.equals("the")
                || word.equals("book")
                || word.equals("want")
                || word.equals("about")
                || word.equals("help");
    }

    // ==========================================================
    // CONTAINS ANY
    // ==========================================================

    private boolean containsAny(
            String text,
            String... values
    ) {

        for (String value : values) {

            if (text.contains(
                    normalize(value)
            )) {

                return true;
            }
        }

        return false;
    }

    // ==========================================================
    // NORMALIZE
    // ==========================================================

    private String normalize(
            String value
    ) {

        if (value == null) {
            return "";
        }

        String normalized =
                Normalizer.normalize(
                        value,
                        Normalizer.Form.NFD
                );

        normalized =
                normalized.replaceAll(
                        "\\p{M}",
                        ""
                );

        return normalized
                .toLowerCase(Locale.ROOT)
                .trim();
    }

    // ==========================================================
    // SCORED BOOK
    // ==========================================================

    private static class ScoredBook {

        private final Book book;
        private final int score;

        public ScoredBook(
                Book book,
                int score
        ) {
            this.book = book;
            this.score = score;
        }

        public Book getBook() {
            return book;
        }

        public int getScore() {
            return score;
        }
    }
}