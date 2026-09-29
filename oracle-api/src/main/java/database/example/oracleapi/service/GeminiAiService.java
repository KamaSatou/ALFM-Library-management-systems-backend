package database.example.oracleapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import database.example.oracleapi.dto.AiSearchIntent;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class GeminiAiService {

    // ==========================================================
    // GEMINI CONFIG
    // ==========================================================

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + "gemini-3.6-flash:generateContent";

    private static final int MAX_RETRIES = 3;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String apiKey;

    public GeminiAiService() {

        this.objectMapper = new ObjectMapper();

        // Tối đa 20 giây để thiết lập kết nối tới Gemini
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        // Đọc API Key từ Environment Variable
        this.apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Khong tim thay GEMINI_API_KEY."
            );
        }
    }

    // ==========================================================
    // PHÂN TÍCH CÂU TÌM KIẾM
    // ==========================================================

    public AiSearchIntent analyzeSearchQuery(String userQuery) {

        try {

            // ==================================================
            // PROMPT
            // ==================================================

            String prompt =
                    """
                    You are an AI search assistant for a library application.

                    Analyze the user's request.

                    Do NOT invent book names.
                    Only analyze what type of book the user wants.

                    Return:
                    - keywords: English search keywords
                    - tags: relevant topic tags
                    - category: one suitable category
                    - explanation: short explanation in Vietnamese

                    Available library categories include:
                    - Self Development
                    - Productivity
                    - Communication
                    - Novel

                    Example:

                    User:
                    Tôi muốn sách giúp xây dựng thói quen tốt

                    Result:
                    keywords = habit, self improvement
                    tags = habit
                    category = Self Development

                    User:
                    Tôi muốn tập trung và làm việc hiệu quả

                    Result:
                    keywords = focus, productivity, deep work
                    tags = productivity
                    category = Productivity

                    User request:
                    """
                    + userQuery;

            // ==================================================
            // RESPONSE JSON SCHEMA
            // ==========================================================

            Map<String, Object> schema = Map.of(
                    "type", "OBJECT",

                    "properties", Map.of(

                            "keywords", Map.of(
                                    "type", "ARRAY",
                                    "items", Map.of(
                                            "type", "STRING"
                                    )
                            ),

                            "tags", Map.of(
                                    "type", "ARRAY",
                                    "items", Map.of(
                                            "type", "STRING"
                                    )
                            ),

                            "category", Map.of(
                                    "type", "STRING"
                            ),

                            "explanation", Map.of(
                                    "type", "STRING"
                            )
                    ),

                    "required", List.of(
                            "keywords",
                            "tags",
                            "category",
                            "explanation"
                    )
            );

            // ==================================================
            // GENERATION CONFIG
            // ==========================================================

            Map<String, Object> generationConfig = Map.of(
                    "responseMimeType", "application/json",
                    "responseSchema", schema
            );

            // ==================================================
            // REQUEST BODY
            // ==========================================================

            Map<String, Object> requestBody = Map.of(

                    "contents", List.of(
                            Map.of(
                                    "parts", List.of(
                                            Map.of(
                                                    "text",
                                                    prompt
                                            )
                                    )
                            )
                    ),

                    "generationConfig",
                    generationConfig
            );

            String requestJson =
                    objectMapper.writeValueAsString(requestBody);

            // ==================================================
            // HTTP REQUEST
            // ==========================================================

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(GEMINI_URL))

                            // Gemini có tối đa 60 giây để xử lý request
                            .timeout(Duration.ofSeconds(60))

                            .header(
                                    "Content-Type",
                                    "application/json"
                            )

                            .header(
                                    "x-goog-api-key",
                                    apiKey
                            )

                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(requestJson)
                            )

                            .build();

            // ==================================================
            // CALL GEMINI
            // RETRY KHI 503 / 429 / TIMEOUT
            // ==========================================================

            HttpResponse<String> response = null;

            for (int attempt = 1;
                 attempt <= MAX_RETRIES;
                 attempt++) {

                try {

                    response =
                            httpClient.send(
                                    request,
                                    HttpResponse.BodyHandlers.ofString()
                            );

                    // ==========================================
                    // THÀNH CÔNG
                    // ==========================================

                    if (response.statusCode() >= 200
                            && response.statusCode() < 300) {

                        System.out.println(
                                "Gemini thanh cong o lan thu "
                                        + attempt
                                        + "."
                        );

                        break;
                    }

                    // ==========================================
                    // GEMINI QUÁ TẢI - HTTP 503
                    // ==========================================

                    if (response.statusCode() == 503) {

                        System.out.println(
                                "Gemini dang qua tai. Thu lai "
                                        + attempt
                                        + "/"
                                        + MAX_RETRIES
                        );

                        if (attempt < MAX_RETRIES) {

                            waitBeforeRetry(attempt);

                            continue;
                        }

                        throw new RuntimeException(
                                "AI dang ban. Vui long thu lai sau."
                        );
                    }

                    // ==========================================
                    // RATE LIMIT - HTTP 429
                    // ==========================================

                    if (response.statusCode() == 429) {

                        System.out.println(
                                "Gemini bi gioi han request (429). Thu lai "
                                        + attempt
                                        + "/"
                                        + MAX_RETRIES
                        );

                        if (attempt < MAX_RETRIES) {

                            waitBeforeRetry(attempt);

                            continue;
                        }

                        throw new RuntimeException(
                                "AI dang nhan qua nhieu yeu cau. "
                                        + "Vui long thu lai sau."
                        );
                    }

                    // ==========================================
                    // HTTP ERROR KHÁC
                    // ==========================================

                    throw new RuntimeException(
                            "Gemini HTTP "
                                    + response.statusCode()
                                    + ": "
                                    + response.body()
                    );

                } catch (HttpConnectTimeoutException e) {

                    System.out.println(
                            "Ket noi Gemini bi timeout. Thu lai "
                                    + attempt
                                    + "/"
                                    + MAX_RETRIES
                    );

                    if (attempt >= MAX_RETRIES) {

                        throw new RuntimeException(
                                "Khong the ket noi Gemini sau "
                                        + MAX_RETRIES
                                        + " lan thu.",
                                e
                        );
                    }

                    waitBeforeRetry(attempt);

                } catch (HttpTimeoutException e) {

                    System.out.println(
                            "Gemini phan hoi qua cham. Thu lai "
                                    + attempt
                                    + "/"
                                    + MAX_RETRIES
                    );

                    if (attempt >= MAX_RETRIES) {

                        throw new RuntimeException(
                                "Gemini phan hoi qua cham. "
                                        + "Vui long thu lai sau.",
                                e
                        );
                    }

                    waitBeforeRetry(attempt);
                }
            }

            // ==================================================
            // KIỂM TRA RESPONSE
            // ==========================================================

            if (response == null) {

                throw new RuntimeException(
                        "Khong nhan duoc phan hoi tu Gemini."
                );
            }

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                throw new RuntimeException(
                        "Gemini HTTP "
                                + response.statusCode()
                                + ": "
                                + response.body()
                );
            }

            // ==================================================
            // ĐỌC JSON GEMINI
            // ==========================================================

            JsonNode root =
                    objectMapper.readTree(
                            response.body()
                    );

            JsonNode candidates =
                    root.path("candidates");

            if (!candidates.isArray()
                    || candidates.size() == 0) {

                throw new RuntimeException(
                        "Gemini khong tra ve candidates."
                );
            }

            JsonNode parts =
                    candidates
                            .get(0)
                            .path("content")
                            .path("parts");

            if (!parts.isArray()
                    || parts.size() == 0) {

                throw new RuntimeException(
                        "Gemini khong tra ve content."
                );
            }

            String aiJson =
                    parts
                            .get(0)
                            .path("text")
                            .asText();

            if (aiJson == null
                    || aiJson.isBlank()) {

                throw new RuntimeException(
                        "Gemini tra ve JSON rong."
                );
            }

            // ==================================================
            // CONVERT JSON -> AiSearchIntent
            // ==========================================================

            return objectMapper.readValue(
                    aiJson,
                    AiSearchIntent.class
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Gemini AI request bi gian doan.",
                    e
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Gemini AI error: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // ==========================================================
    // THỜI GIAN CHỜ TRƯỚC KHI RETRY
    //
    // Lần 1 lỗi -> chờ 2 giây
    // Lần 2 lỗi -> chờ 5 giây
    // ==========================================================

    private void waitBeforeRetry(int attempt)
            throws InterruptedException {

        long waitTime;

        if (attempt == 1) {
            waitTime = 2000;
        } else {
            waitTime = 5000;
        }

        System.out.println(
                "Cho "
                        + (waitTime / 1000)
                        + " giay truoc khi thu lai..."
        );

        Thread.sleep(waitTime);
    }
}