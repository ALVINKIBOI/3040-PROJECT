# Lost and Found System - USIU Africa
## Project Overview

The **Lost and Found System** is a centralized platform designed for USIU Africa students and staff to report lost and found items, identify possible matches, claim belongings, and have administrators verify ownership. The system streamlines the process of recovering lost items through a structured workflow involving reporting, matching, claiming, and administration.

---
## Technology Stack
- **Language**: Java (JDK 17+)
- **Build Tool**: Apache Ant (configured via `build.xml`)
- **GUI**: Java Swing (desktop application)
- **Database**: MySQL
- **Database Connectivity**: JDBC
- **Project Structure**: Default NetBeans convention with source files under `src/`

---
## Existing Architecture
### Package Structure

```
src/
├── Controllers/     ← Empty controllers (logic to be implemented)
│   ├── ClaimController.java
│   ├── ItemController.java
│   └── LoginController.java
├── DAO/             ← Empty Data Access Objects (need implementation)
│   ├── CategoryDAO.java
│   ├── ClaimDAO.java
│   ├── ItemDAO.java
│   └── UserDAO.java
├── Database/        ← JDBC connection setup
│   ├── DataBaseConnection.java
│   └── TestConnection.java
├── Models/          ← Domain models (fully implemented)
│   ├── User.java      ← Abstract base class
│   ├── Student.java   ← Student role
│   ├── StaffMember.java   ← Staff role
│   ├── Administrator.java   ← Administrator role
│   ├── Item.java      ← Abstract base class for items
│   ├── LostItem.java  ← Lost item report
│   ├── FoundItem.java ← Found item report with verification
│   ├── Category.java  ← Item categorization
│   ├── Location.java  ← Report location
│   ├── ItemStatus.java← Item status enum
│   ├── ClaimStatus.java← Claim processing enum
│   ├── ReportType.java← Report type enum (LOST/FOUND)
│   ├── Claim.java     ← Claim/report submission
│   ├── Match.java     ← Lost-found item matching
│   └── Notification.java ← User notification system
└── Views/           ← GUI forms
    └── LoginFrame.java ← Login window (basic Swing JFrame)
```

### Core Models

| Class | Description |
|-------|-------------|
| `User` | Abstract base class with fields: `userID`, `fullName`, `email`, `passwordHash`, `usiuID`, `phone`, `isActive`. Subclasses: `Student`, `StaffMember`, `Administrator`. |
| `Item` | Abstract base class with fields: `itemID`, `title`, `description`, `category`, `location`, `dateReported`, `dateOccured`, `photoPath`, `status`. Subclasses: `LostItem`, `FoundItem`. |
| `LostItem` | Represents a lost item report. Extends `Item`. Adds `reporter` (User), `distinguishingFeatures`, and `rewardOffered`. |
| `FoundItem` | Represents a found item report. Extends `Item`. Adds `finder` (User), `storageLocation`, and verification `question`/`answer` for ownership claims. |
| `Claim` | Represents a claim submission linking a `claimant` (User) to a `foundItem`. Tracks `answerProvided`, `proofDescription`, `status`, and administrator review. |
| `Match` | Represents a potential match between a `LostItem` and a `FoundItem` with a confidence `score` (0-100). |
| `Category` | Item classification (e.g., Electronics, Clothing). |
| `Location` | Physical location where item was lost/found (name + building). |
| `ClaimStatus` | Enum: `PENDING`, `APPROVED`, `REJECTED`. |
| `ItemStatus` | Enum: `OPEN`, `MATCHED`, `CLAIM_PENDING`, `RETURNED`, `ARCHIVED`. |
| `ReportType` | Enum: `LOST`, `FOUND`. |
| `StaffMember` / `Student` / `Administrator` | Role-specific subclasses of `User`. |

### Key Workflows
1. **Reporting**: A student reports a lost or found item via the GUI. The item is stored with category, location, and status.
2. **Matching**: The system identifies potential matches between lost and found items based on category, location, and other attributes. Matches are scored (0-100%).
3. **Claiming**: When a match is identified, the claimant can submit a claim with answers to the verification question (for found items) and proof description.
4. **Administrator Review**: An administrator reviews claims and either approves or rejects them. Approved claims change status to `RETURNED`.
5. **Notification**: Users receive notifications about claim status updates.

### Database
- **Connection**: `DataBaseConnection.java` configures JDBC connection to MySQL at `jdbc:mysql://localhost/LOSTANDFOUNDSYSTEM` with `root`/`""` credentials.
- **DAO Layer**: Currently empty (`ItemDAO`, `CategoryDAO`, `UserDAO`, `ClaimDAO`). These need to be implemented for database operations (CRUD, matching queries, claim management).
- **TestConnection**: Verifies database connectivity and lists existing tables.

### GUI

- **LoginFrame**: Basic Swing JFrame serving as the entry point. Currently has no interactive components beyond the window.
- Additional GUI frames will be needed for: item reporting, item search/matching, claim submission, administrator dashboard, and user profile management.

---
## Setup & Running Instructions
### Prerequisites

- JDK 17 or higher
- Apache Ant (or use NetBeans IDE)
- MySQL Server 8.0+
- MySQL JDBC Driver (connector/j)

### 1. Database Setup
1. Start MySQL server
2. Create the database:
   ```sql
   CREATE DATABASE LOSTANDFOUNDSYSTEM;
   ```
3. Update `src/Database/DataBaseConnection.java` if using different credentials:
   ```java
   private static final String URL = "jdbc:mysql://localhost/LOSTANDFOUNDSYSTEM";
   private static final String User = "root";
   private static final String Password = "";
   ```

### 2. Test Database Connection
```bash
cd 3040-PROJECT
javac src/Database/TestConnection.java
java -cp src Database.TestConnection
```

### 3. Build & Run
Using Ant:
```bash
ant run
```

Or using NetBeans IDE: Open the project (`build.xml`) and use the "Run" target.

---
## Project Conventions
- **Package**: `Models`, `DAO`, `Controllers`, `Database`, `Views`
- **Naming**: CamelCase for classes, enums in PascalCase where applicable
- **Database**: MySQL with JDBC connectivity
- **GUI**: Java Swing (NetBeans Form format)
- **Build**: Apache Ant with `build.xml`
---
