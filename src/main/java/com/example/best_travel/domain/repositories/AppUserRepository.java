package com.example.best_travel.domain.repositories;

import com.example.best_travel.domain.entities.documents.AppUserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends MongoRepository<AppUserDocument, String> {
    Optional<AppUserDocument> findByUsername(String username);
}
