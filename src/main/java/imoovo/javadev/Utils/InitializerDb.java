package imoovo.javadev.Utils;

import imoovo.javadev.domain.User;
import imoovo.javadev.service.UserServiceDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitializerDb implements CommandLineRunner {

	private UserServiceDb userServiceDb;

	@Autowired
	public InitializerDb(UserServiceDb userServiceDb) {
		this.userServiceDb = userServiceDb;
	}

	@Override
	public void run(String... args) throws Exception {

		for (int i = 1; i < 10; i++)
		{
			User user = new User();
			user.setUsername(String.format("user%03d", i));
			user.setRole((i % 2) == 0 ? "admin" : "user");
			user = userServiceDb.save(user);
			for (int j = 0; j < 2; j++)
			{
				userServiceDb.calcDistanceAndSave(user.getUsername(),
					(1000.0 + 100 * i + 10 *j),
					2000.0 + 100 * i + 10 * j,
					1000.0 - 100 * i + - 10 * j,
					2000.0 - 100 * i - 10 * j);
			}
		}
	}
}
