package hub.event.users.testdata;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("faker")
class FakerConfiguration {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
