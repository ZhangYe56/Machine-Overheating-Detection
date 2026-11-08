package edu.northeastern.overheat.model;

import java.time.Instant;
import java.util.Objects;

public record TemperatureReading(Instant timestamp, double celsius) {
    public TemperatureReading { Objects.requireNonNull(timestamp); }
}
