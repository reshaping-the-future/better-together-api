# Better Together API
[Better Together](https://github.com/reshaping-the-future/better-together) is a platform for flexibly connecting multiple smartphones together to separate common tasks. This project is the API to help develop your own plugins.

A documented [sample plugin](https://github.com/reshaping-the-future/better-together-chat-sample) is also available, showing the basic steps for setting up your own plugin.

## Adding to your project
To add Better Together to your project, add it as a dependency to your `build.gradle`.

```groovy
compile 'ac.robinson.bettertogether:api:1.0.0'
```

## Sending and receiving messages
Message are sent to and received from other devices using `PluginConnectionDelegate`. The simplest way to incorporate this into your project is to extend `BasePluginActivity` or `BasePluginFragment`.

The `sendMessage` and `onMessageReceived` methods handle all communication beteen devices. The onMessageReceived method is called whenever a message is received, with a `BroadcastMessage` parameter containing the message itself. To send a new message, first create a new BroadcastMessage object:

```java
new BroadcastMessage(1, "Hi - this is my message");
```

The constructor for BroadcastMessage takes a message type and message contents (`int` and `String`). There is also an additional `int` extra field – set this using `setIntExtra`. All of these parameters can be retrieved using `getType`, `getMessage` and `getIntExtra`, respectively.

Use the message type field to differentiate between messages when they are received, or to filter commands. If you do not need custom message types, `BroadcastMessage.TYPE_DEFAULT` and `BroadcastMessage.TYPE_ERROR` are available. For an example of more detailed message type use, see the inbuilt [video](https://github.com/reshaping-the-future/better-together/tree/master/plugin-video-base) and [shopping](https://github.com/reshaping-the-future/better-together/tree/master/plugin-shopping) plugins.

Your new message can be sent to all connected devices using `sendMessage`, and will be received in the `onMessageReceived` method of the currently open plugin activity on all connected devices.

## Adding your plugin to Better Together
To allow your plugin to be discovered by the Better Together host app, add the following `intent-filter` to the `Activity` instances you would like to use in your `AndroidManifest.xml`.

```xml
<intent-filter>
    <action android:name="ac.robinson.bettertogether.intent.action.LAUNCH_PLUGIN"/>
</intent-filter>
```

### Required manifest elements
In order to be displayed as a plugin, the application element of your `AndroidManifest.xml` must have both an `android:icon` and an `android:label` parameter.

In order for an individual Activity to be displayed, it must have the parameter `android:exported="true"`. It must also have both an `android:icon` and an `android:label` parameter.

Plugins that do not declare all of these attributes will not be displayed in the Better Together host app. If your plugin does not display, check `logcat` to find the reason in an error message.

For a fully documented example of the configuration options available (including themes and connection configuration), see the sample plugin's [`AndroidManifest.xml`](https://github.com/reshaping-the-future/better-together-chat-sample/blob/master/app/src/main/AndroidManifest.xml).

## Development tips
Plugins are loaded and unloaded dynamically, so you do not need to restart the Better Together host app, or disconnect other devices when developing or deploying updates.

If your plugin does not provide a launcher Activity, you can import the default activity from the plugin API for a better experience when installing plugins from Google Play. When launched, `DefaultActivity` will open Better Together if it is installed. If not, it will open Google Play to the Better Together app's page.

## License
Apache 2.0
