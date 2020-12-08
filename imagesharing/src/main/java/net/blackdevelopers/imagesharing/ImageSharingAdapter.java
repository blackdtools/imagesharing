package net.blackdevelopers.imagesharing;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ImageSharingAdapter extends RecyclerView.Adapter<ImageSharingAdapter.ItemViewHolder> {

    private int customImageLayout;
    private ArrayList<Uri> uriArrayList = new ArrayList<>();
    private ArrayList<Boolean> selectedList = new ArrayList<>();
    private InterfaceWithImageSharingView listener = null;


    public ImageSharingAdapter(final @NonNull Context context, final ArrayList<Uri> uriArrayList, final @Nullable Integer customImageLayout) {
        setUriArrayList(uriArrayList,false);
        setCustomImageLayout(context,customImageLayout);
    }

    private void setUriArrayList(final ArrayList<Uri> uriArrayList, boolean notify) {
        this.uriArrayList = uriArrayList;
        this.selectedList = new ArrayList<>();
        for (int i = 0; i < this.uriArrayList.size(); i++) {
            this.selectedList.add(false);
        }
        if (notify) {
            this.notifyDataSetChanged();
            if (listener != null) {
                listener.imageSharingViewSelectionChanged(getSelectedUriList());
            }
        }
    }
    public void setUriArrayList(final ArrayList<Uri> uriArrayList) {
        setUriArrayList(uriArrayList,true);
    }

    public ArrayList<Uri> getSelectedUriList() {
        ArrayList<Uri> selectedUriList = new ArrayList<>();
        int lastElement = selectedList.size() - 1;
        for (int i = 0; i <= lastElement; i++) {
            if (selectedList.get(i)) {
                selectedUriList.add(uriArrayList.get(i));
            }
        }
        return selectedUriList;
    }

    @Override
    public @NonNull
    ImageSharingAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(customImageLayout, parent, false);
        return new ItemViewHolder(this,view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.uriArrayList.size();
    }

    public void setListener(InterfaceWithImageSharingView listener) {
        this.listener = listener;
    }

    public void setCustomImageLayout(final @NonNull Context context, final @Nullable Integer listItemLayout) {
        if (listItemLayout == null) {
            this.customImageLayout = R.layout.image_sharing_view_default_image_layout;
        } else {
           if (ImageSharingUtils.isValidListItemLayout(context, listItemLayout)) {
               this.customImageLayout = listItemLayout;
            } else {
               this.customImageLayout = R.layout.image_sharing_view_default_image_layout;
           }
        }
    }

    public void selectAll() {
        if (this.uriArrayList == null) {
            return;
        }
        boolean somethingChanged = false;
        for (int i = 0; i < this.uriArrayList.size(); i++) {
            if (!this.selectedList.get(i)) {
                somethingChanged=true;
                this.selectedList.set(i, true);
            }
        }
        if (somethingChanged) {
            this.notifyDataSetChanged();
            notifyChangesToListener();
        }
    }

    public void clearSelection() {
        if (this.uriArrayList == null) {
            return;
        }
        boolean somethingChanged = false;
        for (int i = 0; i < this.uriArrayList.size(); i++) {
            if (this.selectedList.get(i)) {
                somethingChanged=true;
                this.selectedList.set(i, false);
            }
        }
        if (somethingChanged) {
            this.notifyDataSetChanged();
            notifyChangesToListener();
        }
    }

    public void setSelectedStatusForPosition(int position, boolean newValue) {
        selectedList.set(position,newValue);
        notifyChangesToListener();
    }

    private void notifyChangesToListener() {
        if (listener != null) {
            listener.imageSharingViewSelectionChanged(getSelectedUriList());
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View layImageItem;
        public final ImageView imgThumbnail;
        public final CheckBox chkSelected;
        private final ImageSharingAdapter adapter;

        ItemViewHolder(final @NonNull ImageSharingAdapter adapter, final @NonNull View view) {
            super(view);
            this.layImageItem = view;
            imgThumbnail = view.findViewById(R.id.imgThumbnail);
            chkSelected = view.findViewById(R.id.chkSelected);
            this.adapter = adapter;
        }

        public void bind(int currentPosition) {
            chkSelected.setOnCheckedChangeListener((compoundButton, b) -> adapter.setSelectedStatusForPosition(currentPosition,b));
            imgThumbnail.setOnClickListener(view -> {
                if (listener != null) {
                    listener.imageSharingViewUserWantFullPhoto(uriArrayList.get(currentPosition));
                }
            });
            Context appContext = this.layImageItem.getContext().getApplicationContext();
            chkSelected.setChecked(selectedList.get(currentPosition));
            int thumbnailSize = appContext.getResources().getDimensionPixelSize(R.dimen._100sdp);
            Uri uriToShow = uriArrayList.get(currentPosition);
            if (uriToShow.getScheme().equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
                final String resourceIdString = uriToShow.getLastPathSegment();
                try {
                    final int resourceId = Integer.parseInt(resourceIdString);
                    imgThumbnail.setImageResource(resourceId);
                }
                catch (Exception ignored) {
                }
            } else {
                Picasso.get()
                        .load(uriToShow)
                        .centerInside()
                        .resize(thumbnailSize, thumbnailSize)
                        .into(imgThumbnail);
            }
        }
    }
}
