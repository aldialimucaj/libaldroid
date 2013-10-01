package al.aldi.andorid.utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Helper Class to access Android SharedPreferences
 * 
 * @author Aldi Alimuçaj
 * 
 */
public class PreferencesUtil {

	/**
	 * Write to prefs as string
	 * 
	 * @param activity
	 * @param store
	 * @param key
	 * @param value
	 */
	public static void writeToPrefs(Activity activity, String store, String key, String value) {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = activity.getSharedPreferences(store, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);

		// Commit the edits!
		editor.commit();
	}

	/**
	 * Write to prefs boolean
	 * 
	 * @param activity
	 * @param store
	 * @param key
	 * @param value
	 */
	public static void writeToPrefs(Activity activity, String store, String key, boolean value) {
		SharedPreferences settings = activity.getSharedPreferences(store, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * Read String
	 * 
	 * @param activity
	 * @param store
	 * @param key
	 * @return
	 */
	public static String readPrefStr(Activity activity, String store, String key) {
		SharedPreferences settings = activity.getSharedPreferences(store, 0);
		return settings.getString(key, null); // return key or null if !exists
	}

	/**
	 * Read Boolean
	 * 
	 * @param activity
	 * @param store
	 * @param key
	 * @return
	 */
	public static boolean readPrefBool(Activity activity, String store, String key) {
		SharedPreferences settings = activity.getSharedPreferences(store, 0);
		return settings.getBoolean(key, false); // return key or false if
												// !exists
	}

	/**
	 * Will clear/delete every element under this key name.
	 * 
	 * @param activity
	 *            needed to access prefs
	 * @param store
	 *            key name
	 * @return
	 */
	public static boolean remove(Activity activity, String store) {
		SharedPreferences settings = activity.getSharedPreferences(store, 0);
		return settings.edit().clear().commit();
	}
}
