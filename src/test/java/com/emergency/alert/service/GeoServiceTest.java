package com.emergency.alert.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoServiceTest {

    private final GeoService geoService = new GeoService();

    @Test
    void shouldReturnTrueWhenInsideRadius() {
        assertTrue(
                geoService.insideRadius(55.75, 37.61, 55.76, 37.62, 5)
        );
    }

    @Test
    void shouldReturnFalseWhenOutsideRadius() {
        assertFalse(
                geoService.insideRadius(55.75, 37.61, 60.0, 30.0, 5)
        );
    }
}