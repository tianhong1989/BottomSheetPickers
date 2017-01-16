package com.philliphsu.bottomsheetpickers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.support.v4.content.ContextCompat.getColor;

/**
 * Created by Phillip Hsu on 10/4/2016.
 */
// TODO: Consider renaming this to BasePickerDialog(Fragment?) and extend from AppCompatDialogFragment.
public abstract class BottomSheetPickerDialog extends BottomSheetDialogFragment {

    // TODO: Other classes in this package may need these? If not, make private.
    protected static final String KEY_DARK_THEME = "dark_theme";
    protected static final String KEY_THEME_SET_AT_RUNTIME = "theme_set_at_runtime";

    private static final String KEY_ACCENT_COLOR = "accent_color";
    private static final String KEY_ACCENT_COLOR_SET_AT_RUNTIME = "accent_color_set_at_runtime";
    private static final String KEY_BACKGROUND_COLOR = "background_color";
    private static final String KEY_BACKGROUND_COLOR_SET_AT_RUNTIME = "background_color_set_at_runtime";
    private static final String KEY_HEADER_COLOR = "header_color";
    private static final String KEY_HEADER_COLOR_SET_AT_RUNTIME = "header_color_set_at_runtime";

    // TODO: Other classes in this package may need this? If not, make private.
    // TODO: Remove duplicates in time picker classes.
    protected boolean mThemeDark;
    protected boolean mThemeSetAtRuntime;
    protected boolean mAccentColorSetAtRuntime;
    protected boolean mBackgroundColorSetAtRuntime;
    protected boolean mHeaderColorSetAtRuntime;

    // TODO: Remove retrievals of these values in time picker classes.
    protected int mDarkGray;
    protected int mLightGray;
    protected int mWhite;
    protected int mAccentColor;
    protected int mBackgroundColor;
    protected int mHeaderColor;

    @LayoutRes
    protected abstract int contentLayout();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mThemeDark = savedInstanceState.getBoolean(KEY_DARK_THEME);
            mThemeSetAtRuntime = savedInstanceState.getBoolean(KEY_THEME_SET_AT_RUNTIME);
            mAccentColor = savedInstanceState.getInt(KEY_ACCENT_COLOR);
            mAccentColorSetAtRuntime = savedInstanceState.getBoolean(KEY_ACCENT_COLOR_SET_AT_RUNTIME);
            mBackgroundColor = savedInstanceState.getInt(KEY_BACKGROUND_COLOR);
            mBackgroundColorSetAtRuntime = savedInstanceState.getBoolean(KEY_BACKGROUND_COLOR_SET_AT_RUNTIME);
            mHeaderColor = savedInstanceState.getInt(KEY_HEADER_COLOR);
            mHeaderColorSetAtRuntime = savedInstanceState.getBoolean(KEY_HEADER_COLOR_SET_AT_RUNTIME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!mThemeSetAtRuntime) {
            mThemeDark = Utils.isDarkTheme(getActivity(), mThemeDark);
        }

        final Context ctx = getActivity();
        mDarkGray = getColor(ctx, R.color.dark_gray);
        mLightGray = getColor(ctx, R.color.light_gray);
        mWhite = getColor(ctx, android.R.color.white);
        if (!mAccentColorSetAtRuntime) {
            mAccentColor = Utils.getThemeAccentColor(getActivity());
        }
        if (!mBackgroundColorSetAtRuntime) {
            mBackgroundColor = mThemeDark? mDarkGray : mWhite;
        }

        View view = inflater.inflate(contentLayout(), container, false);
        // Set background color of entire view
        // TODO: Remove setting of this in subclasses.
        view.setBackgroundColor(mBackgroundColor);

        return view;
    }

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO: Pass in a dark/light BottomSheetDialog theme depending on the theme set.
        // Verify this changes colors for text, selectableBackground, etc. appropriately.
        return new CustomWidthBottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
    }

    // TODO: Time picker subclasses need to call up to super!
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_DARK_THEME, mThemeDark);
        outState.putBoolean(KEY_THEME_SET_AT_RUNTIME, mThemeSetAtRuntime);
        outState.putInt(KEY_ACCENT_COLOR, mAccentColor);
        outState.putBoolean(KEY_ACCENT_COLOR_SET_AT_RUNTIME, mAccentColorSetAtRuntime);
        outState.putInt(KEY_BACKGROUND_COLOR, mBackgroundColor);
        outState.putBoolean(KEY_BACKGROUND_COLOR_SET_AT_RUNTIME, mBackgroundColorSetAtRuntime);
        outState.putInt(KEY_HEADER_COLOR, mHeaderColor);
        outState.putBoolean(KEY_HEADER_COLOR_SET_AT_RUNTIME, mHeaderColorSetAtRuntime);
    }

    // TODO: Delete time picker subclasses' implementation.
    /**
     * Set a dark or light theme. NOTE: this will only take effect for the next onCreateView.
     */
    public void setThemeDark(boolean dark) {
        mThemeDark = dark;
        mThemeSetAtRuntime = true;
    }

    // TODO: Delete time picker subclasses' implementation.
    public boolean isThemeDark() {
        return mThemeDark;
    }

    /**
     * Set the accent color. This color is primarily used to tint views in the picker.
     * If this picker is using the light theme and you did not call {@link #setHeaderColor(int)},
     * this color will also be applied to the dialog's header.
     */
    public final void setAccentColor(@ColorInt int color) {
        mAccentColor = color;
        mAccentColorSetAtRuntime = true;
    }

    /**
     * Set the background color. If this color is dark, consider calling
     * {@link #setThemeDark(boolean)} to ensure text in the picker is visible.
     */
    public final void setBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
        mBackgroundColorSetAtRuntime = true;
    }

    /**
     * Set the header color. This will normally be your Activity's {@code colorAccent}
     * or the accent color set with {@link #setAccentColor(int)}, if this picker is using 
     * the light theme.
     */
    public final void setHeaderColor(@ColorInt int color) {
        mHeaderColor = color;
        mHeaderColorSetAtRuntime = true;
    }
}
