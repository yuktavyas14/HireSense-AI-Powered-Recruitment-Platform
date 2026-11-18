# HireSense – Roles, Permissions and User Stories

> **Version:** 0.3 (Task 0.5)
> **Based on:** `docs/requirements.md` v0.6 (decisions D-01..D-31).
> **Status:** Planning document. **Nothing described here is implemented.**
> If a story and `requirements.md` ever disagree, `requirements.md` wins; fix the story.

---

## 1. How to read this document

**Story format:** *As a `<role>`, I want `<goal>`, so that `<benefit>`.*

**Acceptance criteria (AC)** use *Given / When / Then*:
**Given** the starting state, **when** the user does something, **then** this is what they observe.
Each AC is written to be turned into a test (backend test, frontend test or end-to-end test).

**Each story has:**
- an ID (`US-V` visitor, `US-U` any registered user, `US-C` candidate, `US-R` recruiter, `US-A` admin),
- the requirement IDs it comes from (traceability),
- a priority (`MVP` or `Planned`), using the same tiers as `requirements.md`.

**Tags** are the same as in `requirements.md`: **[A]** approved, **[P]** proposal, **[S]** assumption.
A story with no tag on a line follows directly from approved requirements.

**Stories become Kanban cards** (Task 0.7) and **tests** (every phase). Stories are intentionally small; a
story that cannot be finished in a few days should be split.

---

## 2. Roles

| Role | Stored as a role? | Definition |
|------|-------------------|------------|
| **Visitor** | No | A request with no token. Not an account. [A] (D-01) |
| **Candidate** | Yes (`CANDIDATE`) | A person looking for work. |
| **Recruiter** | Yes (`RECRUITER`) | An employer's representative. Belongs to **one** company (MVP). |
| **Admin** | Yes (`ADMIN`) | A platform operator. Created by other means than self-registration (FR-001 AC4). |

**One role per account** (MVP). The terms below are used in the matrix:
- **Owner (candidate):** the candidate who submitted the application or uploaded the resume.
- **Owning recruiter:** the recruiter whose company owns the job. Another recruiter, even from another company, is a *non-owner*.

---

## 3. Permission matrix

✔ = allowed. ✘ = refused (**401** if no valid token, **403** if logged in without permission). — = not applicable.

| Capability | Visitor | Candidate | Owning recruiter | Other recruiter | Admin |
|------------|:-------:|:---------:|:----------------:|:---------------:|:-----:|
| Browse, search jobs; view job details and basic company info | ✔ | ✔ | ✔ | ✔ | ✔ |
| Register, log in, request password reset | ✔ | — | — | — | — |
| Edit own account/profile | ✘ | ✔ (own) | ✔ (own) | ✔ (own) | ✔ (own) |
| Upload, list and remove **own** resumes | ✘ | ✔ | ✘ | ✘ | ✘ |
| Apply for a job (with a chosen resume) | ✘ (401) | ✔ | ✘ | ✘ | ✘ |
| See own applications and status | ✘ | ✔ (own only) | — | — | — |
| Create/edit company profile | ✘ | ✘ | ✔ (own company) | ✘ | ✘ |
| Create, edit, close/reopen a job | ✘ | ✘ | ✔ (own jobs) | ✘ | ✘ |
| See applicants of a job and their profiles | ✘ | ✘ | ✔ | ✘ | **✘** (D-28) |
| Download a resume | ✘ | ✔ (own) | ✔ (only the one attached to an application for their job) | ✘ | **✘** (D-24) |
| Move an application forward / reject it | ✘ | ✘ | ✔ | ✘ | ✘ |
| Reopen a rejected application | ✘ | ✘ | ✔ (job must be `OPEN`) | ✘ | **✘** (D-20, D-23) |
| Accept or decline an offer | ✘ | ✔ (own application, status `OFFER`) | **✘** (D-18) | ✘ | ✘ |
| Permanently delete a job | ✘ | ✘ | ✘ | ✘ | ✔ only if the job has **no** applications (D-16) |
| List users, enable/disable accounts, change roles | ✘ | ✘ | ✘ | ✘ | ✔ |
| View platform statistics (Planned) | ✘ | ✘ | ✘ | ✘ | ✔ |

