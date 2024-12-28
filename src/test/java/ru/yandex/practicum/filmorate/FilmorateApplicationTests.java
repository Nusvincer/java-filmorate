package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class FilmorateApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		assertNotNull(context, "Spring ApplicationContext должен быть загружен.");
	}

	@Test
	void userControllerBeanExists() {
		Object userController = context.getBean("userController");
		assertNotNull(userController, "UserController должен быть доступен как Spring Bean.");
	}

	@Test
	void filmControllerBeanExists() {
		Object filmController = context.getBean("filmController");
		assertNotNull(filmController, "FilmController должен быть доступен как Spring Bean.");
	}
}
