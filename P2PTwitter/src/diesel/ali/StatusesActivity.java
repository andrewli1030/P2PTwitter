package diesel.ali;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public abstract class StatusesActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<Status>(this, R.layout.status_item,
				this.getStatuses()));

	}

	public abstract List<Status> getStatuses();

}
