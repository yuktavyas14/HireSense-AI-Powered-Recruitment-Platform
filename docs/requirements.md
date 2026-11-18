# HireSense – Requirements

> **Version:** 0.6 (draft, Phase 0)
> **Status:** Planning document. **Nothing described here is implemented yet** unless explicitly marked.
> **Related:** `HIRESENSE.md` (roadmap and rules), `PROJECT_PROGRESS.md` (what is actually done).
> User stories (Task 0.5), the ER design (Task 0.6) and architecture (Task 0.8) refine this document.

### Change log

| Version | Change |
|---------|--------|
| 0.1 | First draft: structure, tiers, FR and NFR. |
| 0.2 | Owner decisions D-01..D-07 incorporated (see section 1.3); status tags added; database implications added; open questions updated. |
| 0.3 | Review of v0.2: proposals approved (D-08..D-12, D-16, D-17); changes D-13 (phone optional), D-14 (`ACCEPTED`/`DECLINED` after `OFFER`), D-15 (resume mandatory to apply); new FR-037; interim data-retention rule; stricter resume security; open questions updated. |
| 0.4 | Review of v0.3: decisions D-18..D-25 (offer response by candidate, UTF-8 byte check for BCrypt, owner-only reopen, resume hiding, public data scope, no reopen after job closes, no admin resume download, mandatory rejection reason); open questions Q8, Q12–Q17 resolved; new questions Q18, Q19. |
| 0.5 | Review of v0.4 and user stories: decisions D-26..D-30 (closed-job pipeline continues, internal reasons, admins cannot see applicant details, role change at next login, offer decisions final); Q18, Q19 resolved; only Q7 and Q9 remain open. |
| 0.6 | After ER design sign-off: decision D-31 (hidden resumes do not count toward the limit of 5; used resumes are retained; unused hidden resumes may be permanently deleted). Q9 resolved. Only Q7 (retention policy) remains open. |

---

## 1. Purpose and scope

HireSense is a recruitment platform connecting **candidates** and **recruiters**. Candidates build
a profile, upload resumes, search for jobs and apply. Recruiters post jobs, review applicants and
move applications through a hiring pipeline. Later phases add notifications, AI-assisted matching
and search.

### 1.1 How to read priorities

Each requirement has a **Priority** tied to the project roadmap in `HIRESENSE.md`.

| Priority | Meaning | Roadmap phases |
|----------|---------|----------------|
| **MVP** | Needed for a working end-to-end product: register, post a job, apply, track status, in a browser, runnable with Docker. | 1–4 |
| **Planned** | In the roadmap, but not designed in detail. Scope may change after the MVP. | 5–8 |
| **Future** | Good ideas outside the current roadmap. Not committed. | – |

**Decision – why tiers:** The roadmap has 13 phases. Calling everything "must-have" would make the
requirements unrealistic and block the first working version. The MVP is deliberately the
smallest product that is still a real hiring workflow.

**Decision – start as a modular monolith:** MVP requirements are written without assuming
microservices. A requirement says *what* the system does, not *how* it is deployed. Service
splitting (Phase 5) must not change these requirements, only where the code runs.

### 1.2 Decision status tags

| Tag | Meaning |
|-----|---------|
| **[A]** | **Approved** by the project owner. Change only with an explicit new decision. |
| **[P]** | **Proposal.** A specific suggestion that still needs the owner's approval. |
| **[S]** | **Assumption.** A working premise used to keep moving; must be validated later. |
| *(no tag)* | Draft content from v0.1. The overall structure is approved; individual details may still be refined. |

### 1.3 Approved decisions

| ID | Decision | Affects |
|----|----------|---------|
| **D-01** [A] | Anonymous visitors can browse and search jobs. Authentication is mandatory to apply. | FR-031, FR-032, FR-005, NFR-038 |
| **D-02** [A] | Application status changes follow a controlled workflow. A rejected application can be reopened only by an authorized recruiter action. (Details: D-10, D-11, D-14.) | FR-035, FR-036 |
| **D-03** [A] | Jobs are normally closed, not deleted. Permanent deletion is restricted to administrators and subject to data-retention rules. (Interim rule: D-16.) | FR-030, FR-052 |
| **D-04** [A] | Minimum password length is 8 characters, with secure hashing and validation. | FR-001, NFR-005, NFR-007 |
| **D-05** [A] | Candidate registration requires name and email (phone is optional, see D-13). Resume upload is optional at registration. | FR-001, FR-010 |
| **D-06** [A] | Candidates can upload multiple resumes and choose a specific resume for each application. (Choosing is mandatory when applying: D-15.) | FR-032, FR-040..FR-042 |
| **D-07** [A] | AI output is advisory only. Final hiring decisions always involve human oversight. | NFR-021, NFR-022, FR-071 |
| **D-08** [A] | Password policy details: minimum 8 and maximum 64 characters, a common-password blocklist, and no mandatory special-character rules. | NFR-007 |
| **D-09** [A] | Resume limits: 5 MB per file and at most 5 resumes per candidate (MVP). | FR-040 |
| **D-10** [A] | Application status moves forward only, with no skipping. Rejection is allowed from `APPLIED`, `SHORTLISTED` and `INTERVIEW`. | FR-035 |
| **D-11** [A] | Reopening a rejected application needs an authorized recruiter and a **mandatory reason**, and returns the application to its previous status. | FR-036 |
| **D-12** [A] | An audit history is kept for application status changes and for administrative deletions. | FR-035, FR-052, NFR-039 |
| **D-13** [A] | Candidate registration requires only **name and email**; **phone is optional**. (Amends D-05.) | FR-001, FR-010, NFR-036 |
| **D-14** [A] | `OFFER` is not the final outcome. After an offer the outcomes are `ACCEPTED` and `DECLINED`. | FR-035, FR-037 |
| **D-15** [A] | A resume is **mandatory to submit a job application** (still optional at registration). | FR-032 |
| **D-16** [A] | Until retention rules are defined, a job that has applications cannot be permanently deleted. | FR-052, NFR-035 |
| **D-17** [A] | Resumes are stored securely and can be downloaded only by authorized users. They are never exposed through public URLs. | FR-040, FR-041, NFR-011 |
| **D-18** [A] | The recruiter creates and issues an offer. Only the candidate can accept or decline it. A recruiter can never mark an offer accepted on the candidate's behalf. | FR-037 |
| **D-19** [A] | The UTF-8 **byte** length of a password is validated before BCrypt hashing. Too long: a clear validation error. Passwords are never silently truncated. | NFR-007 |
| **D-20** [A] | Only the owning recruiter can reopen a rejected application. | FR-036 |
| **D-21** [A] | Removing a resume that an application uses only hides it from the candidate's active list. The resume attached to the historical application is kept. | FR-042 |
| **D-22** [A] | Anonymous visitors see job details and basic company information only. | FR-031, NFR-038 |
| **D-23** [A] | Reopening a rejected application is not permitted after its job is closed (MVP). | FR-036 |
| **D-24** [A] | Admins cannot download candidate resumes in the MVP. | FR-041, NFR-032 |
| **D-25** [A] | A rejection reason is mandatory, for auditability. | FR-035, NFR-039 |
| **D-26** [A] | Closing a job stops **new** applications only. Existing applications continue through interviews and offers, and candidates can still answer offers. Reopening rejected applications after the job closed stays prohibited (D-23). | FR-030, FR-035, FR-036, FR-037 |
| **D-27** [A] | Rejection and reopen reasons are internal recruiter information and are not visible to candidates in the MVP. The UI reminds recruiters to use factual, job-related language. | FR-033, FR-035 |
| **D-28** [A] | Admins cannot view applicant resumes or applicant details in the MVP unless a future requirement authorizes it. | FR-034, FR-041, NFR-032 |
| **D-29** [A] | Role changes take effect at the user's next login. | FR-050 |
| **D-30** [A] | Offer decisions (`ACCEPTED`/`DECLINED`) are final once submitted in the MVP. There is no change-of-decision feature. | FR-037 |
| **D-31** [A] | Hidden resumes do **not** count toward the limit of 5 active resumes. A resume used by an application is always retained for application history. A hidden resume that no application uses may be permanently deleted. | FR-040, FR-042 |

