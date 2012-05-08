package diesel.ali;

import java.util.ArrayList;
import java.util.List;

public class FriendsOnlineActivity extends UsersOnlineActivity {

	public static final List<User> FRIENDS = new ArrayList<User>();
	
	static {
		FRIENDS.add(new User("Andrew Li"));
	}
	
	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return FriendsOnlineActivity.FRIENDS;
	}

}
