# Route Express - E-commerce Back Office
Administrative Back Office for an e-commerce platform built with Spring Boot, Spring Security, JPA/Hibernate and MySQL.


## Project Background


This project was originally developed in 2015 as an academic e-commerce system.

The application is designed using a layered architecture, separating controllers, services, and persistence logic. The focus is on backend robustness, data integrity, and maintainability. This project is a modern rewrite of an earlier academic e-commerce system, rebuilt to apply current backend technologies and cleaner architectural patterns.

It now serves two distinct audiences from the same Spring Boot application: an administrative Back Office, rendered server-side with Thymeleaf and protected by session-based Spring Security, and a public/authenticated REST API, consumed by a separate Angular storefront, covering catalog browsing, shopping cart, customer accounts and JWT based authentication.

## Original Technologies (2015)


- Java
- JSP / Servlets
- JDBC
- Bootstrap
- Session based authentication 


## Modern Stack (Current Version)


- Java 17
- Spring Boot
- Spring MVC + Thymeleaf (admin Back Office)
- JPA / Hibernate
- MySQL
- Spring Mail (account confirmation / password setup e-mails)
- Spring Scheduling (@Scheduled background jobs)
- JWT (io.jsonwebtoken / jjwt) for stateless customer authentication
- RESTful architecture


## Features


Customer Management


- Customer registration and maintenance.
- Address management linked to customer accounts.
- Wish list management.
- Automatic cleanup of related records when a customer is removed.
- Admin created customers receive an e-mail with a one time link to set their initial password and confirm their account (see Account & E-mail Confirmation below).


Beer Catalog Management


- Brewery registration and management.
- Beer registration and maintenance.
- Inventory management.
- Product image upload support (up to three images per product).


Customer Facing Catalog API


- Paginated, filterable beer listing (GET /api/cervejas), automatically excluding products with no available stock.
- Country filter (?pais=) and price sorting (?ordenarPreco=asc|desc), resolved through dedicated JPQL queries (see Technical Notes).
- Full text search by name (GET /api/cervejas/buscar), supporting the same country/price filters.
- Distinct country listing endpoint (GET /api/cervejas/paises) to populate storefront filters.
- Product detail lookup by id.


Shopping Cart API


