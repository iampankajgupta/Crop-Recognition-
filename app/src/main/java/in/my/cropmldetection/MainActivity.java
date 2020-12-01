package in.my.cropmldetection;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.res.Configuration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText name, email, password;
    Button chooseLanguage, register;
    TextView loginTxt;
    AlertDialog.Builder builder;

    DatabaseHelper databaseHelper;


    UserSessionManager userSessionManager;
    SharedPreferences prefs;

    public static final String Settings = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        userSessionManager = new UserSessionManager(getApplicationContext());
        userSessionManager.checkLogin();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        loginTxt = findViewById(R.id.loginTxt);

//        chooseLanguage = findViewById(R.id.chooseLanguage);
        register = findViewById(R.id.registerBtn);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);



        databaseHelper = new DatabaseHelper(this);
//
//        chooseLanguage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showChangeLanguageDialog();
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    name.setError("Please enter Name");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Please enter valid Email Address");
                } else if (password.getText().toString().equals("")) {
                    password.setError("Please enter password");
                } else {
                    if (databaseHelper.CheckEmailExists(email.getText().toString())) {
                        Toast.makeText(MainActivity.this, "User Already Exists Please Login", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        name.setText("");
                        password.setText("");
                    }
                    else{
                        databaseHelper.addUser(name.getText().toString(), email.getText().toString(), password.getText().toString());
                        SaveUserSeesion();
                        Log.d("msg", "Registered Succesfully ");
                        Toast.makeText(MainActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent1);
                    }

                }
            }
        });
    }
    private void SaveUserSeesion() {
        userSessionManager.createUserLoginSession(email.getText().toString(),password.getText().toString());
    }
    private void showChangeLanguageDialog() {

        final String[] listItems = {"English", "हिंदी", "தமிழ்", "తెలుగు"};

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Langaueg...");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i == 0) {
//                     EMGLISH

                    setLocale("");
                    recreate();

                }
                if (i == 1) {
//                     HINDI
                    setLocale("hi");
                    recreate();

                }
                if (i == 2) {
//                    TAMIL
                    setLocale("ta");
                    recreate();

                }
                if (i == 3) {

//                     TELUGU

                    setLocale("te");
                    recreate();

                }

                dialog.dismiss();

            }
        });
        AlertDialog mDialog = builder.create();

        mDialog.show();
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();

        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

// save data to shared preferences

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", language);
        editor.apply();

    }
//    load language saved in shared preferences

    public void loadLocale() {

        prefs = getSharedPreferences(Settings, MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
    }

    public void loginTxt(View view) {
        Intent intent1 = new Intent(MainActivity.this, login.class);
        startActivity(intent1);
    }
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


}



