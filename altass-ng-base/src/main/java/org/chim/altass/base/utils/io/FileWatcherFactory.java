/**
 * Project: x-framework
 * Package Name: org.ike.utils.io
 * Author: Xuejia
 * Date Time: 2016/12/14 18:30
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.io;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class Name: FileWatcherFactory
 * Create Date: 2016/12/14 18:30
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 文件侦测工厂类
 */
public class FileWatcherFactory {
    private static final int MAX_THREADS = 20;                                                      // 最大线程数量，默认为20个
    private ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);            // 线程池服务
    private WatchService watchService = null;
    private String[] listenerPath = null;                                                           // 需要侦测的路径数组
    private FileChangeListener changeListener = null;                                               // 侦测监听
    private static HashMap<String, FileWatcherFactory> watchers = new HashMap<>();                  // 文件观察者集合

    /**
     * 文件改变监听器
     */
    public interface FileChangeListener {
        /**
         * 指定目录文件改变回调
         *
         * @param path    改变的目录
         * @param context 改变的上下文信息
         * @param kind    文件变化类型
         * @param count   文件变化的数量
         */
        void onChange(String path, Object context, Object kind, int count);
    }

    /**
     * 侦测方法
     *
     * @param listener 文件改变监听器
     * @param path     需要监听的路径数组
     * @throws IOException
     */
    public static FileWatcherFactory watch(FileChangeListener listener, String... path) throws IOException {
        return new FileWatcherFactory(listener, path);
    }

    /**
     * 文件监听工厂构造函数
     *
     * @param listener 文件改变监听器
     * @param paths    需要监听的路径数组
     * @throws IOException
     */
    private FileWatcherFactory(FileChangeListener listener, String... paths) throws IOException {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            this.changeListener = listener;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.listenerPath = paths;
        for (String path : listenerPath) {
            Path p = Paths.get(path);
            p.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE);
            executorService.execute(new WatchListener(watchService, path));
        }
    }

    /**
     * 停止所有的监控
     *
     * @return 如果停止成功，那么返回值为true，否则返回值为false
     */
    public boolean shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            System.err.println("execute shutdown");
            executorService.shutdownNow();
            return executorService.isTerminated();
        }
        return false;
    }

    /**
     * 文件监听数组
     */
    private class WatchListener implements Runnable {
        private WatchService service;
        private String rootPath;

        public WatchListener(WatchService service, String rootPath) {
            this.service = service;
            this.rootPath = rootPath;
        }

        public void run() {
            try {
                while (true) {
                    Thread.sleep(5000);
                    WatchKey watchKey = service.take();
                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                    for (WatchEvent<?> event : watchEvents) {
                        changeListener.onChange(rootPath, event.context(), event.kind(), event.count());
                    }
                    watchKey.reset();
                }
            } catch (InterruptedException e) {
                System.err.println("文件监视器关闭了");
            } finally {
                try {
                    service.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String[] getListenerPath() {
        return listenerPath;
    }

    public void setListenerPath(String[] listenerPath) {
        this.listenerPath = listenerPath;
    }
}
