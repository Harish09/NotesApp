package harish.notesui.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FeedItem implements Serializable, JSONable {
    private int id;
    private String name, status, image, profilePic, timeStamp, url;
    private List<String> hashtag;

    public FeedItem() {
    }

    public Object fromJSON(JSONObject feedObj) throws JSONException {

        FeedItem item = new FeedItem ();

        item.setId(feedObj.getInt("id"));
        item.setName(feedObj.getString("name"));
        item.setImage(feedObj.optString("image"));
        item.setStatus(feedObj.getString("status"));
        item.setProfilePic(feedObj.optString("profilePic", "http://delfoo.com/image/profile_image/blank_user.png"));
        item.setTimeStamp(feedObj.getString("timeStamp"));
        item.setUrl(feedObj.optString("url"));

        JSONArray js = feedObj.optJSONArray("hashtag");
        List<String> l = new ArrayList<>();

        if (js != null)
            for (int i = 0, len = js.length(); i < len; i++)
                l.add(js.toString());

        item.setHashtag(l);

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
                .put ("hashtag", new JSONArray(this.getHashtag ()));
    }

    public List<String> getHashtag() {
//        StringBuilder hashtags = new StringBuilder();
//
//        for (String tag: this.hashtag)
//            hashtags.append("#").append(tag).append(" ");

        return hashtag;
//        return hashtags.toString();
    }

    public void setHashtag(List<String> hashtag) {
        this.hashtag = hashtag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public FeedItem(int id, String name, String image, String status,
                    String profilePic, String timeStamp, String url, List<String> hashtag) {
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
