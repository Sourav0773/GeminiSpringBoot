package com.procucev.speech_transcription.repository;

import com.procucev.speech_transcription.entity.TranscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranscriptionRepository extends JpaRepository<TranscriptionEntity, Long> {
    Optional<TranscriptionEntity> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);
}