package org.chim.altass.core.manager;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Class Name: ConnectorManager
 * Create Date: 2017/9/14 18:55
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ConnectorManager {

    private static ConnectorManager connectorManager = null;

    private ConnectionFactory connectionFactory = null;
    private String host = "10.9.96.182";
    private int port = 5672;

    private ConnectorManager() {
        this.connectionFactory = new ConnectionFactory();
        this.connectionFactory.setHost(host);
        this.connectionFactory.setPort(port);
    }

    public static ConnectorManager getFactory() {
        if (connectorManager == null) {
            synchronized (ConnectorManager.class) {
                connectorManager = new ConnectorManager();
            }
        }
        return connectorManager;
    }

    public Connection getConnection() {
        try {
            return this.connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}
