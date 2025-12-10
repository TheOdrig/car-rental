package com.akif.rental.repository;

import com.akif.rental.domain.model.PenaltyWaiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaltyWaiverRepository extends JpaRepository<PenaltyWaiver, Long> {

    List<PenaltyWaiver> findByRentalIdAndIsDeletedFalse(Long rentalId);
}
