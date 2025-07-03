package com.orange.qos.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TypeUtilisateurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TypeUtilisateur getTypeUtilisateurSample1() {
        return new TypeUtilisateur().id(1L).nom("nom1").description("description1").niveau(1).permissions("permissions1");
    }

    public static TypeUtilisateur getTypeUtilisateurSample2() {
        return new TypeUtilisateur().id(2L).nom("nom2").description("description2").niveau(2).permissions("permissions2");
    }

    public static TypeUtilisateur getTypeUtilisateurRandomSampleGenerator() {
        return new TypeUtilisateur()
            .id(longCount.incrementAndGet())
            .nom(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .niveau(intCount.incrementAndGet())
            .permissions(UUID.randomUUID().toString());
    }
}
