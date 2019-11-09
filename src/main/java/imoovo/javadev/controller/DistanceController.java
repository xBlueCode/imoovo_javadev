package imoovo.javadev.controller;

import imoovo.javadev.domain.Distance;
import imoovo.javadev.domain.User;
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
@RequestMapping("/api/dist/")
public class DistanceController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private UserServiceDb userService;

	@Autowired
	public DistanceController(UserServiceDb userService) {
		this.userService = userService;
	}

	@PostMapping("calc")
	public ResponseEntity<?> calc(@RequestParam String username,
								  @RequestParam Double lat1, @RequestParam Double lon1,
								  @RequestParam Double lat2, @RequestParam Double lon2)
	{
		logger.info(String.format("Performing distance calculation for %s", username));
		HashMap<String, Object> body = new HashMap<>();
		Optional<User> opUser = userService.findUserByUsername(username);
		if (!opUser.isPresent())
		{
			body.put("error", "User doesn't exist");
			body.put("code", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}
		Distance distance = userService.calcDistanceAndSave(username, lat1, lon1, lat2, lon2);
		body.put("code", HttpStatus.OK);
		body.put("direct_distance", distance.getDist());
		body.put("unit", "km");
		logger.info(String.format("Calculation for %s has been performed successfully", username));
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@GetMapping("history")
	public ResponseEntity<?> history(@RequestParam String username, @RequestParam(required = false) String forName)
	{
		logger.info(String.format("Requesting calculation history"));
		HashMap<String, Object> body = new HashMap<>();
		Optional<User> opUser = userService.findUserByUsername(username);
		HttpStatus code;
		if (!opUser.isPresent())
			throw new UserNotFoundException(username);
		else if (forName == null)
		{
			code = HttpStatus.OK;
			body.put("user", username);
			body.put("calculations:", opUser.get().getDistances());
			logger.info(String.format("History has been served for %s successfully", username));
		}
		else if (!opUser.get().getRole().equals("admin"))
			throw new UserRoleRequiredException(username, "admin");
		else{
			Optional<User> targetedUser = userService.findUserByUsername(forName);
			if (!targetedUser.isPresent())
				throw new UserNotFoundException(forName);
			else
			{
				code = HttpStatus.OK;
				body.put("user", forName);
				body.put("calculations:", targetedUser.get().getDistances());
				logger.info(String.format("History has been served for %s successfully", forName));
			}
		}
		body.put("code", code);
		return new ResponseEntity<>(body, code);
	}
}
