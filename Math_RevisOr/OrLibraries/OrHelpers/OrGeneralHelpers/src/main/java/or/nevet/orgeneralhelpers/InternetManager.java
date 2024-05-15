package or.nevet.orgeneralhelpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

public class InternetManager {

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = mgr.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = mgr.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
    }
}
