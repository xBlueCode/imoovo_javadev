package imoovo.javadev.exception;

public class UserRoleRequiredException extends UserException{

	public UserRoleRequiredException(String username, String role) {
		super(username, String.format("The user must be '%s' to perform this operation", role));
	}
}
