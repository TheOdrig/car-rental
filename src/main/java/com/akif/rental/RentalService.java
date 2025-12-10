package com.akif.rental;

import com.akif.rental.internal.dto.request.RentalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RentalService {

    RentalResponse requestRental(RentalRequest request, String username);

    RentalResponse confirmRental(Long rentalId);

    RentalResponse pickupRental(Long rentalId, String pickupNotes);

    RentalResponse returnRental(Long rentalId, String returnNotes);

    RentalResponse cancelRental(Long rentalId, String username);

    Page<RentalResponse> getMyRentals(String username, Pageable pageable);

    Page<RentalResponse> getAllRentals(Pageable pageable);

    RentalResponse getRentalById(Long id, String username);
}