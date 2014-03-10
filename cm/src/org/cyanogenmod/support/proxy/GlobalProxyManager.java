package org.cyanogenmod.support.proxy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.ProxyProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

public final class GlobalProxyManager {

    public static final String GLOBAL_PROXY_MANAGEMENT_PERMISSION =
            "cyanogenmod.permission.GLOBAL_PROXY_MANAGEMENT";
    public static final String PROXY_CHANGE_ACTION =
            "cyanogenmod.intent.action.GLOBAL_PROXY_STATE_CHANGED";

    public static final String PROXY_CHANGE_PACKAGE_NAME_EXTRA = "package_name";
    public static final int REQUEST_GLOBAL_PROXY = 1;

    private ConnectivityManager mConnectivityManager;
    private Context mContext;

    public GlobalProxyManager(Context context) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private void enforceGlobalProxyPermission() {
        mContext.enforceCallingOrSelfPermission(GLOBAL_PROXY_MANAGEMENT_PERMISSION,
                GlobalProxyManager.class.getSimpleName());
    }

    private void ensureCurrentManager() {
        if (!isPackageCurrentManager()) {
            String msg = "package %s is not allowed to manage the global proxy";
            throw new SecurityException(String.format(msg, mContext.getBasePackageName()));
        }
    }

    public boolean isPackageCurrentManager() {
        ContentResolver res = mContext.getContentResolver();
        String currentPackage = Settings.Global.getString(res, Settings.Global.GLOBAL_PROXY_PACKAGE_NAME);
        return mContext.getBasePackageName().equalsIgnoreCase(currentPackage);
    }

    public void requestGlobalProxy(Activity activity) {
        Intent i = new Intent();
        i.setClassName("com.android.settings", "com.android.settings.cyanogenmod.GlobalProxyPopupActivity");
        activity.startActivityForResult(i, REQUEST_GLOBAL_PROXY);
    }

    /**
     * Set a network-independent global http proxy.  This is not normally what you want
     * for typical HTTP proxies - they are general network dependent.  However if you're
     * doing something unusual like general internal filtering this may be useful.  On
     * a private network where the proxy is not accessible, you may break HTTP using this.
     *
     * @param proxyProperties The a {@link CmProxyProperites} object defining the new global
     *        HTTP proxy.  A {@code null} value will clear the global HTTP proxy.
     *
     * <p>This method requires the call to hold the permission
     * {@link cyanogenmod.permission.GLOBAL_PROXY_MANAGEMENT}.
     */
    public void setGlobalProxy(CmProxyProperties p) {
        enforceGlobalProxyPermission();
        ensureCurrentManager();
        mConnectivityManager.setGlobalProxy(p.getProxyObject());
    }

    /**
     * Retrieve any network-independent global HTTP proxy.
     *
     * @return {@link CmProxyProperties} for the current global HTTP proxy or {@code null}
     *        if no global HTTP proxy is set.
     *
     * <p>This method requires the call to hold the permission
     * {@link cyanogenmod.permission.GLOBAL_PROXY_MANAGEMENT}.
     */
    public CmProxyProperties getGlobalProxy() {
        enforceGlobalProxyPermission();
        ensureCurrentManager();
        CmProxyProperties props = new CmProxyProperties();
        props.setProxyObject(mConnectivityManager.getGlobalProxy());
        return props;
    }

}
