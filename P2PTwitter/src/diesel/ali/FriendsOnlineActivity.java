package diesel.ali;

import java.util.ArrayList;
import java.util.List;

public class FriendsOnlineActivity extends UsersOnlineActivity {

	public static List<User> FRIENDS = new ArrayList<User>();
	
	@Override
	public List<User> getUsers() {
		return FriendsOnlineActivity.FRIENDS;
	}

}
