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

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import ac.robinson.bettertogether.api.messaging.PluginIntent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity is launched if the user tries to start a plugin directly (e.g., from Google Play). If Better Together is
 * installed, it is opened; otherwise, the Play Store is launched and directed to the Better Together download page.
 */
public class DefaultActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_NoDisplay); // try to avoid any visual appearance of this activity

		Intent intent;
		boolean betterTogetherIsInstalled = betterTogetherIsInstalled();
		if (betterTogetherIsInstalled) {
			// TODO: add flags to indicate we are coming from a specific plugin?
			intent = getPackageManager().getLaunchIntentForPackage(PluginIntent.HOST_PACKAGE);
			if (intent != null) {
				intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
			}
		} else {
			intent = new Intent(Intent.ACTION_VIEW, PluginIntent.MARKET_HOST_APP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}

		boolean error = intent == null;
		if (!error) {
			try {
				startActivity(intent);
				if (!betterTogetherIsInstalled) {
					Toast.makeText(DefaultActivity.this, R.string.bt_hint_install_better_together, Toast.LENGTH_SHORT).show();
				}
			} catch (Exception ignored) {
				error = true;
			}
		}

		if (error) {
			// probably not much else we can do
			Toast.makeText(DefaultActivity.this, R.string.bt_hint_launch_error, Toast.LENGTH_SHORT).show();
		}

		finish();
		super.onCreate(savedInstanceState);
	}

	private boolean betterTogetherIsInstalled() {
		PackageManager packageManager = getPackageManager();
		// TODO: double-check package visibility filtering (https://developer.android.com/training/package-visibility)
		final List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		for (final PackageInfo packageInfo : installedPackages) {
			if (PluginIntent.HOST_PACKAGE.equals(packageInfo.packageName)) {
				return true;
			}
		}
		return false;
	}
}
