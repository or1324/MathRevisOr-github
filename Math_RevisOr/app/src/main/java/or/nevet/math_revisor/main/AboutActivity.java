package or.nevet.math_revisor.main;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;

import or.nevet.math_revisor.R;
import or.nevet.math_revisor.helpers.AppGraphics;
import or.nevet.math_revisor.helpers.Permissions;
import or.nevet.orgeneralhelpers.graphical.UserMessages;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;
import or.nevet.orgeneralhelpers.music_related.MusicSubActivity;

public class AboutActivity extends MusicSubActivity {

    public AppCompatButton sms;
    public AppCompatImageButton back;
    private final ActivityResultLauncher<String> smsPermissionResult = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result)
                sendSMS();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        AppGraphics.initInfo(this);
    }

    public void tryToSendSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Permissions.askForPermission(Manifest.permission.SEND_SMS, this, smsPermissionResult);
        } else {
            sendSMS();
        }
    }

    public void sendSMS() {
        UserMessages.showButtonDialogQuestion(UserMessagesConstants.doYouWantToSendMessage, this, () -> {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(getString(R.string.my_phone_number), null, UserMessagesConstants.smsText, null, null);
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:"));
            smsIntent.putExtra("address", getString(R.string.my_phone_number));
            smsIntent.putExtra("sms_body",UserMessagesConstants.draftSMSText);
            startActivity(smsIntent);
        });
    }
}