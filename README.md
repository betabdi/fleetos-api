# FleetOS API — v0.1

A **Java 21 + Spring Boot + PostgreSQL** fleet operations API designed around the workflows of a family-operated box-truck business. **Portfolio MVP: not yet deployed or used in production.** No real company data or customer information is included.

## What's working in v0.1
- Register and list vehicles; look up a vehicle and change its service status.
- Register and list drivers.
- Schedule loads/trips with pickup/delivery times, route, and freight rate.
- Reject assignments to out-of-service vehicles, inactive drivers, and overlapping driver/truck schedules (basic application-level check).
- Request validation, consistent HTTP error responses, relational persistence, Flyway migrations, and basic API tests.

**Not implemented yet:** login, organization isolation, fleet finance, route tracking, maintenance records, UI, live deployment. **Do not expose the API to the public internet or enter private business data:** endpoints have no authentication. The overlap check is not safe against concurrent dispatcher requests; add transactional database locking before operational use.

## Requirements
- Java 21
- Maven 3.6.3+
- Optional: Docker for PostgreSQL

## Quick start (zero Docker required)
```bash
mvn spring-boot:run
```
Uses an **in-memory H2 database** for quick local experiments. Data resets when the app stops.

## Persist data with PostgreSQL
```bash
docker compose up -d postgres
SPRING_PROFILES_ACTIVE=postgres mvn spring-boot:run
```
For local development only: the Compose database uses `localdev` by default. Set a strong `DB_PASSWORD` before using anywhere beyond your machine; set `DB_URL`, `DB_USER`, `DB_PASSWORD` as needed. Never commit secrets.

## Test
```bash
mvn test
```

## Try the API
```bash
# Create a truck
curl -i -X POST http://localhost:8080/api/v1/vehicles \
  -H 'Content-Type: application/json' \
  -d '{"plate":"MN-BOX-101","make":"Freightliner","model":"M2","year":2021,"odometerMiles":125000}'

# Create a driver
curl -i -X POST http://localhost:8080/api/v1/drivers \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Demo Driver","phone":"555-0100"}'

# Schedule a trip (IDs 1 and 1 in a fresh DB)
curl -i -X POST http://localhost:8080/api/v1/trips \
  -H 'Content-Type: application/json' \
  -d '{"vehicleId":1,"driverId":1,"origin":"Minneapolis, MN","destination":"Chicago, IL","pickupAt":"2027-01-05T08:00:00-06:00","deliveryAt":"2027-01-05T17:00:00-06:00","freightRate":2100.00}'

# View the records
curl http://localhost:8080/api/v1/vehicles
curl http://localhost:8080/api/v1/drivers
curl http://localhost:8080/api/v1/trips
```

## Architecture
`HTTP Controller → Service (business rules) → JPA Repository → Database`.

Modules are grouped by domain (`vehicle`, `driver`, `trip`) instead of a single giant controllers/services folder.

## Roadmap
- **v0.2**: lifecycle transitions, audit trail, pagination, error hardening, full integration tests.
- **v0.3**: secured organization accounts and role-based access; tenant isolation tests.
- **v0.4**: fuel, expenses, maintenance, and profitability calculations.
- **v0.5**: operational dashboard and deployment with monitoring, backup, and security review.

## Real-world motivation
FleetOS is being developed **for potential use** in a family-operated box-truck business. Requirements will be validated with real dispatch workflows before making any claims about production adoption.

## License
No license has been selected yet. Contact the repository owner before reuse.