[A] (D-28) Admins cannot view applicants, applicant details or resumes in the MVP unless a future requirement authorizes it. They get account and platform data only. A denial in this matrix is an **explicit prohibition**, not a missing feature: it must be tested as a 403.

**Decision – a matrix before code:** the matrix is the checklist for Phase 2. Each cell becomes a test, and any
endpoint not in the matrix is protected by default (FR-005 AC4).

### 3.1 Who may make each application status change

| Transition | Who | Reason required | Condition |
|------------|-----|-----------------|-----------|
| `APPLIED → SHORTLISTED` | Owning recruiter | Optional note | – |
| `SHORTLISTED → INTERVIEW` | Owning recruiter | Optional note | – |
| `INTERVIEW → OFFER` (recruiter **issues the offer**) | Owning recruiter | Optional note | – |
| `OFFER → ACCEPTED` | **Candidate (owner) only** | – | Status must be `OFFER` |
| `OFFER → DECLINED` | **Candidate (owner) only** | – | Status must be `OFFER` |
| `APPLIED`/`SHORTLISTED`/`INTERVIEW → REJECTED` | Owning recruiter | **Mandatory** (D-25) | – |
| `REJECTED → previous status` (reopen) | Owning recruiter | **Mandatory** (D-11) | Job must be `OPEN` (D-23) |
| Anything else (skipping, going backwards, from `ACCEPTED`/`DECLINED`, from `OFFER` to `REJECTED`) | Nobody | – | Refused with a clear error |

Every change is written to the audit history (D-12). AI can never trigger any of these (NFR-021).

---

## 4. Visitor stories

**US-V01 – Browse and search jobs.** *MVP.* Requirements: FR-031, NFR-002, NFR-038.
As a **visitor**, I want to search and filter open jobs without creating an account, so that I can see if HireSense has jobs worth signing up for.
- Given I am not logged in, when I open the job list, then I see open jobs, paginated, newest first.
- Given I enter a keyword, location or employment type, when I search, then only matching open jobs are shown.
- Given a job is closed, when I list or search jobs, then it does not appear.
- Given I request an oversized page, when the server answers, then the page size is capped at the maximum.

**US-V02 – View job details.** *MVP.* Requirements: FR-031 AC4/AC6, D-22.
As a **visitor**, I want to read the full job description and basic company information, so that I can judge whether to apply.
- Given I am not logged in, when I open a job, then I see its details and the company's name, description, website and location [S: exact fields].
- Given I am not logged in, when I look at any job or company, then I never see candidate, application or resume data, or a recruiter's personal contact details.

**US-V03 – Be asked to log in when applying.** *MVP.* Requirements: FR-032 AC1, FR-031 AC7, D-01.
As a **visitor**, I want a clear route to log in or register when I try to apply, so that I do not lose my place.
- Given I am not logged in, when I choose "Apply", then I am sent to log in or register and returned to that job afterwards.
- Given I call the apply API without a token, when the server answers, then it returns 401 and creates nothing.

**US-V04 – Register as a candidate.** *MVP.* Requirements: FR-001, D-05, D-13, NFR-007.
As a **visitor**, I want to register as a candidate with just my name, email and password, so that I can start applying quickly.
- Given I enter a valid name, a unique email and a valid password, when I submit, then my account is created and I never see my password again in any response.
- Given I leave out my phone number, when I submit, then registration still succeeds (phone is optional).
- Given I do not upload a resume, when I register, then registration still succeeds.
- Given the email already exists, when I submit, then I get a clear error and no second account is created.
- Given my password is shorter than 8 characters, or longer than 64 characters or 72 bytes, or is on the common-password list, when I submit, then I get a clear message saying why, and nothing is truncated.

