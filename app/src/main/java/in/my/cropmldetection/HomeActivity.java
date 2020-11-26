package in.my.cropmldetection;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    Button choosePhoto, takePhoto, getInfo, info;
    ImageView photo;
    Uri imageUri;
    private static final int PICK_IMAGE = 100;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTUER_CODE = 1001;

    String fileName;
    ProgressBar progressBar;

    FirebaseAutoMLLocalModel localModel;
    FirebaseVisionImageLabeler labeler;
    FirebaseVisionImage image;
    static ArrayList<String> myList;

    Bitmap bitmap;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseApp.initializeApp(this);

        choosePhoto = findViewById(R.id.choosePhoto);
        takePhoto = findViewById(R.id.takePhoto);
        photo = findViewById(R.id.photo);
        getInfo = findViewById(R.id.getInfo);
        info = findViewById(R.id.info);

        progressBar = findViewById(R.id.progressBar);

        myList = new ArrayList<>();

        choosePhoto.setOnClickListener(v -> new Thread(new Runnable() {
            @Override
            public void run() {
                chooseFromGallery();
            }
        }).start());
        takePhoto.setOnClickListener(v -> {
//                 if system OS is >= marshmallow , request runtime permission

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED) {
//                         permission not enabled , request it
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                          show pop up to quest permission
                    requestPermissions(permission, PERMISSION_CODE);
                } else {
//                         PERMISSION  GRANTED
                    openCamera();
                }
            } else {
//                     system os < marshmallow
                openCamera();
            }
        });
        getInfo.setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                            writeFileToInternalStorage(getApplicationContext(),bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                }
            }).start();
            new Thread(() -> setLabelFromRemoteModel(imageUri)).start();
            if (myList.size() != 0) {
                Intent intent1 = new Intent(HomeActivity.this, DetectedCropActivity.class);
                intent1.putExtra("myList", myList);
                intent1.putExtra("imageFilePath", fileName);
                startActivity(intent1);
            }
        });
    }
    private void openCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

//         CAMERA INTENT

        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent1, IMAGE_CAPTUER_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//         this method is called when user grant or deny on permission popup

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
//                     Permission on pop was denied
                    Toast.makeText(this, "Permisson Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void chooseFromGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
//        gallery.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                try {
                    imageUri = data.getData();
                    photo.setImageURI(imageUri);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }


            } else if (requestCode == IMAGE_CAPTUER_CODE) {
                photo.setImageURI(imageUri);
            }

        } else {
            Toast.makeText(HomeActivity.this, "You haven't picked Image Please Pick a Image", Toast.LENGTH_LONG).show();
        }

    }

    private void setLabelFromRemoteModel(Uri uri) {

        localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();
        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.0f)
                            .build();

            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);

            image = FirebaseVisionImage.fromFilePath(HomeActivity.this, imageUri);
            processImageLabeler(labeler, image, myList);

        } catch (Exception e) {
            Log.d("gate", e.getMessage());
        }
    }

    private void processImageLabeler(FirebaseVisionImageLabeler labeler, FirebaseVisionImage image, ArrayList<String> myList) {
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                int i = 0;
                for (FirebaseVisionImageLabel label : labels) {
                    String eachLabel = label.getText().toUpperCase();
                    float confidence = label.getConfidence();
                    if (myList.size() == 0 || myList.size() < 4) {
                        myList.add(i, eachLabel + "-" + ("" + confidence * 100).subSequence(0, 4) + "%");
                        i++;
                    } else {
                        break;
                    }
                }
            }
        }).addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
    }
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(startMain);
}


    public void writeFileToInternalStorage(Context context, Bitmap outputImage) throws IOException {
        fileName = System.currentTimeMillis()+".jpg";
        final FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
        outputImage.compress(Bitmap.CompressFormat.PNG,90,fos);
        fos.close();
    }



}

