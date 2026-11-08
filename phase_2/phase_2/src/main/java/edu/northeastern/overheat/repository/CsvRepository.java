package edu.northeastern.overheat.repository;

import edu.northeastern.overheat.model.*;
import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

/** Append-only records survive application restart. */
public final class CsvRepository {
    private final Path readings, alarms;
    private AlarmLevel previous;

    public CsvRepository(Path directory) throws IOException {
        Files.createDirectories(directory);
        readings = directory.resolve("readings.csv");
        alarms = directory.resolve("alarms.csv");
        initialize(readings, "timestamp,celsius,status,message\n");
        initialize(alarms, "timestamp,previous_status,status,message\n");
    }

    private void initialize(Path path, String header) throws IOException {
        if (!Files.exists(path) || Files.size(path) == 0) append(path, header);
    }

    private void append(Path path, String row) throws IOException {
        Files.writeString(path, row, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private String quote(String text) { return "\"" + text.replace("\"", "\"\"") + "\""; }

    public void recordReading(TemperatureReading reading, Snapshot state) throws IOException {
        append(readings, reading.timestamp() + "," + reading.celsius() + "," + state.level() + "," + quote(state.message()) + "\n");
    }

    public void recordTransition(Snapshot state) throws IOException {
        if (previous == state.level()) return;
        if (previous != null || state.level() != AlarmLevel.NORMAL) {
            append(alarms, state.timestamp() + "," + (previous == null ? "START" : previous) + "," + state.level() + "," + quote(state.message()) + "\n");
        }
        previous = state.level();
    }
}
