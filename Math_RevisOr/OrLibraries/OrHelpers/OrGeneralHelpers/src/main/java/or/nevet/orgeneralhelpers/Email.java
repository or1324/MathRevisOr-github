package or.nevet.orgeneralhelpers;

import androidx.annotation.NonNull;

public class Email {
    String email;
    public Email(String email) {
        this.email = email.toLowerCase();
    }

    @NonNull
    @Override
    public String toString() {
        return email;
    }
}
