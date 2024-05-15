package or.nevet.orgeneralhelpers.background_running_related.exceptions;

import androidx.annotation.Nullable;

import or.nevet.orgeneralhelpers.constants.GeneralConstants;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;

public class NotEnoughSpaceForTimerException extends RuntimeException {
    @Nullable
    @Override
    public String getMessage() {
        return UserMessagesConstants.notEnoughSpaceForTimer;
    }
}
