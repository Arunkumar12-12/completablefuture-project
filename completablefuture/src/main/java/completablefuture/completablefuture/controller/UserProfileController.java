package completablefuture.completablefuture.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import completablefuture.completablefuture.UserProfileService.UserProfileService;
import completablefuture.completablefuture.model.UserProfile;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {
	
	@Autowired
	private UserProfileService userService;
	
	@GetMapping
	public CompletableFuture<ResponseEntity<UserProfile>> getProfile(){
		
		return userService.getUserProfile()
				.thenApply(profile -> ResponseEntity.ok(profile));
		 
	
	}
	
	

}
