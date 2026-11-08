package edu.northeastern.overheat.source;

import edu.northeastern.overheat.model.TemperatureReading;
import java.time.Instant;
import java.util.Optional;

/** Empty means no new sample; nonfinite values mean invalid samples. */
public interface TemperatureSource {
    Optional<TemperatureReading> read(Instant now);
}
