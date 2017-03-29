package com.anandroid.randd.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.ArrayList;

public class UserPrefs {
	public static int totalAmount;
	public static int totalComment ;
	public static boolean isBookmark ;
	public static boolean isShare ;
	private static final String KEY_NOTIFICATIONS = "notifications";

	public static SharedPreferences sp;
	public static String otp;

	private static void init(Context context) {
		sp = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static String getUserId(Context con) {
		init(con);
		return sp.getString("userId", "");
	}

	public static void setUserId(String userId, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("userId", userId);
		edit.commit();
	}
	public static String getAgencyId(Context con) {
		init(con);
		return sp.getString("AgencyId", "");
	}

	public static void setAgencyId(String agencyId, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("AgencyId", agencyId);
		edit.commit();
	}
	public static String getUserUniqueId(Context con) {
		init(con);
		return sp.getString("userUniqueId", "");
	}

	public static void setUserUniqueId(String id, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("userUniqueId", id);
		edit.commit();

	}

	public static String getFname(Context con) {
		init(con);
		return sp.getString("fName", "");
	}

	public static void setFname(String fName, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("fName", fName);
		edit.commit();
	}
	public static String getName(Context con) {
		init(con);
		return sp.getString("Name", "");
	}

	public static void setName(String Name, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("Name", Name);
		edit.commit();
	}

	public static String getLname(Context con) {
		init(con);
		return sp.getString("lName", "");
	}

	public static void setLname(String lName, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("lName", lName);
		edit.commit();
	}

	public static String getEmail(Context con) {
		init(con);
		return sp.getString("emailId", "");
	}

	public static void setEmail(String emailId, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("emailId", emailId);
		edit.commit();
	}

	public static String getDeviceToken(Context con) {
		init(con);
		return sp.getString("deviceToken", "");
	}

	public static void setDeviceToken(String longStr, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("deviceToken", longStr);
		edit.commit();
	}

	public static String getProfileImage(Context con) {
		init(con);
		return sp.getString("imageUrl", "");
	}

	public static void setProfileImage(String imageUrl, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("imageUrl", imageUrl);
		edit.commit();
	}

	public static String getHeaderImage(Context con) {
		init(con);
		return sp.getString("imageHeaderUrl", "");
	}

	public static void setHeaderImage(String imageHeaderUrl, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("imageHeaderUrl", imageHeaderUrl);
		edit.commit();
	}
	public static void setMobile(String mobile, Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("mobile", mobile);
		edit.commit();
	}
	public static String getMobile(Context con) {
		init(con);
		return sp.getString("mobile", "");

	}



	public static void resetPrefs(Context con) {
		init(con);
		Editor edit = sp.edit();
		edit.putString("deviceToken", "");
		edit.putString("mobile", "");
		edit.putString("Name", "");
		edit.putString("userId", "");
		edit.putString("userUniqueId", "");
		edit.putString("imageUrl", "");
		edit.commit();
	}
	public static void updatePrefs(Context con,ArrayList<String> data)
	{
		init(con);
		Editor edit = sp.edit();
	    edit.putString("Name", data.get(0));
		edit.putString("userUniqueId", data.get(1));
		edit.putString("imageUrl", data.get(2));
		edit.putString("mobile",data.get(3));
		edit.commit();
	}
	public void addNotification(String notification) {

		// get old notifications
		String oldNotifications = getNotifications();

		if (oldNotifications != null) {
			oldNotifications += "|" + notification;
		} else {
			oldNotifications = notification;
		}
		Editor edit = sp.edit();
		edit.putString(KEY_NOTIFICATIONS, oldNotifications);
		edit.commit();
	}

	public String getNotifications() {
		return sp.getString(KEY_NOTIFICATIONS, null);
	}

	public void clear() {
		Editor edit = sp.edit();
		edit.clear();
		edit.commit();
	}


}
