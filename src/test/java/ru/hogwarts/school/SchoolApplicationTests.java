package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.hogwarts.school.controller.AvatarController;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.controller.StudentController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchoolApplicationTests {
	@LocalServerPort
	private int port;

	@Autowired
	private AvatarController avatarController;

	@Autowired
	private FacultyController facultyController;

	@Autowired
	private StudentController studentController;

	@Test
	public void contextLoads() {
		Assertions.assertThat(avatarController).isNotNull();
		Assertions.assertThat(facultyController).isNotNull();
		Assertions.assertThat(studentController).isNotNull();
	}

}
