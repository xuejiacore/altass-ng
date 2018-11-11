/**
 * Project: x-framework
 * Package Name: org.ike.core
 * Author: Xuejia
 * Date Time: 2016/10/9 16:35
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base;

import java.util.HashMap;

/**
 * Class Name: ConfigHotSwapper
 * Create Date: 2016/10/9 16:35
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:配置热配置控制器
 */
public class ConfigHotSwapper {
    private static HashMap<String, IConfSwap> confSwapMap = new HashMap<>();

    public static void add2Monitor(String tag, IConfSwap confSwap) {
        confSwapMap.put(tag, confSwap);
    }

    public static boolean reloadConf(String key) {
        return true;
    }

    public static void reloadAllConf() {
        confSwapMap.values().forEach(IConfSwap::hotSwap);
    }
}
