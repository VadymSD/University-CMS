package vadym.spring.console.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestSchoolConsoleAppApplication {

	public static void main(String[] args) {
		SpringApplication.from(UniversityApplication::main).with(TestSchoolConsoleAppApplication.class).run(args);
	}
}
