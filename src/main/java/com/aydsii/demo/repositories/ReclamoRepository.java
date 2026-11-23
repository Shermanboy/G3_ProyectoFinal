package com.aydsii.demo.repositories;

import com.aydsii.demo.entities.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
}