**US-V05 – Register as a recruiter.** *MVP.* Requirements: FR-001 AC1/AC4/AC6.
As a **visitor** who recruits, I want to register as a recruiter, so that I can post jobs for my company.
- Given I enter a valid name, a unique email and a valid password and choose Recruiter, when I submit, then my account is created without a company; I am asked to create one next.
- Given I try to choose the Admin role, when I submit, then it is refused.

---

## 5. Stories for any registered user

**US-U01 – Log in.** *MVP.* Requirements: FR-002, NFR-006, NFR-013.
As a **registered user**, I want to log in with my email and password, so that I can use my account.
- Given valid credentials, when I log in, then I receive an access token and land on my own dashboard.
- Given a wrong email or a wrong password, when I log in, then I get the same generic error (it never reveals whether the email exists).
- Given my account is disabled, when I log in, then I am refused.
- Given I fail to log in many times in a row, when I try again, then I am temporarily limited (rate limiting).

**US-U02 – Stay logged in and log out.** *MVP.* Requirements: FR-003, NFR-006.
As a **registered user**, I want to stay signed in without retyping my password and to log out when I choose, so that I am both comfortable and safe.
- Given my access token expired but my refresh token is valid, when I use the app, then I get a new access token and the old refresh token stops working.
- Given I log out, when someone later presents my refresh token, then it is refused.

**US-U03 – Reset a forgotten password.** *MVP (end of Phase 2).* Requirements: FR-004, NFR-007.
As a **registered user**, I want to reset my password, so that I can regain access.
- Given I request a reset for any email, when the server answers, then the answer is identical whether or not the email exists.
- Given I open a valid reset link, when I set a new password meeting the policy, then it works once and the link can no longer be used.
- Given the link is old, when I use it, then it is refused.

---

## 6. Candidate stories

**US-C01 – Maintain my profile.** *MVP.* Requirements: FR-010, D-13, NFR-032.
As a **candidate**, I want to keep my profile up to date, so that recruiters see my current details.
- Given I am logged in, when I open my profile, then I see only my own data.
- Given I clear my name or email, when I save, then I see a field-level error; clearing my phone number or any optional field is allowed.
- Given I enter a malformed phone number [S], when I save, then I see a field-level error.

**US-C02 – Upload a resume.** *MVP (Phase 4).* Requirements: FR-040, D-09, D-17.
As a **candidate**, I want to upload one or more PDF resumes, so that I can send the right one for each job.
- Given a valid PDF of at most 5 MB, when I upload, then it is stored privately and appears in my resume list (I may label it [P]).
- Given a file that is not a real PDF (even if renamed `.pdf`), when I upload, then it is rejected.
- Given a file larger than 5 MB, when I upload, then it is rejected with a clear message.
- Given I already have 5 **active** resumes, when I upload another, then it is refused with a clear message (D-09, D-31).

**US-C03 – Manage my resumes.** *MVP (Phase 4).* Requirements: FR-042, D-21, D-31.
As a **candidate**, I want to see and remove my resumes, so that I control what I keep on the platform.
- Given I open my resumes, when the list loads, then I see only mine.
- Given I remove a resume **no application uses**, when I confirm, then it is permanently deleted (D-31).
- Given I remove a resume that an application **uses**, when I confirm, then it disappears from my list but the application keeps it for the recruiter (D-21).
- Given I hid a resume that an application uses, when I count my active resumes, then the hidden one does **not** count toward my limit of 5 (D-31).

**US-C04 – Apply for a job.** *MVP.* Requirements: FR-032, D-01, D-06, D-15.
As a **candidate**, I want to apply to a job with a resume of my choice, so that I send the best match for that job.
- Given I am logged in as a candidate and have at least one resume, when I apply to an open job and pick a resume, then my application is created with status `APPLIED`.
- Given I have **no** resume, when I try to apply, then I am guided to upload one first and brought back to the job.
- Given I submit without choosing a resume (for example by calling the API), when the server answers, then I get a validation error (400) and nothing is created.
- Given I pick a resume that belongs to someone else, when I submit, then it is refused.
- Given I already applied to this job, when I apply again, then it is refused and no duplicate is created.
- Given the job is closed, when I apply, then it is refused.

