package net.blackdevelopers.imagesharing;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import net.blackdevelopers.imagesharing.databinding.ImageSharingActivityBinding;

import java.util.ArrayList;

public class ImageSharingActivity extends AppCompatActivity implements InterfaceWithImageSharingView {

    private static final String INTENT_PARAM_URI_LIST = "uriList";
    private ArrayList<Uri> uriList = new ArrayList<>();
    private ImageSharingActivityBinding binding;
    private Menu currentMenu;
    private int currentSelectionCount = 0;
    private Uri currentFullImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uriList = bundle.getParcelableArrayList(INTENT_PARAM_URI_LIST);
        }
        binding = ImageSharingActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.isvInstance.setImageUriList(uriList);
        binding.isvInstance.setListener(this);
        setSupportActionBar(binding.toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        }
        currentSelectionCount = 0;
        changeTitle(currentSelectionCount);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_sharing_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        currentMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            backpressed();
        } else if (itemId == R.id.btnShare) {
            shareSelection();
        } else if (itemId == R.id.btnSelectAll) {
            selectAll();
        } else if (itemId == R.id.btnClearSelection) {
            clearSelection();
        } else if (itemId == R.id.btnCloseImageDetail) {
            backpressed();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        backpressed();
    }

    private void backpressed() {
        if (binding.imgFull.getVisibility() == View.VISIBLE) {
            switchListEnabled(true);
        } else {
            finish();
        }
    }

    private void clearSelection() {
        binding.isvInstance.clearSelection();
    }

    private void selectAll() {
        binding.isvInstance.selectAll();
    }

    @Override
    public void imageSharingViewSelectionChanged(final @NonNull ArrayList<Uri> newSelection) {
        currentSelectionCount = newSelection.size();
        changeTitle(currentSelectionCount);
    }

    private void changeTitle(final Integer selectedCount) {
        if (selectedCount == null) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("");
            }
        } else {
            int selectedCountNotNull = selectedCount;
            String newTitle = String.format(getString(R.string.imagesharing_title), selectedCountNotNull);
            Log.e("TESTX", "newTitle=" + newTitle);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(newTitle);
            }
        }
    }

    private void switchListEnabled(final boolean enabled) {
        ImageSharingUtils.setClickable(binding.isvInstance, enabled);
        binding.isvInstance.suppressLayout(!enabled);
        if (enabled) {
            binding.imgFull.setVisibility(View.INVISIBLE);
        } else {
            binding.imgFull.setImageResource(android.R.color.transparent);
            binding.imgFull.setVisibility(View.VISIBLE);
        }
        if (currentMenu != null) {
            MenuItem btnSelectAll = currentMenu.findItem(R.id.btnSelectAll);
            btnSelectAll.setVisible(enabled);
            MenuItem btnClearSelection = currentMenu.findItem(R.id.btnClearSelection);
            btnClearSelection.setVisible(enabled);
            MenuItem btnCloseImageDetail = currentMenu.findItem(R.id.btnCloseImageDetail);
            btnCloseImageDetail.setVisible(!enabled);
        }
        if (enabled) {
            changeTitle(currentSelectionCount);
        } else {
            changeTitle(null);
        }
    }

    @Override
    public void imageSharingViewUserWantFullPhoto(final @NonNull Uri uriToShow) {
        currentFullImage = uriToShow;
        switchListEnabled(false);
        if (uriToShow.getScheme().equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
            final String resourceIdString = uriToShow.getLastPathSegment();
            try {
                final int resourceId = Integer.parseInt(resourceIdString);
                binding.imgFull.setImageResource(resourceId);
            } catch (Exception ignored) {
            }
        } else {
            Picasso.get()
                    .load(uriToShow)
                    .into(binding.imgFull);
        }
    }

    public static void start(Activity fromActivity, ArrayList<Uri> uriList) {
        Intent intent = new Intent(fromActivity, ImageSharingActivity.class);
        intent.putExtra(INTENT_PARAM_URI_LIST, uriList);
        fromActivity.startActivity(intent);
    }

    private void shareSelection() {
        ArrayList<Uri> selectedUriList;
        if (binding.imgFull.getVisibility() == View.VISIBLE) {
            selectedUriList = new ArrayList<>();
            selectedUriList.add(currentFullImage);
        } else {
            selectedUriList = binding.isvInstance.getSelectedUriList();
        }
        if (selectedUriList.size() == 0) {
            showSimpleToast(getString(R.string.imagesharing_no_selection_warning));
        } else {
            ImageSharingUtils.shareImageUris(this,
                    selectedUriList,
                    getString(R.string.imagesharing_sample_sharing_images));
        }
    }

    private void showSimpleToast(@NonNull String string) {
        Toast toast = Toast.makeText(this, string, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
