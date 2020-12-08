package net.blackdevelopers.imagesharingtest;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class ExampleApp extends MultiDexApplication {

    public static String[] getRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[] {};
        } else {
            return new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
