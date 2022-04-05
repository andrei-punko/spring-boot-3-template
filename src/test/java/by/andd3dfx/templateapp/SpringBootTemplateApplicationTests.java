package by.andd3dfx.templateapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = IntegrationTestInitializer.class)
@SpringBootTest
class SpringBootTemplateApplicationTests {

	@Test
	void contextLoads() {
		// do nothing, this test for just check is context up
	}
}
