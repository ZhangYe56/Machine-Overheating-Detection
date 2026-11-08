package edu.northeastern.overheat.controller;

import edu.northeastern.overheat.model.Snapshot;
import edu.northeastern.overheat.source.TemperatureSource;
import edu.northeastern.overheat.service.MonitoringService;
import edu.northeastern.overheat.repository.CsvRepository;
import java.io.IOException;
import java.time.Clock;

/** One sampling step; independent of the user interface. */
public final class MonitoringController {
    private final TemperatureSource source;
    private final MonitoringService service;
    private final CsvRepository repository;
    private final Clock clock;

    public MonitoringController(TemperatureSource source, MonitoringService service, CsvRepository repository, Clock clock) {
        this.source = source;
        this.service = service;
        this.repository = repository;
        this.clock = clock;
    }

    public Snapshot tick() throws IOException {
        var now = clock.instant();
        var sample = source.read(now);
        var state = service.evaluate(sample, now);
        if (sample.isPresent()) repository.recordReading(sample.get(), state);
        repository.recordTransition(state);
        return state;
    }
}