**US-C05 – Track my applications.** *MVP.* Requirements: FR-033, NFR-032.
As a **candidate**, I want to see all my applications and their current status, so that I know where I stand.
- Given I am logged in, when I open "My applications", then I see only mine, with job title, company and status.
- Given I try to open someone else's application by its ID, when the server answers, then I get 403 (or not-found) and no data.

**US-C06 – See what happened to my application.** *MVP.* Requirements: FR-033 AC2/AC3, D-27.
As a **candidate**, I want to see the history of my application's status, so that I understand its progress.
- Given I open one of my applications, when it loads, then I see each status change with its date.
- Given a recruiter wrote a rejection or reopen reason, when I view my application, then I do **not** see that reason (it is internal, D-27).

**US-C07 – Accept or decline an offer.** *MVP.* Requirements: FR-037, D-14, D-18.
As a **candidate** who received an offer, I want to accept or decline it myself, so that the decision stays mine.
- Given my application is in `OFFER`, when I choose Accept or Decline, then the status becomes `ACCEPTED` or `DECLINED` and the change is recorded.
- Given my application is **not** in `OFFER`, when I try to respond, then it is refused with a clear error.
- Given a recruiter tries to accept for me, when the server answers, then it returns 403 (D-18).
- Given I already accepted or declined, when I try to change my answer, then it is refused: offer decisions are final in the MVP (D-30).

**US-C08 – Keep my resume private.** *MVP (Phase 4).* Requirements: FR-041, D-17, D-24, NFR-032.
As a **candidate**, I want only people I applied to, and me, to be able to download my resume, so that my personal data stays private.
- Given I own a resume, when I download it, then I receive the file as an attachment.
- Given a recruiter whose job I applied to with that resume, when they download it, then they receive it.
- Given the same recruiter asks for a different resume of mine that I did not send them, when the server answers, then it returns 403.
- Given an admin, another recruiter, another candidate or a visitor, when they request my resume, then they get 403 (or 401 without a token). There is **no permanent public link** to any resume (D-17).

---

## 7. Recruiter stories

**US-R01 – Create and edit my company profile.** *MVP.* Requirements: FR-020.
As a **recruiter**, I want a company profile, so that candidates see who is hiring.
- Given I have no company, when I try to post a job, then I am asked to create the company first.
- Given I save a valid company (name, description, website, location, industry), when the server answers, then it is stored and shown on my job pages.
- Given another recruiter's company, when I try to edit it, then I get 403.

**US-R02 – Post a job.** *MVP.* Requirements: FR-030.
As a **recruiter**, I want to create a job posting, so that candidates can find and apply to it.
- Given I have a company and fill title, description, location, employment type and skills, when I save, then the job is created `OPEN` and visible to visitors.
- Given I leave the title blank, when I save, then I see a validation error.

**US-R03 – Edit a job.** *MVP.* Requirements: FR-030, NFR-008.
As a **recruiter**, I want to edit my job postings, so that they stay accurate.
- Given my own job, when I change a field and save, then the change is shown to visitors.
- Given a job that belongs to another company, when I try to edit it, then I get 403.

**US-R04 – Close (and reopen) a job.** *MVP.* Requirements: FR-030 AC3/AC4/AC6, D-03.
As a **recruiter**, I want to close a job instead of deleting it, so that hiring history is preserved.
- Given an open job of mine, when I close it, then it disappears from public search but keeps all its applications.
- Given a closed job of mine [S], when I reopen it, then it is publicly visible again.
- Given I look for a way to permanently delete a job, when I check the app and the API, then no such function exists for recruiters.
- Given I close a job while applications are in progress, when I keep working on them, then they continue through the pipeline (shortlist, interview, offer, reject) and candidates can still answer offers; only reopening a rejected application is blocked (D-26, D-23).

