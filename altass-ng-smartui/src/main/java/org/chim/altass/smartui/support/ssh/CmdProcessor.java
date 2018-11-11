/**
 * Project: x-framework
 * Package Name: org.ike.monitor.support.ssh
 * Author: Xuejia
 * Date Time: 2016/10/10 9:15
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.smartui.support.ssh;

import javax.websocket.Session;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Class Name: CmdProcessor
 * Create Date: 2016/10/10 9:15
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class CmdProcessor extends Thread {

    private BufferedWriter writer = null;
    private Session socketSession = null;

    public CmdProcessor(OutputStream outputStream, Session session) {
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        this.socketSession = session;
    }

    @Override
    public void run() {
        String line;
//        try {
//            writer
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
