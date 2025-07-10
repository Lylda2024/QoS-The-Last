package com.orange.qos.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DegradationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Degradation getDegradationSample1() {
        return new Degradation().id(1L).description("description1").statut("statut1");
    }

    public static Degradation getDegradationSample2() {
        return new Degradation().id(2L).description("description2").statut("statut2");
    }

    public static Degradation getDegradationRandomSampleGenerator() {
        return new Degradation()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .statut(UUID.randomUUID().toString());
    }
}
