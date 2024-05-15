package or.nevet.orusermanager;

public class SettingsObject {

    private final Settings settings;

    SettingsObject(Settings settings) {
        this.settings = settings;
    }

    public <T> T getProperty(String propertyName) {
        return settings.getProperty(propertyName);
    }
}
