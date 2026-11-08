# Machine Overheating Detection System

A Java desktop application for operators to monitor machine temperatures, identify overheating, and keep local records. The planned system receives readings from a K-type probe, MAX6675 and ESP32 through USB serial. This Phase 2 baseline currently uses a deterministic simulator behind a `TemperatureSource` interface. It classifies temperature, displays status in Swing, detects invalid/missing samples and saves CSV records. Physical hardware integration is not implemented yet.

## Team

Fill in the real GitHub usernames and confirm responsibilities before submission.

| Member | GitHub Username | Role |
|---|---|---|
| Ye Zhang | TODO | To be confirmed |
| Shicheng An | TODO | To be confirmed |
| Tingkai Hu | TODO | To be confirmed |

## Repository Structure

```text
pom.xml                         Maven build and JUnit dependency
mvnw.cmd                        Windows Maven bootstrap launcher
run.cmd                         Build, test and launch
scripts/bootstrap-maven.ps1      Downloads Maven and verifies its SHA-512
src/main/java/edu/northeastern/overheat/
  Main.java                     Swing entry point and console demo
  model/                        Reading, status enum and snapshot
  source/                       TemperatureSource and simulator
  service/                      Threshold and fault rules
  controller/                   Input -> rules -> storage coordination
  repository/                   Append-only CSV records
  view/                         Swing dashboard
src/test/java/edu/northeastern/overheat/
docs/                           Architecture, test plan, milestones, submission checklist
data/                           Generated CSV files (ignored by Git)
```

This implements the Java MVC portion of the Phase 1 design. The controller is independent of Swing. Threshold decisions live in the service, rather than in UI event handlers. See [architecture](docs/architecture.md).

## Setup and Run

**Prerequisites:** JDK 17 or newer. Tested locally with JDK 22.0.2. Windows users can use the included bootstrap scripts; other operating systems require Maven 3.9+. First-time builds need internet access to Maven Central. No database, API keys or physical sensor are required for this simulated baseline.

After the team publishes a repository, replace the placeholders below:

```bash
git clone <YOUR_PUBLIC_REPOSITORY_URL>
cd <YOUR_REPOSITORY_FOLDER>
```

### Windows PowerShell

From this folder:

```powershell
# Compile, run all tests and build the executable JAR
.\mvnw.cmd clean package

# Launch the Swing dashboard
java -jar target/overheat-0.1.0.jar

# Run tests only
.\mvnw.cmd test

# Console-only demonstration, suitable for a headless machine
java -jar target/overheat-0.1.0.jar --demo
```

Alternatively, double-click `run.cmd`, or run `.\run.cmd` in PowerShell. This builds and tests before launching. The included `mvnw.cmd` is a small project-specific bootstrap script, not the standard Apache Maven Wrapper. It pins Maven 3.9.9, checks its published SHA-512, and keeps Maven and downloaded dependencies under `.tools/`.

### macOS/Linux or an existing Maven installation

```bash
mvn clean package
java -jar target/overheat-0.1.0.jar
mvn test
java -jar target/overheat-0.1.0.jar --demo
```

### IntelliJ IDEA

Open this directory as a Maven project, select JDK 17 or newer, reload Maven, and run `edu.northeastern.overheat.Main`. IntelliJ can use its bundled Maven. Set the working directory to the project root so generated records are easy to find.

### Output files

`data/readings.csv` stores each received sample, its timestamp, status and message. Invalid nonfinite values are recorded as `NaN` or `Infinity`. Missing samples do not create fabricated temperature records. `data/alarms.csv` records state transitions, including faults and returns to Normal. Repeated identical levels do not generate duplicate alarm events. Files are appended across restarts; each monitoring session starts with a new in-memory status. Timestamps use UTC.

Use `--data-dir PATH` to change the output directory. The program stops monitoring and reports a storage error if records cannot be written. Do not open CSV files in software that locks them while recording.

## Progress So Far

