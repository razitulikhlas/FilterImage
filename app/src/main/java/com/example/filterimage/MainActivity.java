package com.example.filterimage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.filterimage.adapter.ViewPagerAdapter;
import com.example.filterimage.interfaces.AddFrameListener;
import com.example.filterimage.interfaces.AddTextFragmentListener;
import com.example.filterimage.interfaces.BrushFragmentListener;
import com.example.filterimage.interfaces.EditImageFragmentListener;
import com.example.filterimage.interfaces.EmojiFragmentListener;
import com.example.filterimage.interfaces.FiltersListFragmentListener;
import com.example.filterimage.utils.BitmapUtils;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.util.List;
import java.util.UUID;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class MainActivity extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener, AddFrameListener {
    public static String pictureName = "people.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_INSERT_IMAGE = 1001;

    CoordinatorLayout coordinatorLayout;

    Bitmap originalBitmap,filteredBitmap,finalBitmap;

    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;

    CardView btn_filter_list,btn_edit,btn_brush,btn_emo,btn_text,btn_add_image,btn_add_frame,btn_crop;

    int brightnessFinal =0;
    float saturationFinal=1.0f;
    float constraintsFinal=1.0f;

    Uri image_selected_uri;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;

    //load native image filter lib
    static{
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Filter razit");

        //view
        photoEditorView = findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this,photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
                .build();
        coordinatorLayout = findViewById(R.id.coordinator);

        btn_edit = findViewById(R.id.btn_edit);
        btn_filter_list = findViewById(R.id.btn_filter_list);
        btn_brush = findViewById(R.id.btn_brush);
        btn_emo = findViewById(R.id.btn_emoji);
        btn_text = findViewById(R.id.btn_text);
        btn_add_image = findViewById(R.id.btn_add_image);
        btn_add_frame = findViewById(R.id.btn_add_frame);
        btn_crop = findViewById(R.id.btn_add_crop);

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCrop(image_selected_uri);
            }
        });

        btn_filter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filtersListFragment != null){
                    filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());
                }else{
                    FiltersListFragment filtersListFragment = FiltersListFragment.getInstance(null);
                    filtersListFragment.setListener(MainActivity.this);
                    filtersListFragment.show(getSupportFragmentManager(),filtersListFragment.getTag());
                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoEditor.setBrushDrawingMode(true);

                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });

        btn_emo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(MainActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());

            }
        });

        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextAddFragment textAddFragment = TextAddFragment.getInstance();
                textAddFragment.setListener(MainActivity.this);
                textAddFragment.show(getSupportFragmentManager(),textAddFragment.getTag());
            }
        });

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToPicture();
            }
        });

        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameFragment frameFragment = FrameFragment.getInstance();
                frameFragment.setListener(MainActivity.this);
                frameFragment.show(getSupportFragmentManager(),frameFragment.getTag());
            }
        });

        loadImage();


    }

    private void startCrop(Uri uri) {
        String destinationName = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();

        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationName)));

        uCrop.start(MainActivity.this);
    }

    private void addImageToPicture() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_INSERT_IMAGE);
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }

    private void loadImage() {
        originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment,"FILTERS");
        adapter.addFragment(editImageFragment,"EDIT");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBrightnessChange(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChange(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onContrastChange(float contrast) {
        constraintsFinal= contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(contrast));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constraintsFinal));
        finalBitmap = myFilter.processFilter(bitmap);

    }

    @Override
    public void onFilterSelected(Filter filter) {
        //resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    private void resetControl() {
        if(editImageFragment != null)
            editImageFragment.resetControls();
        brightnessFinal=0;
        saturationFinal = 1.0f;
        constraintsFinal = 1.0f;
    }

    //menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=  item.getItemId();
        if(id == R.id.action_open){
            openImageFromGalerry();
            return true;
        }else if(id == R.id.action_save){
            saveImageToGalerry();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImageToGalerry() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                           photoEditor.saveAsBitmap(new OnSaveBitmap() {
                               @Override
                               public void onBitmapReady(Bitmap saveBitmap) {

                                   photoEditorView.getSource().setImageBitmap(saveBitmap);
                                   final String path = BitmapUtils.insertImage(getContentResolver()
                                           ,saveBitmap
                                           ,System.currentTimeMillis()+"_profile.jpeg"
                                           ,null);
                                   if(!TextUtils.isEmpty(path)){
                                       Snackbar snackbar = Snackbar.make(coordinatorLayout,"Image Save to Galerry",Snackbar.LENGTH_LONG)
                                               .setAction("open", new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       openImage(path);
                                                   }
                                               });
                                       snackbar.show();
                                   }else{
                                       Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                               "Unable to save Image",Snackbar.LENGTH_LONG);
                                       snackbar.show();
                                   }
                               }

                               @Override
                               public void onFailure(Exception e) {

                               }
                           });
                        }else{
                            Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGalerry() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PERMISSION_PICK_IMAGE);
                        }else{
                            Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                            token.continuePermissionRequest();
                    }
                })
                .check();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            if(requestCode == PERMISSION_PICK_IMAGE){
                assert data != null;
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                image_selected_uri = data.getData();


                //clear bitmap memory
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();

                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                filtersListFragment = FiltersListFragment.getInstance(originalBitmap);
                filtersListFragment.setListener(this);

            }else if(requestCode == PERMISSION_INSERT_IMAGE){
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this,data.getData(),250,250);
                photoEditor.addImage(bitmap);
            }

            else if(requestCode == UCrop.REQUEST_CROP){
                handleCropResult(data);
            }

        }
        else  if(resultCode == UCrop.RESULT_ERROR)
            handleCropError(data);
    }

    private void handleCropError(Intent data) {
        final Throwable cropError = UCrop.getError(data);
        if(cropError != null){
            Toast.makeText(this, "t:"+cropError.getMessage(), Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri = UCrop.getOutput(data);
        if(resultUri != null)
            photoEditorView.getSource().setImageURI(resultUri);
        else
            Toast.makeText(this, "Cannot retrieve crop image", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBrushSizeChangeListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangeListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangeListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangeListener(boolean isEraser) {
        if(isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }


    @Override
    public void onAddTextButtonClick(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface,text,color);
    }

    @Override
    public void onAddFrame(int frame) {
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),frame);
        photoEditor.addImage(bitmap);
    }
}
