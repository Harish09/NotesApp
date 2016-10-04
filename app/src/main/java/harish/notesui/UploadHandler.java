package harish.notesui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import harish.notesui.app.AppController;
import harish.notesui.data.FeedItem;

public class UploadHandler extends Activity {

    EditText status, drivelink, hashtag;
    TextView hashtags;
    ArrayList<String> feedItems;

    String URL_FEED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        URL_FEED = getString(R.string.url_feed);

        status = (EditText) findViewById(R.id.status);
        drivelink = (EditText) findViewById(R.id.drivelink);
        hashtag = (EditText) findViewById(R.id.hashtag);

        hashtags = (TextView) findViewById(R.id.hashtags);

        feedItems = getIntent ().getExtras ().getStringArrayList ("feed");
    }

    public void submit(View view) {
        String username = "llkl";
        String statusMsg = status.getText ().toString ();
        String url = drivelink.getText ().toString ();
        List<String> tag = Arrays.asList(hashtags.getText ().toString ().split("|"));

        if (statusMsg.length () == 0 || url.length () == 0 || tag.size () == 0)
            alertMessage ("Invalid form");
        else {
            StringRequest stringRequest = new StringRequest (
                    Request.Method.PUT,
                    URL_FEED,
                    response -> {
                        alertMessage ("SUCCESS!");
                        startActivity (new Intent (UploadHandler.this, MainActivity.class));
                        finish ();
                    },
                    volleyError -> {
                        alertMessage ("ERROR!");
                        Log.e("NOTES", volleyError.getMessage());
                        startActivity(new Intent(UploadHandler.this, MainActivity.class));
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
                        FeedItem item = new FeedItem (
                                4,
                                username,
                                null,
                                statusMsg,
                                null,
                                String.format(Locale.ENGLISH, "%d", new Date ().getTime()),
                                url,
                                tag
                        );
                        jsonArray = jsonArray.put(0, item.toJSON ());

                        for (int i = 0, feedItemsSize = feedItems.size (); i < feedItemsSize; i++)
                            jsonArray =  jsonArray.put (i + 1, new JSONObject (feedItems.get (i)));

                        JSONObject finalObj = new JSONObject ();
                        finalObj.put ("feed", jsonArray);
                        return finalObj.toString ().getBytes ("utf-8");
                    } catch (Exception e) {
                        return null;
                    }
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest);
        }
    }

    public void addHashtag(View view) {
        String tag = hashtag.getText().toString();

        if (tag.isEmpty())
            alertMessage("Can't add empty tag");
        else {
            String tags = hashtags.getText().toString();
            tags += tag + "|";
            hashtags.setText(tags);
        }
    }

    public void alertMessage(String message) {
        Toast.makeText (getApplicationContext (), message, Toast.LENGTH_SHORT).show ();
    }
}
