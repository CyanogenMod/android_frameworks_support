package org.cyanogenmod.support.proxy;

import android.content.Context;
import android.content.Intent;

public class Util {

    public static void broadcastProxyStateChange(Context ctx, String pkgName) {
        Intent i = new Intent(GlobalProxyManager.PROXY_CHANGE_ACTION);
        i.putExtra(GlobalProxyManager.PROXY_CHANGE_PACKAGE_NAME_EXTRA, pkgName);
        ctx.sendBroadcast(i, GlobalProxyManager.GLOBAL_PROXY_MANAGEMENT_PERMISSION);
    }

}