D-07 restates a rule from `HIRESENSE.md` ("never allow an AI model to automatically reject candidates").
D-08 to D-17 were approved when v0.2 was reviewed; D-18 to D-25 when v0.3 was reviewed; D-26 to D-30 when v0.4 and the user stories were reviewed; D-31 when the ER design was signed off.

**Decision – why record decisions separately:** later you will ask "why does it work this way?"
(an interview question as well). A short list with IDs, plus ADRs in `docs/adr/` for the
architectural ones, answers that without digging through text.

### 1.4 Out of scope (for now)

- Payments, subscriptions or billing.
- Video interviews, calendar integration, assessments or tests.
- Mobile native apps (the web UI will be responsive).
- Multi-language UI and multi-tenant company workspaces.
- Any automatic hiring or rejection decision by AI (permanently out of scope; see NFR-021).

---

## 2. User roles and responsibilities

| Role | Who | Responsibilities |
|------|-----|------------------|
| **Visitor (anonymous)** [A] | Not logged in | Browse and search jobs and view job details. Cannot apply or see any personal data. |
| **Candidate** | A person looking for work | Maintain own profile and resumes; search jobs; apply with a chosen resume; track own applications; accept or decline job offers. |
| **Recruiter** | An employer's representative | Manage own company profile; create/edit/close own jobs; review applicants for own jobs; change application status, including reopening rejected ones. |
| **Admin** | Platform operator | Manage users and roles; view platform statistics; permanently delete jobs (subject to retention rules); manage platform data. |

The Visitor is not an account role: it describes a request with no token. Only Candidate,
Recruiter and Admin are stored as roles.

**Decision – one role per account (MVP):** A user has exactly one role. It keeps authorization
simple to reason about and test. Supporting people who are both candidate and recruiter is a
Future enhancement.

**Decision – recruiter belongs to one company (MVP):** A recruiter manages one company profile.
Teams of recruiters sharing a company are Future.

---

## 3. Application modules

These are the logical modules (packages in the Phase 1 monolith). Requirements in section 4 are
grouped by them.

| Module | Responsibility | Expected behavior |
|--------|----------------|-------------------|
| **User / Auth** | Accounts, roles, login | Registers users, authenticates them, enforces roles. |
| **Candidate** | Candidate profile | Stores and returns the candidate's own profile data. |
| **Company / Recruiter** | Company profile | Recruiter creates and edits one company profile. |
| **Job** | Job postings | Create, read, update, close, search and filter jobs. Public read access. |
| **Application** | Applying and tracking | One application per candidate per job, with a mandatory resume; controlled status flow (including offer outcomes) with history. |
| **Resume** | Resume files | Upload, list, store, download and remove PDF resumes in private storage with access control. A candidate can have up to 5. |
| **Admin** | Platform management | User management, statistics, permanent job deletion. |
| **Notification** (Planned) | Alerts | Tells users about relevant events. |
| **AI** (Planned) | Assistance (advisory) | Resume parsing, matching, explanations, chatbot. |
| **Search** (Planned) | Fast job search | Full-text search, typo tolerance, autocomplete. |

---

## 4. Functional requirements

Format: **ID – Requirement.** Priority. Acceptance criteria (AC) are written so that each can be
checked by a test or a manual check.

### 4.1 Authentication and accounts

**FR-001 – Registration.** *MVP.* A visitor can create an account as Candidate or Recruiter.
- AC1: Registration requires a valid, unique email, a password meeting the policy (NFR-007), a name and a role.
- AC2 [A] (D-05, D-13): **Candidate** registration requires **name and email**. A **phone number is optional**. Uploading a resume is **optional** at registration and never required to create an account (a resume *is* required later, when applying: FR-032).
- AC3: Registering with an email that already exists is rejected with a clear error and no second account is created.
- AC4: The Admin role cannot be chosen at registration.
- AC5: The password is never returned in any response.
- AC6 [S]: Recruiter registration requires name, email and password only; the company is created afterwards (FR-020).

