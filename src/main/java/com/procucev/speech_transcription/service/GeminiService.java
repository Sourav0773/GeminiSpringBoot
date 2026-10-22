package com.procucev.speech_transcription.service;

import com.procucev.speech_transcription.dto.request.GeminiRequest;
import com.procucev.speech_transcription.dto.response.GeminiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class GeminiService {

    private final RestClient restClient;
    private final String apiKey;

    public GeminiService(@Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .build();
    }

    public String processMultilingualText(String rawText) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_GEMINI_API_KEY")) {
            log.warn("Gemini API key not configured. Returning raw transcript.");
            return rawText;
        }

        try {
            String prompt = "Clean up and correctly format this multilingual speech input (which may contain a mix of Hindi, English, or Hinglish). Return only the corrected transcription text:\n" + rawText;

            GeminiRequest request = new GeminiRequest(
                    List.of(new GeminiRequest.Content(List.of(new GeminiRequest.Part(prompt))))
            );

            GeminiResponse response = restClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/models/gemini-3.5-flash-lite:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GeminiResponse.class);

            if (response != null && response.candidates() != null && !response.candidates().isEmpty()) {
                var candidate = response.candidates().get(0);
                if (candidate.content() != null && candidate.content().parts() != null && !candidate.content().parts().isEmpty()) {
                    return candidate.content().parts().get(0).text();
                }
            }
        } catch (Exception e) {
            log.error("Failed to process transcription with Gemini API: {}", e.getMessage(), e);
        }

        return rawText;
    }
}