package or.nevet.orusermanager;

public class UserObject {
    private final User user;

    UserObject(User user) {
        this.user = user;
    }

    public <T> T getProperty(String propertyName) {
        return user.getProperty(propertyName);
    }
}
