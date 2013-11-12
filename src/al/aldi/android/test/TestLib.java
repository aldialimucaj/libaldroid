package al.aldi.android.test;

import android.app.Activity;
import android.support.v4.view.ViewPager;

public class TestLib {

    /**
     * Simulate swine of fragment sections by setting the current pager item to the next one
     * to the left or right.
     *
     * @param activity Activity under test
     * @param pager Pager object
     * @param direction {@link Direction}
     */
    public static void swipe(final Activity activity, final ViewPager pager, final Direction direction) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                int current = pager.getCurrentItem();
                if (direction == Direction.Right) {
                    if (current > 0) {
                        pager.setCurrentItem(current - 1, true);
                    }
                } else {
                    if (current < pager.getChildCount()) {
                        pager.setCurrentItem(current + 1, true);
                    }
                }
            }
        });
    }

    /**
     * Enum Required for swiping to right or left
     */
    public enum Direction {
        Left, Right
    }
}
