package com.up.habit.kit;

import com.jfinal.aop.Aop;
import com.jfinal.kit.StrKit;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * TODO:类工具类
 * <p>
 *
 * @author 王剑洪 on 2019/10/22 15:29
 */
public class ClassKit {

    public static ClassLoader overridenClassLoader;

    /**
     * 获取类加载器
     *
     * @return
     */
    public static ClassLoader getContextClassLoader() {
        return overridenClassLoader != null ? overridenClassLoader : Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取指定类的全部属性字段
     *
     * @param className    需要获取的类名
     * @param extendsField 是否获取接口或父类中的公共属性
     * @return 属性字段数组
     */
    public final static String[] getField(String className, boolean extendsField) {
        Class classz = loadClass(className);
        Field[] fields = classz.getFields();
        Set<String> set = new HashSet<String>();
        if (fields != null) {
            for (Field f : fields) {
                set.add(f.getName());
            }
        }
        if (extendsField) {
            Field[] fieldz = classz.getDeclaredFields();
            if (fieldz != null) {
                for (Field f : fieldz) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取类中的公共属性
     *
     * @param className    需要获取的类名
     * @param extendsField 是否获取接口或父类中的公共属性
     * @return 属性字段数组
     */
    public final static String[] getPublicField(String className, boolean extendsField) {
        Class classz = loadClass(className);
        Set<String> set = new HashSet<String>();
        Field[] fields = classz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("public")) {
                    set.add(f.getName());
                }
            }
        }
        if (extendsField) {
            Field[] fieldz = classz.getFields();
            if (fieldz != null) {
                for (Field f : fieldz) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取类中定义的protected类型的属性字段
     *
     * @param className 需要获取的类名
     * @return protected类型的属性字段数组
     */
    public final static String[] getProtectedField(String className) {
        Class classz = loadClass(className);
        Set<String> set = new HashSet<String>();
        Field[] fields = classz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("protected")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取类中定义的private类型的属性字段
     *
     * @param className 需要获取的类名
     * @return private类型的属性字段数组
     */
    public final static String[] getPrivateField(String className) {
        Class classz = loadClass(className);
        Set<String> set = new HashSet<String>();
        Field[] fields = classz.getDeclaredFields();
        if (fields != null) {
            for (Field f : fields) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("private")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取对象的全部public类型方法
     *
     * @param className     需要获取的类名
     * @param extendsMethod 是否获取继承来的方法
     * @return 方法名数组
     */
    public final static String[] getPublicMethod(String className, boolean extendsMethod) {
        Class classz = loadClass(className);
        Method[] methods;
        if (extendsMethod) {
            methods = classz.getMethods();
        } else {
            methods = classz.getDeclaredMethods();
        }
        Set<String> set = new HashSet<String>();
        if (methods != null) {
            for (Method f : methods) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("public")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取对象的全部 protected 类型方法
     *
     * @param className     需要获取的类名
     * @param extendsMethod 是否获取继承来的方法
     * @return 方法名数组
     */
    public final static String[] getProtectedMethod(String className, boolean extendsMethod) {
        Class classz = loadClass(className);
        Method[] methods;
        if (extendsMethod) {
            methods = classz.getMethods();
        } else {
            methods = classz.getDeclaredMethods();
        }
        Set<String> set = new HashSet<String>();
        if (methods != null) {
            for (Method f : methods) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("protected")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取对象的全部 private 类型方法
     *
     * @param className 需要获取的类名
     * @return 方法名数组
     */
    public final static String[] getPrivateMethod(String className) {
        Class classz = loadClass(className);
        Method[] methods = classz.getDeclaredMethods();
        Set<String> set = new HashSet<String>();
        if (methods != null) {
            for (Method f : methods) {
                String modifier = Modifier.toString(f.getModifiers());
                if (modifier.startsWith("private")) {
                    set.add(f.getName());
                }
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 获取对象的全部方法
     *
     * @param className     需要获取的类名
     * @param extendsMethod 是否获取继承来的方法
     * @return 方法名数组
     */
    public final static String[] getMethod(String className, boolean extendsMethod) {
        Class classz = loadClass(className);
        Method[] methods;
        if (extendsMethod) {
            methods = classz.getMethods();
        } else {
            methods = classz.getDeclaredMethods();
        }
        Set<String> set = new HashSet<String>();
        if (methods != null) {
            for (Method f : methods) {
                set.add(f.getName());
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 调用对象的setter方法
     *
     * @param obj   对象
     * @param att   属性名
     * @param value 属性值
     * @param type  属性类型
     */
    public final static void setter(Object obj, String att, Object value, Class<?> type) throws InvocationTargetException,
            IllegalAccessException {
        try {
            String name = att.substring(0, 1).toUpperCase() + att.substring(1);
            Method met = obj.getClass().getMethod("set" + name, type);
            met.invoke(obj, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath jar文件路径
     * @return 类的完整名称
     */
    public final static List<String> getClassNameByJar(String jarPath) {
        List<String> myClassName = new ArrayList<String>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    entryName = entryName.replace("/", ".").substring(0,
                            entryName.lastIndexOf("."));
                    myClassName.add(entryName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myClassName;
    }

    /**
     * 加载指定的类
     *
     * @param className 需要加载的类
     * @return 加载后的类
     */
    public final static Class loadClass(String className) {
        Class theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return theClass;
    }

    /**
     * 获取一个类的父类
     *
     * @param className 需要获取的类
     * @return 父类的名称
     */
    public final static String getSuperClass(String className) {
        Class classz = loadClass(className);
        Class superclass = classz.getSuperclass();
        return superclass.getName();
    }

    /**
     * 获取一个类的继承链
     *
     * @param className 需要获取的类
     * @return 继承类名的数组
     */
    public final static String[] getSuperClassChian(String className) {
        Class classz = loadClass(className);
        List<String> list = new ArrayList<String>();
        Class superclass = classz.getSuperclass();
        String superName = superclass.getName();
        if (!"java.lang.Object".equals(superName)) {
            list.add(superName);
            list.addAll(Arrays.asList(getSuperClassChian(superName)));
        } else {
            list.add(superName);
        }
        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取一类实现的全部接口
     *
     * @param className         需要获取的类
     * @param extendsInterfaces 话说getInterfaces能全部获取到才对，然而测试的时候父类的接口并没有 因此就多除了这参数
     * @return 实现接口名称的数组
     */
    public final static String[] getInterfaces(String className, boolean extendsInterfaces) {
        Class classz = loadClass(className);
        List<String> list = new ArrayList<String>();
        Class[] interfaces = classz.getInterfaces();
        if (interfaces != null) {
            for (Class inter : interfaces) {
                list.add(inter.getName());
            }
        }
        if (extendsInterfaces) {
            String[] superClass = getSuperClassChian(className);
            for (String c : superClass) {
                list.addAll(Arrays.asList(getInterfaces(c, false)));
            }
        }
        return list.toArray(new String[list.size()]);
    }


    /**
     * 获取指定包名下的所有类
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClass(String packageName, Class<?> parentClazz) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getContextClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replace("%20", " ");
                        addClass(classSet, packagePath, packageName, parentClazz);
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                        if (className.startsWith(packageName)) {
                                            doAddClass(classSet, className, parentClazz);
                                        }

                                    }
                                }
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException("获取类异常", e);
        }
        return classSet;
    }


    public static void addClass(Set<Class<?>> classSet, String packagePath, String packageName, Class<?> parentClazz) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StrKit.notBlank(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className, parentClazz);
            } else {
                String subPackagePath = fileName;
                if (StrKit.notBlank(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StrKit.notBlank(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName, parentClazz);
            }
        }
    }

    public static void doAddClass(Set<Class<?>> classSet, String className, Class<?> parentClazz) {
        Class<?> cls = loadClass(className, false);
        if (parentClazz != null) {
            if (parentClazz.isAssignableFrom(cls)) {
                classSet.add(cls);
            }
        } else {
            classSet.add(cls);
        }

    }

    /**
     * 加载类
     *
     * @param className
     * @param isInitialized false 代表装载类的时候 不进行初始化工作[不会执行静态代码块]
     * @return
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("加载类异常", e);
        }
        return cls;
    }


    /**
     * 新建类实例
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class getUsefulClass(Class<?> clazz) {
        //ControllerTest$ServiceTest$$EnhancerByGuice$$40471411#hello
        //com.demo.blog.Blog$$EnhancerByCGLIB$$69a17158
        return clazz.getName().indexOf("$$EnhancerBy") == -1 ? clazz : clazz.getSuperclass();
    }

    public static <T> T newInstance(Class<T> clazz, boolean createByAop) {
        if (createByAop) {
            return Aop.get(clazz);
        } else {
            try {
                Constructor constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return (T) constructor.newInstance();
            } catch (Exception e) {
                System.err.printf("can not newInstance class:" + clazz + "\n" + e.toString(), e);
            }

            return null;
        }
    }

    public static List<Method> getClassSetMethods(Class clazz) {
        List<Method> setMethods = new ArrayList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")
                    && Character.isUpperCase(method.getName().charAt(3))
                    && method.getName().length() > 3
                    && method.getParameterCount() == 1
                    && Modifier.isPublic(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())) {

                setMethods.add(method);
            }
        }
        return setMethods;
    }

    public static final Object convert(Class<?> type, String s) {
        if (type == String.class) {
            return s;
        }
        if (type == Integer.class || type == int.class) {
            return Integer.parseInt(s);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(s);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(s);
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(s);
        } else if (type == Boolean.class || type == boolean.class) {
            String value = s.toLowerCase();
            if ("1".equals(value) || "true".equals(value)) {
                return Boolean.TRUE;
            } else if ("0".equals(value) || "false".equals(value)) {
                return Boolean.FALSE;
            } else {
                throw new RuntimeException("Can not parse to boolean type of value: " + s);
            }
        } else if (type == java.math.BigDecimal.class) {
            return new java.math.BigDecimal(s);
        } else if (type == java.math.BigInteger.class) {
            return new java.math.BigInteger(s);
        } else if (type == byte[].class) {
            return s.getBytes();
        }

        throw new RuntimeException(type.getName() + " can not be converted, please use other type in your config class!");

    }

    /**
     * 根据属性名设置属性值
     *
     * @param clazz
     * @param fieldName
     * @param value
     * @return
     */
    public static void setFieldValueByFieldName(Class<?> clazz, String fieldName, String value) {
        try {
            Object object = clazz.newInstance();
            // 获取该类的成员变量
            Field f = clazz.getDeclaredField(fieldName);
            if (f == null) {
                return;
            }
            // 取消语言访问检查
            f.setAccessible(true);
            // 给变量赋值
            f.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static final boolean isBasicType(Class<?> type) {
       /* if (type == String.class
                || type == Integer.class || type == int.class
                || type == Long.class || type == long.class
                || type == Double.class || type == double.class
                || type == Float.class || type == float.class
                || type == Boolean.class || type == boolean.class
                || type == java.math.BigDecimal.class
                || type == java.math.BigInteger.class
                || type == byte[].class) {
            return true;
        }
        return false;*/
        boolean is = type.isPrimitive();
        if (!is) {
            try {
                return ((Class) type.getField("TYPE").get(null)).isPrimitive();
            } catch (Exception e) {
                return false;
            }
        } else {
            return true;
        }
    }
}
