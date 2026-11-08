package edu.northeastern.overheat;

import edu.northeastern.overheat.view.DashboardView;
import edu.northeastern.overheat.controller.MonitoringController;
import edu.northeastern.overheat.source.SimulatedTemperatureSource;
import edu.northeastern.overheat.service.MonitoringService;
import edu.northeastern.overheat.repository.CsvRepository;
import java.nio.file.Path;
import java.time.*;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() { }

    public static void main(String[] args) throws Exception {
        boolean demo = false;
        Path data = Path.of("data");
        for (int i=0; i<args.length; i++) {
            if (args[i].equals("--demo")) demo = true;
            else if (args[i].equals("--data-dir") && i+1<args.length) data=Path.of(args[++i]);
            else throw new IllegalArgumentException("Usage: [--demo] [--data-dir PATH]");
        }
        if (demo) {
            var now = Instant.now();
            var source = new SimulatedTemperatureSource(SimulatedTemperatureSource.Scenario.HEATING);
            var service = new MonitoringService(now);
            var repository = new CsvRepository(data);
            System.out.println("SIMULATED demo with accelerated virtual seconds; no hardware connected.");
            for (int i=0; i<9; i++) {
                var state = new MonitoringController(source, service, repository, Clock.fixed(now.plusSeconds(i), ZoneOffset.UTC)).tick();
                System.out.println(state.timestamp()+" | "+state.celsius()+" C | "+state.level());
            }
            System.out.println("CSV directory: "+data.toAbsolutePath());
        } else {
            Path chosen = data;
            SwingUtilities.invokeLater(() -> new DashboardView(chosen).setVisible(true));
        }
    }
}
