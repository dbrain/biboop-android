package com.biboop.android.fragment;

import android.app.Activity;
import android.graphics.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.biboop.android.R;
import com.biboop.android.util.Preferences;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import static com.biboop.android.util.BitmapUtils.getRoundedBitmap;

public class MenuFragment extends SherlockFragment {
    private Preferences prefs;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        prefs = Preferences.getPreferences(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View menuView = inflater.inflate(R.layout.menu_fragment, container);
        TextView userFullName = (TextView)menuView.findViewById(R.id.user_full_name);
        final ImageView userAvatar = (ImageView)menuView.findViewById(R.id.avatar);

        userFullName.setText(prefs.getUserFullName());
        if (prefs.getUserAvatarUrl() != null) {
            ImageLoader.getInstance().loadImage(prefs.getUserAvatarUrl(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    userAvatar.setImageBitmap(getRoundedBitmap(loadedImage));
                }
            });
        }

        return menuView;
    }
}
