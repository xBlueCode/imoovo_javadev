package imoovo.javadev.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException{

	private String username;

	public UserException(String username, String message)
	{
		super(String.format("%s -> %s", message, username));
		this.username = username;
	}
}
