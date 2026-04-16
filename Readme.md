# B2B Order Tracker API

A production-style RESTful backend that simulates EDI B2B transaction flows
through a supply chain system — built with Java 17 and Spring Boot 3.x.

## 🔗 Live Demo
**Base URL:** `<your-render-url>`  
**Swagger UI:** `<your-render-url>/swagger-ui.html`

---

## 💡 What This Project Does

Models the complete lifecycle of a B2B order — from Purchase Order creation
through shipment and invoicing — mirroring how EDI transactions (850, 856, 810)
flow in real supply chain systems like IBM Sterling B2B Integrator.

**Order Lifecycle:**

**EDI Mapping:**
| Module | EDI Equivalent |
|---|---|
| Orders | 850 - Purchase Order |
| Shipments | 856 - Advance Ship Notice |
| Invoices | 810 - Invoice |
| Audit Log | Transaction Control Logs |

---

## 🏗️ Architecture

- Layered monolith with clear separation of concerns
- Business rules enforced at service layer
- Full audit trail logged on every state change
- Global exception handling with clean error responses

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| ORM | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| API Docs | Springdoc OpenAPI (Swagger) |
| Deployment | Render |

---

## 📡 Key API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/partners | Create trading partner |
| POST | /api/orders | Create purchase order |
| PUT | /api/orders/{id}/status | Update order status |
| POST | /api/shipments | Create shipment (ASN) |
| POST | /api/invoices | Create invoice |
| PUT | /api/invoices/{id}/status | Mark invoice paid |
| GET | /api/orders/{id}/events | Full audit trail |

---

## ⚙️ Run Locally

### Prerequisites
- Java 17
- PostgreSQL
- Maven

### Steps

```bash
# Clone the repo
git clone https://github.com/PrakashSharma2000/b2b-order-tracker.git

# Create database
psql -U postgres -c "CREATE DATABASE b2b_tracker;"

# Update src/main/resources/application.properties
# with your PostgreSQL credentials

# Run
./mvnw spring-boot:run
```

Open Swagger UI at: `http://localhost:8080/swagger-ui.html`

---

## 🔄 Example Flow

```bash
# 1. Create a trading partner
POST /api/partners

# 2. Create an order
POST /api/orders

# 3. Confirm the order
PUT /api/orders/1/status?status=CONFIRMED

# 4. Create shipment — order auto-moves to SHIPPED
POST /api/shipments

# 5. Create invoice — order auto-moves to INVOICED
POST /api/invoices

# 6. Mark invoice paid — order auto-moves to CLOSED
PUT /api/invoices/1/status?status=PAID

# 7. View full audit trail
GET /api/orders/1/events
```