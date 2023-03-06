package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.GoodsDonation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsDonationRepository extends JpaRepository<GoodsDonation, Long> {
}
