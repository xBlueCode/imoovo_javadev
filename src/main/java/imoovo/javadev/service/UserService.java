package imoovo.javadev.service;

import imoovo.javadev.domain.Distance;
import imoovo.javadev.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

	User save(User user);
	void delete(User user);
	User update(User user);
	Optional<User> findUserById(Long id);
	Optional<User> findUserByUsername(String username);
	List<User> findAll();

	Distance calcDistanceAndSave(String username, Double lat1, Double lon1, Double lat2, Double lon2);
}

