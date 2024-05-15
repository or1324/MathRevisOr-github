package or.nevet.orusermanager;

import androidx.annotation.Keep;

import java.util.HashMap;

class Settings extends UserCreatedCloudObject {
    @Keep
    public Settings(HashMap<String, Object> properties) {
        super(properties);
    }

    @Keep
    public Settings() {
        super(new HashMap<>());
    }
}
