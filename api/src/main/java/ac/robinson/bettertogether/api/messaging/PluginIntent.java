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

import android.net.Uri;

/**
 * <p>
 * These {@link android.content.Intent} parameters are used to ensure that your plugin can be launched by Better Together, and
 * are able to receive messages - see {@link #ACTION_LAUNCH_PLUGIN}.
 * </p>
 * <p>
 * Use the additional properties {@link #EXTRA_PLUGIN_THEME}, {@link #EXTRA_REQUIRES_WIFI} and
 * {@link #EXTRA_REQUIRES_BLUETOOTH} to configure the behaviour of your plugin, and its connectivity requirements.
 * </p>
 */
@SuppressWarnings({ "WeakerAccess", "unused" })
public final class PluginIntent {

	/**
	 * <p>
	 * Your plugin must provide at least one {@link android.app.Activity} (for example, by extending
	 * {@link ac.robinson.bettertogether.api.BasePluginActivity}), and it must contain an intent filter with this action. For
	 * example:
	 * </p>
	 * <pre>
	 *      {@code
	 *      <intent-filter>
	 *      <action android:name="ac.robinson.bettertogether.intent.action.LAUNCH_PLUGIN"/>
	 *      </intent-filter>
	 *      }
	 * </pre>
	 */
	public static final String ACTION_LAUNCH_PLUGIN = "ac.robinson.bettertogether.intent.action.LAUNCH_PLUGIN";

	/**
	 * <p>
	 * Your plugin may use any theme you wish, but the plugin launch screen will use the Better Together theme by default. If
	 * you would like to change this, you can choose any of the Material Design colour schemes by adding a theme meta-data
	 * element to the application tag in your AndroidManifest.xml file.
	 * </p>
	 * <p>
	 * The value should be one of the values at <a href="https://material.io/guidelines/style/color.html">https://material
	 * .io/guidelines/style/color.html</a>, with names in lowercase and underscores replacing spaces. For example:
	 * </p>
	 * <pre>
	 *     {@code
	 *     <meta-data android:name="ac.robinson.bettertogether.intent.extra.PLUGIN_THEME" android:value="deep_purple"/>
	 *     }
	 * </pre>
	 */
	public static final String EXTRA_PLUGIN_THEME = "ac.robinson.bettertogether.intent.extra.PLUGIN_THEME";

	/**
	 * <p>
	 * Better Together attempts to connect to other devices using both Bluetooth and Wifi. If your plugin requires one of these
	 * connection methods for its own purposes, you may add this meta-data element (see also {@link #EXTRA_REQUIRES_BLUETOOTH})
	 * in order to hint to the Better Together host application that it should avoid using this connection method.
	 * </p>
	 * <pre>
	 *     {@code
	 *     <meta-data android:name="ac.robinson.bettertogether.intent.extra.EXTRA_REQUIRES_WIFI" android:value="true"/>
	 *     }
	 * </pre>
	 * <p>
	 * Requiring Wifi in your plugin means the connection will use another method (currently Bluetooth). Requiring Bluetooth
	 * means the connection will use another method (currently Wifi). Requiring both of these methods will lead to undefined
	 * behaviour.
	 * </p>
	 */
	public static final String EXTRA_REQUIRES_WIFI = "ac.robinson.bettertogether.intent.extra.REQUIRES_WIFI";

	/**
	 * <p>
	 * Better Together attempts to connect to other devices using both Bluetooth and Wifi. If your plugin requires one of these
	 * connection methods for its own purposes, you may add this meta-data element (see also {@link #EXTRA_REQUIRES_WIFI})
	 * in order to hint to the Better Together host application that it should avoid using this connection method.
	 * </p>
	 * <pre>
	 *     {@code
	 *     <meta-data android:name="ac.robinson.bettertogether.intent.extra.REQUIRES_BLUETOOTH" android:value="true"/>
	 *     }
	 * </pre>
	 * <p>
	 * Requiring Bluetooth means the connection will use another method (currently Wifi). Requiring Wifi in your plugin means
	 * the connection will use another method (currently Bluetooth). Requiring both of these methods will lead to undefined
	 * behaviour.
	 * </p>
	 */
	public static final String EXTRA_REQUIRES_BLUETOOTH = "ac.robinson.bettertogether.intent.extra.REQUIRES_BLUETOOTH";

	// global intent parameters - for internal use
	public static final String HOST_PACKAGE = "ac.robinson.bettertogether";
	public static final String MESSAGE_RECEIVER = "ac.robinson.bettertogether.api.messaging.PluginMessageReceiver";

	// intent Uris for directing to the Play store (see DefaultActivity) - for internal use
	public static final String MARKET_PACKAGE_QUERY = "market://details?id=";
	public static final Uri MARKET_HOST_APP = Uri.parse(MARKET_PACKAGE_QUERY + HOST_PACKAGE);
	public static final Uri MARKET_PLUGIN_SEARCH = Uri.parse("market://search?q=better%20together%20plugin&c=apps");

	// actions that can be sent to control or update plugins - for internal use
	public static final String ACTION_STOP_PLUGIN = "ac.robinson.bettertogether.intent.action.STOP_PLUGIN";
	public static final String ACTION_MESSAGE_RECEIVED = "ac.robinson.bettertogether.intent.action.MESSAGE_RECEIVED";

	// source key for internal plugins - for internal use
	public static final String EXTRA_SOURCE = "ac.robinson.bettertogether.intent.extra.SOURCE";

	// message type keys for BroadcastMessage types - for internal use
	public static final String KEY_BROADCAST_MESSAGE = "ac.robinson.bettertogether.intent.key.BROADCAST";
	public static final String KEY_SERVICE_MESSAGE = "ac.robinson.bettertogether.intent.key.SERVICE";
}
