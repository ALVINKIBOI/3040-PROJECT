
# 🎓 USIU Africa Lost and Found System

A Java-based desktop application for managing lost and found items at United States International University–Africa (USIU-Africa).

The system provides a centralized platform where students and staff can report lost or found items, search for belongings, receive automatically generated matching suggestions, submit ownership claims, and track recovery progress.

Administrators can review claims, verify ownership evidence, manage users, and maintain item categories and campus locations.

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Objectives](#objectives)
3. [Features](#features)
4. [Technology Stack](#technology-stack)
5. [System Architecture](#system-architecture)
6. [Project Structure](#project-structure)
7. [Database Design](#database-design)
8. [Installation and Setup](#installation-and-setup)
9. [Running the Application](#running-the-application)
10. [User Roles](#user-roles)
11. [Automatic Matching Algorithm](#automatic-matching-algorithm)
12. [Ownership Verification](#ownership-verification)
13. [Design Patterns and OOP Concepts](#design-patterns-and-oop-concepts)
14. [Testing](#testing)
15. [Security](#security)
16. [Known Limitations](#known-limitations)
17. [Future Improvements](#future-improvements)
18. [Contributing](#contributing)
19. [License](#license)

---

## 1. Project Overview

The **USIU Africa Lost and Found System** is a desktop application developed to improve the reporting, tracking, matching, and recovery of lost belongings within the university community.

Traditional lost-and-found procedures can involve manual recordkeeping, fragmented communication, and difficulties verifying ownership.

This application addresses these challenges by providing a structured digital workflow that connects individuals reporting lost items with individuals reporting found items.

The system supports:

- User registration and authentication
- Lost and found item reporting
- Item search and filtering
- Automatic matching of lost and found reports
- Ownership verification and claim submission
- Administrator claim approval and rejection
- Claim history tracking
- User notifications
- Administrator management tools

The application was developed as an **Object-Oriented Analysis and Design academic project**.

---

## 2. Project Objectives

### Main Objective

To develop a centralized desktop-based lost-and-found management system for USIU-Africa students and staff.

### Specific Objectives

- Provide secure account registration and authentication.
- Allow users to report lost and found items.
- Store item reports in a relational database.
- Support searching and filtering reported items.
- Automatically identify potential lost-and-found matches.
- Calculate similarity scores between item reports.
- Enable users to submit ownership claims.
- Support ownership verification using security questions.
- Allow administrators to approve or reject claims.
- Track claim statuses and item recovery progress.
- Notify users about important system events.
- Provide administrators with account and catalog management capabilities.

---

## 3. Features

### 🔐 3.1 User Authentication

The application provides:

- Student registration
- Staff registration
- Secure login
- Password hashing using PBKDF2 with SHA-256
- Role-based access control
- Account activation and deactivation
- Session-aware navigation between application windows

Supported roles:

- `STUDENT`
- `STAFF`
- `ADMIN`

Administrator accounts are provisioned through an authorized administrative process rather than public registration.

### 📊 3.2 Dashboard

The Dashboard provides access to the application's features.

Depending on the user's role, it includes:

- Report statistics
- Navigation to lost and found reporting
- Browse Items
- My Reports
- Suggested Matches
- My Claims
- Notifications
- Administrator management options

Administrator-only controls are restricted according to the authenticated user's role.

### 📦 3.3 Lost Item Reporting

Students and staff can report lost items by entering:

- Item title
- Category
- Description
- Location
- Date lost
- Distinguishing features
- Optional reward
- Optional item photograph

Reports are stored in MySQL.

New reports initially use the `OPEN` status and may subsequently be marked `MATCHED` when qualifying suggestions are generated.

### 🔎 3.4 Found Item Reporting

Users can report found items using:

- Item title
- Category
- Description
- Location found
- Date found
- Storage location
- Distinguishing features
- Verification question
- Verification answer
- Optional photograph

Verification answers are hashed before storage to protect sensitive ownership information.

### 🔍 3.5 Browse Items

The Browse Items interface supports:

- Viewing reported items
- Searching by keywords
- Filtering by item type and other available criteria
- Viewing item details
- Viewing uploaded photographs
- Opening the ownership claim form for eligible found items

The interface uses a Java Swing `JTable` connected to MySQL through JDBC.

### 📝 3.6 My Reports

Authenticated users can manage their own reports.

Available operations include:

- View submitted reports
- Search personal reports
- Edit eligible reports
- Soft-delete reports

Soft deletion preserves records in the database using the `is_deleted` field rather than permanently removing them.

Editing and deletion are subject to ownership and status restrictions.

### 🤖 3.7 Automatic Item Matching

The system automatically identifies potential matches between lost and found item reports.

Matching is implemented using the **Strategy Design Pattern**.

The current strategy evaluates:

| Matching Factor | Weight |
|-----------------|--------|
| Item title similarity | 35% |
| Description similarity | 25% |
| Category | 20% |
| Location | 10% |
| Date proximity | 10% |
| **Total** | **100%** |

Text similarity uses token-based Jaccard similarity.

A matching score is calculated from 0 to 100.

Suggested score classifications:

| Score | Classification |
|-------|----------------|
| 80–100 | High similarity |
| 65–79.99 | Moderate similarity |
| 55–64.99 | Possible match |
| Below 55 | Not recommended |

The current algorithm requires matching categories and excludes a user's own lost/found report pairs.

Potential matches are stored in the `matches` table.

**Note:** Matching scores represent heuristic similarity between reports, not a statistically calibrated probability of ownership.

### 🎯 3.8 Suggested Matches

The Suggested Matches JFrame allows users to:

- View potential matches for their lost items
- Compare matching scores
- Review selected suggested items
- Refresh matching suggestions
- Understand the matching score calculation
- Open the Claim Item form

Only eligible items are displayed as active suggestions.

### 🛡️ 3.9 Ownership Verification and Claims

Users can submit an ownership claim for an eligible found item.

The claim form collects:

- Verification question response
- Description of ownership evidence

The submitted answer is checked against the stored verification hash.

The verification outcome is stored in the database without storing the claimant's plaintext answer.

New claims receive the `PENDING` status.

The corresponding found item changes to:

`CLAIM_PENDING`

The same user cannot claim a found item they reported themselves.

### ✅ 3.10 Administrator Claim Review

Administrators can:

- View pending claims
- Inspect claimant information
- Review proof of ownership
- View verification results
- Approve ownership claims
- Reject ownership claims
- Add decision notes

Claim status transitions:

| Administrator Action | Claim Status | Found Item Status |
|----------------------|-------------|-------------------|
| Claim submitted | PENDING | CLAIM_PENDING |
| Approve | APPROVED | RETURNED |
| Reject | REJECTED | OPEN |

Claim and item-status updates are handled using JDBC transactions to maintain consistency.

Approval records the administrative outcome. Physical item handover should still be confirmed operationally.

### 📑 3.11 My Claims

Users can track claims they have submitted.

The interface displays:

- Claim ID
- Found item title
- Date submitted
- Claim status
- Ownership proof
- Administrator decision
- Review date
- Decision notes

Users can view their own claim history, including pending, approved, and rejected claims.

### 🔔 3.12 Notifications

The Notifications feature informs users about important events.

Supported notification types include:

- `CLAIM_SUBMITTED`
- `CLAIM_RECEIVED`
- `CLAIM_APPROVED`
- `CLAIM_REJECTED`
- `NEW_MATCH`

Users can:

- View notifications
- Check read/unread status
- Mark an individual notification as read
- Mark all notifications as read
- Refresh notification records

Notifications are stored in MySQL and accessed through the desktop application.

### 👨‍💼 3.13 Administrator User Management

Administrators can:

- View registered users
- Search by name, email, or USIU ID
- Activate accounts
- Deactivate accounts
- Change authorized account roles

The system includes restrictions against certain sensitive account-management operations, such as an administrator deactivating their own account.

### 🗂️ 3.14 Category Management

Administrators can:

- View item categories
- Add categories
- Update names and descriptions
- Delete categories not referenced by existing reports

Referenced categories are protected against deletion.

### 📍 3.15 Location Management

Administrators can:

- View campus locations
- Add locations
- Update location and building information
- Delete unused locations

Location records already associated with item reports are protected.

---

## 4. Technology Stack

| Technology | Purpose |
|-----------|---------|
| Java JDK 17+ | Application development |
| Java Swing | Desktop GUI |
| Apache NetBeans | Development environment |
| Apache Ant | Build automation |
| FlatLaf | Modern Swing interface styling |
| MySQL / MariaDB | Relational database |
| JDBC | Database connectivity |
| MySQL Connector/J | MySQL JDBC driver |
| XAMPP | Local database environment |
| PBKDF2WithHmacSHA256 | Password and verification-answer hashing |
| JUnit 5 | Unit testing, where configured |
| Git and GitHub | Source code version control |

---

## 5. System Architecture

The application follows a layered object-oriented architecture.

```text
+-------------------------------+
|       Presentation Layer      |
|       Java Swing Views        |
+-------------------------------+
                |
                v
+-------------------------------+
|        Controller Layer       |
|      Request Coordination     |
+-------------------------------+
                |
                v
+-------------------------------+
|         Service Layer         |
|   Validation & Business Rules |
+-------------------------------+
                |
                v
+-------------------------------+
|            DAO Layer          |
|         JDBC Operations       |
+-------------------------------+
                |
                v
+-------------------------------+
|        MySQL Database         |
+-------------------------------+
```

### Presentation Layer

Contains Java Swing JFrame Forms.

Responsibilities:

- Display application screens
- Accept user input
- Display validation messages
- Present database records
- Coordinate navigation between windows

### Controller Layer

Coordinates interactions between Views and Services.

Examples:

- Authentication controllers
- Item reporting controllers
- Claim controllers
- Matching controllers
- Notification controllers
- Administrator management controllers

### Service Layer

Handles application rules and validation.

Examples:

- Authentication validation
- Item reporting validation
- Ownership verification
- Matching calculations
- Claim processing
- Administrator authorization
- Catalog management

### DAO Layer

Handles SQL queries and database transactions.

Prepared statements are used for parameterized database operations.

### Model Layer

Contains domain entities, enums, and application data-transfer records.

---

## 6. Project Structure

The following structure represents the main implemented packages and components.

```text
3040-PROJECT/
|
|-- src/
|   |
|   |-- Controllers/
|   |   |-- AuthController.java
|   |   |-- ItemController.java
|   |   |-- ClaimController.java
|   |   |-- AdminClaimController.java
|   |   |-- MatchingController.java
|   |   |-- MyClaimsController.java
|   |   |-- NotificationController.java
|   |   |-- UserManagementController.java
|   |   |-- AdminCatalogController.java
|   |
|   |-- DAO/
|   |   |-- UserDAO.java
|   |   |-- ItemDAO.java
|   |   |-- ClaimDAO.java
|   |   |-- AdminClaimDAO.java
|   |   |-- MatchingDAO.java
|   |   |-- MyClaimsDAO.java
|   |   |-- NotificationDAO.java
|   |   |-- UserManagementDAO.java
|   |   |-- AdminCatalogDAO.java
|   |
|   |-- Database/
|   |   |-- DataBaseConnection.java
|   |   |-- TestConnection.java
|   |
|   |-- Models/
|   |   |-- User.java
|   |   |-- Student.java
|   |   |-- StaffMember.java
|   |   |-- Administrator.java
|   |   |-- Item.java
|   |   |-- LostItem.java
|   |   |-- FoundItem.java
|   |   |-- Claim.java
|   |   |-- Match.java
|   |   |-- Notification.java
|   |   |-- Category.java
|   |   |-- Location.java
|   |   |-- AuthUser.java
|   |   |-- MatchableItem.java
|   |   |-- MatchSuggestion.java
|   |   |-- MyClaimData.java
|   |   |-- NotificationData.java
|   |   |-- ManagedUser.java
|   |   |-- ManagedCatalogEntry.java
|   |   |-- CatalogKind.java
|   |
|   |-- Service/
|   |   |-- UserService.java
|   |   |-- ItemService.java
|   |   |-- ClaimService.java
|   |   |-- AdminClaimService.java
|   |   |-- MatchingStrategy.java
|   |   |-- WeightedMatchingStrategy.java
|   |   |-- MatchingService.java
|   |   |-- MyClaimsService.java
|   |   |-- NotificationService.java
|   |   |-- AdminAccess.java
|   |   |-- UserManagementService.java
|   |   |-- AdminCatalogService.java
|   |
|   |-- Views/
|   |   |-- LoginFrame.java
|   |   |-- RegisterForm.java
|   |   |-- Dashboard.java
|   |   |-- ReportLostItem.java
|   |   |-- ReportFoundItem.java
|   |   |-- BrowseItem.java
|   |   |-- MyReports.java
|   |   |-- ClaimItemForm.java
|   |   |-- ReviewClaims.java
|   |   |-- SuggestedMatches.java
|   |   |-- MyClaims.java
|   |   |-- Notifications.java
|   |   |-- ManageUsers.java
|   |   |-- ManageCategories.java
|   |   |-- ManageLocations.java
|   |
|   |-- utils/
|   |   |-- PasswordUtils.java
|   |   |-- VerificationAnswerUtils.java
|   |   |-- AppTheme.java
|   |   |-- PhotoUtils.java
|   |   |-- CatalogLoader.java
|   |   |-- CatalogManagementUI.java
|   |
|   |-- resources/
|   |   |-- USIU_Africa_Logo.png
|   |
|   |-- Main/
|   |   |-- Main.java
|
|-- nbproject/
|-- build.xml
|-- README.md
```

**Note:** The tree summarizes the main classes developed during the project. The exact repository contents may include additional model records, DAOs, forms, and NetBeans `.form` files.

---

## 7. Database Design

The application uses the MySQL database:

```sql
usiu_lost_found
```

### Main Database Tables

| Table | Description |
|-------|-------------|
| `users` | Registered students, staff, and administrators |
| `categories` | Item classifications |
| `locations` | Campus locations |
| `items` | Lost and found reports |
| `claims` | Ownership claims and decisions |
| `matches` | Automatically generated matching suggestions |
| `notifications` | User notifications |

### Users

The `users` table stores account information, including:

- User ID
- Full name
- USIU ID
- Email
- Password hash
- Phone number
- Role
- Account status
- Role-specific information

### Items

The `items` table stores:

- Item ID
- Item type
- Reporting user
- Category and location references
- Title and description
- Reported and occurrence dates
- Photo path
- Item status
- Lost-item distinguishing features and reward
- Found-item storage location
- Verification question and hashed answer
- Soft-deletion flag

### Claims

The `claims` table stores:

- Claim ID
- Claimant ID
- Found item ID
- Claim date
- Proof description
- Ownership verification result
- Claim status
- Reviewing administrator
- Decision note
- Review date

### Matches

The `matches` table stores:

- Match ID
- Lost item ID
- Found item ID
- Matching score
- Creation date
- Dismissal status

### Notifications

The `notifications` table stores:

- Notification ID
- Recipient ID
- Message
- Type
- Creation date
- Read status

### Referential Integrity

Foreign keys are used to maintain relationships between users, reports, claims, categories, locations, matches, and notifications.

---

## 8. Installation and Setup

### Prerequisites

Install the following:

- JDK 17 or later
- Apache NetBeans IDE
- Apache Ant
- XAMPP or another compatible MySQL server
- MySQL Connector/J
- FlatLaf library

### Step 1: Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
cd YOUR_REPOSITORY
```

Replace the placeholder values with your repository details.

### Step 2: Start MySQL

Using XAMPP:

1. Launch XAMPP Control Panel.
2. Start MySQL.
3. Start Apache if using phpMyAdmin.
4. Open:

```text
http://localhost/phpmyadmin
```

### Step 3: Create the Database

If a complete project SQL export is provided, import it using phpMyAdmin.

Otherwise, create the database first:

```sql
CREATE DATABASE IF NOT EXISTS usiu_lost_found;

USE usiu_lost_found;
```

Creating the database alone does not create its tables. The application's seven-table schema must also be imported or created before running the system.

### Step 4: Configure JDBC

Open:

```text
src/Database/DataBaseConnection.java
```

Ensure the JDBC connection uses your local database configuration.

Example:

```java
String url =
    "jdbc:mysql://localhost:3306/usiu_lost_found";

String username = System.getenv("USIU_DB_USER");
String password = System.getenv("USIU_DB_PASSWORD");
```

Configure the required environment variables before launching the application.

Avoid publishing database passwords or privileged production credentials in the repository.

### Step 5: Configure Project Libraries

Ensure these libraries are available in the NetBeans project:

- MySQL Connector/J
- FlatLaf
- JUnit dependencies, if running unit tests

For an Ant-based NetBeans project, inspect the project's Libraries section and any paths stored in its project configuration.

### Step 6: Open the Project

1. Launch NetBeans.
2. Select **File → Open Project**.
3. Select the cloned project directory.
4. Confirm that NetBeans recognizes `build.xml`.
5. Resolve any missing library references.
6. Select **Clean and Build**.

---

## 9. Running the Application

### Using NetBeans

1. Open the project.
2. Start MySQL.
3. Verify the JDBC configuration.
4. Right-click the project.
5. Select **Run**.

The application should open the Login JFrame through its configured main class.

### Using Apache Ant

From the project directory:

```bash
ant clean
ant build
ant run
```

The `run` target must be configured correctly in the NetBeans Ant project.

### Database Connection Test

Use the included:

```text
Database.TestConnection
```

Run it from NetBeans to verify JDBC connectivity.

A successful connection test should confirm that the application can access the configured database.

---

## 10. User Roles

### Student

Students can:

- Register and log in
- Report lost and found items
- Browse reports
- Search and filter items
- Manage their own reports
- View suggested matches
- Submit ownership claims
- Track claims
- Read notifications

### Staff

Staff members have similar item-reporting and claiming features, with role-specific account information.

### Administrator

Administrators can:

- Log in with an authorized administrator account
- Access administrator dashboard features
- Review submitted claims
- Approve or reject ownership claims
- Manage user accounts and roles
- Manage item categories
- Manage campus locations
- View relevant administrative records

Administrator functions are restricted through role checks in the service and database-access layers.

---

## 11. Automatic Matching Algorithm

The system implements weighted item matching through the Strategy Pattern.

### Matching Process

```text
New Lost or Found Report
          |
          v
Retrieve Eligible Opposite-Type Reports
          |
          v
Compare Report Attributes
          |
          v
Calculate Similarity Score
          |
          v
Check Minimum Threshold (55)
          |
          v
Store Qualifying Match in MySQL
          |
          v
Display Suggested Matches
          |
          v
User May Submit Ownership Claim
```

### Score Formula

```text
Score =
    (Title Similarity       × 35)
  + (Description Similarity × 25)
  + (Category Match         × 20)
  + (Location Match         × 10)
  + (Date Proximity         × 10)
```

The score ranges from 0 to 100.

The system uses Jaccard similarity to compare normalized sets of words in item titles and descriptions.

### Strategy Pattern Components

```text
MatchingStrategy (Interface)
          |
          v
WeightedMatchingStrategy
          |
          v
MatchingService
          |
          v
MatchingDAO
          |
          v
MySQL matches table
```

The strategy can be replaced or improved without redesigning the entire matching subsystem.

---

## 12. Ownership Verification

Found item reports include a verification question and answer.

### Reporting Process

1. A user reports a found item.
2. The user provides a verification question and expected answer.
3. The application hashes the expected answer.
4. The hash is stored in MySQL.

### Claim Process

1. Another user selects an eligible found item.
2. The claimant answers the verification question.
3. The system compares the submitted answer against the stored hash.
4. The verification result is recorded.
5. The claim is submitted for administrator review.

The verification result does not automatically approve the claim.

An administrator must consider the ownership evidence before making a decision.

---

## 13. Design Patterns and OOP Concepts

### Strategy Pattern

Used in automatic item matching.

Key classes:

- `MatchingStrategy`
- `WeightedMatchingStrategy`
- `MatchingService`

### Model-View-Controller Architecture

Separates:

- User interface
- Application logic
- Business rules
- Data persistence

### Data Access Object Pattern

DAO classes encapsulate JDBC operations and SQL queries.

### Encapsulation

Classes maintain private attributes and expose appropriate operations through constructors and methods.

### Inheritance

Examples from the domain model:

```text
User
├── Student
├── StaffMember
└── Administrator

Item
├── LostItem
└── FoundItem
```

### Polymorphism

Different matching strategies can implement the same `MatchingStrategy` interface.

### Abstraction

Abstract model classes and interfaces represent common behavior and contracts.

### Java Records

Records are used for several immutable application data-transfer objects.

### JDBC Transactions

Transactions help maintain consistency during:

- Claim submission
- Administrator approval or rejection
- Related notification creation
- Administrative updates

---

## 14. Testing

Testing focuses on both individual functionality and complete application workflows.

### Main Test Areas

| Test Area | Description |
|----------|-------------|
| Authentication | Valid and invalid login |
| Registration | Validation and duplicate-account prevention |
| Reporting | Saving lost and found items |
| Search | Searching and filtering records |
| Report Management | Editing and soft deletion |
| Automatic Matching | Similarity calculation |
| Ownership Verification | Correct and incorrect verification answers |
| Claims | Claim submission and status updates |
| Administrator Review | Approval and rejection |
| Notifications | Creation and read-status updates |
| User Management | Roles and account activation |
| Catalog Management | Category and location CRUD |
| Navigation | JFrame transitions and session preservation |

### Unit Testing

The matching algorithm can be tested independently using JUnit.

Example scenarios:

- Identical item descriptions
- Different item categories
- Reports created by the same user
- Different item titles
- Date proximity

### Integration Testing

End-to-end testing verifies:

```text
Registration
    ↓
Login
    ↓
Lost Item Report
    ↓
Found Item Report
    ↓
Automatic Matching
    ↓
Ownership Claim
    ↓
Administrator Review
    ↓
Claim Status Update
    ↓
User Notification
```

Test outcomes and screenshots should be recorded as part of the final academic documentation.

---

## 15. Security

The project includes the following security measures:

### Password Hashing

User passwords are stored using PBKDF2 with HMAC-SHA256 and per-password salts.

### Verification Answer Protection

Expected ownership-verification answers are hashed instead of stored as plaintext.

### Prepared Statements

Parameterized JDBC statements are used to reduce SQL injection risks.

### Role-Based Access Control

Administrative operations verify the user's role rather than relying exclusively on visible GUI buttons.

### Ownership Restrictions

Users are restricted from editing or deleting reports belonging to other accounts.

### Soft Deletion

Item reports are marked as deleted instead of immediately being permanently removed.

### Transaction Management

Related database updates are performed in transactions to reduce inconsistent records.

### Current Security Scope

This project is a desktop academic prototype.

Because JDBC database connectivity is embedded in the client application, it is not designed to provide production-grade isolation of database credentials or server-side authorization.

A production deployment should use a dedicated backend API, least-privilege database accounts, audit logging, and stronger session management.

---

## 16. Known Limitations

- The application is designed primarily for desktop computers.
- A local or reachable MySQL server is required.
- Notification delivery is currently database-based rather than email or push notification.
- Matching scores are heuristic estimates, not proof of ownership.
- Matching depends on accurate descriptions and categorization.
- The current matching strategy uses word similarity rather than semantic AI.
- Physical item collection is not independently verified through a separate handover confirmation workflow.
- The direct database connection architecture is intended for a controlled academic environment.
- Existing matching records may require reconciliation after report edits or deletion, depending on the final implementation.
- Previously submitted claims are not automatically reverified when verification rules change.

---

## 17. Future Improvements

Potential improvements include:

- Web-based application using Spring Boot or another backend framework
- Android and iOS support
- Email notifications
- Push notifications
- Advanced AI-based semantic item matching
- Image-based item similarity
- QR-code-based lost-item identification
- Audit logging for administrator actions
- Physical handover confirmation
- Improved reporting and analytics
- Multi-campus support
- Deployment to a managed server

---

## 18. Contributing

Contributions and improvements are welcome, subject to the repository owner's approval.

To contribute:

1. Fork the repository.
2. Create a feature branch.

```bash
git checkout -b feature/new-feature
```

3. Implement and test your changes.
4. Commit your changes.

```bash
git add .
git commit -m "Add new feature"
```

5. Push your branch.

```bash
git push origin feature/new-feature
```

6. Open a pull request for review.

---

## 19. License

This project was developed for academic purposes as part of an Object-Oriented Analysis and Design course at USIU-Africa.

No open-source license is declared in this README. See the repository for a `LICENSE` file if one is added.

---

## Project Status

**Major Functional Development: Completed**

Implemented modules include:

- Authentication and registration
- Dashboard
- Lost and found reporting
- Browse Items
- My Reports
- Ownership claims
- Administrator claim reviews
- Automatic item matching
- Suggested Matches
- My Claims
- Notifications
- User management
- Category management
- Location management

**Final integration testing, documentation verification, and submission preparation remain subject to completion and review.**

---

## Acknowledgements

This project was developed as part of academic coursework at **United States International University–Africa (USIU-Africa)**.

It demonstrates object-oriented programming, software architecture, relational database integration, GUI development, software design patterns, and application testing.

---

**USIU Africa Lost and Found System — Improving the reporting, matching, and recovery of lost belongings through technology.**
