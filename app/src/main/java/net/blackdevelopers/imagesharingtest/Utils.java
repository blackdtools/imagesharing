package net.blackdevelopers.imagesharingtest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class Utils {
    public static class System {
        private static final int PERMISSION_CHECK = 2349;

        public static boolean hasPermissions(@NonNull final Context context,
                                             @NonNull String... permissions) {
            if (Build.VERSION.SDK_INT < 23) {
                return true;
            }
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }

        public static void requestPermissions(@NonNull final Activity activity,
                                              @NonNull String... permissions) {
            ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CHECK);
        }


        public static boolean checkPermissionsResult(int requestCode,
                                                     @NonNull int[] grantResults) {
            if (requestCode == PERMISSION_CHECK) {
                for (int res : grantResults) {
                    if (res == PackageManager.PERMISSION_DENIED) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        public static void showSimpleToast(@NonNull Context appContext, @NonNull String string) {
            Toast toast = Toast.makeText(appContext, string, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

    }

    public static class Uri {
        public static String getPersistableUriAsString(@NonNull Context appContext, @NonNull android.net.Uri rawUri, final int takeFlags) {
            appContext.grantUriPermission(appContext.getPackageName(), rawUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            ContentResolver resolver = appContext.getContentResolver();
            resolver.takePersistableUriPermission(rawUri, takeFlags);
            return String.valueOf(rawUri);
        }
    }

    public static class Preferences {
        private static final String SHARED_PREFS = "prefs";

        public static void saveString(@NonNull final Context context, @NonNull final String key, final String value) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }

        public static String loadString(@NonNull final Context context, @NonNull final String key, final String defaultValue) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, defaultValue);
        }
    }
}
