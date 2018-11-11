/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/10 18:51
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils.type;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chim.altass.base.utils.AssertUtil;

import java.beans.Introspector;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class Name: ClassUtil
 * Create Date: 2016/12/10 18:51
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ClassUtil {
    private static final Log logger = LogFactory.getLog(ClassUtil.class);
    public static final String ARRAY_SUFFIX = "[]";
    private static final String INTERNAL_ARRAY_PREFIX = "[L";
    private static final char PACKAGE_SEPARATOR = '.';
    private static final char INNER_CLASS_SEPARATOR = '$';
    public static final String CGLIB_CLASS_SEPARATOR = "$$";
    public static final String CLASS_FILE_SUFFIX = ".class";
    private static final Map<Class, Type> primitiveWrapperTypeMap = new HashMap<>(8);
    private static final Map<String, Class> primitiveTypeNameMap = new HashMap<>(16);

    /**
     * 获得默认的ClassLoader
     *
     * @return 返回默认的ClassLoader
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = null;

        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
        }

        return classLoader;
    }

    /**
     * 获取资源的路径
     *
     * @param resourceLocation 资源路径
     * @return 返回资源的URL
     * @throws Throwable
     */
    public static URL getURL(String resourceLocation) throws Throwable {
        if (resourceLocation.startsWith("/")) {
            resourceLocation = resourceLocation.substring(1);
        }

        return getDefaultClassLoader().getResource(resourceLocation);
    }

    /**
     * 覆盖ClassLoader
     *
     * @param classLoaderToUse 需要使用的ClassLoader
     * @return 如果当前ClassLoader与提花你的Class Loader不同，那么进行替换后返回当前的Class Loader
     */
    public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse) {
        Thread currentThread = Thread.currentThread();
        ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
        if (classLoaderToUse != null && !classLoaderToUse.equals(threadContextClassLoader)) {
            currentThread.setContextClassLoader(classLoaderToUse);
            return threadContextClassLoader;
        } else {
            return null;
        }
    }

    /**
     * 根据名称获取类
     *
     * @param name 类名称
     * @return 返回类名称对应的类对象
     * @throws ClassNotFoundException
     * @throws LinkageError
     */
    public static Class forName(String name) throws ClassNotFoundException, LinkageError {
        return forName(name, getDefaultClassLoader());
    }

    /**
     * 根据类的名称（带包名），在指定的Class Loader中获得Class实例
     *
     * @param name        类名称
     * @param classLoader Class Loader
     * @return 返回类名称对应的Class实例
     * @throws ClassNotFoundException
     * @throws LinkageError
     */
    public static Class forName(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
        AssertUtil.notNull(name, "Name must not be null");
        Class clazz = resolvePrimitiveClassName(name);
        if (clazz != null) {
            return clazz;
        } else if (name.endsWith("[]")) {
            String internalArrayMarker1 = name.substring(0, name.length() - "[]".length());
            Class classLoaderToUse2 = forName(internalArrayMarker1, classLoader);
            return Array.newInstance(classLoaderToUse2, 0).getClass();
        } else {
            int internalArrayMarker = name.indexOf("[L");
            if (internalArrayMarker != -1 && name.endsWith(";")) {
                String classLoaderToUse1 = null;
                if (internalArrayMarker == 0) {
                    classLoaderToUse1 = name.substring("[L".length(), name.length() - 1);
                } else if (name.startsWith("[")) {
                    classLoaderToUse1 = name.substring(1);
                }

                Class elementClass = forName(classLoaderToUse1, classLoader);
                return Array.newInstance(elementClass, 0).getClass();
            } else {
                ClassLoader classLoaderToUse = classLoader;
                if (classLoader == null) {
                    classLoaderToUse = getDefaultClassLoader();
                }

                return classLoaderToUse.loadClass(name);
            }
        }
    }

    public static Class resolveClassName(String className, ClassLoader classLoader) throws IllegalArgumentException {
        IllegalArgumentException iae;
        try {
            return forName(className, classLoader);
        } catch (ClassNotFoundException classNotFound) {
            throw new IllegalArgumentException("Cannot find class [" + className + "]", classNotFound);
        } catch (LinkageError linkageError) {
            throw new IllegalArgumentException("Error loading class [" + className + "]: problem with class file or dependent class.", linkageError);
        }
    }

    public static Class resolvePrimitiveClassName(String name) {
        Class result = null;
        if (name != null && name.length() <= 8) {
            result = (Class) primitiveTypeNameMap.get(name);
        }

        return result;
    }

    /**
     * 判断类名是否能够在当前的Class Loader中找到
     *
     * @param className 需要判断的Class
     * @return 如果类名在当前的Class Loader中，那么返回值为true，否则返回值为false
     */
    public static boolean isPresent(String className) {
        return isPresent(className, getDefaultClassLoader());
    }

    /**
     * 判断类名是否能够在指定的Class Loader中找到
     *
     * @param className   需要判断的Class
     * @param classLoader Class Loader
     * @return 如果类名在当前的Class Loader中，那么返回值为true，否则返回值为false
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            forName(className, classLoader);
            return true;
        } catch (Throwable var3) {
            return false;
        }
    }

    public static Class getUserClass(Object instance) {
        AssertUtil.notNull(instance, "Instance must not be null");
        return getUserClass(instance.getClass());
    }

    public static Class getUserClass(Class clazz) {
        return clazz != null && clazz.getName().contains("$$") ? clazz.getSuperclass() : clazz;
    }

    /**
     * 判断类是否是缓存安全的
     *
     * @param clazz       类
     * @param classLoader Class Loader
     * @return 如果是安全的，那么返回值为true，否则返回值为false
     */
    public static boolean isCacheSafe(Class clazz, ClassLoader classLoader) {
        AssertUtil.notNull(clazz, "Class must not be null");
        ClassLoader target = clazz.getClassLoader();
        if (target == null) {
            return false;
        } else {
            ClassLoader cur = classLoader;
            if (classLoader == target) {
                return true;
            } else {
                do {
                    if (cur == null) {
                        return false;
                    }
                    cur = cur.getParent();
                } while (cur != target);
                return true;
            }
        }
    }

    /**
     * 获得类名的短名称
     *
     * @param className Class Name
     * @return 返回类名对应的短名称
     */
    public static String getShortName(String className) {
        AssertUtil.hasLength(className, "Class name must not be empty");
        int lastDotIndex = className.lastIndexOf(46);
        int nameEndIndex = className.indexOf("$$");
        if (nameEndIndex == -1) {
            nameEndIndex = className.length();
        }

        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
        shortName = shortName.replace('$', '.');
        return shortName;
    }

    /**
     * 获得类名的短名称
     *
     * @param clazz 需要获取短名称的类
     * @return 返回类的短名称
     */
    public static String getShortName(Class clazz) {
        return getShortName(getQualifiedName(clazz));
    }

    /**
     * 获得类名的驼峰命名法规则
     *
     * @param clazz 需要获得驼峰命名的类
     * @return 返回驼峰命名的类
     */
    public static String getShortNameAsProperty(Class clazz) {
        String shortName = getShortName(clazz);
        int dotIndex = shortName.lastIndexOf(46);
        shortName = dotIndex != -1 ? shortName.substring(dotIndex + 1) : shortName;
        return Introspector.decapitalize(shortName);
    }

    /**
     * 获得类的文件名称
     *
     * @param clazz 类
     * @return 返回类的文件名称
     */
    public static String getClassFileName(Class clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(46);
        return className.substring(lastDotIndex + 1) + ".class";
    }

    /**
     * 获得类所在的包名
     *
     * @param clazz 类
     * @return 返回类所在的包名
     */
    public static String getPackageName(Class clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf(46);
        return lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "";
    }

    public static String getQualifiedName(Class clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        return clazz.isArray() ? getQualifiedNameForArray(clazz) : clazz.getName();
    }

    private static String getQualifiedNameForArray(Class clazz) {
        StringBuilder buffer = new StringBuilder();

        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
            buffer.append("[]");
        }

        buffer.insert(0, clazz.getName());
        return buffer.toString();
    }

    public static String getQualifiedMethodName(Method method) {
        AssertUtil.notNull(method, "Method must not be null");
        return method.getDeclaringClass().getName() + "." + method.getName();
    }

    public static String getDescriptiveType(Object value) {
        if (value == null) {
            return null;
        } else {
            Class clazz = value.getClass();
            if (Proxy.isProxyClass(clazz)) {
                StringBuilder buf = new StringBuilder(clazz.getName());
                buf.append(" implementing ");
                Class[] clazzInterfaces = clazz.getInterfaces();

                for (int i = 0; i < clazzInterfaces.length; ++i) {
                    buf.append(clazzInterfaces[i].getName());
                    if (i < clazzInterfaces.length - 1) {
                        buf.append(',');
                    }
                }

                return buf.toString();
            } else {
                return clazz.isArray() ? getQualifiedNameForArray(clazz) : clazz.getName();
            }
        }
    }

    public static boolean hasConstructor(Class clazz, Class[] paramTypes) {
        return getConstructorIfAvailable(clazz, paramTypes) != null;
    }

    public static Constructor getConstructorIfAvailable(Class<?> clazz, Class[] paramTypes) {
        AssertUtil.notNull(clazz, "Class must not be null");

        try {
            return clazz.getConstructor(paramTypes);
        } catch (NoSuchMethodException var3) {
            return null;
        }
    }

    public static boolean hasMethod(Class clazz, String methodName, Class[] paramTypes) {
        return getMethodIfAvailable(clazz, methodName, paramTypes) != null;
    }

    public static Method getMethodIfAvailable(Class<?> clazz, String methodName, Class[] paramTypes) {
        AssertUtil.notNull(clazz, "Class must not be null");
        AssertUtil.notNull(methodName, "Method name must not be null");

        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException var4) {
            return null;
        }
    }

    public static int getMethodCountForName(Class clazz, String methodName) {
        AssertUtil.notNull(clazz, "Class must not be null");
        AssertUtil.notNull(methodName, "Method name must not be null");
        int count = 0;
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (int index = 0; index < declaredMethods.length; ++index) {
            Method method = declaredMethods[index];
            if (methodName.equals(method.getName())) {
                ++count;
            }
        }

        Class[] clazzInterfaces = clazz.getInterfaces();

        for (Class clazzInterface : clazzInterfaces) {
            count += getMethodCountForName(clazzInterface, methodName);
        }

        if (clazz.getSuperclass() != null) {
            count += getMethodCountForName(clazz.getSuperclass(), methodName);
        }

        return count;
    }

    public static boolean hasAtLeastOneMethodWithName(Class clazz, String methodName) {
        AssertUtil.notNull(clazz, "Class must not be null");
        AssertUtil.notNull(methodName, "Method name must not be null");
        Method[] declaredMethods = clazz.getDeclaredMethods();

        for (int index = 0; index < declaredMethods.length; ++index) {
            Method method = declaredMethods[index];
            if (method.getName().equals(methodName)) {
                return true;
            }
        }

        Class[] clazzInterfaces = clazz.getInterfaces();

        for (int i = 0; i < clazzInterfaces.length; ++i) {
            if (hasAtLeastOneMethodWithName(clazzInterfaces[i], methodName)) {
                return true;
            }
        }

        return clazz.getSuperclass() != null && hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName);
    }

    public static Method getMostSpecificMethod(Method method, Class<?> targetClass) {
        if (method != null && targetClass != null && !targetClass.equals(method.getDeclaringClass())) {
            try {
                method = targetClass.getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return method;
    }

    public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>[] args) {
        AssertUtil.notNull(clazz, "Class must not be null");
        AssertUtil.notNull(methodName, "Method name must not be null");

        try {
            Method ex = clazz.getDeclaredMethod(methodName, args);
            if ((ex.getModifiers() & 8) != 0) {
                return ex;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean isPrimitiveWrapper(Class clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

    public static boolean isPrimitiveOrWrapper(Class clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz);
    }

    public static boolean isPrimitiveArray(Class clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        return clazz.isArray() && clazz.getComponentType().isPrimitive();
    }

    public static boolean isPrimitiveWrapperArray(Class clazz) {
        AssertUtil.notNull(clazz, "Class must not be null");
        return clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType());
    }

    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        AssertUtil.notNull(lhsType, "Left-hand side type must not be null");
        AssertUtil.notNull(rhsType, "Right-hand side type must not be null");
        return lhsType.isAssignableFrom(rhsType) || lhsType.equals(primitiveWrapperTypeMap.get(rhsType));
    }

    public static boolean isAssignableValue(Class type, Object value) {
        AssertUtil.notNull(type, "Type must not be null");
        return value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive();
    }

    public static String convertResourcePathToClassName(String resourcePath) {
        return resourcePath.replace('/', '.');
    }

    public static String convertClassNameToResourcePath(String className) {
        return className.replace('.', '/');
    }

    public static String addResourcePathToPackagePath(Class clazz, String resourceName) {
        AssertUtil.notNull(resourceName, "Resource name must not be null");
        return !resourceName.startsWith("/") ? classPackageAsResourcePath(clazz) + "/" + resourceName : classPackageAsResourcePath(clazz) + resourceName;
    }

    public static String classPackageAsResourcePath(Class clazz) {
        if (clazz == null) {
            return "";
        } else {
            String className = clazz.getName();
            int packageEndIndex = className.lastIndexOf(46);
            if (packageEndIndex == -1) {
                return "";
            } else {
                String packageName = className.substring(0, packageEndIndex);
                return packageName.replace('.', '/');
            }
        }
    }

    public static String classNamesToString(Class[] classes) {
        return classNamesToString((Collection) Arrays.asList(classes));
    }

    public static String classNamesToString(Collection classes) {
        if (CollectionUtil.isEmpty(classes)) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder("[");
            Iterator it = classes.iterator();

            while (it.hasNext()) {
                Class clazz = (Class) it.next();
                sb.append(clazz.getName());
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }

            sb.append("]");
            return sb.toString();
        }
    }

    public static Class[] getAllInterfaces(Object instance) {
        AssertUtil.notNull(instance, "Instance must not be null");
        return getAllInterfacesForClass(instance.getClass());
    }

    public static Class[] getAllInterfacesForClass(Class clazz) {
        return getAllInterfacesForClass(clazz, (ClassLoader) null);
    }

    public static Class[] getAllInterfacesForClass(Class clazz, ClassLoader classLoader) {
        AssertUtil.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            return new Class[]{clazz};
        } else {
            ArrayList<Class> interfaces;
            for (interfaces = new ArrayList<>(); clazz != null; clazz = clazz.getSuperclass()) {
                for (int i = 0; i < clazz.getInterfaces().length; ++i) {
                    Class ifc = clazz.getInterfaces()[i];
                    if (!interfaces.contains(ifc) && (classLoader == null || isVisible(ifc, classLoader))) {
                        interfaces.add(ifc);
                    }
                }
            }

            return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
        }
    }

    public static Set getAllInterfacesAsSet(Object instance) {
        AssertUtil.notNull(instance, "Instance must not be null");
        return getAllInterfacesForClassAsSet(instance.getClass());
    }

    public static Set getAllInterfacesForClassAsSet(Class clazz) {
        return getAllInterfacesForClassAsSet(clazz, (ClassLoader) null);
    }

    public static Set getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader) {
        AssertUtil.notNull(clazz, "Class must not be null");
        if (clazz.isInterface()) {
            return Collections.singleton(clazz);
        } else {
            LinkedHashSet<Class> interfaces;
            for (interfaces = new LinkedHashSet<>(); clazz != null; clazz = clazz.getSuperclass()) {
                for (int i = 0; i < clazz.getInterfaces().length; ++i) {
                    Class ifc = clazz.getInterfaces()[i];
                    if (classLoader == null || isVisible(ifc, classLoader)) {
                        interfaces.add(ifc);
                    }
                }
            }

            return interfaces;
        }
    }

    public static Class createCompositeInterface(Class[] interfaces, ClassLoader classLoader) {
        AssertUtil.notEmpty(interfaces, "Interfaces must not be empty");
        AssertUtil.notNull(classLoader, "ClassLoader must not be null");
        return Proxy.getProxyClass(classLoader, interfaces);
    }

    public static boolean isVisible(Class clazz, ClassLoader classLoader) {
        if (classLoader == null) {
            return true;
        } else {
            try {
                Class ex = classLoader.loadClass(clazz.getName());
                return clazz == ex;
            } catch (ClassNotFoundException var3) {
                return false;
            }
        }
    }

    public static Set<Class<?>> getClasses(String pack, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = getDefaultClassLoader();
        }
        LinkedHashSet<Class<?>> classes = new LinkedHashSet<>();
        boolean recursive = true;
        String packageName = pack;
        String packageDirName = pack.replace('.', '/');

        try {
            Enumeration dirs = classLoader.getResources(packageDirName);

            while (true) {
                label:
                while (dirs.hasMoreElements()) {
                    URL url = (URL) dirs.nextElement();
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        logger.debug("file类型扫描");
                        String packagePath = URLDecoder.decode(url.getFile(), "UTF-8");
                        findAndAddClassesInPackageByFile(packageName, packagePath, recursive, classes);
                    } else if ("jar".equals(protocol)) {
                        logger.debug("jar类型扫描");
                        try {
                            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                            Enumeration entries = jar.entries();

                            while (true) {
                                JarEntry entry;
                                String name;
                                int idx;
                                do {
                                    do {
                                        if (!entries.hasMoreElements()) {
                                            continue label;
                                        }

                                        entry = (JarEntry) entries.nextElement();
                                        name = entry.getName();
                                        if (name.charAt(0) == 47) {
                                            name = name.substring(1);
                                        }
                                    } while (!name.startsWith(packageDirName));

                                    idx = name.lastIndexOf(47);
                                    if (idx != -1) {
                                        packageName = name.substring(0, idx).replace('/', '.');
                                    }
                                } while (idx == -1 && !recursive);

                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);

                                    try {
                                        classes.add(forName(packageName + '.' + className, classLoader));
                                        logger.debug("扫描jar文件中的类 ==> " + packageName + '.' + className);
                                    } catch (ClassNotFoundException e) {
                                        logger.debug("添加用户自定义视图类错误找不到此类的.class文件");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (IOException e) {
                            logger.debug("在扫描用户定义视图时从jar包获取文件出错");
                            e.printStackTrace();
                        }
                    }
                }

                return classes;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return classes;
        }
    }

    public static Set<Class<?>> getClasses(String pack) {
        return getClasses(pack, getDefaultClassLoader());
    }

    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (dir.exists() && dir.isDirectory()) {
            File[] dirFiles = dir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return recursive && file.isDirectory() || file.getName().endsWith(".class");
                }
            });
            if (dirFiles != null) {
                logger.debug(dir.getAbsoluteFile() + "目录下共有文件" + dirFiles.length + "个");
            }
            int fileSize = dirFiles.length;

            for (int index = 0; index < fileSize; ++index) {
                File file = dirFiles[index];
                if (file.isDirectory()) {
                    findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        if (!classes.add(forName(packageName + '.' + className))) {
                            logger.warn("检测到jar包中包含有相同的包下的Class:" + packageName + "." + className);
                        }
                        logger.debug("扫描用户自定义类 ==> " + packageName + '.' + className);
                    } catch (ClassNotFoundException var12) {
                        logger.debug("添加用户自定义类" + packageName + '.' + className + "错误,找不到此类的.class文件");
                        var12.printStackTrace();
                    }
                }
            }

        } else {
            logger.error("用户定义包名 " + packageName + " 下没有任何文件");
        }
    }

    static {
        primitiveWrapperTypeMap.put(Boolean.class, Boolean.TYPE);
        primitiveWrapperTypeMap.put(Byte.class, Byte.TYPE);
        primitiveWrapperTypeMap.put(Character.class, Character.TYPE);
        primitiveWrapperTypeMap.put(Double.class, Double.TYPE);
        primitiveWrapperTypeMap.put(Float.class, Float.TYPE);
        primitiveWrapperTypeMap.put(Integer.class, Integer.TYPE);
        primitiveWrapperTypeMap.put(Long.class, Long.TYPE);
        primitiveWrapperTypeMap.put(Short.class, Short.TYPE);
        HashSet<Type> primitiveTypeNames = new HashSet<>(16);
        primitiveTypeNames.addAll(primitiveWrapperTypeMap.values());
        primitiveTypeNames.addAll(Arrays.asList(boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class));

        for (Object primitiveTypeName : primitiveTypeNames) {
            Class primitiveClass = (Class) primitiveTypeName;
            primitiveTypeNameMap.put(primitiveClass.getName(), primitiveClass);
        }

    }
}
