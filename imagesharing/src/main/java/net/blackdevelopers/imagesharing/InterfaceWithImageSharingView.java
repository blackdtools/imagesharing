package net.blackdevelopers.imagesharing;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public interface InterfaceWithImageSharingView {
    void imageSharingViewSelectionChanged(final @NonNull ArrayList<Uri> newSelection);
    void imageSharingViewUserWantFullPhoto(final @NonNull Uri uri);
}
