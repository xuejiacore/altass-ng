/*
 * Project: x-framework
 * Package Name: org.ike.core.ftp
 * Author: Xuejia
 * Date Time: 2017/3/7 14:27
 * Copyright: 2016 www.zigui.site. All rights reserved.
 */
package org.chim.altass.base.ftp;

/**
 * Class Name: FtpFactory
 * Create Date: 2017/3/7 14:27
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * FTP工厂，创建FTP以及SFTP
 */
public class FtpFactory {
    public static final String TYPE_FTP = "FTP";
    public static final String TYPE_SFTP = "SFTP";

    public static IFtp get(String type) throws Exception {
        if (type.toUpperCase().equals(TYPE_FTP)) {
            return new FTPImpl();
        } else if (type.toUpperCase().equals(TYPE_SFTP)) {
            return new SFTPImpl();
        } else {
            throw new Exception("没有找到该FTP类型");
        }
    }
}
