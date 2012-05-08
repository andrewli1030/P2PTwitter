package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class StatusHistoryActivity extends Activity {

	private ListView statusHistory;
	private EditText editTextOut;
	private Button buttonSend;

	private List<Status> STATUSES = new ArrayList<Status>();
	private StatusHistoryDataSource datasource;
	private User recipient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.status_history);

		statusHistory = (ListView) findViewById(R.id.status_history);
		editTextOut = (EditText) findViewById(R.id.edit_text_out);
		buttonSend = (Button) findViewById(R.id.button_send);

		datasource = new StatusHistoryDataSource(this);
		datasource.open();

		recipient = new User(getIntent().getExtras().getString("Recipient"));

		STATUSES = datasource.getStatusHistory(recipient);

		statusHistory.setAdapter(new ArrayAdapter<Status>(this,
				R.layout.status_item, STATUSES));

		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String statusText = editTextOut.getText().toString();
				datasource.insertStatus(P2PTwitterActivity.SENDER, recipient, statusText, (int) System.currentTimeMillis()/1000);
			}
		});

	}

}
