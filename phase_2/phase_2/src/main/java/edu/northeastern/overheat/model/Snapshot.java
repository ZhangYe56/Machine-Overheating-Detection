package edu.northeastern.overheat.model;

import java.time.Instant;

public record Snapshot(Instant timestamp, Double celsius, AlarmLevel level, String message) { }
