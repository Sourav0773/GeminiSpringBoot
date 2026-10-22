package com.procucev.speech_transcription.service;

import com.procucev.speech_transcription.dto.request.TranscriptionRequest;
import com.procucev.speech_transcription.entity.TranscriptionEntity;
import com.procucev.speech_transcription.repository.TranscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranscriptionService {

    private final TranscriptionRepository repository;
    private final GeminiService geminiService;

    public Page<TranscriptionEntity> getAllTranscriptions(Pageable pageable) {
        log.info("Fetching transcriptions with pagination");
        return repository.findAll(pageable);
    }

    public TranscriptionEntity saveTranscription(TranscriptionRequest request) {
        log.info("Processing and saving new transcription request");

        String cleanedText = geminiService.processMultilingualText(request.getText());

        TranscriptionEntity transcription = new TranscriptionEntity();
        transcription.setText(cleanedText != null ? cleanedText.trim() : request.getText());

        TranscriptionEntity saved = repository.save(transcription);
        log.info("Transcription saved with internal ID={} and UUID={}", saved.getId(), saved.getUuid());
        return saved;
    }

    public TranscriptionEntity updateTranscription(UUID uuid, TranscriptionRequest request) {
        log.info("Updating transcription with UUID={}", uuid);
        TranscriptionEntity existing = repository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Transcription not found with uuid: " + uuid));

        existing.setText(request.getText());
        return repository.save(existing);
    }

    @Transactional
    public void deleteTranscription(UUID uuid) {
        log.info("Deleting transcription with UUID={}", uuid);
        if (repository.findByUuid(uuid).isEmpty()) {
            throw new RuntimeException("Transcription not found with uuid: " + uuid);
        }
        repository.deleteByUuid(uuid);
        log.info("Transcription deleted successfully for UUID={}", uuid);
    }
}