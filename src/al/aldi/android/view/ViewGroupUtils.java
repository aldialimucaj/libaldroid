package al.aldi.android.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * User: Aldi Alimucaj
 * Date: 12.10.13
 * Time: 14:28
 * <p/>
 * Utils about taking care of view and viewgroup objects.
 */


public class ViewGroupUtils {

    /**
     * Return the parent if it registered and not null
     * @param view
     * @return parent or null
     */
    public static ViewGroup getParent(View view) {
        if (null == view) {
            return null;
        }
        return (ViewGroup) view.getParent();
    }

    /**
     * Removes view if registered.
     * @param view
     */
    public static void removeView(View view) {
        ViewGroup parent = getParent(view);
        if (parent != null) {
            parent.removeView(view);
        }
    }

    /**
     * Removes view if registered.
     * @param view
     */
    public static void addView(ViewGroup parent, View view) {
        parent.addView(view);
    }

    /**
     * Replaces currentView with newView.
     *
     * @param currentView
     * @param newView
     */
    public static void replaceView(View currentView, View newView) {
        ViewGroup parent = getParent(currentView);
        if (parent == null) {
            return;
        }
        final int index = parent.indexOfChild(currentView);
        removeView(currentView);
        removeView(newView);
        parent.addView(newView, index);
    }

    public static int getIndex(View currentView) {
        int index = -1;
        ViewGroup parent = getParent(currentView);
        if (parent != null) {
            return parent.indexOfChild(currentView);
        }
        return  index;
    }
}