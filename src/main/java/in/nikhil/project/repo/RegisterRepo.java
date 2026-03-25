package in.nikhil.project.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.nikhil.project.modal.Users;

@Repository
public interface RegisterRepo extends JpaRepository<Users, UUID> {

	Optional<Users> findByEmail(String email);

}
