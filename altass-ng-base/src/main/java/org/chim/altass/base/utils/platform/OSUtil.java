package org.chim.altass.base.utils.platform;

public class OSUtil {

    private static String OS = System.getProperty("os.name").toLowerCase();

    private static OSUtil _instance = new OSUtil();

    private OSType platform;

    private OSUtil() {
    }

    public static boolean isLinux() {
        return OS.contains("linux");
    }

    public static boolean isMacOS() {
        return OS.contains("mac") && OS.indexOf("os") > 0 && !OS.contains("x");
    }

    public static boolean isMacOSX() {
        return OS.contains("mac") && OS.indexOf("os") > 0 && OS.indexOf("x") > 0;
    }

    public static boolean isWindows() {
        return OS.contains("windows");
    }

    public static boolean isOS2() {
        return OS.contains("os/2");
    }

    public static boolean isSolaris() {
        return OS.contains("solaris");
    }

    public static boolean isSunOS() {
        return OS.contains("sunos");
    }

    public static boolean isMPEiX() {
        return OS.contains("mpe/ix");
    }

    public static boolean isHPUX() {
        return OS.contains("hp-ux");
    }

    public static boolean isAix() {
        return OS.contains("aix");
    }

    public static boolean isOS390() {
        if (OS.contains("os/390")) return true;
        else return false;
    }

    public static boolean isFreeBSD() {
        return OS.contains("freebsd");
    }

    public static boolean isIrix() {
        return OS.contains("irix");
    }

    public static boolean isDigitalUnix() {
        return OS.contains("digital") && OS.indexOf("unix") > 0;
    }

    public static boolean isNetWare() {
        return OS.contains("netware");
    }

    public static boolean isOSF1() {
        return OS.contains("osf1");
    }

    public static boolean isOpenVMS() {
        return OS.contains("openvms");
    }

    /**
     * 获取操作系统名字
     *
     * @return 操作系统名
     */
    public static OSType getOSname() {
        if (isAix()) {
            _instance.platform = OSType.AIX;
        } else if (isDigitalUnix()) {
            _instance.platform = OSType.Digital_Unix;
        } else if (isFreeBSD()) {
            _instance.platform = OSType.FreeBSD;
        } else if (isHPUX()) {
            _instance.platform = OSType.HP_UX;
        } else if (isIrix()) {
            _instance.platform = OSType.Irix;
        } else if (isLinux()) {
            _instance.platform = OSType.Linux;
        } else if (isMacOS()) {
            _instance.platform = OSType.Mac_OS;
        } else if (isMacOSX()) {
            _instance.platform = OSType.Mac_OS_X;
        } else if (isMPEiX()) {
            _instance.platform = OSType.MPEiX;
        } else if (isNetWare()) {
            _instance.platform = OSType.NetWare_411;
        } else if (isOpenVMS()) {
            _instance.platform = OSType.OpenVMS;
        } else if (isOS2()) {
            _instance.platform = OSType.OS2;
        } else if (isOS390()) {
            _instance.platform = OSType.OS390;
        } else if (isOSF1()) {
            _instance.platform = OSType.OSF1;
        } else if (isSolaris()) {
            _instance.platform = OSType.Solaris;
        } else if (isSunOS()) {
            _instance.platform = OSType.SunOS;
        } else if (isWindows()) {
            _instance.platform = OSType.Windows;
        } else {
            _instance.platform = OSType.Others;
        }
        return _instance.platform;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(OSUtil.getOSname());
    }

}  