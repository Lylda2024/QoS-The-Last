package com.orange.qos.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DelaiInterventionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DelaiIntervention getDelaiInterventionSample1() {
        return new DelaiIntervention().id(1L).commentaire("commentaire1");
    }

    public static DelaiIntervention getDelaiInterventionSample2() {
        return new DelaiIntervention().id(2L).commentaire("commentaire2");
    }

    public static DelaiIntervention getDelaiInterventionRandomSampleGenerator() {
        return new DelaiIntervention().id(longCount.incrementAndGet()).commentaire(UUID.randomUUID().toString());
    }
}
