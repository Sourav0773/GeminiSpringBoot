package com.procucev.speech_transcription.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TranscriptionRequest {

    @NotBlank(message = "Transcription text is required")
    @Size(min = 1, max = 5000, message = "Transcription text must be between 1 and 5000 characters")
    private String text;
}
