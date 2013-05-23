package al.aldi.android.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class ImageUtils {
    public static void setImageAlpha(ImageView img, int alpha)
    {
        if(Build.VERSION.SDK_INT > 15)
        {
            img.setImageAlpha(alpha);
        }
        else
        {
            img.setAlpha(alpha);
        }
    }
}
