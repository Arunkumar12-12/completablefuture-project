package completablefuture.completablefuture.Client;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import completablefuture.completablefuture.model.User;

@Service
public class UserClient {
	
	@Autowired
	private CircuitBreakerFactory circuitBreakerFactory;
	
	@Async
	public CompletableFuture<User> getUser(){
		return CompletableFuture.supplyAsync(()->{
			return circuitBreakerFactory.create("userServiceCB")
					.run(() -> {
//						simulateSlowService();
						User user= new User();
						user.setName("arun");
						user.setAge(27);
						System.out.println("User Service Success");
						return user;
					}
					, throwable ->{
						System.out.println("user service FallBack triggered"+ throwable);
						User fallbackUser= new User();
						fallbackUser.setName("Unknown");
	                    fallbackUser.setAge(0);
	                    return fallbackUser;
					});
		});
	}
	
	private void simulateSlowService() {
		try {
			Thread.sleep(2500);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}
