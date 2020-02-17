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

import android.content.BroadcastReceiver;
import android.content.Context;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * This class exists so we can direct our broadcast messages at a specific app-related receiver (for privacy/security), rather
 * than sending them to all receivers. All it does is forward messages to clients via a LocalBroadcastManager.
 */
public class PluginMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, android.content.Intent intent) {
		String action = intent.getAction();
		if (action == null) {
			return;
		}
		switch (action) {
			case PluginIntent.ACTION_STOP_PLUGIN:
				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
				break;

			case PluginIntent.ACTION_MESSAGE_RECEIVED:
				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
				break;

			default:
				break;
		}
	}
}
