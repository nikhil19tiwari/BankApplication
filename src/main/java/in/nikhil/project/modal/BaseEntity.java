package in.nikhil.project.modal;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	  
	@CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt ;
	
	@UpdateTimestamp
	@Column(name = "updated_at" , nullable = false, updatable = true)
	private Instant updatedAt ;
	
	
}
