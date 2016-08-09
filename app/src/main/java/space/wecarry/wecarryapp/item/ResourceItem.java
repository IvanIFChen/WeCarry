package space.wecarry.wecarryapp.item;

/**
 * Created by Blair on 2016/8/9.
 */
public class ResourceItem {
    private String title;
    private String email;

    public ResourceItem() {
        this.title = "";
        this.email = "";
    }

    public ResourceItem(String title, String email) {
        this.title = title;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ResourceItem{" +
                "title='" + title + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
