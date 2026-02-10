package com.my.mybatis.executor.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * 代理连接
 */
public class PooledConnection implements InvocationHandler {

    private Connection connection;

    private Connection proxyConnection;

    private PooledDataSource pooledDataSource;

    public PooledConnection(PooledDataSource pooledDataSource, Connection connection) {
        this.connection = connection;
        this.pooledDataSource = pooledDataSource;
    }

    public Connection getProxy() {
        Connection proxy = (Connection) Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                this
        );
        this.proxyConnection = proxy;
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("close")) {
            pooledDataSource.returnConnection(proxyConnection);
        } else {
            return method.invoke(connection, args);
        }
        return null;
    }
}
