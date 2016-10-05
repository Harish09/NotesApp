package harish.notesui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import harish.notesui.app.AppController;
import harish.notesui.data.User;

public class RegisterActivity extends Activity {

    EditText name, password;
    String USER_LIST;
    List<User> userList;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        v = findViewById(R.id.activity_register);
        USER_LIST = getString(R.string.user_list);
        userList = new ArrayList<>();

        name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        findViewById(R.id.register).setOnClickListener(this::register);

        getUsers();
    }

    public void register(View view) {
        String username = name.getText().toString();
        String pass = password.getText().toString();

        if (username.isEmpty() || pass.isEmpty()) {
            Snackbar.make(v, "Invalid form", Snackbar.LENGTH_SHORT).show();
        } else {
            addUser(new User(username, pass));
        }
    }

    public void getUsers() {
        AppController.getInstance().addToRequestQueue(new JsonObjectRequest(
                Request.Method.GET,
                USER_LIST,
                null,
                this::parseList,
                error -> Log.e("REGISTER/NOTES", error.getMessage())
        ));
    }

    private void parseList(JSONObject response) {
        User dummy = new User();
        try {

            JSONArray users = response.getJSONArray("users");
            for (int i = 0, len = users.length(); i < len; i++)
                userList.add((User) dummy.fromJSON(users.getJSONObject(i)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public  void addUser(User user) {
        StringRequest stringRequest = new StringRequest (
                Request.Method.PUT,
                USER_LIST,
                response -> {
                    startActivity (new Intent(RegisterActivity.this, MainActivity.class));
                    finish ();
                },
                volleyError -> {
                    Log.e("NOTES", volleyError.getMessage());
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish ();
                }
        ) {
            @Override
            public String getBodyContentType () {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody () throws AuthFailureError {
                try {
                    JSONArray jsonArray = new JSONArray ();

                    userList.add(user);
                    for (int i = 0, feedItemsSize = userList.size (); i < feedItemsSize; i++)
                        jsonArray =  jsonArray.put (i, userList.get(i).toJSON());

                    JSONObject finalObj = new JSONObject ().put ("users", jsonArray);
                    return finalObj.toString ().getBytes ("utf-8");
                } catch (Exception e) { return null; }
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
