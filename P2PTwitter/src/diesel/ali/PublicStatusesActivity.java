package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PublicStatusesActivity extends StatusesActivity {

	public static final List<Status> PUBLIC_STATUSES = new ArrayList<Status>();
	

	@Override
	public List<Status> getStatuses() {
		// TODO Auto-generated method stub
		return PublicStatusesActivity.PUBLIC_STATUSES;
	} 

	

	
}
