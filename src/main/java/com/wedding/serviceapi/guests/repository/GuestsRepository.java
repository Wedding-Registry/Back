package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.Guests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestsRepository extends JpaRepository<Guests, Long> {
}
