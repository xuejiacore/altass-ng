package org.chim.altass.core.streaming;

import org.chim.altass.core.constant.StreamData;

/**
 * Class Name: DataTransformer
 * Create Date: 11/3/18 5:15 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface IAltassChannel {

    void open();

    void connectPrevious(String precursorId);

    void connectSuccessor(String successorsId);

    void publish(StreamData data);

    byte[] receive();

    void close();

    void close(String tag);

    ITransaction getTransaction();
}
