package org.cyanogenmod.support.proxy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Util {

    public static String getApplicationName(Context ctx, String pkgName) {
        PackageManager manager = ctx.getPackageManager();
        String applicationName = null;
        try {
            applicationName = (String) manager.getApplicationInfo(pkgName, 0)
                    .loadLabel(manager);
        } catch (NameNotFoundException ignore) {
        }
        return applicationName;
    }

    public static void broadcastProxyStateChange(Context ctx, String pkgName) {
        Intent i = new Intent(GlobalProxyManager.PROXY_CHANGE_ACTION);
        i.putExtra(GlobalProxyManager.PROXY_CHANGE_PACKAGE_NAME_EXTRA, pkgName);
        ctx.sendBroadcast(i, GlobalProxyManager.GLOBAL_PROXY_MANAGEMENT_PERMISSION);
    }

}
