package imoovo.javadev.controller;

import imoovo.javadev.domain.User;
import imoovo.javadev.exception.UserExistException;
import imoovo.javadev.exception.UserNotFoundException;
import imoovo.javadev.exception.UserRoleRequiredException;
import imoovo.javadev.service.UserServiceDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserServiceDb userService;

	@Autowired
	public UserController(UserServiceDb userService) {
		this.userService = userService;
	}

	@PostMapping("/save")
	public ResponseEntity<?> register(@RequestBody User user)
	{
		HashMap<String, Object> body = new HashMap<>();
		logger.info(String.format("Registering the user %s, %s",
			user.getUsername(), user.getRole()));
		if (userService.findUserByUsername(user.getUsername()).isPresent())
			throw new UserExistException(user.getUsername());
		else if (!user.getRole().equals("admin") && !user.getRole().equals("user"))
		{
			body.put("error", "Role must be 'admin' or 'user'");
			body.put("code", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}
		else
		{
			User savedUser = userService.save(user);
			body.put("action", "saved");
			body.put("user", savedUser);
			logger.info(String.format("The user %s had been saved", user.getUsername()));
			return new ResponseEntity<>(body, HttpStatus.CREATED);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<?> get(@RequestParam String username)
	{
		HashMap<String, Object> body = new HashMap<>();
		Optional<User> opUser = userService.findUserByUsername(username);
		logger.info(String.format("Requesting list of users by %s", username));
		if (!opUser.isPresent())
			throw new UserNotFoundException(username);
		else if (!opUser.get().getRole().equals("admin"))
			throw new UserRoleRequiredException(username, "admin");
		else
		{
			body.put("code", HttpStatus.OK);
			body.put("Users", userService.findAll());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}
	}
}
