package edu.northeastern.overheat.view;

import edu.northeastern.overheat.controller.MonitoringController;
import edu.northeastern.overheat.source.SimulatedTemperatureSource;
import edu.northeastern.overheat.service.MonitoringService;
import edu.northeastern.overheat.repository.CsvRepository;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Path;
import java.time.Clock;
import java.io.IOException;
import java.util.Locale;

public final class DashboardView extends JFrame {
    private final JLabel temperature = new JLabel("Temperature: --");
    private final JLabel status = new JLabel("STOPPED");
    private final JLabel detail = new JLabel("Choose a scenario and click Start.");
    private final JComboBox<SimulatedTemperatureSource.Scenario> scenario = new JComboBox<>(SimulatedTemperatureSource.Scenario.values());
    private final JButton start = new JButton("Start"), stop = new JButton("Stop");
    private final Timer timer;
    private final Path data;
    private MonitoringController controller;

    public DashboardView(Path data) {
        super("Machine Overheating Detection | Phase 2");
        this.data = data;
        timer = new Timer(1000, e -> sample());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        var title = new JLabel("Machine Overheating Detection");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        temperature.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
        status.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(new JLabel("SIMULATION ONLY | No physical sensor or machine control"));
        panel.add(Box.createVerticalStrut(24));
        panel.add(temperature); panel.add(status);
        panel.add(Box.createVerticalStrut(12)); panel.add(detail);
        var actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(scenario); actions.add(start); actions.add(stop); panel.add(actions);
        panel.add(new JLabel("Normal < 60 C | Warning 60 to < 80 C | Critical >= 80 C"));
        panel.add(new JLabel("Fault: invalid input or no valid data for 10 seconds"));
        panel.add(new JLabel("CSV directory: " + data.toAbsolutePath()));
        for (Component component : panel.getComponents()) {
            if (component instanceof JComponent item) {
                item.setAlignmentX(Component.LEFT_ALIGNMENT);
                if (item instanceof JLabel) {
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.getPreferredSize().height));
                }
            }
        }
        stop.setEnabled(false);
        start.addActionListener(e -> begin());
        stop.addActionListener(e -> halt());
        setContentPane(panel);
        setSize(840, 480);
        setMinimumSize(new Dimension(840, 480));
        setLocationRelativeTo(null);
    }

    private void begin() {
        try {
            var clock = Clock.systemUTC();
            controller = new MonitoringController(
                new SimulatedTemperatureSource((SimulatedTemperatureSource.Scenario) scenario.getSelectedItem()),
                new MonitoringService(clock.instant()), new CsvRepository(data), clock);
            start.setEnabled(false); stop.setEnabled(true); scenario.setEnabled(false);
            timer.start(); sample();
        } catch (IOException error) { fail(error); }
    }

    private void sample() {
        try {
            var state = controller.tick();
            temperature.setText(state.celsius() == null ? "Temperature: --" : String.format(Locale.ROOT, "Temperature: %.1f C", state.celsius()));
            status.setText(state.level().name()); detail.setText(state.message());
            status.setForeground(switch(state.level()) {
                case NORMAL -> new Color(25,110,60);
                case WARNING -> new Color(150,100,0);
                case CRITICAL -> new Color(190,30,40);
                case FAULT -> new Color(130,45,150);
            });
        } catch (IOException error) { fail(error); }
    }

    private void halt() {
        timer.stop(); start.setEnabled(true); stop.setEnabled(false); scenario.setEnabled(true);
        status.setText("STOPPED"); status.setForeground(Color.DARK_GRAY);
        detail.setText("Stopped. Start resets the simulation; existing CSV records are preserved.");
    }

    private void fail(IOException error) {
        halt(); status.setText("STORAGE ERROR");
        JOptionPane.showMessageDialog(this, error.getMessage(), "Cannot save monitoring records", JOptionPane.ERROR_MESSAGE);
    }
}
