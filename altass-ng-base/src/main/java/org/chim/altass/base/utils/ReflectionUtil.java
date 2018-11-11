/**
 * Project: x-framework
 * Package Name: org.ike.utils
 * Author: Xuejia
 * Date Time: 2016/12/8 0:06
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.base.utils;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import org.chim.altass.base.utils.type.ArrayUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class Name: ReflectionUtil
 * Create Date: 2016/12/8 0:06
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 反射工具类
 */
@SuppressWarnings("Duplicates")
public class ReflectionUtil {

    public static ReflectionUtil.FieldFilter COPYABLE_FIELDS = new ReflectionUtil.FieldFilter() {
        public boolean matches(Field field) {
            return !Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers());
        }
    };

    /**
     * 根据字段的名称获得字段
     *
     * @param clazz 类
     * @param name  字段的名称
     * @return 返回字段实例
     */
    public static Field findField(Class clazz, String name) {
        return findField(clazz, name, null);
    }

    /**
     * 根据字段的名称获取字段
     *
     * @param clazz 类
     * @param name  字段的名称
     * @param type  字段的类型
     * @return 返回查找的类的字段实例
     */
    public static Field findField(Class clazz, String name, Class type) {
        AssertUtil.notNull(clazz, "Class must not be null");
        AssertUtil.isTrue(name != null || type != null, "Either name or type of the field must be specified");
        for (Class searchType = clazz; !Object.class.equals(searchType) && searchType != null; searchType = searchType.getSuperclass()) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * 向类中设置字段
     *
     * @param field  需要设置字段的实例
     * @param target 字段目标
     * @param value  字段的值
     */
    public static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            handleReflectionException(e);
            throw new IllegalStateException("Unexpected reflection exception - " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 获得字段
     *
     * @param field  字段的实例
     * @param target 字段的目标
     * @return 返回字段的值
     */
    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            handleReflectionException(e);
            throw new IllegalStateException("Unexpected reflection exception - " + e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 获得类中所有的可访问字段实例数组
     *
     * @param clazz 需要获取的类
     * @return 返回可访问字段实例数组
     */
    public static Field[] getAllDeclaredFields(Class clazz) {
        Field[] fields;
        for (fields = null; !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
            fields = ArrayUtil.addAll(clazz.getDeclaredFields(), fields);
        }
        return fields;
    }

    /**
     * 根据名称查找某一个类中的方法实例
     *
     * @param clazz 类
     * @param name  方法名称
     * @return 返回方法名称对应的方法实例
     */
    public static Method findMethod(Class clazz, String name) {
        return findMethod(clazz, name, new Class[0]);
    }

    /**
     * 根据名称查找某一个类方法中的方法实例
     *
     * @param clazz      类
     * @param name       方法名称
     * @param paramTypes 方法参数
     * @return 返回方法名称以及参数对应的方法实例
     */
    public static Method findMethod(Class clazz, String name, Class[] paramTypes) {
        AssertUtil.notNull(clazz, "Class must not be null");
        AssertUtil.notNull(name, "Method name must not be null");
        for (Class searchType = clazz; !Object.class.equals(searchType) && searchType != null; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
            int i = 0;
            for (int len = methods.length; i < len; ++i) {
                Method method = methods[i];
                if (name.equals(method.getName()) && (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    return method;
                }
            }
        }

        return null;
    }

    /**
     * 调用某指定的方法
     *
     * @param method 方法实例
     * @param target 执行方法的对象实例
     * @return 返回方法调用后的执行结果
     */
    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, null);
    }

    /**
     * 调用某个指定类的方法
     *
     * @param method 需要调用的方法实例
     * @param target 调用目标
     * @param args   调用方式的参数
     * @return 返回方法调用的返回值
     */
    public static Object invokeMethod(Method method, Object target, Object[] args) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            handleReflectionException(e);
            throw new IllegalStateException("Should never get here");
        }
    }

    /**
     * 处理反射异常
     *
     * @param ex 需要处理的异常实例
     */
    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        } else if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
        } else {
            if (ex instanceof InvocationTargetException) {
                handleInvocationTargetException((InvocationTargetException) ex);
            }
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                handleUnexpectedException(ex);
            }
        }
    }

    /**
     * 处理调用目标异常
     *
     * @param ex 需要处理的调用目标异常
     */
    public static void handleInvocationTargetException(InvocationTargetException ex) {
        rethrowRuntimeException(ex.getTargetException());
    }

    /**
     * 抛出运行时异常
     *
     * @param ex 需要抛出的异常
     */
    public static void rethrowRuntimeException(Throwable ex) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else if (ex instanceof Error) {
            throw (Error) ex;
        } else {
            handleUnexpectedException(ex);
        }
    }

    /**
     * 抛出异常
     *
     * @param ex 需要抛出的异常74
     * @throws Exception
     */
    public static void rethrowException(Throwable ex) throws Exception {
        if (ex instanceof Exception) {
            throw (Exception) ex;
        } else if (ex instanceof Error) {
            throw (Error) ex;
        } else {
            handleUnexpectedException(ex);
        }
    }

    /**
     * 处理非法参数异常
     *
     * @param ex 异常
     */
    private static void handleUnexpectedException(Throwable ex) {
        throw new IllegalStateException("Unexpected exception thrown", ex);
    }

    /**
     * @param method
     * @param exceptionType
     * @return
     */
    public static boolean declaresException(Method method, Class<?> exceptionType) {
        AssertUtil.notNull(method, "Method must not be null");
        Class[] declaredExceptions = method.getExceptionTypes();

        for (Class declaredException : declaredExceptions) {
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断是否是 public static final 字段
     *
     * @param field 需要判断的字段实例
     * @return 如果是public static final 那么返回值为true，否则返回值为false
     */
    public static boolean isPublicStaticFinal(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    }

    /**
     * 判断是否是equals方法
     *
     * @param method 需要判断的方法实例
     * @return 如果是equals方法，那么返回值为true，否则返回值为false
     */
    public static boolean isEqualsMethod(Method method) {
        if (method != null && method.getName().equals("equals")) {
            Class[] paramTypes = method.getParameterTypes();
            return paramTypes.length == 1 && paramTypes[0] == Object.class;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是hashCode方法
     *
     * @param method 需要判断的方法实例
     * @return 如果是hashCode方法，那么返回值为true，否则返回值为false
     */
    public static boolean isHashCodeMethod(Method method) {
        return method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0;
    }

    /**
     * 判断是否是toString方法
     *
     * @param method 需要判断的方法实例
     * @return 如果是toString方法，那么返回值为true，否则返回值为false
     */
    public static boolean isToStringMethod(Method method) {
        return method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0;
    }

    /**
     * 使某一个字段能够被访问
     *
     * @param field 需要改变可访问性的字段实例
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * 使某一个方法能够被访问
     *
     * @param method 需要改变可访问性的方法实例
     */
    public static void makeAccessible(Method method) {
        if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) {
            method.setAccessible(true);
        }
    }

    /**
     * 使一个构造实例能够被访问
     *
     * @param ctor 需要改变可访问性的构造方法
     */
    public static void makeAccessible(Constructor ctor) {
        if (!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) {
            ctor.setAccessible(true);
        }
    }

    /**
     * @param targetClass
     * @param mc
     * @throws IllegalArgumentException
     */
    public static void doWithMethods(Class targetClass, ReflectionUtil.MethodCallback mc) throws IllegalArgumentException {
        doWithMethods(targetClass, mc, null);
    }

    /**
     * @param targetClass
     * @param mc
     * @param mf
     * @throws IllegalArgumentException
     */
    public static void doWithMethods(Class targetClass, ReflectionUtil.MethodCallback mc, ReflectionUtil.MethodFilter mf) throws IllegalArgumentException {
        do {
            Method[] methods = targetClass.getDeclaredMethods();

            for (Method method : methods) {
                if (mf == null || mf.matches(method)) {
                    try {
                        mc.doWith(method);
                    } catch (IllegalAccessException var6) {
                        throw new IllegalStateException("Shouldn\'t be illegal to access method \'" + method.getName() + "\': " + var6);
                    }
                }
            }

            targetClass = targetClass.getSuperclass();
        } while (targetClass != null);

    }

    /**
     * 获得所有的可访问的类方法数组
     *
     * @param leafClass 需要获得方法的类
     * @return 返回类中可访问的方法数组
     * @throws IllegalArgumentException
     */
    public static Method[] getAllDeclaredMethods(Class leafClass) throws IllegalArgumentException {
        final ArrayList list = new ArrayList(32);
        doWithMethods(leafClass, new ReflectionUtil.MethodCallback() {
            public void doWith(Method method) {
                list.add(method);
            }
        });
        return (Method[]) list.toArray(new Method[list.size()]);
    }

    /**
     * @param targetClass
     * @param fc
     * @throws IllegalArgumentException
     */
    public static void doWithFields(Class targetClass, ReflectionUtil.FieldCallback fc) throws IllegalArgumentException {
        doWithFields(targetClass, fc, (ReflectionUtil.FieldFilter) null);
    }

    /**
     * @param targetClass
     * @param fc
     * @param ff
     * @throws IllegalArgumentException
     */
    public static void doWithFields(Class targetClass, ReflectionUtil.FieldCallback fc, ReflectionUtil.FieldFilter ff) throws IllegalArgumentException {
        do {
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                if (ff == null || ff.matches(field)) {
                    try {
                        fc.doWith(field);
                    } catch (IllegalAccessException var6) {
                        throw new IllegalStateException("Shouldn\'t be illegal to access field \'" + field.getName() + "\': " + var6);
                    }
                }
            }
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

    }


    /**
     * 浅复制一个字段实例
     *
     * @param src  源
     * @param dest 目标
     * @throws IllegalArgumentException
     */
    public static void shallowCopyFieldState(final Object src, final Object dest) throws IllegalArgumentException {
        if (src == null) {
            throw new IllegalArgumentException("Source for field copy cannot be null");
        } else if (dest == null) {
            throw new IllegalArgumentException("Destination for field copy cannot be null");
        } else if (!src.getClass().isAssignableFrom(dest.getClass())) {
            throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
        } else {
            doWithFields(src.getClass(), new ReflectionUtil.FieldCallback() {
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtil.makeAccessible(field);
                    Object srcValue = field.get(src);
                    field.set(dest, srcValue);
                }
            }, COPYABLE_FIELDS);
        }
    }

    /**
     * 获得一个方法的返回类型
     *
     * @param method 需要操作的方法实例
     * @return 方法的返回类型
     */
    public static Class<?> getMethodReturnType(Method method) {
        AssertUtil.notNull(method, "Method must not be null");
        return method.getReturnType();
    }

    /**
     * 返回方法返回类型的名称
     *
     * @param method 需要操作的方法实例
     * @return 方法的返回类型名称
     */
    public static String getMethodReturnTypeName(Method method) {
        return getMethodReturnType(method).getName();
    }

    /**
     * 返回泛型返回类型名称
     *
     * @param method 需要操作的方法
     * @return 泛型返回类型名称
     */
    public static String[] getMethodGenericReturnTypeName(Method method) {
        AssertUtil.notNull(method, "Method must not be null");
        String[] genericTypeNames = null;
        Type genericType = method.getGenericReturnType();
        if (genericType.toString().trim().endsWith(">")) {
            Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
            int len = types.length;
            genericTypeNames = new String[len];

            for (int i = 0; i < len; ++i) {
                String typeStr = types[i].toString();
                if (typeStr.indexOf("<") > 0) {
                    genericTypeNames[i] = typeStr.substring(0, typeStr.indexOf("<"));
                } else {
                    genericTypeNames[i] = typeStr.substring(typeStr.lastIndexOf(" ") + 1);
                }
            }
        }

        return genericTypeNames;
    }

    /**
     * 获得方法的泛型返回类型
     *
     * @param method 需要操作的方法实例
     * @return 方法泛型返回类型
     * @throws ClassNotFoundException
     */
    public static Class<?>[] getMethodGenericReturnType(Method method) throws ClassNotFoundException {
        Class[] genericTypes = null;
        String[] genericTypeNames = getMethodGenericReturnTypeName(method);
        if (genericTypeNames != null) {
            int len = genericTypeNames.length;
            genericTypes = new Class[len];

            for (int i = 0; i < len; ++i) {
                genericTypes[i] = Class.forName(genericTypeNames[i]);
            }
        }

        return genericTypes;
    }

    /**
     * 获得字段的类型
     *
     * @param field 需要操作的字段实例
     * @return 实例的字段类型
     */
    public static Class<?> getFieldType(Field field) {
        AssertUtil.notNull(field, "Field must not be null");
        return field.getType();
    }

    /**
     * 获得字段的类型名称
     *
     * @param field 需要操作的字段实例
     * @return 返回字段的类型名称
     */
    public static String getFieldTypeName(Field field) {
        return getFieldType(field).getName();
    }

    /**
     * 获得字段泛型类型名称
     *
     * @param field 需要操作的字段实例
     * @return 字段的泛型类型名称
     */
    public static String[] getFieldGenericTypeName(Field field) {
        AssertUtil.notNull(field, "Field must not be null");
        String[] genericTypeNames = null;
        Type genericType = field.getGenericType();
        if (genericType.toString().trim().endsWith(">")) {
            Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
            int len = types.length;
            genericTypeNames = new String[len];

            for (int i = 0; i < len; ++i) {
                String typeStr = types[i].toString();
                genericTypeNames[i] = typeStr.substring(typeStr.lastIndexOf(" ") + 1);
            }
        }

        return genericTypeNames;
    }

    /**
     * 获得字段的泛型类型
     *
     * @param field 需要操作的字段实例
     * @return 字段的泛型类型
     * @throws ClassNotFoundException
     */
    public static Class<?>[] getFieldGenericType(Field field) throws ClassNotFoundException {
        Class[] genericTypes = null;
        String[] genericTypeNames = getFieldGenericTypeName(field);
        if (genericTypeNames != null) {
            int len = genericTypeNames.length;
            genericTypes = new Class[len];

            for (int i = 0; i < len; ++i) {
                genericTypes[i] = Class.forName(genericTypeNames[i]);
            }
        }

        return genericTypes;
    }

    /**
     * 查看方法的参数数组
     *
     * @param method 需要操作的方法实例
     * @return 返回方法的参数数组
     */
    public static String[] lookupParameterNames(Method method) {
        BytecodeReadingParanamer methodReader = new BytecodeReadingParanamer();
        return methodReader.lookupParameterNames(method, false);
    }

    /**
     * 获得对象对应字段Field的get方法
     *
     * @param obj   需要操作的对象实例
     * @param field 需要获得对应get方法的Field实例
     * @return 返回Field实例对应的get方法
     */
    public static Method getGetMethod(Object obj, Field field) {
        String name = field.getName();
        try {
            return obj.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得对象对应字段Field的set方法
     *
     * @param obj   需要操作的对象实例
     * @param field 需要获得对应的set方法的field实例
     * @return 返回field实例对应的set方法
     */
    public static Method getSetMethod(Object obj, Field field) {
        String name = field.getName();
        Class[] fieldTypes = new Class[]{field.getType()};
        try {
            return obj.getClass().getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), fieldTypes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface FieldFilter {
        boolean matches(Field var1);
    }

    public interface FieldCallback {
        void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    public interface MethodFilter {
        boolean matches(Method method);
    }

    public interface MethodCallback {
        void doWith(Method method) throws IllegalArgumentException, IllegalAccessException;
    }

    /**
     * 获得所有实现某个接口的类
     *
     * @param clz 接口类
     * @return 返回所有实现了某个接口的类
     */
    public static List<Class> getAllClassByInterface(Class clz) {
        return getAllClassByInterface(clz.getPackage().getName(), clz);
    }

    /**
     * 获得所有实现了某个接口的类
     *
     * @param pkg 需要检测的包名
     * @param clz 需要检测的接口类
     * @return 返回所有实现了某个接口的类
     */
    public static List<Class> getAllClassByInterface(String pkg, Class<?> clz) {
        List<Class> returnClassList = new ArrayList<Class>();
        if (clz.isInterface()) {
            // 获取当前包下以及子包下所以的类
            List<Class<?>> allClass = getClasses(pkg);
            if (allClass != null) {
                returnClassList = new ArrayList<>();
                for (Class classes : allClass) {
                    // 判断是否是同一个接口
                    if (clz.isAssignableFrom(classes) || classes.getDeclaredAnnotation(clz) != null) {
                        returnClassList.add(classes);
                    }
                }
            }
        }

        return returnClassList;
    }

    /**
     * 获得某一个类所在包的所有类名，不迭代
     *
     * @param classLocation 类的位置
     * @param packageName   包名
     * @return 返回某一个类所在包的所有类名
     */
    public static String[] getPackageAllClassName(String classLocation, String packageName) {
        // 分解包名
        String[] packagePathSplit = packageName.split("[.]");
        String realClassLocation = classLocation;
        for (String aPackagePathSplit : packagePathSplit) {
            realClassLocation = realClassLocation + File.separator + aPackagePathSplit;
        }

        File packageDir = new File(realClassLocation);
        if (packageDir.isDirectory()) {
            return packageDir.list();
        }
        return null;
    }

    /**
     * 获得某一个包名下的所有类
     *
     * @param packageName 包名
     * @return 某一个包名下的所有类
     */
    public static List<Class<?>> getClasses(String packageName) {
        //第一个class类的集合
        List<Class<?>> classes = new ArrayList<Class<?>>();
        //是否循环迭代
        boolean recursive = true;
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            //循环迭代下去
            while (dirs.hasMoreElements()) {
                //获取下一个元素
                URL url = dirs.nextElement();
                //得到协议的名称
                String protocol = url.getProtocol();
                //如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    //如果是jar包文件
                    //定义一个JarFile
                    JarFile jar;
                    try {
                        //获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        //从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        //同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            //如果是以/开头的
                            if (name.charAt(0) == '/') {
                                //获取后面的字符串
                                name = name.substring(1);
                            }
                            //如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                //如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    //获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                //如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    //如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        //去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            //添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式获得某一个物理文件包下的所有类Class
     *
     * @param packageName 包名
     * @param packagePath 包的路径
     * @param recursive   是否递归
     * @param classes     类
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirFiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        if (dirFiles != null) {
            for (File file : dirFiles) {
                //如果是目录 则继续扫描
                if (file.isDirectory()) {
                    findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
                } else {
                    //如果是java类文件 去掉后面的.class 只留下类名
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        //添加到集合中去
                        classes.add(Class.forName(packageName + '.' + className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 获得封装类型的Class
     *
     * @param c 需要转化为封装类型的类（基本类型会转化）
     * @return class
     */
    public static Class<?> getBoxedClass(Class<?> c) {
        if (c == int.class)
            c = Integer.class;
        else if (c == boolean.class)
            c = Boolean.class;
        else if (c == long.class)
            c = Long.class;
        else if (c == float.class)
            c = Float.class;
        else if (c == double.class)
            c = Double.class;
        else if (c == char.class)
            c = Character.class;
        else if (c == byte.class)
            c = Byte.class;
        else if (c == short.class)
            c = Short.class;
        return c;
    }
}
