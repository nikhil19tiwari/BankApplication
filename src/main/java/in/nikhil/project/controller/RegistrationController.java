package in.nikhil.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.nikhil.project.modal.Users;
import in.nikhil.project.serviceImpl.RegisterService;
@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    private final RegisterService userService;

    public RegistrationController(RegisterService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Users user) {

        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        userService.saveUser(user);
        return ResponseEntity.ok("User registered");
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody Users user,Authentication authentication) {
    	   String loggedInEmail = authentication.getName(); 
    	    Users existingUser = userService.findByEmail(loggedInEmail);
    	    user.setEmail(existingUser.getEmail());
		if (!userService.existsByEmail(user.getEmail())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
		userService.updateUser(user);
		return ResponseEntity.ok("User updated");
	}
}