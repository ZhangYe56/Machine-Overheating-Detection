package edu.northeastern.overheat.source;

import edu.northeastern.overheat.model.TemperatureReading;
import java.time.Instant;
import java.util.Optional;

/** Repeatable software demonstration scenarios, not hardware integration. */
public final class SimulatedTemperatureSource implements TemperatureSource {
    public enum Scenario { HEATING, INVALID, MISSING }
    private static final double[] VALUES = {25, 40, 59, 60, 65, 75, 79.9, 80, 85};
    private final Scenario scenario;
    private int index;

    public SimulatedTemperatureSource(Scenario scenario) { this.scenario = scenario; }

    @Override public Optional<TemperatureReading> read(Instant now) {
        int step = index++;
        if (scenario == Scenario.MISSING && step % 14 != 0) return Optional.empty();
        double value = scenario == Scenario.HEATING ? VALUES[step % VALUES.length] : 25;
        if (scenario == Scenario.INVALID && step % 6 == 3) value = Double.NaN;
        return Optional.of(new TemperatureReading(now, value));
    }
}
