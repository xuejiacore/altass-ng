package org.chim.altass.toolkit;


import org.chim.altass.base.exception.XmlParserException;
import org.chim.altass.base.parser.EXmlParser;
import org.chim.altass.core.configuration.AltassSiteConfiguration;
import org.chim.altass.core.configuration.GlobalVars;

import java.io.IOException;

/**
 * Class Name: SysConfigManager
 * Create Date: 2017/9/6 21:18
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class SysConfigManager {

    private AltassSiteConfiguration configuration = null;

    private static SysConfigManager configManager = null;

    private SysConfigManager() {
        String xmlFilePath = GlobalVars.ETL_SITE_CONFIG_PATH;
        try {
            configuration = EXmlParser.readFrom(xmlFilePath, AltassSiteConfiguration.class);
        } catch (XmlParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public static SysConfigManager getInstance() {
        if (configManager == null) {
            synchronized (SysConfigManager.class) {
                if (configManager == null) {
                    configManager = new SysConfigManager();
                }
            }
        }
        return configManager;
    }

    public AltassSiteConfiguration getConfiguration() {
        return configuration;
    }
}
