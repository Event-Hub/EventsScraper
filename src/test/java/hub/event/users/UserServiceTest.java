package hub.event.users;

import hub.event.users.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles(profiles = {"dev","test"})
class UserServiceTest {


    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository,userDtoMapper);
    }

    @Test
    void getUserById() {

        System.out.println("Get by Id Test");
        Optional<UserDto> userById = userService.getUserById(1L);
        userById.ifPresentOrElse(System.out::println,() -> System.out.println("Not found"));
        Optional<UserDto> userById2 = userService.getUserById(2L);
        userById2.ifPresentOrElse(System.out::println,() -> System.out.println("Not found"));

        //TODO Finish Test
    }


    @Test
    void getUserByUserName() {
        fail("Not Implemented");
    }

    @Test
    void saveUser() {
        //given
        UserDto givenUserDto = new UserDto();
        givenUserDto.setUsername("TestUser");
        givenUserDto.setEmail("test.user@gmail.com");
        givenUserDto.setBirthDate(LocalDate.of(1989,3,12));

        //when
        UserDto userDtoSaved = userService.saveUser(givenUserDto);

        System.out.println("Save User Test");
        System.out.println("User to save:");
        System.out.println(givenUserDto);

        System.out.println("Saved user:");
        System.out.println(userDtoSaved);

        //then
        assertAll(
                () -> assertEquals(givenUserDto.getUsername(),userDtoSaved.getUsername()),
                () -> assertEquals(givenUserDto.getEmail(),userDtoSaved.getEmail()),
                () -> assertEquals(givenUserDto.getBirthDate(),userDtoSaved.getBirthDate()),
                () -> assertNotNull(userDtoSaved.getRegistrationDate()),
                () -> assertNotNull(userDtoSaved.getId())
        );

    }

    @Test
    void updateUser() {
        fail("Not Implemented");
    }

    @Test
    void deleteUser() {
        fail("Not Implemented");
    }
}