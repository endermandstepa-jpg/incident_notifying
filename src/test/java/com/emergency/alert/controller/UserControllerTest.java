@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository repository;

    @MockBean
    private ApiKeyFilter apiKeyFilter;

    @Test
    void shouldReturnUsers() throws Exception {

        when(repository.findAll()).thenReturn(List.of(new User()));

        mockMvc.perform(get("/api/users")
                .header("X-API-Key", "default-key-for-development"))
                .andExpect(status().isOk());
    }
}