package hub.event.users.testdata.random;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


//Class to load only on empty database
//Generates fake data which can be exported and then used for liquibase data

@SpringBootTest()
@ActiveProfiles(profiles = {"prod","faker"})
@Disabled
class RandomFilterDataTest {

    @Test
    @Disabled
    void createRandomFilters(){
        System.out.println("Sample data created");
    }

}
