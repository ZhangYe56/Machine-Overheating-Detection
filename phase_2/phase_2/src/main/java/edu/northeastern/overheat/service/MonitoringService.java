package edu.northeastern.overheat.service;

import edu.northeastern.overheat.model.*;
import java.time.*;
import java.util.Optional;

/** Basic threshold mode: immediate reclassification; hysteresis is not implemented. */
public final class MonitoringService {
    private Instant lastValid;
    private Double temperature;
    private AlarmLevel level = AlarmLevel.NORMAL;
    private String message = "Waiting for first reading";

    public MonitoringService(Instant started) { lastValid = started; }

    public Snapshot evaluate(Optional<TemperatureReading> sample, Instant now) {
        if (sample.isPresent()) {
            double value = sample.get().celsius();
            if (!Double.isFinite(value)) {
                level = AlarmLevel.FAULT;
                message = "Invalid sample; displayed temperature is the last valid value";
            } else {
                temperature = value;
                lastValid = now;
                level = value >= 80 ? AlarmLevel.CRITICAL : value >= 60 ? AlarmLevel.WARNING : AlarmLevel.NORMAL;
                message = "Valid reading | Basic threshold mode";
            }
        }
        if (Duration.between(lastValid, now).compareTo(Duration.ofSeconds(10)) >= 0) {
            level = AlarmLevel.FAULT;
            message = "No valid data for 10 seconds; displayed value may be stale";
        }
        return new Snapshot(now, temperature, level, message);
    }
}
