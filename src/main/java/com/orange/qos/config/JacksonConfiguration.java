package com.orange.qos.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module.Feature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Configuration centralisée de Jackson pour :
 *  - Java Time (JSR-310)
 *  - JDK 8 Optionals
 *  - Hibernate 6 (lazy-loading)
 *  - Format personnalisé pour LocalTime
 */
@Configuration
public class JacksonConfiguration {

    /** Module Java Time (JSR-310) */
    @Bean
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();
        // Sérialise LocalTime au format HH:mm:ss
        module.addSerializer(LocalTime.class, new LocalTimeSerializer());
        return module;
    }

    /** Module JDK 8 (Optionals) */
    @Bean
    public Jdk8Module jdk8Module() {
        return new Jdk8Module();
    }

    /** Module Hibernate 6 – évite les proxies non initialisés */
    @Bean
    public Hibernate6Module hibernate6Module() {
        return new Hibernate6Module()
            // Sérialise l’ID même si la relation n’est pas chargée
            .configure(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true)
            // N’essaie pas de charger les collections lazy
            .configure(Feature.FORCE_LAZY_LOADING, false);
    }

    /** Personnalisation globale de l’ObjectMapper */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            builder.modules(javaTimeModule(), jdk8Module(), hibernate6Module());
            // Toujours écrire les dates en ISO-8601
            builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            // Ne pas échouer sur des propriétés inconnues
            builder.failOnUnknownProperties(false);
            // Ne pas inclure les valeurs null
            builder.serializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        };
    }

    /** Serializer personnalisé pour LocalTime */
    public static class LocalTimeSerializer extends JsonSerializer<LocalTime> {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value != null ? value.format(FORMATTER) : null);
        }
    }
}
