package fr.omnilog.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ValeurTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Valeur getValeurSample1() {
        return new Valeur().id(1L).code("code1").ordre(1).libelle("libelle1").abreviation("abreviation1").description("description1");
    }

    public static Valeur getValeurSample2() {
        return new Valeur().id(2L).code("code2").ordre(2).libelle("libelle2").abreviation("abreviation2").description("description2");
    }

    public static Valeur getValeurRandomSampleGenerator() {
        return new Valeur()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .ordre(intCount.incrementAndGet())
            .libelle(UUID.randomUUID().toString())
            .abreviation(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
