package imoovo.javadev.exception;

public class UserNotFoundException extends UserException{

	public UserNotFoundException(String username) {
		super(username, "User doesn't exist");
	}
}
