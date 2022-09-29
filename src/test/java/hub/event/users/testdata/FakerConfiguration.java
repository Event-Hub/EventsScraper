package hub.event.users.testdata;

import com.github.javafaker.Faker;
import hub.event.users.testdata.random.FileReader;
import hub.event.users.testdata.random.RandomFilter;
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

    @Bean
    public RandomFilter random(){
        FileReader fileReader = new FileReader();
        return new RandomFilter(fileReader);
    }
}
