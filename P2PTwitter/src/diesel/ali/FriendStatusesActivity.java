package diesel.ali;

import java.util.ArrayList;
import java.util.List;

public class FriendStatusesActivity extends StatusesActivity  {

	public static final List<Status> FRIEND_STATUSES = new ArrayList<Status>();
	
	static {
		FRIEND_STATUSES.add(new Status(new User("Andrew Li"), "SLOPE DAY!"));
	}
	
	@Override
	public List<Status> getStatuses() {
		return FriendStatusesActivity.FRIEND_STATUSES;
	}

}
