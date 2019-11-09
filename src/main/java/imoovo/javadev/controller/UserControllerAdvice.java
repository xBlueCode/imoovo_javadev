package imoovo.javadev.controller;

import imoovo.javadev.exception.UserExistException;
import imoovo.javadev.exception.UserNotFoundException;
import imoovo.javadev.exception.UserRoleRequiredException;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class UserControllerAdvice{

	@ExceptionHandler(UserExistException.class)
	public ResponseEntity<?> userExistExceptionHandler(final UserExistException excep)
	{
		return error(excep, HttpStatus.BAD_REQUEST, excep.getUsername());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> userNotFoundExceptionHandler(final UserNotFoundException excep)
	{
		return error(excep, HttpStatus.FORBIDDEN, excep.getUsername());
	}

	@ExceptionHandler(UserRoleRequiredException.class)
	public ResponseEntity<?> userRoleRequiredExceptionHandler(final UserRoleRequiredException excep)
	{
		return error(excep, HttpStatus.FORBIDDEN, excep.getUsername());
	}

	private ResponseEntity<?> error
		(final Exception excep, final HttpStatus httpStatus, final String logRef)
	{
		HashMap<String, Object> body = new HashMap<>();
		body.put("code", httpStatus);
		body.put("error", new VndErrors.VndError(logRef, excep.getMessage()));
		return new ResponseEntity<>(body, httpStatus);
	}
}
