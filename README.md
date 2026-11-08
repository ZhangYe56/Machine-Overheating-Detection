# Machine Overheating Detection System

The Machine Overheating Detection System is a Java-based desktop application designed to monitor machine temperatures, identify overheating conditions, and detect abnormal sensor behavior. The system provides temperature classification, fault detection, real-time status visualization, and local data logging to help operators identify potential overheating problems and improve equipment safety. The current Phase 2 implementation uses simulated temperature readings to demonstrate the core software functionality, while physical sensor integration is planned for Phase 3.

## Team

| Member | GitHub Username | Role |
|---|---|---|
| Ye Zhang | @ZhangYe56 | Testing |
| Shicheng An | @WhatIsYourDream | UI Development |
| Tingkai Hu | @ISUKI | Backend Development |

## Repository Structure

The project follows an MVC-inspired architecture, separating temperature input, monitoring logic, data storage, and user interface components.

```text
Machine-Overheating-Detection/
├── pom.xml
├── mvnw.cmd
├── run.cmd
├── .gitignore
├── scripts/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── edu/northeastern/overheat/
│   │           ├── Main.java
│   │           ├── model/
│   │           ├── source/
│   │           ├── service/
│   │           ├── controller/
│   │           ├── repository/
│   │           └── view/
│   └── test/
│       └── java/
│           └── edu/northeastern/overheat/
│               ├── MonitoringServiceTest.java
│               └── PipelineTest.java
└── docs/
    ├── architecture.md
    ├── milestones.md
    ├── test-plan.md
    └── submission-checklist.md
```

### Architecture Overview

- **Model:** Defines temperature readings, alarm levels, and monitoring snapshots.
- **Source:** Provides temperature readings through a simulated data source.
- **Service:** Implements temperature classification and fault detection logic.
- **Controller:** Coordinates temperature input, monitoring logic, and data storage.
- **Repository:** Handles CSV-based storage of temperature readings and alarm events.
- **View:** Provides a Java Swing dashboard for displaying monitoring information.

## Setup and Run

### Prerequisites

- JDK 17 or newer.
- Apache Maven 3.9+ (if not using the included Windows Maven wrapper).
- Internet connection for downloading dependencies during the first build.

No database, API keys, or physical temperature sensors are required for the current simulated implementation.

### 1. Clone the Repository

```bash
git clone <YOUR_PUBLIC_REPOSITORY_URL>
cd Machine-Overheating-Detection
```

### 2. Build the Project

**Windows PowerShell:**

```powershell
.\mvnw.cmd clean package
```

**macOS/Linux:**

```bash
mvn clean package
```

### 3. Run the Application

```bash
java -jar target/overheat-0.1.0.jar
```

On Windows, the application can also be launched using:

```powershell
.\run.cmd
```

### 4. Run Tests

**Windows PowerShell:**

```powershell
.\mvnw.cmd test
```

**macOS/Linux:**

```bash
mvn test
```

### 5. Run the Console Demonstration

```bash
java -jar target/overheat-0.1.0.jar --demo
```

The console demonstration uses simulated temperature readings.

### Data Storage

The application stores monitoring records locally in CSV files:

- `readings.csv`: Stores temperature readings, timestamps, monitoring status, and related messages.
- `alarms.csv`: Stores alarm events and monitoring state transitions.

## Progress So Far

The following table summarizes the current implementation.

| Functional Requirement | Status | Notes |
|---|---|---|
| Temperature input | Partial | Simulated temperature source implemented; physical sensor integration is pending. |
| Temperature classification | Complete | Supports Normal, Warning, and Critical states. |
| Overheating detection | Complete | Warning at 60°C and Critical at 80°C. |
| Invalid input detection | Complete | Detects invalid temperature values. |
| Missing input detection | Complete | Detects missing readings after 10 seconds. |
| Monitoring dashboard | Partial | Java Swing dashboard with temperature display, status indicators, and monitoring controls. |
| Local data logging | Complete | Temperature readings and alarm events are stored in CSV files. |
| Recovery hysteresis | Not Started | Planned for Phase 3. |
| Rapid temperature-rise detection | Not Started | Planned for Phase 3. |
| Hardware alerts | Not Started | Buzzer and LED integration planned for Phase 3. |
| Alarm acknowledgement | Not Started | Planned for future development. |
| Historical charts and filtering | Not Started | Planned for future development. |

### Working End to End

The current implementation supports an end-to-end simulated monitoring workflow:

1. The simulated temperature source generates temperature readings.
2. The monitoring controller receives and processes the readings.
3. The monitoring service evaluates temperature values and determines the alarm level.
4. The Swing dashboard displays the current temperature and monitoring status.
5. Temperature readings and alarm events are recorded in CSV files.

The system currently supports three temperature levels:

| Temperature | Status |
|---|---|
| Below 60°C | Normal |
| 60°C to below 80°C | Warning |
| 80°C and above | Critical |

Additional simulated scenarios include:

- **HEATING:** Simulates increasing temperatures and demonstrates Normal, Warning, and Critical states.
- **INVALID:** Simulates invalid temperature readings and triggers a Fault state.
- **MISSING:** Simulates missing sensor input and triggers a Fault state after 10 seconds without valid readings.

The current implementation uses immediate temperature classification. Recovery hysteresis has not yet been implemented.

## Testing So Far

JUnit 5 is used for unit and integration testing of the core monitoring components.

The initial local test execution passed 15 test cases with zero failures and zero errors.

| Test Suite | Number of Tests | Result |
|---|---:|---|
| MonitoringServiceTest | 11 | Passed |
| PipelineTest | 4 | Passed |
| Total | 15 | Passed |

Testing covers:

- Temperature threshold classification.
- Invalid temperature input handling.
- Missing input timeout detection.
- Monitoring state transitions.
- Data processing and storage pipeline.
- CSV formatting and persistence.
- Duplicate alarm event suppression.

Hardware integration and physical sensor performance require additional validation.

## Next Steps (Phase 3)

- Integrate an ESP32 microcontroller with a MAX6675 temperature sensor.
- Implement USB serial communication for real-time temperature acquisition.
- Implement temperature recovery hysteresis.
- Add rapid temperature-rise detection.
- Integrate hardware buzzer and LED alerts.
- Implement alarm acknowledgement functionality.
- Add administrator settings and configurable thresholds.
- Develop historical temperature charts and data filtering.
- Perform hardware integration and system-level testing.

## Team Contributions

| Member | Contributions This Phase |
|---|---|
| Ye Zhang | Testing: Responsible for validating core monitoring functionality, reviewing test cases, identifying issues, and documenting test results. |
| Shicheng An | UI Development: Responsible for the Java Swing dashboard, temperature display, monitoring status presentation, and user interactions. |
| Tingkai Hu | Backend Development: Responsible for temperature processing, monitoring logic, controller coordination, and CSV data storage. |

Individual contributions are tracked through GitHub commits. Each member's contribution details should reflect their actual completed work and commit history.

---

**Project:** Machine Overheating Detection System

**Course:** Program Design Paradigm

**Development Phase:** Phase 2 – Code Submission 1

**Current Status:** Core simulated monitoring workflow implemented; hardware integration planned for Phase 3.
