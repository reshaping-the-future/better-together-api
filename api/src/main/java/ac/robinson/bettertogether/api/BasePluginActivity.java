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

package ac.robinson.bettertogether.api;

import android.os.Bundle;
import android.view.MenuItem;

import ac.robinson.bettertogether.api.messaging.BroadcastMessage;
import ac.robinson.bettertogether.api.messaging.PluginConnectionDelegate;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Extend this activity to create your own plugin. The methods {@link #sendMessage(BroadcastMessage)} and
 * {@link #onMessageReceived(BroadcastMessage)} form the basis of inter-phone communication.
 */
public abstract class BasePluginActivity extends AppCompatActivity {

	private PluginConnectionDelegate mDelegate;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDelegate = new PluginConnectionDelegate(BasePluginActivity.this, mMessageReceivedCallback);
		mDelegate.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		mDelegate.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDelegate.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			// NavUtils.navigateUpFromSameTask(BaseHotspotActivity.this); // only API 16+; requires manifest tag
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private final PluginConnectionDelegate.PluginMessageCallback mMessageReceivedCallback =
			new PluginConnectionDelegate.PluginMessageCallback() {
		@Override
		public void onMessageReceived(@NonNull BroadcastMessage message) {
			BasePluginActivity.this.onMessageReceived(message);
		}
	};

	/**
	 * Used to send messages to all other connected devices.
	 *
	 * @param message the message to send
	 */
	@SuppressWarnings("unused")
	public void sendMessage(@NonNull BroadcastMessage message) {
		mDelegate.sendMessage(message);
	}

	/**
	 * Called whenever a message is received from other clients.
	 *
	 * @param message the message received
	 */
	protected abstract void onMessageReceived(@NonNull BroadcastMessage message);
}
