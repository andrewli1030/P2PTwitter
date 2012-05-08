package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import org.alljoyn.services.Observable;
import org.alljoyn.services.Observer;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PublicStatusesActivity extends ListActivity implements Observer {

	public static List<Status> PUBLIC_STATUSES = new ArrayList<Status>();
	private StatusHistoryDataSource statusHistoryDataSource;

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
				intent.putExtra("Public", true);
				startActivity(intent);
			}
		});

	}

	public List<Status> getStatuses() {
		PUBLIC_STATUSES = statusHistoryDataSource
				.getStatusHistory(P2PTwitterActivity.PUBLIC);
		return PublicStatusesActivity.PUBLIC_STATUSES;
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

	
	private List<Observer> mObservers = new ArrayList<Observer>();

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
	

	// TODO update

}
