package harish.notesui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import harish.notesui.app.AppController;
import harish.notesui.data.FeedItem;

public class UploadHandler extends Activity {

    EditText name, status, drivelink, hashtag;
	ArrayList<String> feedItems;

	String URL_FEED;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

	    URL_FEED = Resources.getSystem ().getString (R.string.url_feed);

	    name = (EditText) findViewById(R.id.name);
	    status = (EditText) findViewById(R.id.status);
	    drivelink = (EditText) findViewById(R.id.drivelink);
	    hashtag = (EditText) findViewById(R.id.hashtag);

	    feedItems = getIntent ().getExtras ().getStringArrayList ("feed");
    }

    public void submit(View view) {
		String username = name.getText ().toString ();
		String statusMsg = status.getText ().toString ();
		String url = drivelink.getText ().toString ();
		String tag = hashtag.getText ().toString ();

	    final FeedItem item = new FeedItem (4, username, null, statusMsg, null, new Date ().toString (), url, tag );

	    if ( username.length () == 0 || statusMsg.length () == 0 || url.length () == 0 || tag.length () == 0 ) {
		    alertMessage ("Invalid form");
	    } else {

		    StringRequest stringRequest = new StringRequest (Request.Method.PUT, URL_FEED, new Response.Listener<String> () {
			    @Override
			    public void onResponse (String s) {
					alertMessage ("SUCCESS!");
				    startActivity (new Intent (UploadHandler.this, MainActivity.class));
				    finish ();
			    }
		    }, new Response.ErrorListener () {
			    @Override
			    public void onErrorResponse (VolleyError volleyError) {
				    alertMessage ("ERROR!");
				    startActivity (new Intent (UploadHandler.this, MainActivity.class));
				    finish ();
			    }
		    }) {
			    @Override
			    public String getBodyContentType () {
				    return "application/json; charset=utf-8";
			    }

			    @Override
			    public byte[] getBody () throws AuthFailureError {
				    try {
					    JSONArray jsonArray = new JSONArray ();
					    jsonArray = jsonArray.put(0, item.toJSON ());
					    for (int i = 0, feedItemsSize = feedItems.size (); i < feedItemsSize; i++) {
						    String f = feedItems.get (i);
						    jsonArray =  jsonArray.put (i + 1, new JSONObject (f));
					    }
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

	public void alertMessage(String message) {
		Toast.makeText (getApplicationContext (), message, Toast.LENGTH_SHORT).show ();
	}
}
