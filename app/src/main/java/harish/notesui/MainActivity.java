package harish.notesui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import harish.notesui.adapter.FeedListAdapter;
import harish.notesui.app.AppController;
import harish.notesui.data.FeedItem;


public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private Trie hashtagTrie;

	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String URL_FEED = getString (R.string.url_feed);

		ListView listView = (ListView) findViewById (R.id.list);
		EditText sV = (EditText) findViewById (R.id.searchView);

		feedItems = new ArrayList<>();
		hashtagTrie = new Trie('\0', false);

		listAdapter = new FeedListAdapter(this, feedItems);
		listView.setAdapter(listAdapter);

		if (getActionBar() != null) {
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
			getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		}

		showFeed(URL_FEED);

		sV.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String query = s.toString();

				if (query.isEmpty()) {
					showFeed(URL_FEED);
					return;
				}

				List<String> suggestions = hashtagTrie.searchSubsequence(query);
				List<FeedItem> list = new ArrayList<>();

				for (FeedItem feedItem : feedItems)
					for (String tag: feedItem.getHashtag())
						if (suggestions.contains(tag))
							list.add(feedItem);

				feedItems.clear();
				feedItems.addAll(list);

				listAdapter.notifyDataSetChanged();
			}

			@Override
			public void afterTextChanged(Editable s) {}
		});
	}

	private void showFeed(String URL_FEED) {
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		if (entry != null)
			try {
				parseJsonFeed(new JSONObject(new String(entry.data, "UTF-8")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			AppController.getInstance().addToRequestQueue(getFeed(URL_FEED));
	}

	@NonNull
	private JsonObjectRequest getFeed(String URL_FEED) {
		return new JsonObjectRequest(Method.GET,
				URL_FEED,
				null,
                this::parseJsonFeed,
				error -> VolleyLog.d(TAG, "Error: " + error.getMessage())
		);
	}

	private void parseJsonFeed(@NonNull JSONObject response) {
		FeedItem dummy = new FeedItem();
		try {
			JSONArray feedArray = response.getJSONArray("feed");

			for (int i = 0; i < feedArray.length(); i++) {
				FeedItem item = (FeedItem) dummy.fromJSON (feedArray.getJSONObject(i));
                feedItems.add(item);

                hashtagTrie.insertAllWords(item.getHashtag ());
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

	public void upload(View view) {
		ArrayList<String> list = new ArrayList<>();

		for (FeedItem feedItem : feedItems)
			try {
				list.add (feedItem.toJSON ().toString ());
			} catch (JSONException e) {
				e.printStackTrace ();
			}

		Intent uploadIntent = new Intent(MainActivity.this, UploadHandler.class);
		uploadIntent.putStringArrayListExtra ("feed", list);

		startActivity(uploadIntent);
	}
}
