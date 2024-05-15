package or.nevet.orusermanager;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.HashMap;
//the @Keep is needed in case I want to use this with firebase reflections somehow (For example, saving the object in firebase).
@Keep
public abstract class UserCreatedCloudObject implements Serializable {
    @Keep
    private HashMap<String, Object> properties;

    @Keep
    public UserCreatedCloudObject(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    //for firebase reflections.
    @Keep
    public UserCreatedCloudObject() {
        properties = new HashMap<>();
    }

    @Keep
    public <T> T getProperty(String propertyName) {
        return (T) properties.get(propertyName);
    }
    @Keep
    public void setProperty(String propertyName, Object propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    @Keep
    public HashMap<String, Object> getProperties() {
        return properties;
    }

    @Keep
    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }


}
