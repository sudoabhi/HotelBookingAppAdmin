package com.example.hotelloadmin.HomePackage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import java.io.File;

//This class selects photo and crops it and then gives the URI back to the activity that starts it.
public class PhotoSelector extends AppCompatActivity implements UCropFragmentCallback {

    String activity;
    ImageView image;

    TextView title;
    TextView aspectT;

    AlertDialog.Builder builder;

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    private Toolbar toolbar;
    private FrameLayout photoSelector;

    private UCropFragment fragment;
    private boolean mShowLoader;


    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    private AlertDialog mAlertDialog;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);


        title=findViewById(R.id.title);
        aspectT=findViewById(R.id.textView2);

        photoSelector = findViewById(R.id.photo_selector);

        Intent i=getIntent();
        activity=i.getStringExtra("Activity");

        if(activity.equals("ProfilePic")){
            title.setText("Club Profile Photo");
            aspectT.append("1:1 )");
        }
        if(activity.equals("OrganiserPic")){
            title.setText("Organiser Photo");
            aspectT.append("7:8 )");
        }
        if(activity.equals("EventPic")){
            title.setText("Event banner");
            aspectT.append("7:8 )");
        }
        if(activity.equals("EditEventPic")){
            title.setText("Event banner");
            aspectT.append("7:8 )");
        }
        if(activity.equals("EditOrganiser")){
            title.setText("Organiser Photo");
            aspectT.append("7:8 )");
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PhotoSelector.this, R.style.AlertDialogTheme);
        } else {
            builder = new AlertDialog.Builder(PhotoSelector.this);
        }
        builder.setTitle("Add photo")
                .setMessage("Choose photo from")
                .setNegativeButton(R.string.GALLERY, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        pickFromGallery();
                    }
                })
                .show();



        image = (ImageView) findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(PhotoSelector.this, R.style.AlertDialogTheme);
                } else {
                    builder = new AlertDialog.Builder(PhotoSelector.this);
                }
                builder.setTitle("Add photo")
                        .setMessage("Choose photo from")
                        .setNegativeButton(R.string.GALLERY, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                pickFromGallery();
                            }
                        })
                        .show();
            }
        });

    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PhotoSelector.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }


    private void pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                    .setType("image/*")
                    .addCategory(Intent.CATEGORY_OPENABLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }

            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                Uri selectedUri = imageReturnedIntent.getData();
                if (selectedUri != null) {
                    startCrop(selectedUri);
                } else {
                    Toast.makeText(PhotoSelector.this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(imageReturnedIntent);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(imageReturnedIntent);
        }

    }



    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;
        destinationFileName += ".jpg";


        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.start(PhotoSelector.this);

    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {

        if(activity.equals("ProfilePic")) {
            uCrop = uCrop.withAspectRatio(1, 1);
            uCrop = uCrop.withMaxResultSize(800, 800);

        }
        else if(activity.equals("OrganiserPic")) {
            uCrop = uCrop.withAspectRatio(7, 8);
            uCrop = uCrop.withMaxResultSize(700, 800);

        }
        else if(activity.equals("EventPic")) {
            uCrop = uCrop.withAspectRatio(7, 8);
            uCrop = uCrop.withMaxResultSize(700, 800);

        }
        else if(activity.equals("EditEventPic")) {
            uCrop = uCrop.withAspectRatio(7, 8);
            uCrop = uCrop.withMaxResultSize(700, 800);

        }
        else if(activity.equals("EditOrganiser")) {
            uCrop = uCrop.withAspectRatio(7, 8);
            uCrop = uCrop.withMaxResultSize(700, 800);

        }
        else {
            Log.e("Else is run","sahasjh");
            uCrop = uCrop.withAspectRatio(7, 8);
        }
        /*if (mCheckBoxMaxSize.isChecked()) {
            try {
                int maxWidth = Integer.valueOf(mEditTextMaxWidth.getText().toString().trim());
                int maxHeight = Integer.valueOf(mEditTextMaxHeight.getText().toString().trim());
                if (maxWidth > UCrop.MIN_SIZE && maxHeight > UCrop.MIN_SIZE) {
                    uCrop = uCrop.withMaxResultSize(maxWidth, maxHeight);
                }
            } catch (NumberFormatException e) {
                Log.e("asaa", "Number please", e);
            }
        }*/

        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        //options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        //options.setCompressionQuality(mSeekBarQuality.getProgress());
        options.setCompressionQuality(100);
        //options.setHideBottomControls(mCheckBoxHideBottomControls.isChecked());
        //options.setFreeStyleCropEnabled(mCheckBoxFreeStyleCrop.isChecked());

        /*
        If you want to configure how gestures work for all UCropActivity tabs

        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        * */

        /*
        This sets max size for bitmap that will be decoded from source Uri.
        More size - more memory allocation, default implementation uses screen diagonal.

        options.setMaxBitmapSize(640);
        * */


       /*

        Tune everything (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧

        options.setMaxScaleMultiplier(5);
        options.setImageToCropBoundsAnimDuration(666);
        options.setDimmedLayerColor(Color.CYAN);
        options.setCircleDimmedLayer(true);
        options.setShowCropFrame(false);
        options.setCropGridStrokeWidth(20);
        options.setCropGridColor(Color.GREEN);
        options.setCropGridColumnCount(2);
        options.setCropGridRowCount(1);
        options.setToolbarCropDrawable(R.drawable.your_crop_icon);
        options.setToolbarCancelDrawable(R.drawable.your_cancel_icon);

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.your_color_res));
        options.setRootViewBackgroundColor(ContextCompat.getColor(this, R.color.your_color_res));

        // Aspect ratio options
        options.setAspectRatioOptions(1,
            new AspectRatio("WOW", 1, 2),
            new AspectRatio("MUCH", 3, 4),
            new AspectRatio("RATIO", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
            new AspectRatio("SO", 16, 9),
            new AspectRatio("ASPECT", 1, 1));

       */

        return uCrop.withOptions(options);
    }

    @Override
    public void loadingProgress(boolean showLoader) {
        mShowLoader = showLoader;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        switch (result.mResultCode) {
            case RESULT_OK:
                handleCropResult(result.mResultData);
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(result.mResultData);
                break;
            default:
                Toast.makeText(PhotoSelector.this,"sads",Toast.LENGTH_LONG).show();
        }
        removeFragmentFromScreen();
    }

    public void removeFragmentFromScreen() {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();
        toolbar.setVisibility(View.GONE);
        photoSelector.setVisibility(View.VISIBLE);
    }

    private void handleCropResult(@NonNull Intent result) {
        Uri resultUri = UCrop.getOutput(result);
        String resultUriS=resultUri.toString();
        if (resultUriS != null) {

            if(activity.equals("ProfilePic")) {
                Intent intent = new Intent("ProfilePhotoSelector");
                intent.putExtra("ProfileStringUri", resultUriS);
                setResult(RESULT_OK, intent);
                finish();

            }
            if(activity.equals("OrganiserPic")) {
                Intent intent = new Intent("OrganiserPhotoSelector");
                intent.putExtra("OrganiserStringUri", resultUriS);
                setResult(RESULT_OK, intent);
                finish();

            }
            if(activity.equals("EventPic")) {
                Intent intent = new Intent("EventPhotoSelector");
                intent.putExtra("EventStringUri", resultUriS);
                setResult(RESULT_OK, intent);
                finish();
            }
            if(activity.equals("EditEventPic")) {
                Intent intent = new Intent("EditEventPhotoSelector");
                intent.putExtra("EditEventPicStringUri", resultUriS);
                setResult(RESULT_OK, intent);
                finish();
            }
            if(activity.equals("EditOrganiser")) {
                Intent intent = new Intent("EditOrganiserPhotoSelector");
                intent.putExtra("EditOrganiserStringUri", resultUriS);
                setResult(RESULT_OK, intent);
                finish();

            }
            image.setImageURI(resultUri);



        } else {
            Toast.makeText(PhotoSelector.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("adsd", "handleCropError: ", cropError);
            Toast.makeText(PhotoSelector.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PhotoSelector.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.ucrop_menu_activity, menu);

        // Change crop & loader menu icons color to match the rest of the UI colors


        MenuItem menuItemLoader = menu.findItem(R.id.menu_loader);
        Drawable menuItemLoaderIcon = menuItemLoader.getIcon();
        if (menuItemLoaderIcon != null) {
            try {
                menuItemLoaderIcon.mutate();
                menuItemLoaderIcon.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                menuItemLoader.setIcon(menuItemLoaderIcon);
            } catch (IllegalStateException e) {
                Log.i(this.getClass().getName(), String.format("%s - %s", e.getMessage(), getString(R.string.ucrop_mutate_exception_hint)));
            }
            ((Animatable) menuItemLoader.getIcon()).start();
        }

        MenuItem menuItemCrop = menu.findItem(R.id.menu_crop);
        Drawable menuItemCropIcon = ContextCompat.getDrawable(this, R.drawable.ucrop_ic_done);
        if (menuItemCropIcon != null) {
            menuItemCropIcon.mutate();
            menuItemCropIcon.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
            menuItemCrop.setIcon(menuItemCropIcon);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_crop).setVisible(!mShowLoader);
        menu.findItem(R.id.menu_loader).setVisible(mShowLoader);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_crop) {
            if (fragment != null && fragment.isAdded())
                fragment.cropAndSaveImage();
        } else if (item.getItemId() == android.R.id.home) {
            removeFragmentFromScreen();
        }
        return super.onOptionsItemSelected(item);
    }






    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    protected void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }



}
