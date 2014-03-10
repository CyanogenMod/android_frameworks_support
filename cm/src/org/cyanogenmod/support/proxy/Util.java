package org.cyanogenmod.support.proxy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * @hide
 */
public class Util {

    public static void broadcastProxyStateChange(Context ctx, String pkgName) {
        Intent i = new Intent(GlobalProxyManager.PROXY_CHANGE_ACTION);
        i.putExtra(GlobalProxyManager.PROXY_CHANGE_PACKAGE_NAME_EXTRA, pkgName);
        ctx.sendBroadcast(i, GlobalProxyManager.GLOBAL_PROXY_MANAGEMENT_PERMISSION);
    }

    public static void resetGlobalProxyIfOwnerRemoved(Context context) {
        ContentResolver res = context.getContentResolver();
        String currentPackage = Settings.Global.getString(res, Settings.Global.GLOBAL_PROXY_PACKAGE_NAME);
        if (!TextUtils.isEmpty(currentPackage)) {
            PackageManager manager = context.getPackageManager();
            try {
                manager.getPackageInfo(currentPackage, 0);
            } catch (NameNotFoundException e) {
                Settings.Global.putString(res, Settings.Global.GLOBAL_PROXY_PACKAGE_NAME, null);
            }
        }
    }
}
