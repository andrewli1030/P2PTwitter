package diesel.ali;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class PublicActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_tabs);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, PublicStatusesActivity.class);
		spec = tabHost.newTabSpec("public_statuses").setIndicator("Statuses")
				.setContent(intent);
		tabHost.addTab(spec);

		
		intent = new Intent().setClass(this, PublicOnlineActivity.class);
		spec = tabHost.newTabSpec("public_online").setIndicator("Online")
				.setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTabByTag("public_statuses");
		
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 55;
		}
	}

	
}
