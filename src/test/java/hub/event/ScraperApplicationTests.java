package hub.event;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = {"dev","test"})
class ScraperApplicationTests {

	@Test
	void contextLoads() {
	}

}
