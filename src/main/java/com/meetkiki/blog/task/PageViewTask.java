package com.meetkiki.blog.task;

import org.springframework.stereotype.Component;

/**
 * 定时刷新 PV 到数据库
 *
 * @author biezhi
 * @date 2018/8/13
 */
@Component
public class PageViewTask {

//    public static Map<Long, AtomicLong> counts = new ConcurrentHashMap<>();
//
//    @Schedule(cron = "*/5 * * * * ?")
//    public void syncToDB() {
//        Set<Long> cids = counts.keySet();
//        for (Long cid : cids) {
//            long count = counts.get(cid).getAndSet(0);
//            Anima.execute("update contents set hits = hits + " + count + " where cid = " + cid);
//        }
//    }

}
