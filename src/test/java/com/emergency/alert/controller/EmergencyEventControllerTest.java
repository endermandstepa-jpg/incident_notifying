@WebMvcTest(EmergencyEventController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmergencyEventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmergencyEventService service;

    @MockBean
    private EmergencyEventRepository repository; // 🔥 ВОТ ЭТО ИСПРАВЛЕНИЕ

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEvent() throws Exception {

        CreateEventRequest request = new CreateEventRequest();
        request.setTitle("Fire");
        request.setMessageText("Evacuate");
        request.setCity("Berlin");

        when(service.create(any()))
                .thenReturn(new EmergencyEvent());

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}