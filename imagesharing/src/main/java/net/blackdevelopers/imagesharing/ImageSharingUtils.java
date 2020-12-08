package net.blackdevelopers.imagesharing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class ImageSharingUtils {
    public static final int REQUEST_CODE = 3333;

    public static void shareImageUris(final @NonNull Activity currentActivity,
                                      final ArrayList<Uri> imageUris,
                                      final @NonNull String title) {
        if (imageUris == null) {
            return;
        }
        int listSize = imageUris.size();
        if (listSize <= 0) {
            return;
        }
        Intent shareIntent = new Intent();
        if (listSize == 1) {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUris.get(0));
        } else {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        }
        shareIntent.setType("image/*");


        currentActivity.startActivity(Intent.createChooser(shareIntent, title));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static void shareImageUris(final @NonNull Activity currentActivity,
                                      final ArrayList<Uri> imageUris,
                                      final @NonNull String title,
                                      final @NonNull Class<?> completionReceiverClass) {
        if (imageUris == null) {
            return;
        }
        int listSize = imageUris.size();
        if (listSize <= 0) {
            return;
        }
        Intent shareIntent = new Intent();
        if (listSize == 1) {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUris.get(0));
        } else {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        }
        shareIntent.setType("image/*");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(currentActivity, REQUEST_CODE,
                new Intent(currentActivity,completionReceiverClass), FLAG_UPDATE_CURRENT);
        currentActivity.startActivity(Intent.createChooser(shareIntent, title, pendingIntent.getIntentSender()));
    }

    public static boolean isValidListItemLayout(final @NonNull Context context, @Nullable Integer listItemLayout) {
        if (listItemLayout == null) {
            return true;
        }
        final LayoutInflater layoutInflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams")
        View view = layoutInflater.inflate(listItemLayout,null, false);
        if (view == null) {
            return false;
        }
        try {
            ImageView imgThumbnail = view.findViewById(R.id.imgThumbnail);
            if (imgThumbnail == null) {
                return false;
            }
            CheckBox chkSelected = view.findViewById(R.id.chkSelected);
            if (chkSelected == null) {
                return false;
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static void setClickable(View view, boolean clickable) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    setClickable(viewGroup.getChildAt(i), clickable);
                }
            }
            view.setClickable(clickable);
        }
    }


}
