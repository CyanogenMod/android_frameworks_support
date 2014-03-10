package org.cyanogenmod.support.proxy;

import android.net.ProxyProperties;

public final class CmProxyProperties {

    ProxyProperties mProxyObject;

    protected CmProxyProperties() {}

    public CmProxyProperties(String host, int port, String exclList) {
        mProxyObject = new ProxyProperties(host, port, exclList);
    }

    void setProxyObject(ProxyProperties proxyProperties) {
        mProxyObject = proxyProperties;
    }

    ProxyProperties getProxyObject() {
        return mProxyObject;
    }

    public String getHost() {
        return mProxyObject.getHost();
    }

    public int getPort() {
        return mProxyObject.getPort();
    }

    public String getExclusionList() {
        return mProxyObject.getExclusionList();
    }

    public boolean isExcluded(String url) {
        return mProxyObject.isExcluded(url);
    }

    public boolean isValid() {
        return mProxyObject.isValid();
    }

    public java.net.Proxy makeProxy() {
        return mProxyObject.makeProxy();
    }

    @Override
    public String toString() {
        return mProxyObject.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CmProxyProperties)) return false;
        return mProxyObject.equals(((CmProxyProperties) o).mProxyObject);
    }

    @Override
    public int hashCode() {
        return mProxyObject.hashCode();
    }
}