**US-R05 – See applicants for my job.** *MVP.* Requirements: FR-034, NFR-008, NFR-032.
As a **recruiter**, I want to list the applicants of each of my jobs, so that I can review them.
- Given my own job, when I open its applicants, then I see each application with status and candidate name.
- Given a job of another company, when I request its applicants, then I get 403.

**US-R06 – Review a candidate's application and resume.** *MVP (resume: Phase 4).* Requirements: FR-034, FR-041, D-06, D-17.
As a **recruiter**, I want to see the candidate's profile and the resume they chose for this job, so that I can evaluate them.
- Given an application to my job, when I open it, then I see the profile and can download the **chosen** resume.
- Given the candidate later removed that resume from their list, when I open the application, then I can still download it (D-21).
- Given a candidate who did not apply to my jobs, when I request their profile or any resume, then I get 403.

**US-R07 – Move an application forward.** *MVP.* Requirements: FR-035, D-10, D-12, NFR-021.
As a **recruiter**, I want to shortlist a candidate and move them to interview, so that the pipeline reflects reality.
- Given an application in `APPLIED`, when I shortlist it, then it becomes `SHORTLISTED` and the change (who, when) is recorded.
- Given an application in `APPLIED`, when I try to move it directly to `INTERVIEW` or `OFFER`, then it is refused with a clear error.
- Given an application in `INTERVIEW`, when I try to move it back to `SHORTLISTED`, then it is refused.
- Given an application of another company, when I try to change it, then I get 403.

**US-R08 – Issue an offer.** *MVP.* Requirements: FR-035, FR-037, D-14, D-18.
As a **recruiter**, I want to issue an offer to a candidate, so that they can decide.
- Given an application in `INTERVIEW`, when I issue an offer, then the status becomes `OFFER` and the candidate can respond.
- Given an application in `OFFER`, when I try to mark it `ACCEPTED` or `DECLINED`, then I get 403 (only the candidate can).
- Given an application in `OFFER`, when I try to reject it, then it is refused (rejection is not allowed from `OFFER`).

**US-R09 – Reject with a reason.** *MVP.* Requirements: FR-035 AC5, D-10, D-25.
As a **recruiter**, I want to reject an application and record why, so that the decision can be audited.
- Given an application in `APPLIED`, `SHORTLISTED` or `INTERVIEW`, when I reject it with a reason, then it becomes `REJECTED` and the reason is stored in the audit history.
- Given I reject without a reason (empty or blank), when I submit, then it is refused and the status is unchanged.
- Given the UI shows the reason field, when I read it, then I am reminded to write factual, job-related reasons (D-27); the reason is never shown to the candidate.

**US-R10 – Reopen a rejected application.** *MVP.* Requirements: FR-036, D-11, D-20, D-23.
As a **recruiter**, I want to reopen an application I rejected by mistake, so that a good candidate is not lost.
- Given a `REJECTED` application of my open job, when I reopen it with a reason, then it returns to the status it had before the rejection and the reopen is recorded.
- Given I reopen without a reason, when I submit, then it is refused.
- Given the job is `CLOSED`, when I try to reopen the application, then it is refused (D-23).
- Given I am an admin, another recruiter or the candidate, when I try to reopen, then I get 403 (D-20).
- Given an `ACCEPTED` or `DECLINED` application, when I try to reopen it, then it is refused.

**US-R11 – Keep my applicants private from other recruiters.** *MVP.* Requirements: FR-005, NFR-008, NFR-032.
As a **recruiter**, I want other companies' recruiters not to see my applicants, so that my hiring stays confidential.
- Given another recruiter, when they request my job's applicants, an application or its resume by ID, then they get 403 (or not-found), and no data is returned.

---

## 8. Admin stories

**US-A01 – List and find users.** *MVP.* Requirements: FR-050.
As an **admin**, I want to see all accounts, so that I can manage the platform.
- Given I am an admin, when I open the user list, then I see accounts (paginated) with email, role and enabled state, but no password data.
- Given I am not an admin, when I request it, then I get 403.

