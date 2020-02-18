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

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import ac.robinson.bettertogether.api.messaging.BroadcastMessage;
import ac.robinson.bettertogether.api.messaging.PluginConnectionDelegate;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * For fragment-based apps, extend this fragment to create your own plugin. As in {@link BasePluginActivity}, the methods
 * {@link #sendMessage(BroadcastMessage)} and {@link #onMessageReceived(BroadcastMessage)} form the basis of inter-phone
 * communication.
 */
@SuppressWarnings("unused")
public abstract class BasePluginFragment extends Fragment {
	private PluginConnectionDelegate mDelegate;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Activity activity = getActivity();
		if (activity == null) {
			// probably not much else we can do
			Toast.makeText(getContext(), R.string.bt_hint_launch_error, Toast.LENGTH_SHORT).show();
			return;
		}

		mDelegate = new PluginConnectionDelegate(getActivity(), mMessageReceivedCallback);
		mDelegate.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState) {
		mDelegate.onSaveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mDelegate.onDestroy();
	}

	private final PluginConnectionDelegate.PluginMessageCallback mMessageReceivedCallback =
			new PluginConnectionDelegate.PluginMessageCallback() {
		@Override
		public void onMessageReceived(@NonNull BroadcastMessage message) {
			BasePluginFragment.this.onMessageReceived(message);
		}
	};

	/**
	 * Used to send messages to all other connected devices.
	 *
	 * @param message the message to send
	 */
	@SuppressWarnings("WeakerAccess")
	public void sendMessage(@NonNull BroadcastMessage message) {
		mDelegate.sendMessage(message);
	}

	/**
	 * Called whenever a message is received from other clients.
	 *
	 * @param message the message received
	 */
	@SuppressWarnings("WeakerAccess")
	protected abstract void onMessageReceived(@NonNull BroadcastMessage message);
}
