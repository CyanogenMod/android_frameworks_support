package org.cyanogenmod.support.proxy;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ProxyProperties;
import android.provider.Settings;

public final class GlobalProxyManager {

    public static final String PROXY_CHANGE_ACTION = "cyanogenmod.intent.action.GLOBAL_PROXY_STATE_CHANGED";
    public static final String PROXY_CHANGE_PACKAGE_NAME_EXTRA = "package_name";

    private ConnectivityManager mConnectivityManager;
    private Context mContext;

    public GlobalProxyManager(Context context) {
        mContext = context;
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Set a network-independent global http proxy.  This is not normally what you want
     * for typical HTTP proxies - they are general network dependent.  However if you're
     * doing something unusual like general internal filtering this may be useful.  On
     * a private network where the proxy is not accessible, you may break HTTP using this.
     *
     * @param proxyProperties The a {@link ProxyProperites} object defining the new global
     *        HTTP proxy.  A {@code null} value will clear the global HTTP proxy.
     *
     * <p>This method requires the call to hold the permission
     * {@link cyanogenmod.permission.GLOBAL_PROXY_MANAGEMENT}.
     */
    public void setGlobalProxy(ProxyProperties p) {
        enforceGlobalProxyPermission();
        ensureCurrentManager();
        mConnectivityManager.setGlobalProxy(p);
    }

    /**
     * Retrieve any network-independent global HTTP proxy.
     *
     * @return {@link ProxyProperties} for the current global HTTP proxy or {@code null}
     *        if no global HTTP proxy is set.
     *
     * <p>This method requires the call to hold the permission
     * {@link cyanogenmod.permission.GLOBAL_PROXY_MANAGEMENT}.
     */
    public ProxyProperties getGlobalProxy() {
        enforceGlobalProxyPermission();
        ensureCurrentManager();
        return mConnectivityManager.getGlobalProxy();
    }

    private void enforceGlobalProxyPermission() {
        mContext.enforceCallingOrSelfPermission(
                "cyanogenmod.permission.GLOBAL_PROXY_MANAGEMENT",
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
}
