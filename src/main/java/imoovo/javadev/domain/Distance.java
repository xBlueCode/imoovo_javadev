package imoovo.javadev.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Component
@Data
@Entity
@Table(name = "distance")
public class Distance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double lat1;
	private Double lon1;
	private Double lat2;
	private Double lon2;

	private Double dist;

	private LocalDateTime dateTime;
}
