/*
 * Copyright 2011, Qualcomm Innovation Center, Inc.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.alljoyn.bus.sample.chat;

import org.alljoyn.bus.sample.chat.ChatApplication;
import org.alljoyn.bus.sample.chat.Observable;
import org.alljoyn.bus.sample.chat.Observer;
import org.alljoyn.bus.sample.chat.DialogBuilder;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import android.app.Activity;
import android.app.Dialog;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UseActivity extends Activity implements Observer {
	private static final String TAG = "chat.UseActivity";
	public static String username = "diesel2";

	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.use);

		mHistoryList = new ArrayAdapter<String>(this,
				android.R.layout.test_list_item);
		ListView hlv = (ListView) findViewById(R.id.useHistoryList);
		hlv.setAdapter(mHistoryList);

		EditText nameBox = (EditText) findViewById(R.id.useName);
		nameBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView view, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_NULL
						&& event.getAction() == KeyEvent.ACTION_UP) {
					String name = view.getText().toString();
					Log.i("TAPG", "Got name " + name);
					username = name;
				}
				return true;
			}
		});

		EditText messageBox = (EditText) findViewById(R.id.useMessage);
		messageBox
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView view, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_NULL
								&& event.getAction() == KeyEvent.ACTION_UP) {
							String message = view.getText().toString();
							Log.i(TAG,
									"useMessage.onEditorAction(): got message "
											+ message + ")");
							mChatApplication.newLocalUserMessage(message);
							view.setText("");

							Tweet m = new Tweet((Integer) null, (new Date())
									.getTime(), username, message);
							dbhelper.saveMessage(m);
						}
						return true;
					}
				});

		mJoinButton = (Button) findViewById(R.id.useJoin);
		mJoinButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				List<String> ch = mChatApplication.getFoundChannels();
				boolean found = false;
				Log.i("TAGP", "Looking for public channel");
				for (Iterator<String> i = ch.iterator(); i.hasNext();) {
					String channelName = i.next();
					Log.i("TAGP",
							"Found "
									+ channelName
									+ " and t/f: "
									+ "org.alljoyn.bus.samples.chat.public"
											.equals(channelName));
					if (channelName
							.equals("org.alljoyn.bus.samples.chat.public")) {
						Log.i("TAGP", "Found public channel");
						mChatApplication.useSetChannelName("public");
						mChatApplication.useJoinChannel();
						found = true;
					}
				}

				if (!found) {
					Log.i("TAGP", "Creating public channel");
					mChatApplication.hostSetChannelName("public");
					mChatApplication.hostInitChannel();
					mChatApplication.hostStartChannel();
					mChatApplication.useSetChannelName("public");
					mChatApplication.useJoinChannel();
				}
			}
		});

		mLeaveButton = (Button) findViewById(R.id.useLeave);
		mLeaveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_LEAVE_ID);
			}
		});

		mChannelName = (TextView) findViewById(R.id.useChannelName);
		mChannelStatus = (TextView) findViewById(R.id.useChannelStatus);

		/*
		 * Keep a pointer to the Android Appliation class around. We use this as
		 * the Model for our MVC-based application. Whenever we are started we
		 * need to "check in" with the application so it can ensure that our
		 * required services are running.
		 */
		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.checkin();

		/*
		 * Call down into the model to get its current state. Since the model
		 * outlives its Activities, this may actually be a lot of state and not
		 * just empty.
		 */
		updateChannelState();
		updateHistory();

		/*
		 * Now that we're all ready to go, we are ready to accept notifications
		 * from other components.
		 */
		mChatApplication.addObserver(this);

		dbhelper = new DatabaseHelper(getApplicationContext());

		ArrayList<Tweet> at = dbhelper.getHistory();
		for (Tweet t : at) {
			mChatApplication.newRemoteUserMessage(t.getUser(), t.getMessage());
		}
	}

	private DatabaseHelper dbhelper = null;
	public boolean synced = false;

	public void onDestroy() {
		Log.i(TAG, "onDestroy()");
		mChatApplication = (ChatApplication) getApplication();
		mChatApplication.deleteObserver(this);
		super.onDestroy();
	}

	public static final int DIALOG_JOIN_ID = 0;
	public static final int DIALOG_LEAVE_ID = 1;
	public static final int DIALOG_ALLJOYN_ERROR_ID = 2;
	public static final int DIALOG_GOT_A_ID = 3;

	protected Dialog onCreateDialog(int id) {
		Log.i(TAG, "onCreateDialog()");
		Dialog result = null;
		switch (id) {
		case DIALOG_JOIN_ID: {
			DialogBuilder builder = new DialogBuilder();
			result = builder.createUseJoinDialog(this, mChatApplication);
		}
			break;
		case DIALOG_LEAVE_ID: {
			DialogBuilder builder = new DialogBuilder();
			result = builder.createUseLeaveDialog(this, mChatApplication);
		}
			break;
		case DIALOG_ALLJOYN_ERROR_ID: {
			DialogBuilder builder = new DialogBuilder();
			result = builder.createAllJoynErrorDialog(this, mChatApplication);
		}
			break;
		case DIALOG_GOT_A_ID: {
			DialogBuilder builder = new DialogBuilder();
			result = builder.gotADialog(this, mChatApplication);
			break;
		}
		}
		return result;
	}

	public synchronized void update(Observable o, Object arg) {
		Log.i(TAG, "update(" + arg + ")");
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

		if (qualifier.equals(ChatApplication.GOT_A)) {
			Message message = mHandler.obtainMessage(HANDLE_GOT_A_EVENT);
			mHandler.sendMessage(message);
		}
	}

	private void updateHistory() {
		Log.i(TAG, "updateHistory()");
		mHistoryList.clear();
		List<String> messages = mChatApplication.getHistory();
		for (String message : messages) {
			mHistoryList.add(message);
		}
		mHistoryList.notifyDataSetChanged();
	}

	private void updateChannelState() {
		Log.i(TAG, "updateHistory()");
		AllJoynService.UseChannelState channelState = mChatApplication
				.useGetChannelState();
		String name = mChatApplication.useGetChannelName();
		if (name == null) {
			name = "Not set";
		}
		mChannelName.setText(name);

		switch (channelState) {
		case IDLE:
			mChannelStatus.setText("Idle");
			mJoinButton.setEnabled(true);
			mLeaveButton.setEnabled(false);
			break;
		case JOINED:
			if (!synced)
				mChatApplication.newLocalUserMessage("#!user=" + username
						+ ";type=historyrequest;parameters="
						+ dbhelper.getLastTime());
			mChannelStatus.setText("Joined");
			mJoinButton.setEnabled(false);
			mLeaveButton.setEnabled(true);
			break;
		}
	}

	/**
	 * An AllJoyn error has happened. Since this activity pops up first we
	 * handle the general errors. We also handle our own errors.
	 */
	private void alljoynError() {
		if (mChatApplication.getErrorModule() == ChatApplication.Module.GENERAL
				|| mChatApplication.getErrorModule() == ChatApplication.Module.USE) {
			showDialog(DIALOG_ALLJOYN_ERROR_ID);
		}
	}

	private void gota() {
		showDialog(DIALOG_GOT_A_ID);
	}

	private static final int HANDLE_APPLICATION_QUIT_EVENT = 0;
	private static final int HANDLE_HISTORY_CHANGED_EVENT = 1;
	private static final int HANDLE_CHANNEL_STATE_CHANGED_EVENT = 2;
	private static final int HANDLE_ALLJOYN_ERROR_EVENT = 3;
	private static final int HANDLE_GOT_A_EVENT = 4;

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
				updateChannelState();
				break;
			}
			case HANDLE_ALLJOYN_ERROR_EVENT: {
				Log.i(TAG,
						"mHandler.handleMessage(): HANDLE_ALLJOYN_ERROR_EVENT");
				alljoynError();
				break;
			}
			case HANDLE_GOT_A_EVENT: {
				gota();
				break;
			}
			default:
				break;
			}
		}
	};

	private ChatApplication mChatApplication = null;

	private ArrayAdapter<String> mHistoryList;

	private Button mJoinButton;
	private Button mLeaveButton;

	private TextView mChannelName;

	private TextView mChannelStatus;
}
