package org.chim.altass.core;

/**
 * Class Name: PauseListener
 * Create Date: 18-3-13 下午8:03
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface PauseListener {

    void onPausing(String nodeId);

    void onPaused(String nodeId, Integer total);
}
