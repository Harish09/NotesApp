package harish.notesui.data;

import org.json.JSONException;
import org.json.JSONObject;

interface JSONable {
    JSONObject toJSON() throws JSONException;

    Object fromJSON(JSONObject object) throws JSONException;
}
