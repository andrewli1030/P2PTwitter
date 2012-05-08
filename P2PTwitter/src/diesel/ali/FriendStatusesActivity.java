package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendStatusesActivity extends ListActivity {

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

}
