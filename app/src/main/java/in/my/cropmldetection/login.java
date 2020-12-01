package in.my.cropmldetection;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class login extends AppCompatActivity {
    EditText email, password;
    Button chooseLanguage, login;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;


    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userSessionManager = new UserSessionManager(getApplicationContext());
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

//        chooseLanguage = findViewById(R.id.chooseLanguage);

        login = findViewById(R.id.login);

        databaseHelper = new DatabaseHelper(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Please enter valid Email Address...");
                    email.setText("");
                } else if (password.getText().toString().equals("")) {
                    password.setError("Please Enter password...");
                    password.setText("");
                }
//                else if (databaseHelper.CheckEmailExists(email.getText().toString())) {
//                    Toast.makeText(login.this, "Username and Password does not match", Toast.LENGTH_SHORT).show();
//                    email.setText("");
//                    password.setText("");
//                }
                else {
                    if (!databaseHelper.UserExists(email.getText().toString(), password.getText().toString())) {
                        Toast.makeText(login.this, "User Not Exists Please Register ", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("LoginMasg", "Login Sucessfully ");
                        Toast.makeText(login.this, "Login Sucessfully...", Toast.LENGTH_SHORT).show();
//                        userSessionManager.createUserLoginSession(email.getText().toString(), password.getText().toString());
                        SaveUserSeesion();
                        Intent intent1 = new Intent(login.this, HomeActivity.class);
                        startActivity(intent1);
                    }
                }
            }
        });


    }

    private void SaveUserSeesion() {
        userSessionManager.createUserLoginSession(email.getText().toString(),password.getText().toString());
    }

    public void registerTxt(View view) {
        Intent intent1 = new Intent(login.this, MainActivity.class);
        startActivity(intent1);
    }


}
