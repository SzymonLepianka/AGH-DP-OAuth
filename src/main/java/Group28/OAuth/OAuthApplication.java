package Group28.OAuth;

import Group28.OAuth.Model.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class OAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(OAuthApplication.class, args);
	}
}
