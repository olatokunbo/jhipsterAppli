package fr.omnilog.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UniteAdministrativeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UniteAdministrative getUniteAdministrativeSample1() {
        return new UniteAdministrative().id(1L).code("code1").ordre(1).libelle("libelle1");
    }

    public static UniteAdministrative getUniteAdministrativeSample2() {
        return new UniteAdministrative().id(2L).code("code2").ordre(2).libelle("libelle2");
    }

    public static UniteAdministrative getUniteAdministrativeRandomSampleGenerator() {
        return new UniteAdministrative()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .ordre(intCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString());
    }
}
