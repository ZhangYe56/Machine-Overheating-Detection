# Requirement and Test Plan

| Requirement | Current validation | Remaining validation |
|---|---|---|
| Threshold classification | JUnit at 25, 59.9, 60, 79.9, 80 and 85 C | Configurable thresholds |
| Invalid input | JUnit NaN and both infinities | Malformed serial messages and physical limits |
| Missing input fault | JUnit at 9 and 10 seconds; timer resets after valid input | Real serial disconnect/reconnect |
| Recovery | Basic immediate reclassification test only | Confirm margin and test hysteresis; not yet implemented |
| Rapid rise | Not implemented | Confirm rate and window, then test boundaries |
| CSV records | Pipeline, reopen persistence, escaping, duplicate-event suppression | Longer runs, disk failure and concurrent access |
| Dashboard | Manual scenario checklist below | Hardware latency measurement, history charts |
| Hardware sample rate | Not implemented | Measure actual ESP32 1 Hz output |
| Acknowledgement/admin | Not implemented | Authorization and event logging tests |

## Manual demo checklist

1. Build with `mvnw.cmd clean package` (Windows) or `mvn clean package`.
2. Launch the JAR, select HEATING and click Start. Observe Normal, Warning and Critical with both labels and colors.
3. Click Stop: updates end. Click Start: the sequence restarts and existing CSV files remain.
4. Stop, choose INVALID, start. The fourth sample is invalid and produces Fault; the last valid temperature is retained with an explanatory message.
5. Stop, choose MISSING, start. At 10 seconds without valid data, Fault appears. A later valid sample restores basic classification.
6. Close and reopen the app, run again, and verify CSV records were appended rather than overwritten.

No hardware, authentication or recovery-margin behavior is claimed by these checks.