**FR-002 – Login.** *MVP.* A registered user can log in with email and password and receive an access token.
- AC1: Valid credentials return a token; invalid credentials return an error that does not reveal whether the email exists.
- AC2: Disabled/blocked users cannot log in.

**FR-003 – Session renewal and logout.** *MVP.* A logged-in user can stay logged in without re-entering credentials, and can log out.
- AC1: A valid refresh token yields a new access token; the old refresh token is no longer usable (rotation).
- AC2: After logout, the refresh token cannot be used again.

**FR-004 – Password reset.** *MVP (end of Phase 2).* A user can reset a forgotten password.
- AC1: A reset request never reveals whether the email exists.
- AC2: A reset link/token works once and expires after a short time.
- AC3: The new password must meet the same policy as registration (NFR-007).
- Note: sending real email needs a mail service; before Phase 6 the email may be logged in development only.

**FR-005 – Role-based access.** *MVP.* Every protected action checks the user's role on the server.
- AC1: A request to a protected endpoint with no valid token gets **401**.
- AC2: A logged-in user without the required role gets **403**.
- AC3: Hiding a button in the UI is never the only protection.
- AC4 [A] (D-01): The set of **public** (no-token) endpoints is explicit and minimal: job list, job detail, registration, login, password-reset request, health. Every other endpoint requires a token. A new endpoint is protected by default.

**Decision – "secure by default":** listing the few public endpoints, and protecting everything
else by default, is safer than listing what is protected. Forgetting an entry then fails closed
(401), not open.

### 4.2 Candidate profile

**FR-010 – Manage candidate profile.** *MVP.* A candidate can create, view and update their own profile.
- AC1: A candidate can read and edit only their own profile.
- AC2 [A] (D-05, D-13): **Mandatory fields:** full name and email.
- AC3 [A] (D-13): **Optional fields:** phone number, city/location, headline/summary, skills, experience summary and education summary.
- AC4: Required fields are validated; invalid input returns field-level error messages. [S] Optional fields are also format-checked when provided (e.g. a malformed phone number is rejected).
- AC5: A recruiter can see a candidate's profile only if that candidate applied to one of the recruiter's jobs (FR-032).

**Decision – structured vs. free-text profile:** In the MVP, skills and experience are simple
fields. Splitting them into separate normalized tables (Skills, Experience, Education) is
postponed until Task 0.6 / Phase 7 shows it is worth the complexity.

### 4.3 Company and recruiter

**FR-020 – Manage company profile.** *MVP.* A recruiter can create and edit their company (name, description, website, location, industry).
- AC1: A recruiter can edit only their own company.
- AC2: A recruiter cannot post jobs until a company exists.

### 4.4 Jobs

**FR-030 – Create and manage jobs.** *MVP.* A recruiter can create, update and **close** jobs for their company.
- AC1: A job has title, description, location, employment type, required skills and status (`OPEN` or `CLOSED`).
- AC2: A recruiter can modify only jobs of their own company; others get 403.
- AC3 [A] (D-03): A recruiter **closes** a job instead of deleting it. A recruiter has no permanent-delete function.
- AC4 [A] (D-03): Closing a job keeps all of its applications; candidates still see their applications to it.
- AC5: Invalid input (blank title, etc.) returns validation errors.
- AC6 [S]: A closed job can be reopened by its owning recruiter.
- AC7 [A] (D-26): Closing a job stops **new** applications only. Existing applications continue through the pipeline (shortlisting, interviews, offers, rejection) and candidates can still answer offers. Only reopening a rejected application is blocked (FR-036 AC6).

**FR-031 – Browse, search and filter jobs.** *MVP.* **Anyone, including anonymous visitors,** can list open jobs with filters and paging.
- AC1: Filter by keyword in title, location, employment type and skills.
- AC2: Results are paginated and sortable (e.g. newest first); there is a maximum page size.
- AC3: Closed jobs do not appear in results.
- AC4: A job's detail page shows full job information to anyone.
- AC5 [A] (D-01): **No authentication is needed** to browse, search or view job details.
- AC6 [A] (D-01, D-22): An anonymous request exposes **only job details and basic company information**. It never exposes candidate, application or resume data, and never a recruiter's personal contact details. [S] "Basic company information" = company name, description, website, location and industry (exact fields confirmed in Phase 1).
- AC7 [A] (D-01): Visitors who try to apply are asked to log in or register (backend returns 401; the UI redirects to login and returns to the job afterwards).

**FR-052 – Permanently delete a job (admin only).** *MVP (simple).*
- AC1 [A] (D-03): Only an **Admin** can permanently delete a job. Recruiters and candidates get 403.
- AC2 [A] (D-03): Deletion is **subject to data-retention rules**.
- AC3 [A] (D-16): The full retention policy is not yet defined (Q7). **Until it is, permanent deletion of any job that has at least one application is refused**, even for admins, with a clear error (e.g. HTTP 409). Such a job can only be closed. An admin may permanently delete a job that has **no** applications.
- AC4 [A] (D-12): Each permanent deletion is written to the audit history (who, when, which job).

**Decision – why restrict hard deletion:** applications are other people's data and hiring history.
Deleting a job could silently remove a candidate's record of having applied, and may conflict with
legal retention duties. Restricting it to admins and leaving the rules open is the conservative choice.

### 4.5 Applications

