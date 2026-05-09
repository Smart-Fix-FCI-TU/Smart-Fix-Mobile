# Domain-Specific Requirement Patterns

Reference this file when the feature being analyzed falls into one of the domains below.
Use the patterns as supplements — add them to the relevant sections of the main template.

---

## Table of Contents
1. [Mobile App Features](#1-mobile-app-features)
2. [REST API Endpoints](#2-rest-api-endpoints)
3. [Admin Dashboards](#3-admin-dashboards)
4. [Authentication & Authorization Flows](#4-authentication--authorization-flows)
5. [Notification Systems](#5-notification-systems)
6. [Data Listing / Search / Filter Screens](#6-data-listing--search--filter-screens)
7. [Forms & Data Entry](#7-forms--data-entry)
8. [Payment & Checkout Flows](#8-payment--checkout-flows)

---

## 1. Mobile App Features

### Additional Questions to Ask
- Android, iOS, or both? Minimum OS version?
- Offline support required?
- Are there platform-specific behaviors expected (e.g. iOS haptics, Android back button)?
- Is the feature gated behind permissions (camera, location, notifications)?
- Any deep-link or push notification entry points?

### Extra Sections to Add

**Platform & Device Requirements**
| Platform | Min Version | Notes |
|----------|-------------|-------|
| Android | 9.0+ | |
| iOS | 15.0+ | |

**Permissions Required**
- e.g. Camera — for profile photo upload
- e.g. Push notifications — for order updates

**Offline Behavior**
- What data is available offline?
- What actions are blocked without connectivity?
- How does the UI communicate offline state?

---

## 2. REST API Endpoints

### Additional Questions to Ask
- Who are the API consumers (mobile, web, third-party)?
- What authentication scheme is used (JWT, OAuth, API key)?
- Are there rate limits required?
- What versioning strategy is in use (v1/, header-based)?
- Are there bulk operations needed?

### Extra Sections to Add

**Endpoint Specification**

For each endpoint:
```
[METHOD] /path/to/resource

Description: What this endpoint does

Auth: Required / Optional / None
Scope required: e.g. write:orders

Path Parameters:
- :id — UUID — The resource ID

Query Parameters:
- page — integer — Page number (default: 1)
- limit — integer — Items per page (default: 20, max: 100)

Request Body:
{
  "field": "type" — description
}

Success Response: 200 OK
{
  "field": "value"
}

Error Responses:
- 400 Bad Request — Invalid input
- 401 Unauthorized — Missing or invalid token
- 404 Not Found — Resource does not exist
- 422 Unprocessable Entity — Validation failed
```

---

## 3. Admin Dashboards

### Additional Questions to Ask
- Who are the admins? Is there a hierarchy (super-admin vs. read-only)?
- What actions can admins take (view-only, edit, delete, impersonate)?
- Is audit logging required for actions taken?
- What date ranges or filters are needed on data?
- Are exports (CSV, PDF) required?

### Extra Sections to Add

**Admin Roles & Permissions**
| Role | Can View | Can Edit | Can Delete | Can Export |
|------|----------|----------|------------|------------|
| Super Admin | ✅ | ✅ | ✅ | ✅ |
| Support Agent | ✅ | ✅ | ❌ | ❌ |
| Read-Only | ✅ | ❌ | ❌| ✅ |

**Audit Requirements**
- Which actions must be logged?
- What metadata to capture (user, timestamp, before/after values)?
- How long are audit logs retained?

---

## 4. Authentication & Authorization Flows

### Additional Questions to Ask
- Login methods: email/password, SSO (Google, Apple), magic link, OTP?
- MFA required? Optional or enforced for certain roles?
- Session management: token expiry, refresh tokens, simultaneous sessions?
- Password policy: length, complexity, expiry, history?
- Account recovery: forgot password, locked accounts?
- What happens to session on password change?

### Extra Sections to Add

**Auth Methods**
| Method | Required | Notes |
|--------|----------|-------|
| Email + Password | Yes | |
| Google SSO | Yes | |
| Apple Sign-In | iOS only | |
| MFA (TOTP) | Optional for users, required for admins | |

**Token Behavior**
- Access token TTL: e.g. 15 minutes
- Refresh token TTL: e.g. 30 days, sliding window
- Revocation: on logout, password change, and admin action

**Security Requirements**
- Brute-force protection: lockout after N failed attempts
- Rate limiting on auth endpoints
- Secure token storage (HttpOnly cookies / secure keychain)

---

## 5. Notification Systems

### Additional Questions to Ask
- Channels: push, email, SMS, in-app, or combination?
- Who controls notification preferences — the user, admin, or both?
- Are notifications transactional (triggered by action) or marketing/promotional?
- Is there a notification center / inbox in the app?
- Localization / timezone handling needed?
- What are retry and failure behaviors?

### Extra Sections to Add

**Notification Catalog**
| Event | Channel(s) | Recipient | Can User Disable? | Template |
|-------|-----------|-----------|-------------------|----------|
| Order confirmed | Email + Push | Buyer | No | order_confirmed |
| New message | Push + In-app | Recipient | Yes | new_message |
| Payment failed | Email | Account owner | No | payment_failed |

**Delivery Requirements**
- Max delay from trigger to delivery: e.g. < 5 seconds for push
- Retry policy on failure: e.g. 3 retries with exponential backoff
- Bounce / unsubscribe handling (especially for email — CAN-SPAM / GDPR)

**User Preferences**
- Which notifications can users opt out of?
- Where do they manage preferences (profile settings)?
- Are preferences per-channel (e.g. email yes, push no)?

---

## 6. Data Listing / Search / Filter Screens

### Additional Questions to Ask
- What entity is being listed?
- What columns/fields are shown in the list vs. detail view?
- Is search required? Full-text or field-specific?
- What filters are available? Are they persisted?
- Sorting — which columns, default sort, single vs. multi-column?
- Pagination, infinite scroll, or load-more?
- Empty state: what does the user see when there's no data?

### Extra Sections to Add

**List View Spec**
| Column | Type | Sortable | Filterable | Default |
|--------|------|----------|------------|---------|
| Name | Text | Yes | No | — |
| Status | Badge | No | Yes | All |
| Created At | Date | Yes | Yes (range) | Desc |

**Search Behavior**
- Search scope: which fields are indexed?
- Min characters before search triggers: e.g. 2
- Debounce: e.g. 300ms
- Search performed: client-side or server-side?

**Pagination**
- Style: numbered pages / infinite scroll / load-more button
- Default page size: e.g. 20
- Max page size: e.g. 100

---

## 7. Forms & Data Entry

### Additional Questions to Ask
- What data is collected? Is any of it sensitive (PII, financial)?
- Is the form single-step or multi-step?
- Are there conditional fields (show field B if field A = X)?
- What are the validation rules per field?
- Is draft/auto-save needed?
- What happens on submit success and failure?
- Is the form editable after submission?

### Extra Sections to Add

**Field Specification**
| Field | Type | Required | Validation | Notes |
|-------|------|----------|------------|-------|
| Full Name | Text | Yes | Max 100 chars, letters only | |
| Email | Email | Yes | Valid format, unique | |
| Phone | Tel | No | E.164 format | |
| Date of Birth | Date | Yes | Must be 18+ | |

**Validation Strategy**
- Client-side: real-time inline validation on blur
- Server-side: full validation on submit; errors returned per-field
- Error display: inline below each field, summary at top of form for accessibility

**Submission Behavior**
- Success: [action — e.g. redirect to confirmation page, show inline success]
- Failure (validation): highlight fields, focus first error
- Failure (server): show dismissible error banner, do not clear form

---

## 8. Payment & Checkout Flows

### Additional Questions to Ask
- Payment provider: Stripe, PayPal, other?
- Payment methods: card, Apple Pay, Google Pay, bank transfer?
- One-time or recurring (subscriptions)?
- Multi-currency support?
- Are there promo codes or discounts?
- Refund policy — can users request via the app?
- What tax handling is required?
- Compliance requirements: PCI-DSS, SCA (Europe)?

### Extra Sections to Add

**Payment Methods**
| Method | Required | Notes |
|--------|----------|-------|
| Credit / Debit Card | Yes | Via Stripe Elements |
| Apple Pay | iOS | Requires domain verification |
| Google Pay | Android | |
| SEPA Direct Debit | EU users | |

**Checkout Flow States**
- Idle → Item added to cart
- Cart → Review → Payment info → Confirmation
- Payment processing → Success | Failure
- On failure: allow retry without re-entering info

**Compliance**
- PCI scope: use Stripe-hosted fields to minimize scope
- 3DS / SCA: handled by Stripe, redirect flow supported
- Receipts: email sent on successful charge, includes itemized breakdown