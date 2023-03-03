package src.main.java.com.yuandragon.base.rateLimiter.impl;

import src.main.java.com.yuandragon.base.rateLimiter.RateLimiter;
import src.main.java.com.yuandragon.base.sync.Sync;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * 令牌桶
 */
public class TokenRateLimiter implements RateLimiter {

    /**
     * 给定数量
     */
    final int token;

    final Sync sync;

    public TokenRateLimiter(int token) {
        sync = new Sync.FairSync(token);
        this.token = token;
        Thread thread = new Thread(() -> {
            int time = 1000 / token;
            for (;;) {
                if (sync.getPermits() < token) {
                    sync.release(1);
                }
                try {
                    // 休息200毫秒
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
