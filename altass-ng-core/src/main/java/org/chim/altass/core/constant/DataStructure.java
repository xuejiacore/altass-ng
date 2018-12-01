package org.chim.altass.core.constant;

/**
 * Class Name: DataStructure
 * Create Date: 11/24/18 1:07 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * Transfer data structure constant
 */
public interface DataStructure {
    byte JSON = 1;              // Standard Json Data Structure
    byte XML = 2;               // Standard Xml Data Structure
    byte BYTE = 3;              // Pure Bytes Data
    byte ROW = 4;               // Row Data, Such as Text
    byte SERIALIZABLE = 5;      // Java Serializable bytes
}
