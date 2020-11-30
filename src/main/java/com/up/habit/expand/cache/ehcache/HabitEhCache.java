package com.up.habit.expand.cache.ehcache;

import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.ehcache.IDataLoader;
import com.up.habit.Habit;
import com.up.habit.expand.cache.IHabitCache;
import com.up.habit.kit.StrKit;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.*;
import net.sf.ehcache.distribution.RMICacheManagerPeerListenerFactory;
import net.sf.ehcache.distribution.RMICacheManagerPeerProviderFactory;
import net.sf.ehcache.distribution.RMICacheReplicatorFactory;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/27 1:29
 */
public class HabitEhCache implements IHabitCache {
    private static final Log log = Log.getLog(HabitEhCache.class);

    private CacheManager cacheManager;
    private CacheEventListener cacheEventListener;
    private static Object locker = new Object();

    private static HabitEhCache me = new HabitEhCache();

    public static HabitEhCache me() {
        return me;
    }

    private HabitEhCache() {
        EhCacheConfig config = Habit.config(EhCacheConfig.class);
        if (StrKit.isBlank(config.getConfigFileName())) {
            cacheManager = CacheManager.create(config());
        } else {
            String configPath = config.getConfigFileName();
            if (!configPath.startsWith("/")) {
                configPath = PathKit.getRootClassPath() + "/" + configPath;
            }
            cacheManager = CacheManager.create(configPath);
        }
    }

    private Configuration config() {
        Configuration configuration = new Configuration();
        configuration.setUpdateCheck(false);
        configuration.setMaxBytesLocalHeap(5000L);
        /** 磁盘存储:将缓存中暂时不使用的对象,转移到硬盘,类似于Windows系统的虚拟内存
         * path:指定在硬盘上存储对象的路径*/
        configuration.diskStore(new DiskStoreConfiguration().path("java.io.tmpdir"));
        /**指定除自身之外的网络群体中其他提供同步的主机列表，用“|”分开不同的主机*/
        configuration.cacheManagerPeerProviderFactory(new FactoryConfiguration<FactoryConfiguration<?>>()
                .className(RMICacheManagerPeerProviderFactory.class.getName())
                .properties("peerDiscovery=manual,rmiUrls=//localhost:40004/metaCache|//localhost:40005/metaCache")
        );
        /**配宿主主机配置监听程序*/
        configuration.cacheManagerPeerListenerFactory(new FactoryConfiguration<FactoryConfiguration<?>>()
                .className(RMICacheManagerPeerListenerFactory.class.getName())
                .properties("port=40004,socketTimeoutMillis=2000")
        );
        /**默认配置*/
        CacheConfiguration defConfig=new CacheConfiguration();
        /**缓存最大个数*/
        defConfig.setMaxElementsOnDisk(10000);
        /**对象是否永久有效，一但设置了，timeout将不起作用*/
        defConfig.setEternal(false);
        /**设置对象在失效前的允许闲置时间（单位：秒）。
         * 仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
         * */
        defConfig.setTimeToIdleSeconds(1200);
        /**设置对象在失效前允许存活时间（单位：秒）。
         * 最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
         * */
        defConfig.setTimeToLiveSeconds(3000);
        /**这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。*/
        defConfig.setDiskSpoolBufferSizeMB(30);
        /**限制在磁盘上所能保存的元素的最大数量的。*/
        defConfig.setMaxEntriesLocalDisk(10000);
        /**磁盘失效线程运行时间间隔，默认是120秒。*/
        defConfig.setDiskExpiryThreadIntervalSeconds(120);
        /**当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。
         * 默认策略是LRU（最近最少使用）。FIFO（先进先出）,LFU（较少使用）。
         * */
        defConfig.setMemoryStoreEvictionPolicy("LRU");
        /**是否收集统计信息。如果需要监控缓存使用情况，应该打开这个选项。默认为关闭（统计会影响性能）。设置statistics="true"开启统计。*/
        defConfig.setStatistics(false);
        configuration.setDefaultCacheConfiguration(defConfig);
        return configuration;
    }

    public HabitEhCache(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public CacheEventListener getCacheEventListener() {
        return cacheEventListener;
    }

    public void setCacheEventListener(CacheEventListener cacheEventListener) {
        this.cacheEventListener = cacheEventListener;
    }

    public Cache getOrAddCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            synchronized (locker) {
                cache = cacheManager.getCache(cacheName);
                if (cache == null) {
                    cacheManager.addCacheIfAbsent(cacheName);
                    cache = cacheManager.getCache(cacheName);
                    if (cacheEventListener != null) {
                        cache.getCacheEventNotificationService().registerListener(cacheEventListener);
                    }
                }
            }
        }
        return cache;
    }

    @Override
    public <T> T get(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        return element != null ? (T) element.getObjectValue() : null;
    }

    @Override
    public void put(String cacheName, Object key, Object value) {
        getOrAddCache(cacheName).put(new Element(key, value));
    }

    @Override
    public void put(String cacheName, Object key, Object value, int liveSeconds) {
        if (liveSeconds <= 0) {
            put(cacheName, key, value);
            return;
        }
        Element element = new Element(key, value);
        element.setTimeToLive(liveSeconds);
        getOrAddCache(cacheName).put(element);
    }

    @Override
    public List getKeys(String cacheName) {
        return getOrAddCache(cacheName).getKeys();
    }

    @Override
    public void remove(String cacheName, Object key) {
        getOrAddCache(cacheName).remove(key);
    }

    @Override
    public void removeAll(String cacheName) {
        getOrAddCache(cacheName).removeAll();
    }

    @Override
    public <T> T get(String cacheName, Object key, IDataLoader dataLoader) {
        Object data = get(cacheName, key);
        if (data == null) {
            data = dataLoader.load();
            put(cacheName, key, data);
        }
        return (T) data;
    }

    @Override
    public <T> T get(String cacheName, Object key, IDataLoader dataLoader, int liveSeconds) {
        if (liveSeconds <= 0) {
            return get(cacheName, key, dataLoader);
        }
        Object data = get(cacheName, key);
        if (data == null) {
            data = dataLoader.load();
            put(cacheName, key, data, liveSeconds);
        }
        return (T) data;
    }

    @Override
    public Integer getTtl(String cacheName, Object key) {
        Element element = getOrAddCache(cacheName).get(key);
        return element != null ? element.getTimeToLive() : null;
    }

    @Override
    public void setTtl(String cacheName, Object key, int seconds) {
        Element element = getOrAddCache(cacheName).get(key);
        if (element == null) {
            return;
        }
        element.setTimeToLive(seconds);
        getOrAddCache(cacheName).put(element);
    }

    @Override
    public void refresh(String cacheName, Object key) {

    }

    @Override
    public void refresh(String cacheName) {

    }

    @Override
    public List getNames() {
        return Arrays.asList(cacheManager.getCacheNames());
    }
}
