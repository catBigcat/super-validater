package apaas.super_validator;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Email;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import apaas.support.validator.MapNotNull;

/**
 * Hello world!
 *
 */
@SpringBootApplication

public class App {


	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