**FR-032 – Apply for a job.** *MVP.* An authenticated candidate can apply to an open job.
- AC1 [A] (D-01): The applicant must be **logged in** as a Candidate. An unauthenticated request gets 401; a Recruiter or Admin gets 403.
- AC2: Applying to a closed job is rejected.
- AC3: Applying twice to the same job is rejected (no duplicate application).
- AC4: A new application starts in status `APPLIED`.
- AC5 [A] (D-06, D-15): **A resume is mandatory.** The candidate must select a specific resume, from their own resumes (FR-042), for this application. A request without a resume is rejected with a validation error (HTTP 400) and no application is created.
- AC6 [A] (D-06): The chosen resume must belong to the applying candidate; otherwise the request is rejected.
- AC7 [A] (D-15): A candidate with **no resume** cannot apply. The UI must guide them to upload one first (FR-040) and then return to the job. (A resume stays optional at registration, D-05.)
- AC8 [P]: The application keeps pointing to the exact resume chosen at application time, even if the candidate later uploads newer resumes. The recruiter always sees what was submitted.

**FR-033 – Track my applications.** *MVP.* A candidate can list their own applications with current status, and open one.
- AC1: A candidate can never see another candidate's application.
- AC2 [A] (D-27): A candidate sees the status history (what changed, when), not internal recruiter notes. Rejection and reopen reasons are **internal** and are not shown to the candidate in the MVP.
- AC3 [A] (D-14): When an application is in status `OFFER`, the candidate can respond to it (FR-037).

**FR-034 – Review applicants.** *MVP.* A recruiter can list applicants for each of their own jobs and open an application (profile and the **resume chosen for that application**).
- AC1: A recruiter sees applications only for jobs of their own company.
- AC2 [A] (D-06): The recruiter sees the resume the candidate selected, not other resumes of that candidate.
- AC3 [A] (D-28): Admins cannot view applicants or their details in the MVP; they get 403.

**FR-035 – Change application status (controlled workflow).** *MVP.* A recruiter moves an application through the hiring pipeline. (After an offer, the candidate answers it: FR-037.)
- AC1 [A] (D-02): Status changes follow a **defined, controlled workflow**; any change not allowed by it is rejected with a clear error.
- AC2 [A] (D-10, D-14): The workflow is:

  ```
  APPLIED → SHORTLISTED → INTERVIEW → OFFER → ACCEPTED
                                          └──→ DECLINED
  Any of APPLIED, SHORTLISTED, INTERVIEW  →  REJECTED
  REJECTED → (reopen, see FR-036)
  ```
  Moves are **forward only, with no skipping** (e.g. `APPLIED → OFFER` and `INTERVIEW → SHORTLISTED` are rejected). Rejection is allowed **only** from `APPLIED`, `SHORTLISTED` and `INTERVIEW`, not from `OFFER`. `OFFER` is **not final**: its outcomes are `ACCEPTED` and `DECLINED` (FR-037).
