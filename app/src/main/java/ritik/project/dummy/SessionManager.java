package ritik.project.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.HashMap;

/**
 * Created by SuperUser on 09-11-2016.
 */
public class SessionManager {



    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;


    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = Constants.NAME;

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setUser(String name,String roll,String phone,String email,Boolean verified){
        editor.putString("ROLL_NO",roll);
        editor.putString("NAME",name);
        editor.putString("PHONE",phone);
        editor.putString("EMAIL",email);
        editor.putBoolean("VERIFIED",verified);


        editor.commit();
    }
    public HashMap<String,String> getUser(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put("roll", pref.getString("ROLL_NO","XX/XXX/XXX"));
        user.put("name", pref.getString("NAME","Anonymous"));
        user.put("email", pref.getString("EMAIL"," "));
        user.put("phone", pref.getString("PHONE"," "));
        return user;
    }
    public Boolean isVerified(){
        return pref.getBoolean("VERIFIED",false);
    }
    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);


        editor.commit();

        Log.d(Constants.TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }


}
