class EmergencyEventServiceTest {

    private final EmergencyEventRepository eventRepo = Mockito.mock(EmergencyEventRepository.class);
    private final GeoZoneRepository geoRepo = Mockito.mock(GeoZoneRepository.class);
    private final UserRepository userRepo = Mockito.mock(UserRepository.class);
    private final NotificationRepository notificationRepo = Mockito.mock(NotificationRepository.class);
    private final EmergencyTelegramBot bot = Mockito.mock(EmergencyTelegramBot.class);
    private final GeoService geoService = new GeoService();

    private final EmergencyEventService service =
            new EmergencyEventService(eventRepo, geoRepo, userRepo, notificationRepo, bot, geoService);

    @Test
    void shouldCreateEventObject() {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Test");
        request.setMessageText("Message");

        Mockito.when(eventRepo.save(Mockito.any()))
                .thenAnswer(i -> i.getArgument(0));

        EmergencyEvent result = service.create(request);

        assertNotNull(result);
        assertEquals("Test", result.getTitle());
    }
}