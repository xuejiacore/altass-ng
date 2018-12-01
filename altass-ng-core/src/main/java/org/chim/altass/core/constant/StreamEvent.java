package org.chim.altass.core.constant;

/**
 * Class Name: StreamEvent
 * Create Date: 11/27/18 8:43 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public interface StreamEvent {

    byte EVENT_START = 1;
    byte EVENT_FINISHED = -1;
    byte EVENT_DATA = 2;
    byte EVENT_GROUP_DATA = 3;                  // Group Data
    byte EVENT_GROUP_FINISHED = 4;              // Group Finished
    byte EVENT_SKIP = 5;
}
