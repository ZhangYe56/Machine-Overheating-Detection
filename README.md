README
Machine Overheating Detection System
1. Introduction
The Machine Overheating Detection System is a Java-based desktop application designed to monitor machine temperatures, identify overheating conditions, and detect abnormal sensor behavior.

The system provides temperature monitoring, alarm classification, fault detection, and local data logging to help operators identify potential overheating problems and improve equipment safety.

2. Project Objectives
The main objectives of this project are:

Monitor machine temperature readings.
Identify overheating conditions through temperature classification.
Generate alerts when abnormal temperature conditions occur.
Detect invalid and missing temperature readings.
Display monitoring information through a graphical user interface.
Record temperature readings and alarm events locally.
Integrate physical temperature sensors in future development.
3. Technologies and Tools
Java
Java Swing
Apache Maven
JUnit 5
CSV
ESP32 and MAX6675 (planned)
4. System Architecture
The application follows an MVC-inspired architecture.

Temperature Source: Provides simulated temperature readings.
Monitoring Service: Evaluates temperature readings and determines alarm levels.
Monitoring Controller: Coordinates data processing and storage.
Data Repository: Stores temperature readings and alarm events.
Dashboard View: Displays monitoring information through a Java Swing interface.
5. Temperature Monitoring
The system supports three temperature levels:

Temperature	Status
Below 60°C	Normal
60°C to below 80°C	Warning
80°C and above	Critical
The system also detects invalid temperature readings and missing input conditions.

6. Current Implementation (Phase 2)
Implemented features include:

Simulated temperature data generation.
Temperature classification.
Alarm status management.
Invalid input detection.
Missing input timeout detection.
Java Swing graphical dashboard.
CSV temperature data logging.
CSV alarm event logging.
Automated testing using JUnit 5.
The current implementation uses simulated temperature readings. Physical sensor integration is planned for Phase 3.

7. Future Development (Phase 3)
Planned features include:

ESP32 and MAX6675 sensor integration.
USB serial communication.
Temperature recovery hysteresis.
Rapid temperature-rise detection.
Hardware buzzer and LED alerts.
Alarm acknowledgement.
Historical temperature charts.
Hardware integration and system testing.
8. Testing
JUnit 5 is used to test the core monitoring functionality.

The tests cover:

Temperature threshold classification.
Invalid temperature input handling.
Missing input timeout detection.
Monitoring state transitions.
CSV data persistence.
Alarm event management.
The initial local test execution passed 15 test cases with zero failures or errors.

9. Data Storage
The application uses CSV files for local data storage.

readings.csv: Stores temperature readings and monitoring information.
alarms.csv: Stores alarm events and state transitions.
10. Project Management
GitHub Projects is used to organize development tasks, assign responsibilities, and track project progress.

The team follows this workflow:

Backlog
Ready
In Progress
In Review
Done
11. Team Members
Team Member	Responsibilities
Ye Zhang	To be updated
Shicheng An	To be updated
Tingkai Hu	To be updated
12. Project Scope and Limitations
The current implementation focuses on software-based temperature monitoring and overheating detection.

Automatic machine shutdown, fan control, cloud monitoring, and remote equipment management are outside the scope of the current implementation.

Future development will focus on real sensor integration and improved monitoring capabilities.

Project: Machine Overheating Detection System

Development Phase: Phase 2 – Core Software Implementation

Status: Software baseline implemented; hardware integration planned for Phase 3.
