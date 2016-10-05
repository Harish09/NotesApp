package harish.notesui.data;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements JSONable {
    private String username;
    private String password;

    public User() {

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        return new JSONObject ()
                .put ("username", this.getUsername ())
                .put ("password", this.getPassword ());
    }

    @Override
    public Object fromJSON(JSONObject object) throws JSONException {
        User u = new User();

        u.setUsername(object.optString("username", "Anonymous"));
        u.setPassword(object.optString("password", "password"));

        return u;
    }
}
