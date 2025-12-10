/**
 * Car Module - Public API
 * 
 * This module manages car inventory, availability, pricing, and search functionality.
 * 
 * Public API (top-level package):
 * - CarService: Public service interface for cross-module access
 * - CarDto: Cross-module data transfer object
 * - CarSearchCriteria: Search parameters
 * 
 * Internal packages:
 * - domain: Car entity and domain enums
 * - internal: Service implementations, availability, pricing logic
 * - repository: Data access layer
 * - web: REST controllers
 * - mapper: Entity-DTO mappings
 * 
 * Dependencies: currency, shared
 */
@org.springframework.modulith.ApplicationModule(
    allowedDependencies = {"currency", "shared"}
)
package com.akif.car;
