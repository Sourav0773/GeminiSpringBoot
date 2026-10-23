package com.procucev.speech_transcription.controllers;

import com.procucev.speech_transcription.dto.request.TranscriptionRequest;
import com.procucev.speech_transcription.entity.TranscriptionEntity;
import com.procucev.speech_transcription.service.TranscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/transcriptions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    @GetMapping("/get-all-transcriptions")
    public ResponseEntity<Page<TranscriptionEntity>> getAllTranscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("API called: GET /api/transcriptions | page={} | size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(transcriptionService.getAllTranscriptions(pageable));
    }

    @PostMapping("/gemini")
    public ResponseEntity<TranscriptionEntity> saveTranscription(@Valid @RequestBody TranscriptionRequest request) {
        log.info("API called: POST /api/transcriptions");
        return ResponseEntity.ok(transcriptionService.saveTranscription(request));
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<TranscriptionEntity> updateTranscription(
            @PathVariable UUID uuid,
            @Valid @RequestBody TranscriptionRequest request) {
        log.info("API called: PUT /api/transcriptions/{}", uuid);
        return ResponseEntity.ok(transcriptionService.updateTranscription(uuid, request));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<String> deleteTranscription(@PathVariable UUID uuid) {
        log.info("API called: DELETE /api/transcriptions/{}", uuid);
        transcriptionService.deleteTranscription(uuid);
        return ResponseEntity.ok().build();
    }
}