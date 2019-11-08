package imoovo.javadev.controller;

import imoovo.javadev.domain.Distance;
import imoovo.javadev.domain.User;
import imoovo.javadev.service.UserServiceDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/dist/")
public class DistanceController {

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
		return new ResponseEntity<>(body, HttpStatus.OK);
	}

	@GetMapping("history")
	public ResponseEntity<?> history(@RequestParam String username, @RequestParam(required = false) String forName)
	{
		HashMap<String, Object> body = new HashMap<>();
		Optional<User> opUser = userService.findUserByUsername(username);
		HttpStatus code;
		if (!opUser.isPresent())
		{
			code = HttpStatus.BAD_REQUEST;
			body.put("error", String.format("The user %s doesn't exist", forName));
		}
		else if (forName == null)
		{
			code = HttpStatus.OK;
			body.put("user", username);
			body.put("calculations:", opUser.get().getDistances());
		}
		else if (!opUser.get().getRole().equals("admin"))
		{
			code = HttpStatus.FORBIDDEN;
			body.put("error", "User must be admin to perform this action");
		}
		else{
			Optional<User> targetedUser = userService.findUserByUsername(forName);
			if (!targetedUser.isPresent())
			{
				code = HttpStatus.BAD_REQUEST;
				body.put("error", String.format("The user %s doesn't exist", forName));
			}
			else
			{
				code = HttpStatus.OK;
				body.put("user", forName);
				body.put("calculations:", targetedUser.get().getDistances());
			}
		}
		body.put("code", code);
		return new ResponseEntity<>(body, code);
	}
}
