package in.my.cropmldetection;

import android.content.Intent;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class InformationActivity extends AppCompatActivity {
    TextView name, description;

    private static final String fileName = "Example.pdf";

    Toolbar toolbar;

    String cropName;
    Button getMarketPrice, logOut;
    FloatingActionButton ShareBtn;
    FloatingActionButton cropLocation;
    ImageView cropImage;


    UserSessionManager userSessionManager;

    String combine;

    String imagePath;
    Bitmap bitmap;

    DetectedCropActivity detectedCropActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        userSessionManager = new UserSessionManager(getApplicationContext());

        imagePath = getIntent().getStringExtra("imagePath");

        detectedCropActivity = new DetectedCropActivity();

        toolbar = findViewById(R.id.LogOut);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.cropName);
        description = findViewById(R.id.cropDescription);

        ShareBtn = findViewById(R.id.ShareBtn);
//        logOut = findViewById(R.id.LogOut);
        getMarketPrice = findViewById(R.id.getPriceInfo);

        cropLocation = findViewById(R.id.cropLocation);

        cropImage = findViewById(R.id.cropImage);

        bitmap = detectedCropActivity.loadImageBitmap(this, imagePath);
        cropImage.setImageBitmap(bitmap);

//        GET THE NAME OF THE CLICKED CROP

        cropName = getIntent().getStringExtra("clickedCrop");


        switch (cropName) {
            case "WHEAT":
                name.setText(R.string.wheat);
                description.setText(R.string.wheatDetail);
                break;
            case "RICE":
                name.setText(R.string.rice);
                description.setText(R.string.riceDetail);
                break;
            case "JOWAR":
                name.setText(R.string.jowar);
                description.setText(R.string.jowarDetail);
                break;
            default:
                name.setText(R.string.bajra);
                description.setText(R.string.bajraDetail);
                break;
        }

        combine = name.getText().toString() + "\n\n" + description.getText().toString();

        cropLocation.setOnClickListener(v -> {
            Intent intent1 = new Intent(InformationActivity.this, ShowCropLocationActivity.class);
            intent1.putExtra("cropName", cropName);
            startActivity(intent1);
        });

        ShareBtn.setOnClickListener(v -> {


            Document document = new Document();
            File externalStorage = Environment.getExternalStorageDirectory();
            String fileName ="/"+System.currentTimeMillis()+".pdf";
            File file = new File(externalStorage,fileName);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] byteArray = baos.toByteArray();



            try {
                PdfWriter.getInstance(document,new FileOutputStream(file));
                document.open();


                Image image = Image.getInstance(byteArray);

                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / image.getWidth()) * 50; // 0 means you have no indentation. If you have any, change it.
                image.scalePercent(scaler);
                image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

                document.add(image);
                document.add(new Paragraph(combine));
                document.close();
                Log.d("msg","document created");

            } catch (DocumentException e) {
                Log.d("msg",e.getMessage());
            }
            catch (FileNotFoundException e){
                Log.d("msg",e.getMessage());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Uri uri = Uri.fromFile(new File(externalStorage,fileName));

            Uri uri = FileProvider.getUriForFile(this,"in.my.cropmldetection",file);
            Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.putExtra(Intent.EXTRA_STREAM,uri);
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent1.setType("application/pdf");
            startActivity(Intent.createChooser(intent1, "Choose App to Send Message"));
//            Toast.makeText(InformationActivity.this, "Success", Toast.LENGTH_SHORT).show();

        });

               getMarketPrice.setOnClickListener(v -> {
            String url = "https://www.google.com/search?q="+cropName+"+price+in+mandi&oq=rice+price+in+mandi+&aqs=chrome..69i57j0i22i30l7.11086j0j7&sourceid=chrome&ie=UTF-8";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

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
                Intent intent1 = new Intent(InformationActivity.this, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
        }
        return super.onOptionsItemSelected(item);
    }
}
