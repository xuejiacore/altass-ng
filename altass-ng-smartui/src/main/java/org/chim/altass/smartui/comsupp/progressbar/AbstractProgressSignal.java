/**
 * Project: x-framework
 * Package Name: org.ike.monitor.comsupp.progressbar
 * Author: Xuejia
 * Date Time: 2016/10/9 13:09
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.comsupp.progressbar;

import org.chim.altass.smartui.comsupp.face.IInterruptListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: AbstractProgressSignal
 * Create Date: 2016/10/9 13:09
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description: 抽象进度信号类
 * 进度信号的作用是获取进度信息
 */
public abstract class AbstractProgressSignal<T> implements IProgressListener, IInterruptListener {

    private ProgressJob<T> progressJob = null;

    private boolean interrupted = false;

    public AbstractProgressSignal(ProgressJob<T> job) {
        this.progressJob = job;
        onBegin(this.progressJob.getJobMeta());
    }

    /**
     * 进度开始
     */
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<T> data = progressJob.getJobData();
                if (data == null) {
                    onEnd(null);
                    return;
                }
                int dSize = data.size();
                double currIdx = 0;
                for (Object d : progressJob.getJobData()) {
                    onProcess(currIdx++ / dSize, d);
                    if (interrupted) {
                        onInterrupt(d);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                onEnd(null);
            }
        }).start();
    }

    /**
     * 结束进度
     */
    public void stop() {
        this.interrupted = true;
    }

    @Override
    public boolean onInterrupt(Object data) {
        return true;
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("data" + i);
        }
        AbstractProgressSignal<String> progressSignal = new AbstractProgressSignal<String>(new ProgressJob<String>(data)) {

            @Override
            public void onBegin(Object data) {
                System.err.println("作业进度开始");
            }

            @Override
            public boolean onProcess(double percentage, Object data) {
                System.err.println("当前进度：" + percentage + " 数据：" + data);
                return true;
            }

            @Override
            public boolean onEnd(Object data) {
                System.err.println("作业进度结束");
                return true;
            }
        };
        System.out.println("==================1");
        progressSignal.start();
        System.out.println("==================2");
    }
}
