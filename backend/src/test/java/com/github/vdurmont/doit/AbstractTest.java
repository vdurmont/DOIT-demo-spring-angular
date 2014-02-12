package com.github.vdurmont.doit;

import com.github.vdurmont.doit.config.AppConfig;
import com.github.vdurmont.doit.repository.ProjectRepository;
import com.github.vdurmont.doit.repository.TodoRepository;
import com.github.vdurmont.doit.repository.UserRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public abstract class AbstractTest {
	@Inject
	protected ProjectRepository projectRepository;
	@Inject
	protected TodoRepository todoRepository;
	@Inject
	protected UserRepository userRepository;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@After
	public void tearDownEnvironment() {
		DateTimeUtils.setCurrentMillisSystem();
		this.projectRepository.deleteAll();
		this.todoRepository.deleteAll();
		this.userRepository.deleteAll();
	}

	protected static void setCurrentDate(DateTime dateTime) {
		DateTimeUtils.setCurrentMillisFixed(dateTime.getMillis());
	}

	protected static String randomString() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	protected static Boolean randomBoolean() {
		return new Random().nextBoolean();
	}
}
