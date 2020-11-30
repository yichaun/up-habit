package com.up.habit.kit;

import com.jfinal.kit.Kv;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/1/8 0:10
 */
public class SystemKit {
    private static final int OSHI_WAIT_SECOND = 1000;

    public static Kv info() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        return Kv.by("cpu", getCpu(hal.getProcessor()))
                .set("memory", setMemoryInfo(hal.getMemory()))
                .set("system", getSystem())
                .set("jvm", getJvmInfo())
                .set("disk", getDisk(si.getOperatingSystem()));
    }

    /**
     * TODO:设置内存信息
     * total    内存总量
     * used     已用内存
     * free     剩余内存
     *
     * @param memory
     * @return com.jfinal.kit.Kv
     * @Author 王剑洪 on 2020/1/8 0:55
     **/
    public static Kv setMemoryInfo(GlobalMemory memory) {
        BigDecimal total = new BigDecimal(memory.getTotal()).divide(new BigDecimal(1024 * 1024 * 1024), 2, RoundingMode.HALF_UP);
        BigDecimal used = new BigDecimal(memory.getTotal() - memory.getAvailable()).divide(new BigDecimal(1024 * 1024 * 1024), 2, RoundingMode.HALF_UP);
        BigDecimal free = new BigDecimal(memory.getAvailable()).divide(new BigDecimal(1024 * 1024 * 1024), 2, RoundingMode.HALF_UP);
        BigDecimal usage = used.multiply(new BigDecimal(100)).divide(total,4, RoundingMode.HALF_UP);
        return Kv.by("total", total)
                .set("used", used)
                .set("free", free)
                .set("usage", usage);
    }

    /**
     * TODO:CPU信息
     * cpuNum   核心数
     * total    CPU总的使用率
     * sys      CPU系统使用率
     * used     CPU用户使用率
     * wait     CPU当前等待率
     * free     CPU当前空闲率
     *
     * @param processor
     * @return com.jfinal.kit.Kv
     * @Author 王剑洪 on 2020/1/8 0:52
     **/
    public static Kv getCpu(CentralProcessor processor) {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        BigDecimal total = new BigDecimal(totalCpu).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal sys = new BigDecimal(cSys).multiply(new BigDecimal(100)).divide(new BigDecimal(totalCpu), 2, RoundingMode.HALF_UP);
        BigDecimal used = new BigDecimal(user).multiply(new BigDecimal(100)).divide(new BigDecimal(totalCpu), 2, RoundingMode.HALF_UP);
        BigDecimal wait = new BigDecimal(iowait).multiply(new BigDecimal(100)).divide(new BigDecimal(totalCpu), 2, RoundingMode.HALF_UP);
        BigDecimal free = new BigDecimal(idle).multiply(new BigDecimal(100)).divide(new BigDecimal(totalCpu), 2, RoundingMode.HALF_UP);
        return Kv.by("cpuNum", processor.getLogicalProcessorCount())
                .set("total", total)
                .set("sys", sys)
                .set("used", used)
                .set("wait", wait)
                .set("free", free);
    }

    /**
     * TODO:服务器信息
     * name 服务器名称
     * ip   服务器Ip
     * osName   操作系统
     * osArch   系统架构
     * userDir  项目路径
     *
     * @return com.jfinal.kit.Kv
     * @Author 王剑洪 on 2020/1/8 0:23
     **/
    public static Kv getSystem() {
        Properties props = System.getProperties();
        return Kv.by("name", IpKit.getHostName())
                .set("ip", IpKit.getHostIp())
                .set("osName", props.getProperty("os.name"))
                .set("osArch", props.getProperty("os.arch"))
                .set("userDir", props.getProperty("user.dir"));
    }

    /**
     * TODO:Java虚拟机信息
     * total    当前JVM占用的内存总数(M)
     * max      JVM最大可用内存总数(M)
     * free     JVM空闲内存(M)
     * version  JDK版本
     * home     JDK路径
     * startTimeJDK启动时间
     * runTime  JDK运行时间
     * name     获取JDK名称
     *
     * @return com.jfinal.kit.Kv
     * @Author 王剑洪 on 2020/1/8 0:24
     **/
    public static Kv getJvmInfo() {
        Properties props = System.getProperties();
        double total = Runtime.getRuntime().totalMemory();
        double max = Runtime.getRuntime().maxMemory();
        double free = Runtime.getRuntime().freeMemory();
        BigDecimal used = new BigDecimal(total - free).divide(new BigDecimal(1024 * 1024), 2, RoundingMode.HALF_UP);
        BigDecimal usage = new BigDecimal(total - free).divide(new BigDecimal(total), 2, RoundingMode.HALF_UP);
        return Kv.by("total", new BigDecimal(total).divide(new BigDecimal(1024 * 1024), 2, RoundingMode.HALF_UP))
                .set("max", new BigDecimal(max).divide(new BigDecimal(1024 * 1024), 2, RoundingMode.HALF_UP))
                .set("free", new BigDecimal(free).divide(new BigDecimal(1024 * 1024), 2, RoundingMode.HALF_UP))
                .set("used", used.toString())
                .set("usage", usage.toString())
                .set("version", props.getProperty("java.version"))
                .set("home", props.getProperty("java.home"))
                .set("startTime", DateKit.toStr(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()), DateKit.timeStampPattern))
                .set("runTime", DateKit.getDatePoor(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()), new Date()))
                .set("name", ManagementFactory.getRuntimeMXBean().getVmName());
    }

    /**
     * TODO:磁盘信息
     * name     盘符路径
     * sysType  盘符类型
     * type     文件类型
     * total    总大小
     * free     剩余大小
     * used     已经使用量
     * usage    资源的使用率
     *
     * @param os
     * @return java.util.List<com.jfinal.kit.Kv>
     * @Author 王剑洪 on 2020/1/8 1:06
     **/
    public static List<Kv> getDisk(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        List<Kv> diskList = new ArrayList<>();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            BigDecimal usage = new BigDecimal(used).divide(new BigDecimal(total), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            diskList.add(
                    Kv.by("name", fs.getMount())
                            .set("sysType", fs.getType())
                            .set("type", fs.getName())
                            .set("total", convertFileSize(total))
                            .set("free", convertFileSize(free))
                            .set("used", convertFileSize(used))
                            .set("usage", usage)

            );
        }
        return diskList;
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
