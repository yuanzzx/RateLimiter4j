package src.main.java.com.yuandragon.base.rateLimiter.impl;

import src.main.java.com.yuandragon.base.rateLimiter.RateLimiter;
import src.main.java.com.yuandragon.base.sync.Sync;

/**
 * 漏桶
 */
public class BucketRateLimiter implements RateLimiter {

    final Sync sync;

    public BucketRateLimiter(int token, int period) {
        sync = new Sync.FairSync(1);
        Thread thread = new Thread(() -> {
            int time = period / token;
            for (;;) {
                try {
                    // 休息200毫秒
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (sync.getPermits() < 1) {
                    sync.release(1);
                }
            }
        });
        thread.setName("token insert");
        thread.start();
    }

    @Override
    public long acquire() {
        long startTime = System.currentTimeMillis();
        sync.acquire(1);
        return System.currentTimeMillis() - startTime;
    }



}
