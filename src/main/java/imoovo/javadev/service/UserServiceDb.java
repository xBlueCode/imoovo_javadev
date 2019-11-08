package imoovo.javadev.service;

import imoovo.javadev.Utils.DistanceUtils;
import imoovo.javadev.domain.Distance;
import imoovo.javadev.domain.User;
import imoovo.javadev.repository.DistanceRepository;
import imoovo.javadev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceDb implements UserService{

	private UserRepository userRepository;
	private DistanceRepository distanceRepository;

	@Autowired
	public UserServiceDb(UserRepository userRepository, DistanceRepository distanceRepository) {
		this.userRepository = userRepository;
		this.distanceRepository = distanceRepository;
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

	@Override
	public Distance calcDistanceAndSave(String username, Double lat1, Double lon1, Double lat2, Double lon2) {

		Optional<User> opUser = userRepository.findUserByUsername(username);
		if (!opUser.isPresent())
			return null;
		User user = opUser.get();
		Double dist = DistanceUtils.calcDirectDistance(lat1, lon1, lat2, lon2);
		LocalDateTime localDateTime = LocalDateTime.now();
		Distance distance = new Distance();
		distance.setLat1(lat1);
		distance.setLon1(lon1);
		distance.setLat2(lat2);
		distance.setLon2(lon2);
		distance.setDist(dist);
		distance.setDateTime(localDateTime);
		distance = distanceRepository.save(distance);
		user.getDistances().add(distance);
		userRepository.saveAndFlush(user);
		return distance;
	}
}
