package completablefuture.completablefuture.UserProfileService;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import completablefuture.completablefuture.Client.UserClient;
import completablefuture.completablefuture.model.Notification;
import completablefuture.completablefuture.model.Order;
import completablefuture.completablefuture.model.User;
import completablefuture.completablefuture.model.UserProfile;

@Service
public class UserProfileService {
	
	@Autowired
	private UserClient userClient;
	
	public CompletableFuture<UserProfile> getUserProfile(){
		
		CompletableFuture<User> userFuture=userClient.getUser()
				.orTimeout(3, TimeUnit.SECONDS)
				.exceptionally(ex ->{
					System.out.println("Timeout Fallback :Returning empty user");
					User fallbackUser = new User();
	                fallbackUser.setName("TimeoutFallback");
	                fallbackUser.setAge(-1);
	                return fallbackUser;
				});
		CompletableFuture<Order> orderFuture=CompletableFuture.completedFuture(new Order());
		CompletableFuture<Notification> notificationfuture=CompletableFuture.completedFuture(new Notification());
		return CompletableFuture.allOf(userFuture,userFuture,notificationfuture)
				.thenApply(v ->{
					try {
						User user=userFuture.get();
						Order order=orderFuture.get();
						Notification notification=notificationfuture.get();
						UserProfile profile= new UserProfile();
						profile.setName(user.getName());
						profile.setAge(user.getAge());
						profile.setOrders(order.getItems()!=null? order.getItems(): Collections.emptyList());
						profile.setNotifications(notification.getMessages()!=null? notification.getMessages() :Collections.emptyList()  );
						return profile;
						
			}catch(Exception e) {
				throw new RuntimeException("Error combining futures", e);
				
			}
				});
	}

}
