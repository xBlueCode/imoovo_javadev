package imoovo.javadev.controller;

import imoovo.javadev.domain.User;
import imoovo.javadev.service.UserServiceDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserServiceDb userService;

	@Autowired
	public UserController(UserServiceDb userService) {
		this.userService = userService;
	}

	@PostMapping("/save")
	public ResponseEntity<?> register(@RequestBody User user)
	{
		HashMap<String, Object> body = new HashMap<>();
		if (userService.findUserByUsername(user.getUsername()).isPresent())
		{
			body.put("error", "User already exist");
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}
		else if (!user.getRole().equals("admin") && !user.getRole().equals("user"))
		{
			body.put("error", "Role must be 'admin' or 'user'");
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}
		else
		{
			User savedUser = userService.save(user);
			body.put("action", "saved");
			body.put("user", savedUser);
			return new ResponseEntity<>(body, HttpStatus.CREATED);
		}
	}

	@GetMapping("/get")
	public ResponseEntity<?> get(@RequestParam String username)
	{
		HashMap<String, Object> body = new HashMap<>();
		Optional<User> opUser = userService.findUserByUsername(username);
		if (!opUser.isPresent())
		{
			body.put("error", "User doesn't exist");
			body.put("code", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}
		else if (!opUser.get().getRole().equals("admin"))
		{
			body.put("error", "User must be an admin to perform this operation");
			body.put("code", HttpStatus.FORBIDDEN);
			return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
		}
		else
		{
			body.put("code", HttpStatus.OK);
			body.put("Users", userService.findAll());
			return new ResponseEntity<>(body, HttpStatus.OK);
		}
	}
}
