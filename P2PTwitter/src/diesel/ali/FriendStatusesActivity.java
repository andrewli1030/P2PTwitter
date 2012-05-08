package diesel.ali;

import java.util.ArrayList;
import java.util.List;

public class FriendStatusesActivity extends StatusesActivity  {

	public static final List<Status> FRIEND_STATUSES = new ArrayList<Status>();
	
	
	
	@Override
	public List<Status> getStatuses() {
		return FriendStatusesActivity.FRIEND_STATUSES;
	}

}
