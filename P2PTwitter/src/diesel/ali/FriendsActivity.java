package diesel.ali;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class FriendsActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_tabs);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, FriendStatusesActivity.class);
		spec = tabHost.newTabSpec("friend_statuses").setIndicator("Statuses")
				.setContent(intent);
		tabHost.addTab(spec);

		
		intent = new Intent().setClass(this, FriendsOnlineActivity.class);
		spec = tabHost.newTabSpec("friends_online").setIndicator("Online")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FriendsListActivity.class);
		spec = tabHost.newTabSpec("friends_list").setIndicator("List")
				.setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTabByTag("friends_statuses");
		
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 55;
		}
	}

}
