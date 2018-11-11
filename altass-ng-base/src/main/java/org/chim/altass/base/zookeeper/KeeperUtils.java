/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/11/18 19:40
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Class Name: KeeperUtils
 * Create Date: 2016/11/18 19:40
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class KeeperUtils {

    public static ZooKeeper createNewKeeper(String ip, String port, Watcher watcher) throws IOException {
        return new ZooKeeper(ip + ":" + port, 2000, watcher);
    }

    /**
     * 在指定节点之下创建一个数据
     *
     * @param path 节点路径
     * @param data 需要写入的数据
     * @param zk   zk实例
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     */
    public static boolean create(String path, Object data, ZooKeeper zk, CreateMode mode) {
        try {
            zk.create(path, String.valueOf(data).getBytes("UTF-8"), ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
        } catch (KeeperException | InterruptedException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 如果存在则不重新创建
     *
     * @param path 创建的节点的路径
     * @param data 创建节点的数据
     * @param zk   zk实例
     * @param mode 创建的方式
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     */
    public static boolean createIgExist(String path, Object data, ZooKeeper zk, CreateMode mode) {
        try {
            if (zk.exists(path, false) == null) {
                create(path, data, zk, mode);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建一个持久化的节点
     *
     * @param path 节点路径
     * @param data 结点数据
     * @param zk   zk实例
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     */
    public static boolean createPersistent(String path, Object data, ZooKeeper zk) {
        return createIgExist(path, data, zk, CreateMode.PERSISTENT);
    }

    /**
     * 创建一个会话级别的节点
     *
     * @param path 节点路径
     * @param data 节点数据
     * @param zk   zk实例
     * @return 如果创建成功，那么返回值为true，否则返回值为false
     */
    public static boolean createSession(String path, Object data, ZooKeeper zk) {
        return createIgExist(path, data, zk, CreateMode.EPHEMERAL);
    }

    /**
     * 判断某一个路径下的节点是否存在
     *
     * @param path 需要检测的路径
     * @param zk   zk实例
     * @return 如果对应路径下的节点存在，那么返回值为true，否则返回值为false
     */
    public static boolean isNodeExist(String path, ZooKeeper zk) {
        try {
            return zk.exists(path, false) != null;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
