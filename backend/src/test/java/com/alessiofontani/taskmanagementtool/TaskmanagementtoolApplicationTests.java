package com.alessiofontani.taskmanagementtool;

import com.alessiofontani.taskmanagementtool.controller.AuthController;
import com.alessiofontani.taskmanagementtool.controller.AuthControllerApiTest;
import com.alessiofontani.taskmanagementtool.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TaskmanagementtoolApplicationTests {


	@Test
	void contextLoads() {
	}

}
