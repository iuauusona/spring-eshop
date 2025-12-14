package com.isagulova.spring_eshop.service;

import com.isagulova.spring_eshop.dao.UserRepository;
import com.isagulova.spring_eshop.domain.User;
import com.isagulova.spring_eshop.dto.UserDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private MailSenderService mailSenderService;

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before All tests");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Before each test");
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userRepository = Mockito.mock(UserRepository.class);
        mailSenderService = Mockito.mock(MailSenderService.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder,  mailSenderService);
    }

    @AfterEach
    void afterEach(){
        System.out.println("After each test");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("After All test");
    }

    @Test
    void checkFindByName() {
        //have
        String name = "petr";
        User expectedUser = User.builder().id(1L).name(name).build();

        Mockito.when(userRepository.findByName(Mockito.anyString())).thenReturn(expectedUser);

        //execute
        User actualUser = userService.findByName(name);

        //check
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);

    }

    @Test
    void checkFindByNameExact() {
        //have
        String name = "petr";
        User expectedUser = User.builder().id(1L).name(name).build();

        Mockito.when(userRepository.findByName(Mockito.eq(name))).thenReturn(expectedUser);

        //execute
        User actualUser = userService.findByName(name);
        User rndUser = userService.findByName(UUID.randomUUID().toString());

        //check
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);

        Assertions.assertNull(rndUser);

    }

    @Test
    void checkSaveIncorrectPassword(){
        //have
        UserDTO userDto = UserDTO.builder()
                .password("password")
                .matchingPassword("another")
                .build();

        //execute
        Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.save(userDto);
            }
        });

    }

    @Test
    void checkSave(){
        //have
        UserDTO userDto = UserDTO.builder()
                .username("name")
                .email("email")
                .password("password")
                .matchingPassword("password")
                .build();

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");

        //execute
        boolean result = userService.save(userDto);

        //check
        Assertions.assertTrue(result);
        Mockito.verify(passwordEncoder).encode(Mockito.anyString());
        Mockito.verify(userRepository).save(Mockito.any());

    }
}