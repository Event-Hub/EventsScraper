
package hub.event.users.testdata;

import com.github.javafaker.Faker;
import hub.event.users.filter.FilterService;
import hub.event.users.testdata.random.RandomFilter;
import hub.event.users.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import hub.event.users.user.User;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.LocalDate.*;

@Profile("faker")
@Component
public class SampleDataLoader implements CommandLineRunner {

    private final UserRepository repository;
    private final Faker faker;

    private final RandomFilter randomFilter;
    private final FilterService filterService;

    public SampleDataLoader(UserRepository repository, Faker faker, RandomFilter randomFilter, FilterService filterService) {
        this.repository = repository;
        this.faker = faker;
        this.randomFilter = randomFilter;
        this.filterService = filterService;
    }

    @Override
    public void run(String... args) throws Exception {

        // create 100 rows of people in the database
        List<User> people = IntStream.rangeClosed(1, 100)
                .mapToObj(this::randomUser).toList();

        repository.saveAll(people);

        IntStream.rangeClosed(1, 100)
                .mapToObj(i -> randomFilter.randomFilter())
                .forEach(filterService::saveFilter);

    }


    private User randomUser(int i) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().firstName();
        String name = firstName + " " + lastName;
        String email = getEmail(firstName, lastName);
        Date registration;
        registration = faker.date().between(
                getRegistrationBeginningDate(),
                getDate(now().getYear(), now().getMonth().getValue(), now().getDayOfMonth()));
        LocalDate localDateRegistration = covertDateToLocalDate(registration);
        Date birthday = faker.date().birthday(13, 60);
        LocalDate localDateBirthday = covertDateToLocalDate(birthday);


        return new User(null, name, email, localDateRegistration, localDateBirthday);

    }

    private String getEmail(String firstName, String lastName) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
    }

    private LocalDate covertDateToLocalDate(Date registration) {
        return registration.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date getRegistrationBeginningDate() {
        int year = 2022;
        int month = 0; // January
        int date = 1;

        return getDate(year, month, date);
    }

    private Date getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        cal.clear();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, date);

        return cal.getTime();
    }
}

//TODO correct to work with ZonedTimeDate -DONE