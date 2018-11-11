package org.chim.altass.node;

import org.chim.altass.core.ansi.AnsiColor;
import org.chim.altass.core.ansi.AnsiOutput;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class Name: ServiceRunner
 * Create Date: 18-2-6 下午1:36
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * eureka 集群对等节点的启动入口
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class AltassNode {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"classpath:/spring/eureka-core-service-dubbo-provider.xml"}
        );

        context.start();

        // Initialize mop starter banner.
        InputStream bannerStream = AltassNode.class.getClassLoader().getResourceAsStream("META-INF/banner.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(bannerStream));
        String line;
        StringBuilder builder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Fill banner variable.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        String BANNER_TEXT = builder.toString()
                .replaceAll("\\(\\$\\[timestamp\\] ", "(" + dateFormat.format(new Date()) + " vCompile")
                .replaceAll("v\\$\\[mop.starter.version\\]", "");

        System.out.println(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, BANNER_TEXT, AnsiColor.DEFAULT));
        System.in.read();
    }
}
