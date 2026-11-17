# FleetOS API — v0.3

A **Java 21 + Spring Boot + PostgreSQL** fleet operations API designed around the workflows of a family-operated box-truck business. **Portfolio MVP: not yet deployed or used in production.** No real company data or customer information is included.

## What's working in v0.3
- Authenticate every API request with a hashed API key supplied through `X-FleetOS-Api-Key`.
- Isolate vehicles, drivers, trips, scheduling checks, and history by organization.
- Enforce `ADMIN`, `DISPATCHER`, and read-only `VIEWER` roles.
- Register and list vehicles; look up a vehicle and change its service status.
- Register and list drivers.
- Schedule loads/trips with pickup/delivery times, route, and freight rate.
- Move trips through guarded `PLANNED → IN_PROGRESS → COMPLETED` or cancellation workflows.
- Preserve an immutable status audit trail, including optional dispatcher notes.
- Paginate vehicle, driver, and trip collections with a maximum page size of 100.
- Reject assignments to out-of-service vehicles, inactive drivers, and overlapping driver/truck schedules (basic application-level check).
- Request and field-length validation, safe malformed-request errors, relational persistence, additive Flyway migrations, and lifecycle integration tests.

**Not implemented yet:** fleet finance, route tracking, maintenance records, UI, and live deployment. The overlap check is not safe against concurrent dispatcher requests; add transactional database locking before operational use.

## Credential management
Only an `ADMIN` key can list, create, or revoke organization credentials. A newly generated 256-bit key is returned once; only its SHA-256 hash is persisted.

```bash
curl -X POST http://localhost:8080/api/v1/admin/credentials \
  -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" \
  -H 'Content-Type: application/json' \
  -d '{"name":"evening-dispatch","role":"DISPATCHER"}'
```

## Requirements
- Java 21
- Maven 3.6.3+
- Optional: Docker for PostgreSQL

## Quick start (zero Docker required)
```bash
export FLEETOS_BOOTSTRAP_ORG_SLUG=demo-fleet
export FLEETOS_BOOTSTRAP_ORG_NAME='Demo Fleet'
export FLEETOS_BOOTSTRAP_API_KEY="$(openssl rand -hex 32)"
mvn spring-boot:run
```
Uses an **in-memory H2 database** for quick local experiments. Data resets when the app stops.

Save the generated key securely. FleetOS stores only its SHA-256 hash. If bootstrap variables are omitted, no credential is created and API requests remain locked. Use a high-entropy key; never commit it.

When upgrading a v0.2 PostgreSQL database, set `FLEETOS_BOOTSTRAP_ORG_SLUG=legacy` for the first administrator to access the migrated records.

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
  -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" \
  -H 'Content-Type: application/json' \
  -d '{"plate":"MN-BOX-101","make":"Freightliner","model":"M2","year":2021,"odometerMiles":125000}'

# Create a driver
curl -i -X POST http://localhost:8080/api/v1/drivers \
  -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Demo Driver","phone":"555-0100"}'

# Schedule a trip (IDs 1 and 1 in a fresh DB)
curl -i -X POST http://localhost:8080/api/v1/trips \
  -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" \
  -H 'Content-Type: application/json' \
  -d '{"vehicleId":1,"driverId":1,"origin":"Minneapolis, MN","destination":"Chicago, IL","pickupAt":"2027-01-05T08:00:00-06:00","deliveryAt":"2027-01-05T17:00:00-06:00","freightRate":2100.00}'

# View the records
curl -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" 'http://localhost:8080/api/v1/vehicles?page=0&size=20'
curl -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" 'http://localhost:8080/api/v1/drivers?page=0&size=20'
curl -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" 'http://localhost:8080/api/v1/trips?page=0&size=20&sort=pickupAt,asc'

# Start trip 1 and attach an audit note
curl -i -X PATCH http://localhost:8080/api/v1/trips/1/status \
  -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" \
  -H 'Content-Type: application/json' \
  -d '{"status":"IN_PROGRESS","note":"Driver departed terminal"}'

# View trip 1's complete lifecycle history
curl -H "X-FleetOS-Api-Key: $FLEETOS_BOOTSTRAP_API_KEY" http://localhost:8080/api/v1/trips/1/history
```

## Architecture
`HTTP Controller → Service (business rules) → JPA Repository → Database`.

Modules are grouped by domain (`vehicle`, `driver`, `trip`) instead of a single giant controllers/services folder.

## Roadmap
- **v0.2**: lifecycle transitions, audit trail, pagination, error hardening, and lifecycle integration tests.
- **v0.3 (current)**: hashed API-key authentication, organization isolation, role-based access, and tenant-isolation tests.
- **v0.4**: fuel, expenses, maintenance, and profitability calculations.
- **v0.5**: operational dashboard and deployment with monitoring, backup, and security review.

## Real-world motivation
FleetOS is being developed **for potential use** in a family-operated box-truck business. Requirements will be validated with real dispatch workflows before making any claims about production adoption.

## License
No license has been selected yet. Contact the repository owner before reuse.
