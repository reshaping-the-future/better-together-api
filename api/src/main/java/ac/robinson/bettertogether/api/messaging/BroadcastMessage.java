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

import android.support.annotation.Nullable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * BroadcastMessages are the objects sent between connected devices. To send a message, create an instance of this class, then
 * use the send and receive methods of plugin activity or fragment classes to handle message events.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BroadcastMessage implements Serializable {

	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_ERROR = -1;

	private final static long serialVersionUID = 1;

	public static final Charset CHARSET = Charset.forName("UTF-8");

	private final int mType;
	private final String mMessage;

	private int mIntExtra;

	private String mFrom;
	private boolean mIsSystemMessage;


	/**
	 * A single message to be sent.
	 *
	 * @param type    The type of message - a utility field to help with differentiating between different commands in your
	 *                plugin. For very simple plugins, it may be sufficient to just use {@link #TYPE_DEFAULT} and/or
	 *                {@link #TYPE_ERROR}. Use {@link #getType()} to retrieve this value when receiving a message.
	 * @param message The content of the message itself. Use {@link #getMessage()} to retrieve this value when receiving a
	 *                message.
	 */
	public BroadcastMessage(int type, @Nullable String message) {
		mType = type;
		mMessage = message;
	}

	/**
	 * Get the type of this BroadcastMessage, set when creating this object.
	 *
	 * @return The type of this message.
	 */
	public int getType() {
		return mType;
	}

	/**
	 * Get the message content of this BroadcastMessage.
	 *
	 * @return The message (may be null).
	 */
	@Nullable
	public String getMessage() {
		return mMessage;
	}

	/**
	 * Optionally, a BroadcastMessage may have an integer value as an extra to the message.
	 *
	 * @return The value of the extra, or 0 if not set.
	 */
	public int getIntExtra() {
		return mIntExtra;
	}

	/**
	 * Set the optional integer extra value for this message.
	 *
	 * @param extra The value to set.
	 */
	public void setIntExtra(int extra) {
		mIntExtra = extra;
	}

	public String getFrom() {
		return mFrom;
	}

	public void setFrom(String from) {
		mFrom = from;
	}

	public void setSystemMessage() {
		mIsSystemMessage = true;
	}

	public boolean isSystemMessage() {
		return mIsSystemMessage;
	}

	@SuppressFBWarnings("OBJECT_DESERIALIZATION") // we need to be able to serialize to send objects over the local connection
	public static BroadcastMessage fromString(String message) throws IOException, ClassNotFoundException {
		byte[] data = Base64.decode(message, Base64.DEFAULT);
		ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(data)));
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		return (BroadcastMessage) object;
	}

	public static String toString(BroadcastMessage message) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new GZIPOutputStream(byteArrayOutputStream));
		objectOutputStream.writeObject(message);
		objectOutputStream.close();
		return new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), BroadcastMessage.CHARSET);
	}

	public static List<String> splitEqually(String message, int size) {
		List<String> result = new ArrayList<>((message.length() + size - 1) / size);
		for (int start = 0, length = message.length(); start < length; start += size) {
			result.add(message.substring(start, Math.min(length, start + size)));
		}
		return result;
	}
}