These rows describe the supplied baseline; they do not claim individual team authorship. Requirement labels below summarize the PPT and are not invented Phase 1 FR numbers.

| Functional Requirement | Status | Notes |
|---|---|---|
| Real sensor input at 1 Hz | Partial | Simulator ticks approximately once per second; USB serial and firmware not implemented |
| Normal/Warning/Critical classification | Complete for baseline | <60, 60 to <80, >=80 degrees Celsius |
| Recovery margin and rapid-rise alarms | Not started | Confirm exact Phase 1 parameters; current implementation immediately reclassifies each valid reading |
| Invalid and missing input detection | Partial | Nonfinite input faults immediately; no valid input for >=10 seconds faults; serial parsing and physical range checks pending |
| Live status dashboard | Partial | Temperature, text/color status, Start/Stop; measured <2-second hardware latency not verified |
| Local records | Partial | Readings and alarm transitions persist as CSV; configuration records pending |
| Hardware buzzer and LED | Not started | No hardware integration |
| Alarm acknowledgement and administrator settings | Not started | No acknowledgement, authentication or configurable thresholds |
| History charts and filtering | Not started | CSV exists; graphical history pending |

**Working end to end (simulated software path):** Select HEATING, click Start, and watch 25 -> 40 -> 59 -> 60 -> 65 -> 75 -> 79.9 -> 80 -> 85 degrees Celsius. The status becomes Warning at 60 and Critical at 80. Received samples and status changes are saved to CSV. The sequence repeats. The transition back to 25 uses basic immediate reclassification, not the planned recovery-margin rule. Stop ends sampling; Start begins a new simulation without deleting previous records.

**Other scenarios:** INVALID produces an invalid sample on the fourth tick of each six-tick cycle, then resumes valid readings. MISSING produces a sample at the start, pauses for 13 ticks, and repeats; Fault appears 10 seconds after the last valid reading. Stop/Start is required to switch scenarios.

**Console demo:** `--demo` advances virtual time through nine readings immediately. It uses the same controller, rules and CSV path; it is not a real-time hardware measurement.

## Testing So Far

The initial local build passed **15 JUnit 5 test cases, with zero failures or errors**. They cover threshold boundaries, nonfinite values, timeout boundaries, timeout reset, basic reclassification, the simulator/controller/storage pipeline, CSV escaping, duplicate alarm suppression and persistence across repository reopen. Run the tests with the commands above; detailed results are in `target/surefire-reports/`. See [test plan](docs/test-plan.md). GUI and hardware validation have separate scopes; passing software tests does not establish hardware behavior.

## Next Steps (Phase 3)

- Implement an ESP32 USB serial adapter behind `TemperatureSource` and validate message formats and units.
- Confirm and implement the recovery margin and rapid-rise window/rate from Phase 1.
- Connect the real probe, local buzzer and LEDs; measure sampling and display latency.
- Add acknowledgement, password-protected settings and configuration logs.
- Add historical charts and filters; broaden validation and integration tests.

See [milestones](docs/milestones.md) for a proposed assignment table to confirm with the team.

## Out of Scope

- Direct machine shutdown, fan control or other automatic actuation.
- Cloud services and remote monitoring in this local desktop baseline.
- Industrial deployment or certification. NIST SP 800-82 informed the planned design; this baseline does not establish compliance.

## Team Contributions

This initial baseline was generated with AI assistance at Tingkai Hu's request. Team members must review and understand it, make their own substantive changes, and record actual work below. No individual commits or contributions have been fabricated.

| Member | Contributions This Phase |
|---|---|
| Ye Zhang | TODO: add actual implemented/reviewed features, tests and commit references |
| Shicheng An | TODO: add actual implemented/reviewed features, tests and commit references |
| Tingkai Hu | Requested baseline scaffolding; TODO: record actual review, integration, changes and tests |

Before submitting, replace TODOs with accurate information, publish the repository, invite all teammates, and include all names/usernames in the Canvas comment. See [submission checklist](docs/submission-checklist.md).
