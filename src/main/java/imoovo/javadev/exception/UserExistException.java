package imoovo.javadev.exception;

public class UserExistException extends UserException{

	public UserExistException(String username) {
		super(username, "User already exist");
	}
}
