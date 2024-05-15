package or.nevet.multiplayergame.data_objects;

import java.io.Serializable;

public class User implements Serializable {
    private final String identifier;

    public User(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

}
