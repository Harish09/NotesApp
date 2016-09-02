package harish.notesui;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import harish.notesui.adapter.FeedListAdapter;
import harish.notesui.app.AppController;
import harish.notesui.data.FeedItem;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView listView;
	private Trie hashtagTrie;
	private EditText sV;

	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	private static String URL_FEED = "https://api.myjson.com/bins/1wlg8";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		sV = (EditText) findViewById(R.id.searchView);

		feedItems = new ArrayList<>();
		hashtagTrie = new Trie('\0', false);
		listAdapter = new FeedListAdapter(this, feedItems);
		listView.setAdapter(listAdapter);

		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		if (entry != null) {
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					URL_FEED, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							parseJsonFeed(response);
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
						}
					});

			AppController.getInstance().addToRequestQueue(jsonReq);
		}


		sV.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String query = s.toString();

				if (query.isEmpty()) return;

				List<String> suggestions = hashtagTrie.searchSubsequence(query);

				List<FeedItem> list = new ArrayList<>();

				for (FeedItem f : feedItems) {
					if (suggestions.contains(f.getHashtag())) {
						list.add(f);
					}
				}
				feedItems.clear();
				feedItems.addAll(list);

				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	private void parseJsonFeed(@NonNull JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("feed");

			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				FeedItem item = new FeedItem();
				item.setId(feedObj.getInt("id"));
				item.setName(feedObj.getString("name"));

				String image = feedObj.isNull("image") ? null : feedObj
						.getString("image");
				item.setImage(image);
				item.setStatus(feedObj.getString("status"));
				item.setProfilePic(feedObj.getString("profilePic"));
				item.setTimeStamp(feedObj.getString("timeStamp"));

				String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
				item.setUrl(feedUrl);

				String tag = feedObj.isNull("hashtag") ? null : feedObj.getString("hashtag");
				item.setHashtag(tag);

				if (tag != null) hashtagTrie.insertWord(tag);

				feedItems.add(item);
			}

			listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
