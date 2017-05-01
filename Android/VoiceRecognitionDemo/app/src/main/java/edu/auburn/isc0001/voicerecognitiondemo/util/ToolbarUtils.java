package edu.auburn.isc0001.voicerecognitiondemo.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

/**
 * Provides utility methods involving toolbars and menus.
 */
public class ToolbarUtils {
    public static final double ICON_ALPHA_BLACK = 0.6;

    @SuppressWarnings("ConstantConditions") // getSupportActionBar should not require a null check
    public static void init(AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public static void setNavigationIcon(AppCompatActivity activity, Toolbar toolbar,
                                         @DrawableRes int drawableInt, double alpha) {
        try {
            Drawable drawable = ContextCompat.getDrawable(activity, drawableInt);
            drawable.setAlpha((int) (alpha * 255));
            toolbar.setNavigationIcon(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the alpha of all items in the menu to the provided value. Should be called from
     * onCreateOptionsMenu after inflating the menu.
     *
     * @param menu  Menu for the activity
     * @param alpha Alpha to set icons to. Should be between 0 and 1.
     */
    public static void setAllMenuItemsAlpha(Menu menu, double alpha) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            menu.getItem(i).getIcon().setAlpha((int) (alpha * 0xff));
        }
    }
}
