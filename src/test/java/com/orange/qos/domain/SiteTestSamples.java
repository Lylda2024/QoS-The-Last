package com.orange.qos.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SiteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Site getSiteSample1() {
        return new Site().id(1L).nomSite("nomSite1").codeOCI("codeOCI1").statut("statut1").technologie("technologie1");
    }

    public static Site getSiteSample2() {
        return new Site().id(2L).nomSite("nomSite2").codeOCI("codeOCI2").statut("statut2").technologie("technologie2");
    }

    public static Site getSiteRandomSampleGenerator() {
        return new Site()
            .id(longCount.incrementAndGet())
            .nomSite(UUID.randomUUID().toString())
            .codeOCI(UUID.randomUUID().toString())
            .statut(UUID.randomUUID().toString())
            .technologie(UUID.randomUUID().toString());
    }
}
