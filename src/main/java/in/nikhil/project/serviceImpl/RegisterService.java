package in.nikhil.project.serviceImpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.nikhil.project.modal.Users;
import in.nikhil.project.repo.RegisterRepo;
@Service
public class RegisterService implements UserDetailsService {

    private final RegisterRepo registerRepo;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(RegisterRepo registerRepo, PasswordEncoder passwordEncoder) {
        this.registerRepo = registerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existsByEmail(String email) {
        return registerRepo.findByEmail(email).isPresent();
    }

    public void saveUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        registerRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = registerRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public Object getAllUsers() {
        return registerRepo.findAll();
    }
}