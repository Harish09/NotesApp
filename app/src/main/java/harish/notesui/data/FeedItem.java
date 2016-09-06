package harish.notesui.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class FeedItem implements Serializable {
	private String hashtag;
	private int id;
	private String name, status, image, profilePic, timeStamp, url;

	public FeedItem() {
	}


	public static FeedItem fromJSON(JSONObject feedObj) throws JSONException {

		FeedItem item = new FeedItem ();

		item.setId(feedObj.getInt("id"));

		item.setName(feedObj.getString("name"));

		String image = feedObj.isNull("image") ? null : feedObj.getString("image");
		item.setImage(image);

		item.setStatus(feedObj.getString("status"));

		item.setProfilePic(feedObj.getString("profilePic"));

		item.setTimeStamp(feedObj.getString("timeStamp"));

		String feedUrl = feedObj.isNull("url") ? null : feedObj.getString("url");
		item.setUrl(feedUrl);

		String tag = feedObj.isNull("hashtag") ? null : feedObj.getString("hashtag");
		item.setHashtag(tag);

		return item;
	}

	public JSONObject toJSON() throws JSONException {
		return new JSONObject ()
				.put ("id", this.getId ())
				.put ("name", this.getName ())
				.put ("image", this.getImage ())
				.put ("status", this.getStatus ())
				.put ("profilePic", this.getProfilePic ())
				.put ("timeStamp", this.getTimeStamp ())
				.put ("url", this.getUrl ())
				.put ("hashtag", this.getHashtag ());
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public FeedItem(int id, String name, String image, String status,
	                String profilePic, String timeStamp, String url, String hashtag) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.status = status;
		this.profilePic = profilePic;
		this.timeStamp = timeStamp;
		this.hashtag = hashtag;
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