- Guest friendly cart, identified by a client generated session UUID (no login required).
- Add item, update quantity, remove item (/api/carrinho/**); decreasing quantity to zero automatically removes the item.
- Cart and cart item totals computed server side and returned through dedicated DTOs, never exposing JPA entities directly.
- Cart merge on login (POST /api/carrinho/merge, authenticated): links the guest session cart to the now authenticated customer. If the customer has no cart yet, the guest cart is adopted as-is; if they already have one (e.g. from a previous login on another device), item quantities are summed into the existing cart and the guest cart record is discarded.


Cart Maintenance


- Scheduled job to automatically clean up abandoned guest carts after a configurable period of inactivity (default: 7 days).


Account & E-mail Confirmation

Two registration flows share the same token + e-mail infrastructure:


- Admincreated customer: the admin registers the customer through the Back Office without a password. The system generates a one time token and e-mails the customer a link to set their password, where they also confirm their e-mail in the same step.
- Customer self registration: the customer registers directly on the storefront, choosing their own password up front. The system e-mails a confirmation link that activates the account.
- E-mail delivery is handled by a dedicated EmailService (Spring Mail), configured against a Mailtrap sandbox SMTP server for development/testing, with credentials supplied via environment variables (never committed to source control).
- Confirmation tokens are single use: they are cleared from the database immediately after being consumed, so a link can't be replayed.


Customer Authentication (JWT)

- POST /api/clientes/login validates e-mail/password (BCrypt) and requires the account to have a confirmed e-mail before issuing a token.
- Stateless JWT authentication, completely independent from the admin's session-based login: the two coexist in the same SecurityConfig without interfering with each other.
- A JwtAuthenticationFilter validates the token on each request and populates the Spring Security context with the authenticated customer's id, without touching the database on every call.
- GET /api/clientes/me is an authenticated-only endpoint used to confirm the filter correctly resolves the logged-in customer from the token.
Unauthenticated requests to /api/** return a clean JSON 401 response instead of being redirected to the admin's HTML login page (see Technical Notes).



Data Integrity


- Business rules enforced at the service layer.
- Automatic cleanup of orphan records and uploaded files.
- Relational entity management using JPA/Hibernate.
- Response DTOs used throughout the customer facing API (catalog, cart, account) to avoid leaking JPA entities and, in particular, to avoid ever serializing password fields back to the client.



Security & Authentication


- Admin access protected with Spring Security.
- Predefined master administrator account.
- Mandatory password change on first login.
- Session timeout protection for inactive users.
- Password hashing using BCrypt applied to admin users, and to customer passwords from the moment a customer sets/confirms their own password.
- Role based access control (ADMIN / OPERATOR).
- Public, unauthenticated access explicitly scoped to customer facing routes (catalog, cart, registration/confirmation endpoints), kept separate from the admin's session protected routes.



Technical Features


- Layered architecture (Controller, Service, Repository).
- JPA/Hibernate entity relationships.
- Multipart image upload support.
- REST-oriented backend architecture.
- MySQL persistence layer.


## Architecture
```text
┌─────────────┐
│ Controllers │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Services   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│Repositories │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│    MySQL    │
└─────────────┘
```


## Screenshot


## Demo Video


## Technical Notes


1) During development, multipart file uploads required explicit Tomcat configuration due to changes in Spring Boot security defaults. The following property was added:

server.tomcat.max-part-count=30


2) Guest cart lifecycle. Abandoned guest carts (identified by session UUID, with no login required) are automatically removed via a scheduled job (@Scheduled), based on a dataAtualizacao timestamp updated on every cart interaction. This prevents unbounded growth of the carrinho/carrinho_item tables from one-time visitors who never return. The cleanup interval was temporarily reduced to 10 seconds during development to validate the behavior quickly, then restored to the production schedule (daily, 1 AM).


3) Sorting across a @OneToMany relationship. Beer price lives on the related Estoque (stock) entity, not on Cerveja itself. Spring Data's automatic Sort/Pageable resolution cannot navigate through a collection association to order by one of its fields (PathException: Plural path ... refers to a collection). This was solved with explicit JPQL queries per sort direction (ORDER BY e.preco ASC/DESC), applied consistently to both the listing and search endpoints.


4) Query duplication as a conscious trade-off. Supporting country filtering combined with optional price sorting, across both the listing and search endpoints, currently requires a handful of near-duplicate JPQL queries rather than one fully dynamic query. This is acceptable at the current scale, but the natural next step would be migrating to the JPA Criteria API (or Spring Data Specification) to build these queries dynamically and remove the duplication.


5) E-mail sender domain (Mailtrap sandbox). The sandbox SMTP provider used for development rejects messages sent from an arbitrary "from" address it requires a sender address on its own demo domain. This is a sandbox specific constraint and would not apply to a production grade transactional e-mail provider.


6) Credentials kept out of source control. SMTP credentials are injected via environment variables (${MAIL_USERNAME}, ${MAIL_PASSWORD}) rather than hardcoded in application.properties, since this repository is public. An application.properties.example documents the expected configuration shape without exposing real values.


7) Admin edits no longer clear customer passwords or confirmation state. An earlier version of ClienteService.updateCliente unconditionally re-encoded whatever was in the password field of the admin's edit form which has no password input and also force reset emailConfirmado/tokenConfirmacao on every save. In practice this meant any routine admin edit (e.g. updating a phone number) silently wiped the customer's password and invalidated a pending confirmation token. The fix only updates the password when a new one is actually present in the request, and leaves confirmation state untouched outside of the dedicated confirmation/password-setup flows.


8) Stateless JWT, no session, no database lookup per request. Customer authentication is intentionally stateless: the JWT is signed with a server side secret and carries the customer id and e-mail as claims. JwtAuthenticationFilter verifies the signature and expiration on every request without querying the database, keeping the customer facing API session free and easy to scale horizontally in contrast with the admin area, which still uses traditional session based authentication (HttpSession / cookie), since the two areas have different operational needs.


9) Cart merge on login. Since the cart predates any account (identified only by a client-generated session UUID), logging in needs to reconcile that anonymous cart with the customer's own. The merge logic handles three cases: no existing customer cart (the guest cart is simply adopted), an existing customer cart (item quantities are summed and the guest cart is deleted), and an already merged cart (no-op). This was manually verified by adding items to a cart in one browser, then logging into the same account from a different browser and confirming the cart was already populated on login without the second browser ever generating a new guest cart row.


10) API friendly 401 responses. By default, Spring Security's formLogin configuration redirects any unauthenticated request including REST API calls to the admin's HTML login page. A custom AuthenticationEntryPoint now inspects the request path: requests to /api/** receive a clean JSON 401 body instead of an HTML redirect, while admin routes keep their original redirect to login behavior.



## Known Limitations / Roadmap


- Token expiration: confirmation/password setup tokens are single use (cleared on consumption) but do not yet expire on a timer. A dataCriacao + expiry check is a planned improvement.
- Query duplication described above, pending a Criteria API refactor.
- Multi device guest cart edge case: if a customer is logged in simultaneously on two devices and adds items as a guest on a third before logging in there too, the merge correctly reconciles carts at login time, but a stale sessionId left in a device's localStorage after a merge could create a fresh, empty guest cart if that device keeps browsing without refreshing. Acceptable for the current scope; a future improvement would re-sync the session id after a merge.
- JWT refresh / revocation: the current token has a fixed expiration (7 days) and no refresh-token or blacklist mechanism.


## Author


Developed by Daniel Arantes Telles


## License


This project is licensed under the MIT License - see the LICENSE file for details.

