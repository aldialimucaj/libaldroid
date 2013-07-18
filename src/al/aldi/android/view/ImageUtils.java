package al.aldi.android.view;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class ImageUtils {
    @SuppressWarnings("deprecation")
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

    static public Drawable getAndroidDrawable(String pDrawableName){
        int resourceId=Resources.getSystem().getIdentifier(pDrawableName, "drawable", "android");
        if(resourceId==0){
            return null;
        } else {
            return Resources.getSystem().getDrawable(resourceId);
        }
    }
}
