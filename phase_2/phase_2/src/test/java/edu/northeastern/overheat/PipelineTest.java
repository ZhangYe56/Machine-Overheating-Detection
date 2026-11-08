package edu.northeastern.overheat;

import edu.northeastern.overheat.controller.MonitoringController;
import edu.northeastern.overheat.model.*;
import edu.northeastern.overheat.repository.CsvRepository;
import edu.northeastern.overheat.service.MonitoringService;
import edu.northeastern.overheat.source.SimulatedTemperatureSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class PipelineTest {
    @TempDir Path directory;

    @Test void heatingFlowsFromSourceThroughRulesIntoCsv() throws Exception {
        var now = Instant.parse("2026-01-01T00:00:00Z");
        var source = new SimulatedTemperatureSource(SimulatedTemperatureSource.Scenario.HEATING);
        var service = new MonitoringService(now);
        var repository = new CsvRepository(directory);
        Snapshot snapshot = null;
        for (int i=0; i<9; i++) {
            snapshot = new MonitoringController(source, service, repository, Clock.fixed(now.plusSeconds(i), ZoneOffset.UTC)).tick();
        }
        assertEquals(AlarmLevel.CRITICAL, snapshot.level());
        var rows = Files.readAllLines(directory.resolve("readings.csv"));
        assertEquals(10, rows.size());
        assertTrue(rows.get(4).contains(",60.0,WARNING,"));
        var alarms = Files.readAllLines(directory.resolve("alarms.csv"));
        assertEquals(3, alarms.size());
        assertTrue(alarms.get(1).contains("NORMAL,WARNING"));
        assertTrue(alarms.get(2).contains("WARNING,CRITICAL"));
    }

    @Test void missingDataProducesFaultWithoutInventingReadings() throws Exception {
        var now = Instant.parse("2026-01-01T00:00:00Z");
        var source = new SimulatedTemperatureSource(SimulatedTemperatureSource.Scenario.MISSING);
        var service = new MonitoringService(now);
        var repository = new CsvRepository(directory);
        Snapshot snapshot = null;
        for (int i=0; i<=10; i++) {
            snapshot = new MonitoringController(source, service, repository, Clock.fixed(now.plusSeconds(i), ZoneOffset.UTC)).tick();
        }
        assertEquals(AlarmLevel.FAULT, snapshot.level());
        assertEquals(2, Files.readAllLines(directory.resolve("readings.csv")).size());
        assertTrue(Files.readString(directory.resolve("alarms.csv")).contains("NORMAL,FAULT"));
    }

    @Test void reopeningPreservesRecordsAndDoesNotRepeatHeader() throws Exception {
        var now = Instant.now();
        var snapshot = new Snapshot(now, 25.0, AlarmLevel.NORMAL, "sample");
        new CsvRepository(directory).recordReading(new TemperatureReading(now, 25), snapshot);
        new CsvRepository(directory).recordReading(new TemperatureReading(now, 26), snapshot);
        assertEquals(3, Files.readAllLines(directory.resolve("readings.csv")).size());
    }

    @Test void quotesCsvMessagesAndSuppressesRepeatedAlarmLevels() throws Exception {
        var repo = new CsvRepository(directory);
        var snapshot = new Snapshot(Instant.now(), null, AlarmLevel.FAULT, "Bad, \"sample\"");
        repo.recordTransition(snapshot);
        repo.recordTransition(snapshot);
        assertEquals(2, Files.readAllLines(directory.resolve("alarms.csv")).size());
        assertTrue(Files.readString(directory.resolve("alarms.csv")).contains("\"Bad, \"\"sample\"\"\""));
    }
}
