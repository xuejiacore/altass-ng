package org.chim.altass.core.manager;

import org.chim.altass.base.utils.platform.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class Name: AssembleCenter
 * Create Date: 2017/9/15 19:55
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * The center to assemble resources.
 * <p>
 * This class should be a common util which can copy resource or source from jar to the web root or other location to use
 * jar default configuration or source file.
 */
@Component(value = "assembleCenter")
public class AssembleCenter implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(AssembleCenter.class);

    /**
     * resources from jar that will be unzip to target direction
     */
    private List<String> resources = null;

    /**
     * the target direction will be unzip to
     */
    private String output = null;

    // tag load first time
    private static boolean isFirstTime = true;

    /**
     * to initialize assemble center
     */
    public AssembleCenter() {

    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @throws Exception -
     */
    @SuppressWarnings("unused")
    @Override
    public void afterPropertiesSet() throws Exception {
        if (isFirstTime && resources != null) {
            isFirstTime = false;

            // Obtain WebRoot from web.xml's spring configuration to locate unzip target direction.
            String rootPath = System.getProperty("web.root");
            if (rootPath == null || rootPath.length() == 0) {
                return;
            }

            // Obtain jar resources unzip direction.
            String outputTarget = rootPath + File.separator +
                    (output.startsWith("/") || output.startsWith("\\") ? output.substring(1) : output) +
                    (output.endsWith("/") || output.endsWith("\\") ? "" : File.separator);

            for (String resource : resources) {
                logger.info("Unzip resources from jar resources, from: " + resource);
                if (resource.startsWith("classpath:")) {
                    // parse resource location
                    resource = resource.replace("classpath:", "").replace(".", "/");
                    // output direction
                    String outputDirStr = outputTarget + (resource.startsWith("/") || resource.startsWith("\\") ? resource.substring(1) : resource);
                    File outputDir = new File(outputDirStr);
                    if (!outputDir.exists()) {
                        boolean ignored = outputDir.mkdirs();
                    }

                    Resource res = new ClassPathResource(resource);
                    String uri = res.getURI().toString();
                    uri += uri.endsWith("/") ? "" : "/";

                    int lastIndexOf = uri.lastIndexOf(".jar!/");
                    String innerPath = uri.substring(lastIndexOf + ".jar!/".length(), uri.length());
                    String substring = uri.substring(0, lastIndexOf + ".jar!".length() - 1);
                    if (substring.startsWith("jar:")) {
                        substring = substring.replace("jar:", "");
                        if (OSUtil.isWindows()) {
                            substring = substring.replace("file:\\", "")
                                    .replace("file:/", "");
                        } else {
                            substring = substring.replace("file:", "");
                        }
                    }

                    if (uri.contains("jar")) {
                        JarFile jarFile = new JarFile(substring);

                        // obtain jar files and copy to target direction
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry jarEntry = entries.nextElement();
                            String entryName = jarEntry.toString();
                            if (!innerPath.equals(entryName) && entryName.startsWith(innerPath)) {

                                // final target file absolute path
                                String targetFile = !outputTarget.endsWith(File.separator) ? outputTarget + File.separator : outputTarget;

                                targetFile += jarEntry.toString();

                                logger.info("\nUnzip icon resource from [" + jarFile.getName() + "!" + jarEntry.toString() + "] to [" + targetFile + "]");

                                // copy to target direction
                                FileCopyUtils.copy(jarFile.getInputStream(jarEntry), new FileOutputStream(targetFile));
                            }
                        }
                    }
                }
            }

            logger.info("Unzip resources to target direction had finished.");
        }
    }
}
