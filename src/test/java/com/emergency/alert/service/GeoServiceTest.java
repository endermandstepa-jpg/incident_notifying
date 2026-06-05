class GeoServiceTest {

    private final GeoService geoService = new GeoService();

    @Test
    void shouldReturnTrueWhenInsideRadius() {
        assertTrue(geoService.insideRadius(55.75, 37.61, 55.76, 37.62, 5));
    }

    @Test
    void shouldReturnFalseWhenOutsideRadius() {
        assertFalse(geoService.insideRadius(55.75, 37.61, 60.00, 30.00, 5));
    }
}