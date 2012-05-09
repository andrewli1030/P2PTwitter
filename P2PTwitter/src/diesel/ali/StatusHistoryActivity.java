package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import org.alljoyn.services.ChatApplication;
import org.alljoyn.services.Observable;
import org.alljoyn.services.Observer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class StatusHistoryActivity extends Activity implements Observer {

	private ListView statusHistory;
	private EditText editTextOut;
	private Button buttonSend;

	private List<Status> STATUSES = new ArrayList<Status>();
	private StatusHistoryDataSource datasource;
	private User recipient;

	// private ArrayAdapter<Status> mHistoryList;

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

		if (getIntent().getExtras().getBoolean("Public")) {
			editTextOut.setVisibility(View.GONE);
			buttonSend.setVisibility(View.GONE);
		}

		STATUSES = datasource.getStatusHistory(recipient);
		statusHistory.setAdapter(new ArrayAdapter<Status>(this,
				R.layout.status_item, STATUSES));

		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String statusText = editTextOut.getText().toString();
				Status status = new Status(P2PTwitterActivity.SENDER,
						recipient, statusText,
						(int) System.currentTimeMillis() / 1000);

				//mChatApplication.newLocalUserMessage(status);
				mChatApplication.newLocalUserMessage(statusText);
				
				
				editTextOut.setText("");

				/*
				 * if (datasource.insertStatus(status)) { STATUSES.add(status);
				 * editTextOut.setText(""); ((ArrayAdapter)
				 * statusHistory.getAdapter()) .notifyDataSetChanged(); // TODO
				 * send status mChatApplication.newLocalUserMessage(status); }
				 */

			}
		});

		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.checkin();
		mChatApplication.addObserver(this);

	}

	@Override
	public void update(Observable o, Object arg) {
		String qualifier = (String) arg;

		if (qualifier.equals(ChatApplication.APPLICATION_QUIT_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_APPLICATION_QUIT_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.HISTORY_CHANGED_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_HISTORY_CHANGED_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.USE_CHANNEL_STATE_CHANGED_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_CHANNEL_STATE_CHANGED_EVENT);
			mHandler.sendMessage(message);
		}

		if (qualifier.equals(ChatApplication.ALLJOYN_ERROR_EVENT)) {
			Message message = mHandler
					.obtainMessage(HANDLE_ALLJOYN_ERROR_EVENT);
			mHandler.sendMessage(message);
		}
	}

	private static final int HANDLE_APPLICATION_QUIT_EVENT = 0;
	private static final int HANDLE_HISTORY_CHANGED_EVENT = 1;
	private static final int HANDLE_CHANNEL_STATE_CHANGED_EVENT = 2;
	private static final int HANDLE_ALLJOYN_ERROR_EVENT = 3;
	private static final String TAG = "chat.UseActivity";

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_APPLICATION_QUIT_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_APPLICATION_QUIT_EVENT");
				finish();
			}
				break;
			case HANDLE_HISTORY_CHANGED_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_HISTORY_CHANGED_EVENT");
				updateHistory();
				break;
			}
			case HANDLE_CHANNEL_STATE_CHANGED_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_CHANNEL_STATE_CHANGED_EVENT");
				// updateChannelState();
				break;
			}
			case HANDLE_ALLJOYN_ERROR_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_ALLJOYN_ERROR_EVENT");
				// alljoynError();
				break;
			}
			default:
				break;
			}
		}
	};

	private ChatApplication mChatApplication = null;

	/*
	 * private void updateHistory() { // Log.i(TAG, "updateHistory()"); //
	 * mHistoryList.clear();
	 * 
	 * List<String> messages = mChatApplication.getHistory(); for (String
	 * message : messages) { mHistoryList.add(message); }
	 * mHistoryList.notifyDataSetChanged();
	 * 
	 * 
	 * List<Status> statuses = mChatApplication.getStatusHistory(); for (Status
	 * status : statuses) { if (datasource.insertStatus(status)) {
	 * mHistoryList.add(status); } } mChatApplication.clearStatusHistory();
	 * mHistoryList.notifyDataSetChanged();
	 * 
	 * }
	 */
	private void updateHistory() {
		datasource.open();
		STATUSES = datasource.getStatusHistory(recipient);
		statusHistory.setAdapter(new ArrayAdapter<Status>(this,
				R.layout.status_item, STATUSES));
		((ArrayAdapter<Status>) statusHistory.getAdapter())
				.notifyDataSetChanged();
	}

}
