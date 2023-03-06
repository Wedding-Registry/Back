package com.wedding.serviceapi.guests.repository;

import com.wedding.serviceapi.guests.domain.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Long> {
}
