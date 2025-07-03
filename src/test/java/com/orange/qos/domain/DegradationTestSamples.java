package com.orange.qos.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DegradationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Degradation getDegradationSample1() {
        return new Degradation()
            .id(1L)
            .numero("numero1")
            .localite("localite1")
            .contactTemoin("contactTemoin1")
            .typeAnomalie("typeAnomalie1")
            .priorite("priorite1")
            .problem("problem1")
            .porteur("porteur1")
            .actionsEffectuees("actionsEffectuees1");
    }

    public static Degradation getDegradationSample2() {
        return new Degradation()
            .id(2L)
            .numero("numero2")
            .localite("localite2")
            .contactTemoin("contactTemoin2")
            .typeAnomalie("typeAnomalie2")
            .priorite("priorite2")
            .problem("problem2")
            .porteur("porteur2")
            .actionsEffectuees("actionsEffectuees2");
    }

    public static Degradation getDegradationRandomSampleGenerator() {
        return new Degradation()
            .id(longCount.incrementAndGet())
            .numero(UUID.randomUUID().toString())
            .localite(UUID.randomUUID().toString())
            .contactTemoin(UUID.randomUUID().toString())
            .typeAnomalie(UUID.randomUUID().toString())
            .priorite(UUID.randomUUID().toString())
            .problem(UUID.randomUUID().toString())
            .porteur(UUID.randomUUID().toString())
            .actionsEffectuees(UUID.randomUUID().toString());
    }
}
