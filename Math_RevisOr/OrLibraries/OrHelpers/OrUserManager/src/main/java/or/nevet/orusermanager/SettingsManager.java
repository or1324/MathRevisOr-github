package or.nevet.orusermanager;

import android.content.Context;

import java.io.IOException;
import java.util.HashMap;

import or.nevet.orgeneralhelpers.constants.FilesConstants;
import or.nevet.orgeneralhelpers.ExternalStorage;

public class SettingsManager {
    private static Settings settings;

    public static SettingsObject getSettingsObject() {
        return new SettingsObject(settings);
    }
    public static void restoreSettings(Context context, HashMap<String, Object> defaultObject) {
        try {
            settings = (Settings) ExternalStorage.restoreObject(context, FilesConstants.settingsFileName);
        } catch (IOException ignored) {
        }
        if (settings == null) {
            settings = new Settings(defaultObject);
        }
    }

    private static void saveSettings(Context context) {
        ExternalStorage.saveObject(context, FilesConstants.settingsFileName, settings);
    }

    static void resetSettings(Context context) {
        settings = null;
        saveSettings(context);
    }

    public static void updateSettingAndSaveInStorage(String settingName, Object settingValue, Context context) {
        settings.setProperty(settingName, settingValue);
        saveSettings(context);
    }
}
