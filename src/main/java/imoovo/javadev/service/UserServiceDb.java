package imoovo.javadev.service;

import imoovo.javadev.domain.User;
import imoovo.javadev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceDb implements UserService{

	private UserRepository userRepository;

	@Autowired
	public UserServiceDb(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public User update(User user) {
		return userRepository.saveAndFlush(user);
	}

	@Override
	public Optional<User> findUserById(Long id) {
		return userRepository.findUserById(id);
	}

	@Override
	public Optional<User> findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

}
