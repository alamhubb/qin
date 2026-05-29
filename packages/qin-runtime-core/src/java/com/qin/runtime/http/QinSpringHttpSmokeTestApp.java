package com.qin.runtime.http;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Isolated Spring Boot config for HTTP smoke tests.
 *
 * It intentionally lives in its own package so Boot/Jackson package scanning
 * does not traverse the broader qin-runtime-core test output directory.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
public class QinSpringHttpSmokeTestApp {
}
