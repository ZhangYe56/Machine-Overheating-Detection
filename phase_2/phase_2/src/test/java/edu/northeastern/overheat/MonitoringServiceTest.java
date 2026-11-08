package edu.northeastern.overheat;

import edu.northeastern.overheat.model.*;
import edu.northeastern.overheat.service.MonitoringService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class MonitoringServiceTest {
    private final Instant start = Instant.parse("2026-01-01T00:00:00Z");

    @ParameterizedTest
    @CsvSource({"25,NORMAL", "59.9,NORMAL", "60,WARNING", "79.9,WARNING", "80,CRITICAL", "85,CRITICAL"})
    void classifiesThresholdBoundaries(double value, AlarmLevel expected) {
        var service = new MonitoringService(start);
        assertEquals(expected, service.evaluate(Optional.of(new TemperatureReading(start, value)), start).level());
    }

    @Test void rejectsNonFiniteValues() {
        for (double value : new double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            var service = new MonitoringService(start);
            assertEquals(AlarmLevel.FAULT, service.evaluate(Optional.of(new TemperatureReading(start, value)), start).level());
        }
    }

    @Test void faultsExactlyAtTenSecondsWithoutAnySample() {
        var service = new MonitoringService(start);
        assertNotEquals(AlarmLevel.FAULT, service.evaluate(Optional.empty(), start.plusSeconds(9)).level());
        assertEquals(AlarmLevel.FAULT, service.evaluate(Optional.empty(), start.plusSeconds(10)).level());
    }

    @Test void validSampleResetsTimeoutAndRetainsLastValueDuringGap() {
        var service = new MonitoringService(start);
        service.evaluate(Optional.of(new TemperatureReading(start.plusSeconds(8), 65)), start.plusSeconds(8));
        var waiting = service.evaluate(Optional.empty(), start.plusSeconds(17));
        assertEquals(AlarmLevel.WARNING, waiting.level());
        assertEquals(65.0, waiting.celsius());
        assertEquals(AlarmLevel.FAULT, service.evaluate(Optional.empty(), start.plusSeconds(18)).level());
    }

    @Test void invalidSampleDoesNotResetLastValidTime() {
        var service = new MonitoringService(start);
        service.evaluate(Optional.of(new TemperatureReading(start.plusSeconds(9), Double.NaN)), start.plusSeconds(9));
        assertTrue(service.evaluate(Optional.empty(), start.plusSeconds(10)).message().startsWith("No valid data"));
    }

    @Test void baselineReclassifiesImmediatelyAfterFaultOrCritical() {
        var service = new MonitoringService(start);
        service.evaluate(Optional.of(new TemperatureReading(start, 85)), start);
        assertEquals(AlarmLevel.NORMAL, service.evaluate(Optional.of(new TemperatureReading(start.plusSeconds(1), 25)), start.plusSeconds(1)).level());
        service.evaluate(Optional.empty(), start.plusSeconds(11));
        assertEquals(AlarmLevel.WARNING, service.evaluate(Optional.of(new TemperatureReading(start.plusSeconds(12), 65)), start.plusSeconds(12)).level());
    }
}
