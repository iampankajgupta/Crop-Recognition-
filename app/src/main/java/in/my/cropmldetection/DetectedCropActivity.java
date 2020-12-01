package in.my.cropmldetection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.widget.Toolbar;

import java.io.FileInputStream;
import java.util.ArrayList;

public class DetectedCropActivity extends AppCompatActivity {
    ListView listView;
    ImageView cropImage;
    public static String name;
    public static int i;
    ArrayList<String> result;

    String imagePath;
    UserSessionManager userSessionManager;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detected_crop);
        listView = findViewById(R.id.listView);
        cropImage = findViewById(R.id.cropImage);

        toolbar = findViewById(R.id.LogOut);
        setSupportActionBar(toolbar);


        imagePath = getIntent().getStringExtra("imageFilePath");

        cropImage.setImageBitmap(loadImageBitmap(this, imagePath));

        userSessionManager = new UserSessionManager(getApplicationContext());

        result = (ArrayList<String>) getIntent().getSerializableExtra("myList");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
//                language conversion is pending here i.e convert int clicktext to String and manage things
            String clicktext = result.get(position);
            name = "";
            i = 0;
            while (clicktext.charAt(i) != '-') {
                name += clicktext.charAt(i);
                i++;
            }

            Intent intent1 = new Intent(DetectedCropActivity.this, InformationActivity.class);
            intent1.putExtra("clickedCrop", name);
            intent1.putExtra("imagePath", imagePath);
            startActivity(intent1);
        });
    }

    public Bitmap loadImageBitmap(Context context, String name) {
        Bitmap bitmap = null;
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(name);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogOut:
                userSessionManager.logoutUser();
                Intent intent1 = new Intent(DetectedCropActivity.this, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }

}
