package hub.event;

import hub.event.users.UserService;
import hub.event.users.dto.UserDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class ScraperApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ScraperApplication.class, args);

		testUserUpdate(context);


	}

	private static void testUserUpdate(ConfigurableApplicationContext context) {
		UserService userService = context.getBean(UserService.class);
		UserDto givenUser = new UserDto(null,"testUser1", "testUser1@gmail.com", LocalDate.of(2022,7,8),LocalDate.of(1999,7,8));
		UserDto givenUser2 = new UserDto(null,"testUser2", "testUser2@poczta.onet.pl",LocalDate.of(2022,8,17),LocalDate.of(2000,6,5));

		userService.saveUser(givenUser);
		userService.saveUser(givenUser2);

		userService.updateUser(2L, new UserDto(null,"UpdatedName",null,null,null));

		Optional<UserDto> userById = userService.getUserById(2L);

		userById.ifPresentOrElse(System.out::println,() -> System.out.println("Not Found"));


	}

}
