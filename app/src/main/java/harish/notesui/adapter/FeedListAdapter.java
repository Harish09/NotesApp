package harish.notesui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import harish.notesui.app.AppController;
import harish.notesui.data.FeedItem;
import harish.notesui.FeedImageView;
import harish.notesui.R;

public class FeedListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.feedItems = feedItems;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public FeedItem getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feed_item, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        TextView hashtag = (TextView) convertView.findViewById(R.id.txtHashtag);
        TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);

        NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
        FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);

        FeedItem item = getItem(position);

        name.setText(item.getName());

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);

        if (!TextUtils.isEmpty(item.getStatus())) {
            statusMsg.setText(item.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            statusMsg.setVisibility(View.GONE);
        }

        if (item.getUrl() != null) {
            url.setText(Html.fromHtml(String.format("<a href=\"%s\">%s</a> ", item.getUrl(), item.getUrl())));

            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            url.setVisibility(View.GONE);
        }

        if (item.getHashtag() != null) {
            StringBuilder hashtags = new StringBuilder();

            for (String tag: item.getHashtag())
                hashtags.append("#").append(tag).append(" ");

            hashtag.setText(Html.fromHtml(String.format("<b>%s</b>", hashtags.toString() )));
            hashtag.setVisibility(View.VISIBLE);
        } else
            hashtag.setVisibility(View.GONE);

        profilePic.setImageUrl(item.getProfilePic(), imageLoader);

        if (item.getImage() != null) {
            feedImageView.setImageUrl(item.getImage(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView.setResponseObserver(new FeedImageView.ResponseObserver() {
                @Override
                public void onError() {
                }

                @Override
                public void onSuccess() {
                }
            });
        } else {
            feedImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

}
