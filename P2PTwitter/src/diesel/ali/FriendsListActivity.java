package diesel.ali;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class FriendsListActivity extends ListActivity {

	private List<User> FRIENDS = new ArrayList<User>();
	private UsersDataSource userDataSource;
	private StatusHistoryDataSource statusHistoryDataSource;
	private static final int DIALOG_ADD_FRIEND = 1;
	private static final int DIALOG_DELETE_FRIEND = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userDataSource = new UsersDataSource(this);
		userDataSource.open();

		statusHistoryDataSource = new StatusHistoryDataSource(this);
		statusHistoryDataSource.open();

		FRIENDS = userDataSource.getAllUsers();

		setTitle("Friends");
		setListAdapter(new ArrayAdapter<User>(this, R.layout.friends_list_item,
				FRIENDS));

		final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setLongClickable(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle extras = getIntent().getExtras();
				User recipient = (User) lv.getItemAtPosition(position);
				if (extras != null && extras.getString("Status") != null) {
					String statusText = extras.getString("Status");
					statusHistoryDataSource.insertStatus(
							P2PTwitterActivity.SENDER, recipient, statusText,
							(int) System.currentTimeMillis() / 1000);
					// TODO send status
				}
				Intent intent = new Intent(view.getContext(),
						StatusHistoryActivity.class);
				intent.putExtra("Recipient", recipient.getUsername());
				startActivity(intent);

			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putInt("Position", position);
				showDialog(DIALOG_DELETE_FRIEND, bundle);
				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.friends_list_menu, menu);
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id, final Bundle args) {
		Dialog dialog;
		switch (id) {
		case (DIALOG_ADD_FRIEND):
			LayoutInflater factory = LayoutInflater.from(this);
			final EditText textEntryView = (EditText) factory.inflate(
					R.layout.add_friend_dialog, null);
			dialog = new AlertDialog.Builder(
					getParent() instanceof TabActivity ? getParent() : this)
					.setTitle("Enter Username")
					.setView(textEntryView)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@SuppressWarnings("unchecked")
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Editable username = textEntryView.getText();
									User newFriend = new User(username
											.toString());
									userDataSource.insertUser(newFriend);
									FRIENDS.add(newFriend);
									((ArrayAdapter<User>) getListAdapter())
											.notifyDataSetChanged();
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
		case (DIALOG_DELETE_FRIEND):
			dialog = new AlertDialog.Builder(
					getParent() instanceof TabActivity ? getParent() : this)
					.setNegativeButton("Delete",
							new DialogInterface.OnClickListener() {

								@SuppressWarnings("unchecked")
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									User selectedUser = (User) (getListView()
											.getItemAtPosition(args
													.getInt("Position")));
									userDataSource.deleteUser(selectedUser);
									FRIENDS.remove(selectedUser);
									((ArrayAdapter<User>) getListAdapter())
											.notifyDataSetChanged();
									dialog.dismiss();
								}
							}).create();
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					removeDialog(DIALOG_DELETE_FRIEND);
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
		switch (item.getItemId()) {
		case (R.id.add_friend):
			showDialog(DIALOG_ADD_FRIEND);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		userDataSource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		userDataSource.close();
		super.onPause();
	}

}
