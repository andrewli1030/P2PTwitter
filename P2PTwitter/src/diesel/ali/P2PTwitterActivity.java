package diesel.ali;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class P2PTwitterActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, StatusHistoryListActivity.class);
		spec = tabHost.newTabSpec("public").setIndicator("Public")
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, SetStatusActivity.class);
		spec = tabHost.newTabSpec("setstatus").setIndicator("Set Status")
				.setContent(intent);
		tabHost.addTab(spec);

		
//		asdfasdfas
		
		
		tabHost.setCurrentTabByTag("public");
	}
}