**US-A02 – Disable or enable an account.** *MVP.* Requirements: FR-050, FR-002 AC2.
As an **admin**, I want to disable an abusive account, so that it can no longer log in.
- Given an active user, when I disable them, then their next login is refused.
- Given I disable my own admin account, when I confirm, then it is blocked or requires extra confirmation (FR-050 AC2).

**US-A03 – Change a user's role.** *MVP.* Requirements: FR-050.
As an **admin**, I want to change a user's role, so that mistakes can be corrected.
- Given a user, when I change their role, then the new role applies at their next login (D-29).
- Given I demote myself as the last admin [S], when I confirm, then it is blocked.

**US-A04 – Permanently delete a job that has no applications.** *MVP (simple).* Requirements: FR-052, D-03, D-12, D-16.
As an **admin**, I want to remove junk job postings, so that the platform stays clean.
- Given a job with **no** applications, when I delete it, then it is gone and the deletion is recorded (who, when, which job).
- Given a job with **at least one** application, when I try to delete it, then it is refused with a clear error (HTTP 409) and the job stays.
- Given a recruiter or candidate, when they try to delete a job permanently, then they get 403.

**US-A05 – See platform statistics.** *Planned.* Requirements: FR-051.
As an **admin**, I want counts of users, jobs and applications, so that I can see how the platform is used.
- Given I am an admin, when I open the dashboard, then I see these totals. (Details decided when planned.)

---

## 9. Planned stories (not designed in detail)

These are noted only for traceability. They are **not** part of the MVP.

| ID | Story (short) | Requirement | Phase |
|----|---------------|-------------|-------|
| US-P01 | As a candidate I get a live notification when my application's status changes. | FR-060 | 6 |
| US-P02 | As a user I receive an email for key events. | FR-061 | 6 |
| US-P03 | As a recruiter I see an AI match score **with an explanation**, labelled advisory; no applicant is ever hidden or auto-rejected. | FR-071, NFR-021, NFR-022 | 7 |
| US-P04 | As a candidate I get job recommendations and resume tips. | FR-072 | 7 |
| US-P05 | As a recruiter I can generate interview questions. | FR-073 | 7 |
| US-P06 | As a candidate I can ask a recruitment assistant questions. | FR-074 | 7 |
| US-P07 | As a visitor I get fast, typo-tolerant job search with autocomplete. | FR-080 | 8 |

---

## 10. Traceability and coverage

| Requirement area | Covered by |
|------------------|------------|
| FR-001 Registration | US-V04, US-V05 |
| FR-002 Login | US-U01, US-A02 |
| FR-003 Session / logout | US-U02 |
| FR-004 Password reset | US-U03 |
| FR-005 Role-based access | Section 3 matrix, US-R11, and the 401/403 criteria throughout |
| FR-010 Candidate profile | US-C01 |
| FR-020 Company | US-R01 |
| FR-030 Jobs | US-R02, US-R03, US-R04 |
| FR-031 Browse and search | US-V01, US-V02, US-V03 |
| FR-032 Apply | US-C04 |
| FR-033 Track applications | US-C05, US-C06 |
| FR-034 Review applicants | US-R05, US-R06 |
| FR-035 Status workflow | US-R07, US-R08, US-R09, section 3.1 |
| FR-036 Reopen | US-R10 |
| FR-037 Offer response | US-C07, US-R08 |
| FR-040 Upload resume | US-C02 |
| FR-041 Download resume | US-C08, US-R06 |
| FR-042 Manage resumes | US-C03 |
| FR-050 Manage users | US-A01, US-A02, US-A03 |
| FR-051 Statistics | US-A05 (Planned) |
| FR-052 Delete job | US-A04 |

**Count:** 31 MVP stories (5 visitor, 3 registered-user, 8 candidate, 11 recruiter, 4 admin) with 95 Given/When/Then acceptance criteria in total, plus 8 Planned stories (US-A05 and US-P01 to US-P07).

---

## 11. Open points that affect stories

| # | Point | Affects |
|---|-------|---------|
| Q7 | Data-retention policy (deferred by the owner). | US-A04, US-C03 |