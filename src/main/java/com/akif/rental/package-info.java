/**
 * Rental module - manages rental lifecycle, payments, penalties, and late returns.
 * 
 * <p>This module is responsible for:
 * <ul>
 *   <li>Rental request, confirmation, pickup, and return operations</li>
 *   <li>Payment processing via Stripe integration</li>
 *   <li>Late return detection and penalty calculation</li>
 *   <li>Penalty waiver management</li>
 *   <li>Rental reporting and statistics</li>
 * </ul>
 * 
 * <p>Module dependencies:
 * <ul>
 *   <li>car - for car availability and status management</li>
 *   <li>auth - for user information</li>
 *   <li>currency - for multi-currency pricing</li>
 *   <li>shared - for common infrastructure</li>
 * </ul>
 * 
 * <p>Public API:
 * <ul>
 *   <li>{@link com.akif.rental.RentalService} - Query operations for cross-module access</li>
 *   <li>Domain events - RentalConfirmedEvent, RentalCancelledEvent, etc.</li>
 *   <li>DTOs - RentalDto, RentalSummaryDto for cross-module communication</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    allowedDependencies = {"car", "auth", "currency", "shared"}
)
package com.akif.rental;
