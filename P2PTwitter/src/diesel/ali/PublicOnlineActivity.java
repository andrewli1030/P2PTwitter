package diesel.ali;

import java.util.ArrayList;
import java.util.List;

public class PublicOnlineActivity extends UsersOnlineActivity {

	public static final List<User> PUBLIC = new ArrayList<User>();
	
	
	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return PublicOnlineActivity.PUBLIC;
	}

}
