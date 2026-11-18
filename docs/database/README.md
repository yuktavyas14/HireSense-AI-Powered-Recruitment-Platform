# Database documentation

Holds the database design for HireSense (PostgreSQL):

- Entity descriptions and relationships (`User`, `CandidateProfile`, `Company`, `Job`, `Application`, `Resume`).
- Table/column notes: keys, constraints, indexes and why they exist.
- Migration notes (schema changes are made only through Flyway migrations, never by hand).

The ER diagram itself lives in `docs/diagrams/`; this folder explains the design in words.
The ER design is planned for Task 0.6.
