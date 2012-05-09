package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import org.alljoyn.services.ChatApplication;
import org.alljoyn.services.Observable;
import org.alljoyn.services.Observer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendStatusesActivity extends ListActivity implements Observer {

	public static List<Status> FRIEND_STATUSES = new ArrayList<Status>();
	private StatusHistoryDataSource statusHistoryDataSource;
	private UsersDataSource usersDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		statusHistoryDataSource = new StatusHistoryDataSource(this);
		statusHistoryDataSource.open();

		setListAdapter(new ArrayAdapter<Status>(this, R.layout.status_item,
				this.getStatuses()));

		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Status statusClicked = (Status) lv.getItemAtPosition(position);
				Intent intent = new Intent(view.getContext(),
						StatusHistoryActivity.class);
				intent.putExtra("Recipient", statusClicked.getSender()
						.getUsername());
				startActivity(intent);

			}
		});

		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.checkin();
		mChatApplication.addObserver(this);

	}

	public List<Status> getStatuses() {
		usersDataSource = new UsersDataSource(this);
		usersDataSource.open();
		FRIEND_STATUSES = statusHistoryDataSource
				.getAllFriendStatuses(usersDataSource.getAllUsernames());
		return FRIEND_STATUSES;
	}

	@Override
	protected void onResume() {
		statusHistoryDataSource.open();
		setListAdapter(new ArrayAdapter<Status>(this, R.layout.status_item,
				this.getStatuses()));
		((ArrayAdapter<Status>) getListAdapter()).notifyDataSetChanged();
		super.onResume();
	}

	@Override
	protected void onPause() {
		statusHistoryDataSource.close();
		super.onPause();
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

	private void updateHistory() {
		statusHistoryDataSource.open();
		setListAdapter(new ArrayAdapter<Status>(this, R.layout.status_item,
				this.getStatuses()));
		((ArrayAdapter<Status>) getListAdapter()).notifyDataSetChanged();
	}

}
