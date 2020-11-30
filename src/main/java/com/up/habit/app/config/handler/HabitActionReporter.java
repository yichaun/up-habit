package com.up.habit.app.config.handler;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.Action;
import com.jfinal.core.ActionReporter;
import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.up.habit.Habit;
import javassist.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/9/5 1:46
 */
public class HabitActionReporter extends ActionReporter {

    private static final String title = "Up-Habit " + Habit.VERSION + " action report -------- ";

    /**
     * void(V).
     */
    public static final char JVM_VOID = 'V';

    /**
     * boolean(Z).
     */
    public static final char JVM_BOOLEAN = 'Z';

    /**
     * byte(B).
     */
    public static final char JVM_BYTE = 'B';

    /**
     * char(C).
     */
    public static final char JVM_CHAR = 'C';

    /**
     * double(D).
     */
    public static final char JVM_DOUBLE = 'D';

    /**
     * float(F).
     */
    public static final char JVM_FLOAT = 'F';

    /**
     * int(I).
     */
    public static final char JVM_INT = 'I';

    /**
     * long(J).
     */
    public static final char JVM_LONG = 'J';

    /**
     * short(S).
     */
    public static final char JVM_SHORT = 'S';


    /**
     * Report the action
     */
    @Override
    public void report(String target, Controller controller, Action action) {
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(controller.getClass()));
        int lineNumber = 0;
        try {
            CtClass ctClass = ClassPool.getDefault().get(action.getControllerClass().getName());
            String desc = getMethodDescWithoutName(action.getMethod());
            CtMethod ctMethod = ctClass.getMethod(action.getMethodName(), desc);
            lineNumber = ctMethod.getMethodInfo().getLineNumber(0);
        } catch (NotFoundException e) {
        }
        Class<? extends Controller> cc = action.getControllerClass();
        StringBuilder sb = new StringBuilder();
        sb.append("\n--").append(title).append(sdf.get().format(new Date())).append(" ----------------------------");
        sb.append("\nUrl         : ").append(controller.getRequest().getMethod()).append(" ").append(target);

        sb.append("\nController  : ").append(cc.getPackage().getName()).append(".(").append(cc.getSimpleName()).append(".java:" + lineNumber + ")");
        sb.append("\nMethod      : ").append(action.getMethodName());

        String urlParas = controller.getPara();
        if (urlParas != null) {
            sb.append("\nUrlPara     : ").append(urlParas);
        }

        Interceptor[] inters = action.getInterceptors();
        if (inters.length > 0) {
            sb.append("\nInterceptor : ");
            for (int i = 0; i < inters.length; i++) {
                sb.append(i > 0 ? "\n              " : "");
                Interceptor inter = inters[i];
                Class<? extends Interceptor> ic = inter.getClass();
                sb.append(ic.getPackage().getName()).append(".(").append(ic.getSimpleName()).append(".java:1)");
            }
        }

        HttpServletRequest request = controller.getRequest();
        Enumeration<String> e = request.getParameterNames();
        if (e.hasMoreElements()) {
            sb.append("\nParameter   : ");
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String[] values = request.getParameterValues(name);
                if (values.length == 1) {
                    sb.append(name).append("=");
                    if (values[0] != null && values[0].length() > maxOutputLengthOfParaValue) {
                        sb.append(values[0].substring(0, maxOutputLengthOfParaValue)).append("...");
                    } else {
                        sb.append(values[0]);
                    }
                } else {
                    sb.append(name).append("[]={");
                    for (int i = 0; i < values.length; i++) {
                        sb.append(i > 0 ? "," : "");
                        sb.append(values[i]);
                    }
                    sb.append("}");
                }
                sb.append("  ");
            }
        }
        sb.append("\n----------------------------------------------------------------------------------------\n");

        try {
            writer.write(sb.toString());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }



    /**
     * get method desc.
     * "(I)I", "()V", "(Ljava/lang/String;Z)V"
     *
     * @param m method.
     * @return desc.
     */
    public static String getMethodDescWithoutName(Method m) {
        StringBuilder ret = new StringBuilder();
        ret.append('(');
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            ret.append(getDesc(parameterTypes[i]));
        }
        ret.append(')').append(getDesc(m.getReturnType()));
        return ret.toString();
    }

    /**
     * get class desc.
     * boolean[].class => "[Z"
     * Object.class => "Ljava/lang/Object;"
     *
     * @param c class.
     * @return desc.
     */
    public static String getDesc(Class<?> c) {
        StringBuilder ret = new StringBuilder();

        while (c.isArray()) {
            ret.append('[');
            c = c.getComponentType();
        }

        if (c.isPrimitive()) {
            String t = c.getName();
            if ("void".equals(t)) {
                ret.append(JVM_VOID);
            } else if ("boolean".equals(t)) {
                ret.append(JVM_BOOLEAN);
            } else if ("byte".equals(t)) {
                ret.append(JVM_BYTE);
            } else if ("char".equals(t)) {
                ret.append(JVM_CHAR);
            } else if ("double".equals(t)) {
                ret.append(JVM_DOUBLE);
            } else if ("float".equals(t)) {
                ret.append(JVM_FLOAT);
            } else if ("int".equals(t)) {
                ret.append(JVM_INT);
            } else if ("long".equals(t)) {
                ret.append(JVM_LONG);
            } else if ("short".equals(t)) {
                ret.append(JVM_SHORT);
            }
        } else {
            ret.append('L');
            ret.append(c.getName().replace('.', '/'));
            ret.append(';');
        }
        return ret.toString();
    }

}
