package or.nevet.math_revisor.helpers;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.util.List;

import or.nevet.orexercises.main.ExerciseService;
import or.nevet.orgeneralhelpers.constants.UserMessagesConstants;

public class Permissions {

    public static void checkForDrawPermissions(Context context) {
        if (!android.provider.Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }


    public static AlertDialog checkForAccessibilityPermissions(Context context) {
            if (!isAccessibilityServiceEnabled(context, ExerciseService.class)) {
                return new AlertDialog.Builder(context).setMessage(UserMessagesConstants.allowAccessibilityService).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /** if not construct intent to request permission */
                        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        /** request permission via start activity for result */
                        context.startActivity(intent);
                    }
                }).setNegativeButton("No", (dialogInterface, i) -> {
                }).setOnDismissListener(dialogInterface -> {
                }).show();
            }
            return null;
    }

    public static void askForPermission(String permission, Activity activity,ActivityResultLauncher<String> permissionLauncher) {
        if (activity.shouldShowRequestPermissionRationale(permission)) {
            new android.app.AlertDialog.Builder(activity)
                    .setTitle("Please allow the following permission")
                    .setMessage("I need it for you to be able to use the app")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Ask for the permission once explanation has been shown and accepted
                            permissionLauncher.launch(permission);
                        }
                    })
                    .create()
                    .show();
        } else
            permissionLauncher.launch(permission);
    }

    private static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        try {
            int accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            if (accessibilityEnabled == 1)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
                return true;
        }

        return false;
    }
}
