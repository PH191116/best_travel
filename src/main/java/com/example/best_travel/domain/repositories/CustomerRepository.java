package com.example.best_travel.domain.repositories;

import com.example.best_travel.domain.entities.jpa.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
}
