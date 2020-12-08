package net.blackdevelopers.imagesharingtest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import net.blackdevelopers.imagesharing.ImageSharingActivity;
import net.blackdevelopers.imagesharingtest.databinding.MainActivityBinding;
import java.util.ArrayList;

public class MainActivity extends Activity  {
    private static final int PICK_PHOTOS = 1456;
    private static final String URI_LIST = "URI_LIST";
    private static final String URI_LIST_DELIMITER = "!";
    public MainActivityBinding binding;

   // private int selectedCount = 0;
    private ArrayList<Uri> uriList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        uriList = loadArrayList();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (Utils.System.checkPermissionsResult(requestCode, grantResults)) {
            btnPickImages_onClick(null);
        }
    }

    public void btnPickImages_onClick(View view) {
        final String[] requiredPermissions = ExampleApp.getRequiredPermissions();
        if (!Utils.System.hasPermissions(this, requiredPermissions)) {
            Utils.System.requestPermissions(this, requiredPermissions);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivityForResult(Intent.createChooser(intent, this.getString(R.string.imagesharing_sample_pick_title)), PICK_PHOTOS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == PICK_PHOTOS) && (resultCode == Activity.RESULT_OK)) {
            ArrayList<Uri> resultList = getSelectedPhotoUrisFromData(data);
            if (!resultList.isEmpty()) {
                saveArrayList(resultList);
                uriList=resultList;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveArrayList(ArrayList<Uri> resultList) {
        StringBuilder singleString = new StringBuilder();
        for (Uri uri : resultList) {
            if (!singleString.toString().equals("")) {
                singleString.append(URI_LIST_DELIMITER);
            }
            singleString.append(uri);
        }
        String singleStringToSave = singleString.toString();
        Utils.Preferences.saveString(this, URI_LIST, singleStringToSave);
    }

    private ArrayList<Uri> loadArrayList() {
        String listAsString = Utils.Preferences.loadString(this, URI_LIST, "");
        ArrayList<Uri> result = new ArrayList<>();
        if (!listAsString.isEmpty()) {
            String[] listAsArray = listAsString.split(URI_LIST_DELIMITER);
            for (String uriAsString : listAsArray) {
                if (!uriAsString.isEmpty()) {
                    result.add(Uri.parse(uriAsString));
                }
            }
        }
        return result;
    }

    private ArrayList<Uri> getSelectedPhotoUrisFromData(Intent data) {
        ArrayList<Uri> resultList = new ArrayList<>();
        if (data == null) {
            return resultList;
        }
        Uri rawUri;
        String goodUriAsString;
        ClipData clipData;
        try {
            final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            clipData = data.getClipData();
            if (clipData == null) {
                rawUri = data.getData();
                goodUriAsString = Utils.Uri.getPersistableUriAsString(this, rawUri, takeFlags);
                resultList.add(Uri.parse(goodUriAsString));
            } else {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    rawUri = item.getUri();
                    goodUriAsString = Utils.Uri.getPersistableUriAsString(this, rawUri, takeFlags);
                    resultList.add(Uri.parse(goodUriAsString));
                }
            }
        } catch (Exception ignored) {
            Utils.System.showSimpleToast(this, this.getString(R.string.imagesharing_sample_pick_error));
            return resultList;
        }
        return resultList;
    }

    /*
    @Override
    public void imageSharingViewSelectionChanged(ArrayList<Uri> newSelection) {
        int selectedCount=newSelection.size();
        binding.btnShareSelection.setEnabled(selectedCount != 0);
        final String newText = getString(R.string.imagesharing_sample_share_selection) + " (" + selectedCount + ")";
        binding.btnShareSelection.setText(newText);
    }

    @Override
    public void imageSharingViewUserWantFullPhoto(Uri uri) {

    }

    public void btnSelectAll_onClick(View view) {
        binding.isvExampleObject.selectAll();
    }

    public void btnSelectNone_onClick(View view) {
        binding.isvExampleObject.clearSelection();
    }

    public void btnShareSelection_onClick(View view) {
        ImageSharingUtils.shareImageUris(this,
                uriList,
                getString(R.string.imagesharing_sample_sharing_images),
                MyBroadcastReceiver.class);
    }


     */
    public void btnLaunchImageSharingActivity_onClick(View view) {
        ImageSharingActivity.start(this, uriList);
    }
}