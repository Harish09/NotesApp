package harish.notesui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import harish.notesui.app.AppController;
import harish.notesui.data.User;
import harish.notesui.utils.SnackAlert;

public class LoginActivity extends Activity {

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

        findViewById(R.id.register).setOnClickListener(this::login);

        getUsers();
    }

    public void login(View view) {
        String username = name.getText().toString();
        String pass = password.getText().toString();

        if (username.isEmpty() || pass.isEmpty())
            SnackAlert.showAlert(v, "Invalid form");
        else if (userList.contains(new User(username, pass))) {
            SharedPreferences.Editor editor = getSharedPreferences("Register", MODE_PRIVATE).edit();
            editor.putString("user", username).apply();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else
            SnackAlert.showAlert(v, "Wrong credentials");
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

}
