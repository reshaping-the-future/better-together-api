/*
 * Copyright (C) 2017 The Better Together Toolkit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package ac.robinson.bettertogether.api.messaging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * For more direct integration with your app, use this connection helper class to send and receive messages. You must first
 * initialise, providing a {@link PluginMessageCallback} for message handling, then call {@link #onCreate(Bundle)} to register
 * the helper, {@link #onSaveInstanceState(Bundle)} to handle screen rotation, and {@link #onDestroy()} to clean up.
 * <p>
 * See {@link ac.robinson.bettertogether.api.BasePluginActivity} for a reference implementation.
 */
public class PluginConnectionDelegate {

	private final Context mContext;
	private final PluginMessageCallback mMessageCallback;

	public interface PluginMessageCallback {
		void onMessageReceived(@NonNull BroadcastMessage message);
	}

	public PluginConnectionDelegate(@NonNull Context context, @NonNull PluginMessageCallback callback) {
		mContext = context;
		mMessageCallback = callback;
	}

	public void onCreate(@SuppressWarnings("UnusedParameters") @Nullable Bundle savedInstanceState) {
		// note: savedInstanceState is passed for potential future usage; unused for now
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(PluginIntent.ACTION_STOP_PLUGIN);
		intentFilter.addAction(PluginIntent.ACTION_MESSAGE_RECEIVED);
		LocalBroadcastManager.getInstance(mContext).registerReceiver(mLocalBroadcastReceiver, intentFilter);
	}

	@SuppressWarnings("EmptyMethod")
	public void onSaveInstanceState(@SuppressWarnings("UnusedParameters") Bundle outState) {
		// note: saved state is passed for potential future usage; unused for now
	}

	public void onDestroy() {
		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mLocalBroadcastReceiver);
	}

	public void sendMessage(@NonNull BroadcastMessage message) {
		Intent intent = new Intent();
		intent.setAction(PluginIntent.ACTION_MESSAGE_RECEIVED);
		intent.setClassName(PluginIntent.HOST_PACKAGE, PluginIntent.MESSAGE_RECEIVER);
		intent.putExtra(PluginIntent.KEY_BROADCAST_MESSAGE, message);

		// note: source is only necessary for internal plugins - it may be removed at a later date
		intent.putExtra(PluginIntent.EXTRA_SOURCE, PluginMessageCallback.class.getPackage().toString());

		mContext.sendBroadcast(intent);
	}

	private final BroadcastReceiver mLocalBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// note: source is only necessary for internal plugins - it may be removed at a later date
			if (PluginMessageCallback.class.getPackage().toString().equals(intent.getStringExtra(PluginIntent.EXTRA_SOURCE))) {
				return; // ignore this message - its source is us
			}

			switch (intent.getAction()) {
				case PluginIntent.ACTION_STOP_PLUGIN:
					if (mContext instanceof Activity) {
						((Activity) mContext).finish();
					}
					break;

				case PluginIntent.ACTION_MESSAGE_RECEIVED:
					BroadcastMessage message = (BroadcastMessage) intent.getSerializableExtra(PluginIntent
							.KEY_BROADCAST_MESSAGE);
					mMessageCallback.onMessageReceived(message);
					break;

				default:
					break;
			}
		}
	};
}
