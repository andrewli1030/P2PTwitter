package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class FriendsListActivity extends ListActivity {

	private List<User> FRIENDS = new ArrayList<User>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setTitle("Friends");
		/*
		 * setListAdapter(new ArrayAdapter<String>(this,
		 * R.layout.friends_list_item, FRIENDS));
		 */
		setListAdapter(new ArrayAdapter<User>(this, R.layout.friends_list_item,
				FRIENDS));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.friends_list_menu, menu);
		return true;
	}

	private static final int DIALOG_ADD_FRIEND = 1;

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog;
		switch (id) {
		case (DIALOG_ADD_FRIEND):
			LayoutInflater factory = LayoutInflater.from(this);
			final EditText textEntryView = (EditText) factory.inflate(
					R.layout.add_friend_dialog, null);
			dialog = new AlertDialog.Builder(this)
					.setTitle("Enter Username")
					.setView(textEntryView)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Editable username = textEntryView.getText();
									User newFriend = new User(username.toString());
									FRIENDS.add(newFriend);
									dialog.dismiss();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							}).create();
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					removeDialog(DIALOG_ADD_FRIEND);
				}
			});
			break;
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case (R.id.add_friend):
			showDialog(DIALOG_ADD_FRIEND);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
