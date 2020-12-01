package in.my.cropmldetection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.HashMap;

public class UserSessionManager {
    SharedPreferences pref;


    SharedPreferences.Editor editor;

    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "SessionInfo";

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";

    public static final String KEY_PASSWORD = "password";

    public static final String KEY_EMAIL = "email";

    public UserSessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(String email,String password){
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing password in pref
        editor.putString(KEY_PASSWORD,password);

        // commit changes
        editor.apply();

    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */

        public boolean checkLogin(){
            // Check login status

            if(this.isUserLoggedIn()){

                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(context, HomeActivity.class);

                // Closing all the Activities from stack
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                context.startActivity(i);

                return true;
            }
        return false;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user name
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, MainActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return pref.getBoolean(IS_USER_LOGIN, false);
    }






}
