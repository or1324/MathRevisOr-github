package or.nevet.orusermanager;


import androidx.annotation.Keep;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;

//The @Keep annotation ensures that the names of the functions/variables in the release keystore will not change due to obfuscating. That way, if you have a debug keystore, and create an account, and then log out and connect from a release keystore, it will know how to create the objects.
@Keep
class User extends UserCreatedCloudObject {
    @Keep
    private String email;
    @Keep
    public User(HashMap<String, Object> userProperties, String email) {
        super(userProperties);
        this.email = email;
    }

    @Keep
    public String getEmail() {
        return email;
    }

    @Keep
    public User() {
        super(new HashMap<>());
    }

}