- AC3 [A] (D-02): The recruiter-driven steps (`SHORTLISTED`, `INTERVIEW`, `OFFER`, `REJECTED`, reopen) can be done only by the owning recruiter (the recruiter of the job's company), and only through an explicit human action (see NFR-021).
- AC4 [A] (D-12): Every status change is recorded in an **audit history**: previous status, new status, who, when, and a note or reason.
- AC5 [A] (D-25): Rejecting requires a **mandatory reason** (non-empty text), for auditability. A rejection request without one is refused and the status does not change. The reason is stored in the audit history. For other transitions the note is optional.
- AC6 [S]: `ACCEPTED`, `DECLINED` and (until reopened) `REJECTED` are end states: no other transition leaves them.
- AC7 [A] (D-27): The UI reminds recruiters that a reason must be factual and job-related, because it is personal data about the candidate (NFR-033, NFR-036).

**FR-036 – Reopen a rejected application.** *MVP.*
- AC1 [A] (D-02, D-20): A `REJECTED` application can be reopened **only by an authorized recruiter action**: the owning recruiter. Candidates, other recruiters, admins and AI cannot reopen it.
- AC2 [A] (D-02): Reopening is recorded in the status history like any other change.
- AC3 [A] (D-11): A reopen requires a **mandatory reason** (non-empty text). A request without one is rejected, and the reason is stored in the status history.
- AC4 [A] (D-11): Reopening returns the application to the status it had **before** it was rejected (taken from the status history), not to `APPLIED`.
- AC5 [S]: There is no limit on the number of reopens in the MVP.
- AC6 [A] (D-23): Reopening is **not permitted when the job is `CLOSED`** (MVP); the request is refused with a clear error. [S] If the recruiter reopens the *job* (FR-030 AC6), its rejected applications can be reopened again. What else may happen to applications of a closed job is settled in D-26.
- AC7: Only `REJECTED` applications can be reopened. `ACCEPTED` and `DECLINED` cannot.

**Decision – controlled workflow with history:** a hiring pipeline is a *state machine*: each state
only allows certain next states. Enforcing this in the **service layer** (not only in the UI)
prevents invalid data, and the history table makes reopening and mistakes auditable.

**FR-037 – Respond to a job offer.** *MVP.* A candidate can accept or decline an offer made on their own application.
- AC1 [A] (D-14): From status `OFFER` the possible outcomes are `ACCEPTED` and `DECLINED`. `OFFER` is not a final outcome.
- AC2 [A] (D-18): The recruiter creates and issues the offer (FR-035: status `OFFER`). Only the **candidate who owns the application** can set `ACCEPTED` or `DECLINED`, and only while the status is `OFFER`. A recruiter can never mark an offer accepted or declined on the candidate's behalf.
- AC3 [A] (D-30): `ACCEPTED` and `DECLINED` are final: they cannot be changed or reopened. There is no change-of-decision feature in the MVP.
- AC4 [A] (D-12): The response is recorded in the audit history (who, when).
- AC5: Another candidate, a recruiter or an admin who tries to answer an offer gets 403; a response when the status is not `OFFER` is rejected with a clear error.
- AC6 [S]: Withdrawing an offer after it was made is out of scope for the MVP (rejection is not allowed from `OFFER`).

**Decision – why separate `ACCEPTED`/`DECLINED` outcomes:** "we made an offer" and "the candidate took
it" are different facts. Keeping them separate gives a truthful record and meaningful statistics
later, and it models the candidate as a participant who makes their own decision.

### 4.6 Resumes

**FR-040 – Upload resume.** *MVP (Phase 4).* A candidate can upload a PDF resume **at any time after registration**. **A candidate may have several resumes.**
- AC1: Only PDF files up to a maximum size are accepted; other files are rejected. [A] (D-09) The maximum is **5 MB**.
- AC2: The file type is checked from the file content, not only the file name or declared MIME type.
- AC3: File metadata (owner, original name, size, storage key, upload time) is stored in the database; the file itself is stored in object storage (MinIO) in a **private** bucket (D-17).
- AC4: Stored files get unique, non-guessable names.
- AC5 [A] (D-05): Uploading a resume is **optional** and is not part of registration.
- AC6 [A] (D-06): A candidate can upload **more than one** resume. [A] (D-09, D-31) At most **5 active** (non-hidden) resumes per candidate (storage and abuse control); a sixth active upload is rejected with a clear error.
- AC7 [P]: The candidate can give each resume a label (e.g. "Backend CV") to tell them apart.

**FR-041 – Download resume.** *MVP (Phase 4).* A resume can be downloaded only by its owner, or by a recruiter whose job the candidate applied to **using that resume**.
- AC1: Any other user gets 403 (no token: 401); resumes are never publicly accessible by URL, and anonymous visitors never reach them.
- AC2 [A] (D-06): A recruiter can download only the resume attached to an application for one of their jobs, not the candidate's other resumes.
- AC3 [A] (D-17): Resume files are stored in a **private** bucket with **no public-read access**. The system never exposes a permanent public URL to a resume file: no stored or returned value is a directly fetchable link.
- AC4 [A] (D-17): Every download is checked on the server. The requester must be authenticated and authorized (the owning candidate, or the owning recruiter of the application's job). Frontend checks are never the only protection.
- AC5 [P]: In the MVP, downloads are streamed **through the backend** after the authorization check, which is the simplest design to reason about and test. Short-lived pre-signed URLs, issued only after the same check, may be considered later. Both satisfy D-17; a permanent public link never does.
- AC6 [P]: Files are returned as attachments with a fixed `application/pdf` type and `X-Content-Type-Options: nosniff`, so a browser never executes uploaded content.
- AC7 [A] (D-24): Admins **cannot** download candidate resumes in the MVP (least privilege; see NFR-032). They get 403.
- AC8 [S]: Storage credentials come from environment variables (NFR-009). Encryption at rest and backups are deployment settings to review in Phases 4 and 10; not yet decided.

**FR-042 – Manage my resumes.** *MVP (Phase 4).* A candidate can list and remove their own resumes.
- AC1 [A] (D-06): The candidate sees all their own resumes and no one else's.
- AC2 [A] (D-21): When a candidate removes a resume that an application uses, the resume is only **hidden from the candidate's active resume list**. The resume attached to the historical application is kept and stays available to the owning recruiter.
- AC3 [A] (D-31): A resume that **no** application uses is physically deleted when the candidate removes it (database row and stored file).
- AC4 [A] (D-31): Hidden resumes do **not** count toward the limit of 5 active resumes. A resume used by an application is **always retained** for application history; a hidden resume that no application uses may be permanently deleted. How long hidden files are kept depends on the retention policy (Q7, still open).

### 4.7 Administration

**FR-050 – Manage users.** *MVP (simple).* An admin can list users, disable/enable accounts and change roles.
- AC1: Only admins can use these functions.
- AC2: An admin cannot remove their own admin access by mistake (blocked or confirmed).
- AC3 [A] (D-29): A role change takes effect at the user's **next login**.
- AC4 [P]: Changing a role or disabling an account also revokes that user's refresh tokens, so the old role or access cannot be renewed past the short access-token lifetime (NFR-006).

**FR-051 – Platform statistics.** *Planned.* An admin sees counts such as users, jobs and applications.

(FR-052 – permanent job deletion – is in section 4.4 next to the job requirements.)

### 4.8 Planned (Phases 6–8)

These are listed for traceability only; they are **not** designed in detail.

| ID | Requirement | Phase |
|----|-------------|-------|
| FR-060 | In-app live notifications when an application's status changes. | 6 |
| FR-061 | Email notifications for key events (e.g. application received, status changed). | 6 |
| FR-070 | Parse an uploaded resume into structured data (skills, experience, education). | 7 |
| FR-071 | Show recruiters a match score and **explanation** for each applicant as **advisory decision support only** (D-07). It never hides, filters out or rejects an applicant; every applicant stays visible. | 7 |
| FR-072 | Recommend jobs to candidates; give resume improvement tips. Advisory only. | 7 |
| FR-073 | Generate interview questions for recruiters. Advisory only. | 7 |
| FR-074 | Recruitment assistant chatbot (RAG) for candidates. | 7 |
| FR-080 | Fast job search with typo tolerance, autocomplete and facets. Must remain publicly accessible for jobs (D-01). | 8 |

### 4.9 Future (not committed)

- FUT-001: Candidate withdraws an application.
- FUT-002: Multiple recruiters per company; users with several roles.
- FUT-003: Saved jobs and job alerts.
- FUT-004: Interview scheduling.
- FUT-005: Resume versions and cover letters.
- FUT-006: Admin-defined retention periods and automated purge.

---

## 5. Non-functional requirements

**Honesty rule:** numeric targets below are *goals for a learning/portfolio project on a single
developer machine or small container setup*. They are **not measured yet**. A target is only
claimed as met after it is measured and recorded.

### 5.1 Performance and scalability

| ID | Requirement | Acceptance criteria | Priority |
|----|-------------|---------------------|----------|
| NFR-001 | Normal API reads respond quickly. | Goal: 95% of job list/detail requests under 500 ms on the dev setup with realistic test data. Measured and recorded in Phase 8. | MVP (goal) |
| NFR-002 | Lists never return unbounded data. | Every list endpoint is paginated with a maximum page size. | MVP |
| NFR-003 | Database queries are efficient. | Columns used for filtering/joining have indexes; N+1 query problems checked in Phase 8. | MVP |
| NFR-004 | The backend can run as more than one instance. | Backend keeps no user session state in memory (stateless, token-based auth), so it can be scaled later. | MVP |

**Decision – stateless backend:** JWT-based, stateless authentication costs a little more
complexity than server sessions but allows horizontal scaling (Kubernetes, Phase 10) without
changing the auth design.

### 5.2 Security and authentication

| ID | Requirement | Acceptance criteria | Priority |
|----|-------------|---------------------|----------|
| NFR-005 | Passwords are never stored in plain text. | [A] (D-04) Passwords are hashed with a strong, salted, adaptive algorithm (**BCrypt**); verified by a test that the stored value is not the password and that two identical passwords give different hashes. | MVP |
| NFR-006 | Tokens are validated on every protected request. | Signature and expiry are checked; expired/forged tokens give 401. Access tokens are short-lived. | MVP |
| NFR-007 | Password policy and validation. | [A] (D-04) **Minimum 8 characters**; shorter passwords are rejected with a clear message. [A] (D-08) **Maximum 64 characters**: BCrypt only uses the first 72 **bytes**, so an upper limit avoids silent truncation and very long input. [A] (D-08) Passwords found in a **common-password blocklist** (e.g. "password", "12345678") are rejected. [S] The blocklist is a bundled file of roughly the 10,000 most common passwords, compared case-insensitively; its source is chosen in Phase 2. [A] (D-08) **No mandatory special-character, digit or uppercase rules**: length matters more than complexity rules. [A] (D-19) 64 characters can be more than 72 bytes in UTF-8 (non-English letters, emoji), so the **UTF-8 byte length is validated before BCrypt hashing** (limit 72 bytes). A longer password gets a **clear validation error** that states the limit, and passwords are **never silently truncated**. [P] At login, an over-long password is refused as invalid credentials without hashing, and is also never truncated. The same policy applies to registration, password reset and any password change. | MVP |
| NFR-008 | Authorization is enforced server-side, including ownership. | Role and ownership tests exist for every protected endpoint. | MVP |
| NFR-009 | No secrets in source control or logs. | Credentials come from environment variables; `.env` is ignored by Git; `.env.example` has placeholders only; logs contain no passwords, tokens or resume contents. | MVP |
| NFR-010 | Input is validated and output is safe. | Request validation on all endpoints; the UI does not render untrusted HTML unsafely. | MVP |
| NFR-011 | Uploads are restricted and stored privately. | [A] (D-17) See FR-040 and FR-041: type, size, content check, unique names, private bucket, no public URLs, authorized downloads only. | MVP |
| NFR-012 | Cross-origin access is restricted. | CORS allows only the known frontend origin(s). | MVP |
| NFR-013 | Login abuse is limited. | Repeated failed logins are rate-limited. | MVP (end of Phase 2) |
| NFR-014 | Keycloak replaces the hand-built auth. | Planned for Phase 5; FR-001..FR-005 behavior must still hold afterwards. | Planned |
| NFR-038 | Public endpoints are safe to expose. | [A] (D-01) Anonymous endpoints return only public data (see FR-031 AC6), are paginated with a maximum page size, and have no way to reach private data. [P] Basic rate limiting on anonymous endpoints (full gateway rate limiting arrives in Phase 8). | MVP |

**Decision – build JWT auth first, Keycloak later:** the roadmap does this on purpose so you learn
what Spring Security and JWT do before delegating them. Requirements describe behavior, so they
survive the switch.

**Decision – anonymous browsing:** public job browsing helps both candidates (no sign-up barrier)
and search engines. The price is a larger attack surface and scraping risk, so public data is
minimal and rate-limited, and everything else stays protected by default (FR-005 AC4).

### 5.3 Reliability and availability

| ID | Requirement | Acceptance criteria | Priority |
|----|-------------|---------------------|----------|
| NFR-015 | Errors are handled consistently. | All APIs return a consistent error format with correct HTTP status; no stack traces to clients. | MVP |
| NFR-016 | Data is not lost on restart. | Database and uploaded files persist across container restarts (tested in Phase 4). | MVP |
| NFR-017 | Database changes are controlled. | Schema changes only through Flyway migrations; `ddl-auto=update` is never used. | MVP |
| NFR-018 | Concurrent updates are safe. | Concurrent edits to the same record do not silently overwrite each other (optimistic locking). [S] This matters especially for two recruiters changing the same application's status. | MVP |
| NFR-019 | Health can be checked. | A health endpoint reports whether the app and database are up. | MVP |
| NFR-020 | Availability target. | Goal for the demo deployment: runs reliably during demos. **No uptime SLA is promised** (single instance, no redundancy). | Goal |
| NFR-039 | Important changes are auditable. | [A] (D-12) Application status changes (including offer responses and reopens) and admin permanent deletions record who did them and when (see FR-035 AC4, FR-037 AC4, FR-052 AC4). [P] The audit history is append-only: no API can edit or delete its entries. | MVP |

### 5.4 Responsible AI and fairness

| ID | Requirement | Acceptance criteria | Priority |
|----|-------------|---------------------|----------|
| NFR-021 | **AI is advisory only; humans make hiring decisions.** | [A] (D-07) No code path changes an application's status, hides an applicant or removes a candidate from a list based on AI output alone. Every status change, including rejection and reopening, is an explicit action by an authorized human: the owning recruiter, or the candidate for `ACCEPTED`/`DECLINED`. A test proves AI-related code cannot call the status-change function. | Planned (rule applies from now) |
| NFR-022 | AI output is validated, labelled and explained. | [A] (D-07) Structured AI output is schema-validated. Every AI score or recommendation shown in the UI is labelled as AI-generated and advisory, comes with an explanation, and states that it is not a validated hiring score. | Planned |
| NFR-040 | AI use is transparent and fair. | [P] Candidates are told that AI assists recruiters. Sensitive attributes (age, gender, religion, etc.) are not used for ranking. Bias is evaluated in Phase 7. | Planned |

### 5.5 Maintainability

| ID | Requirement | Acceptance criteria | Priority |
|----|-------------|---------------------|----------|
| NFR-023 | Layered, clean structure. | Controller → Service → Repository; business logic not in controllers or Angular components; DTOs at API boundaries; entities not exposed directly. The status workflow rules live in the service layer. | MVP |
| NFR-024 | Automated tests exist in every phase. | Service-layer logic has unit tests; goal of about 70% **meaningful** service-layer coverage by Phase 9. | MVP |
| NFR-025 | Build is reproducible. | `mvnw` builds the backend and `npm run build` builds the frontend from a clean checkout. | MVP |
| NFR-026 | Documented. | README, API docs (Swagger), database and architecture docs, ADRs and progress file are kept up to date. | MVP |
| NFR-027 | Consistent code style. | Formatting rules agreed and checked (Phase 9). | Planned |

### 5.6 Usability and accessibility

| ID | Requirement | Acceptance criteria | Priority |
|----|-------------|---------------------|----------|
| NFR-028 | Responsive UI. | Core pages usable on phone and desktop widths. | MVP |
| NFR-029 | Clear feedback. | Loading, empty and error states are shown; form errors appear next to the field. Password rules are shown before the user submits. | MVP |
| NFR-030 | Basic accessibility. | Forms have labels, keyboard navigation works, colors have adequate contrast (checked against common accessibility guidelines). | MVP (goal) |
| NFR-031 | Browser support. | Latest versions of Chrome, Edge and Firefox. | MVP |

### 5.7 Data privacy

Candidate data (names, contact details, resumes) is personal data. Even for a portfolio project it
is handled as if real.

| ID | Requirement | Acceptance criteria | Priority |
|----|-------------|---------------------|----------|
| NFR-032 | Least-privilege access to personal data. | Candidate data is visible only to the candidate, relevant recruiters (applicants to their jobs, and only the resume attached to the application) and, for account and platform data, admins. [A] (D-24, D-28) Admins cannot download resume files or view applicant details in the MVP unless a future requirement authorizes it. Anonymous visitors see none. | MVP |
| NFR-033 | No personal data in logs. | Logs contain no resume text, passwords, tokens, rejection or reopen reasons, or full personal details (including phone numbers). | MVP |
| NFR-034 | Only synthetic data in demos and public material. | Seed/demo data is fake; no real candidate data in the repository, screenshots or demos. | MVP |
| NFR-035 | Data deletion and retention. | [A] (D-03) Permanent deletion of jobs is admin-only and subject to retention rules. [A] (D-16) The full retention policy is **not yet defined** (Q7). Until it is, the system refuses to permanently delete any job that has applications. [S] What happens to applications and resumes when an account is deleted is also undefined, so **account deletion is not offered in the MVP**. | Planned |
| NFR-036 | Data minimization. | Only data needed for hiring is collected; sensitive attributes (e.g. age, gender, religion) are not collected or used for ranking. Contact details are limited to the email (required) and an optional phone number (D-13). | MVP |
| NFR-037 | Data in transit is protected in any real deployment. | HTTPS is required outside local development. | Planned |

**Decision – privacy is a requirement, not a polish step:** designing "who may see what" now
affects the ER design (Task 0.6) and every endpoint, and is much harder to retrofit.

---

## 6. Design implications for the database (input to Task 0.6)

These follow from the approved decisions. They are **inputs to the ER design, not the design
itself**. Task 0.6 will decide the details.

| Decision | Implication for the database |
|----------|------------------------------|
| D-01 Anonymous browsing | No schema change, but public-vs-private fields must be separable: job and company data are public; candidate, application and resume data never are. Index the job columns used for filtering (status, location, type, created date). |
| D-02, D-10, D-11, D-14 Controlled workflow, reopen, offer outcomes | `Application.status` holds the current status with the values `APPLIED`, `SHORTLISTED`, `INTERVIEW`, `OFFER`, `ACCEPTED`, `DECLINED`, `REJECTED`. A new **ApplicationStatusHistory** table (application, from-status, to-status, changed-by user, timestamp, note/reason) is needed for FR-035 AC4, FR-036 and FR-037. The changed-by user may be a recruiter **or the candidate** (offer response). The reason is **mandatory (non-empty) for rejection and reopen** rows (D-25, D-11), enforced in the service layer and, where practical, by a database check constraint. Reopen reads the `from_status` of the latest rejection row. Optimistic locking (version column) on `Application`. |
| D-03, D-16 Close, not delete | `Job.status` (`OPEN`/`CLOSED`) is required. `Application → Job` must **not** cascade deletes (`ON DELETE RESTRICT`), so the database itself also refuses to delete a job that has applications. Because such jobs can never be hard-deleted in the MVP, soft-delete for jobs is not needed yet; the retention policy (Q7) may change that. |
| D-12 Audit history | Besides `ApplicationStatusHistory`, an **AuditLog** table records admin deletions (who, when, entity type, entity id and a plain-value title). It has **no foreign key** to the deleted row, otherwise the audit entry would disappear with it. Append-only: no update or delete endpoint. |
| D-04 Password policy | `User.password_hash` stores only the BCrypt hash. Hash column length must fit it. No plain password column anywhere. |
| D-05, D-13 Registration fields | Name and email are mandatory and email is unique on `User`. `phone` and every other profile field are **nullable**. No resume is required to create a candidate. (Task 0.6 proposal DD-13 stores `full_name` on `User`, because recruiters and admins have names too; see `docs/database/database-design.md`.) |
| D-06 Multiple resumes | **Resume is one-to-many from the candidate** (CandidateProfile 1 → N Resume), not one-to-one. `Application.resume_id` is a **NOT NULL** foreign key to `Resume`: a resume is mandatory when applying (D-15). The number of **active** (non-hidden) resumes per candidate (max 5, D-09, D-31) is enforced in the service layer; the 5 MB limit applies at upload, and `size_bytes` is stored. A constraint or service check ensures the resume belongs to the same candidate. A **unique** constraint on (candidate, job) prevents duplicate applications (FR-032 AC3). A resume referenced by an application must not be physically deleted (FR-042 AC2): hence a "hidden" flag on `Resume` plus `ON DELETE RESTRICT`, which is now required because the foreign key is NOT NULL (D-21). A resume that no application uses can be physically deleted. |
| D-17 Resume security | `Resume` stores only metadata and an opaque `storage_key`, **never a URL**. The bucket name comes from configuration, not from a row. Downloads always go through an authorization check (FR-041). |
| One role, one company (MVP) | `User` has a single `role` column. Company belongs to its recruiter user. The roadmap lists six entities (`User`, `CandidateProfile`, `Company`, `Job`, `Application`, `Resume`) with no separate recruiter table; Task 0.6 decides whether recruiter data lives on `User` or in a small `RecruiterProfile`, and how it links to `Company`. |
| D-07 AI advisory | AI results (Phase 7) are stored in **separate tables** from `Application` and never write `Application.status`. |

---

## 7. Traceability

Each requirement ID is intended to be referenced by:
- Kanban cards (Task 0.7),
- user stories (Task 0.5),
- tests (test names or comments mention the ID),
- `PROJECT_PROGRESS.md` entries.

---

## 8. Assumptions and constraints

- Single developer; learning is a first-class goal (see `HIRESENSE.md`).
- Tech stack as in `HIRESENSE.md` (Spring Boot + Angular + PostgreSQL first). The project currently uses newer framework versions than the roadmap text (for example Angular 21 and Spring Boot 4.x); requirements do not depend on versions.
- Local AI models (Ollama) are used in Phase 7, so AI quality will be modest.
- No real users, no real personal data.

---

## 9. Open questions

### 9.1 Resolved by the owner

| # | Question | Resolution |
|---|----------|------------|
| Q1 | May anonymous visitors browse jobs? | **Yes**; login only to apply (D-01). |
| Q2 | Status transitions and reopening? | Controlled forward-only workflow, rejection from `APPLIED`/`SHORTLISTED`/`INTERVIEW`, reopen by an authorized recruiter with a mandatory reason, back to the previous status (D-02, D-10, D-11). |
| Q2a | Confirm the proposed workflow and the reopen rules. | **Approved**, extended with `ACCEPTED`/`DECLINED` after `OFFER` (D-14). |
| Q3 | Can a job be deleted, or only closed? | Normally closed; admin-only permanent deletion, refused while the job has applications (D-03, D-16). |
| Q4 | Final password policy? | 8–64 characters, common-password blocklist, no special-character rules, secure hashing (D-04, D-08). |
| Q5 | Which candidate fields are mandatory? | **Name and email only** (D-13). |
| Q6 | Multiple resumes per candidate? | Yes, up to 5, 5 MB each, one chosen per application (D-06, D-09). |
| Q8 | May an admin reopen a rejected application? | **No.** Only the owning recruiter (D-20). |
| Q9 | What happens to a resume an application still uses when the candidate removes it? Do hidden resumes count toward the limit of 5? | It is only hidden from the active list and always retained for application history (D-21). Hidden resumes do **not** count toward the limit of 5, and a hidden resume that no application uses may be permanently deleted (D-31). |
| Q10 | Should a resume be required to apply? | **Yes** (D-15). |
| Q11 | Is "basic contact information" just a phone number? | Phone is **optional** (D-13). |
| Q12 | What is public data for anonymous visitors? | **Job details and basic company information only** (D-22). The exact company fields are assumed [S] in FR-031 AC6. |
| Q13 | Who sets `ACCEPTED` / `DECLINED`? | **The candidate.** The recruiter issues the offer but cannot answer for the candidate (D-18). |
| Q14 | May a rejected application be reopened after its job closes? | **No**, not in the MVP (D-23). |
| Q15 | BCrypt 72-byte limit vs 64 characters? | **Validate the UTF-8 byte length before hashing**, show a clear error, never truncate (D-19). |
| Q16 | May admins download resumes? | **No**, not in the MVP (D-24). |
| Q17 | Is a reason required when rejecting? | **Yes, mandatory** (D-25). |
| Q18 | What can still happen to applications of a closed job? | Closing stops **new** applications only; existing ones continue (interviews, offers, offer responses). Reopening rejected ones stays prohibited (D-26, D-23). |
| Q19 | Are rejection and reopen reasons visible to candidates? | **No**, internal in the MVP; the UI reminds recruiters to be factual and job-related (D-27). |
| – | May admins see applicants or resumes? | **No**, unless a future requirement authorizes it (D-28). |
| – | When does a role change take effect? | At the user's **next login** (D-29). |
| – | Can a candidate change an accepted or declined decision? | **No**, offer decisions are final in the MVP (D-30). |

### 9.2 Still open

| # | Question | Interim position | Needed by |
|---|----------|------------------|-----------|
| Q7 | **Data-retention policy:** how long are applications, resumes (including hidden ones) and closed jobs kept? What happens when an account is deleted? When may an admin delete a job that has applications? | Interim [A] (D-16): jobs with applications cannot be permanently deleted; account deletion is not offered in the MVP [S]. The owner will finalize the policy in a later task. | Task 0.6 / Phase 2 |

---

## 10. Requirement index

| Area | IDs |
|------|-----|
| Auth and accounts | FR-001 to FR-005 |
| Candidate | FR-010 |
| Company | FR-020 |
| Jobs | FR-030, FR-031, FR-052 |
| Applications | FR-032 to FR-037 |
| Resumes | FR-040 to FR-042 |
| Admin | FR-050, FR-051 (FR-052 is listed under Jobs) |
| Planned | FR-060 to FR-080 |
| Non-functional | NFR-001 to NFR-040 |
| Approved decisions | D-01 to D-31 |
