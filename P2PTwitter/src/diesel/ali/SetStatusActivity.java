package diesel.ali;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SetStatusActivity extends Activity {

	private Button privateButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_status);

		privateButton = (Button) findViewById(R.id.private_button);
		privateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						FriendsListActivity.class);
				startActivity(intent);
			}
		});
	}
}
