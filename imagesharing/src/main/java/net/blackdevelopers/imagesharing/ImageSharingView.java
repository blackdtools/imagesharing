package net.blackdevelopers.imagesharing;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class ImageSharingView extends RecyclerView {

    private static final int defaultExampleImageAmount = 12;

    private ImageSharingAdapter imageSharingAdapter;
    private LayoutManager layoutManager;

    public ImageSharingView(@NonNull Context context) {
        super(context);
        init(context, null, R.attr.recyclerViewStyle);
    }

    public ImageSharingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.attr.recyclerViewStyle);
    }

    public ImageSharingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setImageUriList(@NonNull ArrayList<Uri> uriArrayList) {
        imageSharingAdapter.setUriArrayList(uriArrayList);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyle) {
        final LayoutManager xmlLayoutManager = getLayoutManager();
        if (xmlLayoutManager == null) {
            layoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
            this.setLayoutManager(layoutManager);
        } else {
            layoutManager = xmlLayoutManager;
        }
        Integer customImageLayout = null;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.ImageSharingView,
                    defStyle, 0
            );

            try {

                customImageLayout = a.getResourceId(R.styleable.ImageSharingView_listitem, 0);
                if (customImageLayout == 0) {
                    customImageLayout = null;
                }
            } finally {
                a.recycle();
            }
        }
        imageSharingAdapter = new ImageSharingAdapter(getContext(), new ArrayList<>(), customImageLayout);
        this.setAdapter(imageSharingAdapter);
    }

    public boolean setListItemLayout(@Nullable Integer listItemLayout) {
        if (!ImageSharingUtils.isValidListItemLayout(getContext(), listItemLayout)) {
            return false;
        }
        this.setAdapter(null);
        this.setLayoutManager(null);
        imageSharingAdapter.setCustomImageLayout(getContext(), listItemLayout);
        this.setAdapter(imageSharingAdapter);
        this.setLayoutManager(layoutManager);
        imageSharingAdapter.notifyDataSetChanged();
        return true;
    }

    public void selectAll() {
        imageSharingAdapter.selectAll();
    }

    public void clearSelection() {
        imageSharingAdapter.clearSelection();
    }

    public void setListener(InterfaceWithImageSharingView listener) {
        imageSharingAdapter.setListener(listener);
    }

    public ArrayList<Uri> getSelectedUriList() {
        return imageSharingAdapter.getSelectedUriList();
    }
}
