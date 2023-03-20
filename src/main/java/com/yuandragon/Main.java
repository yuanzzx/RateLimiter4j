package src.main.java.com.yuandragon;

import src.main.java.com.yuandragon.base.rateLimiter.RateLimiter;
import src.main.java.com.yuandragon.base.rateLimiter.impl.BucketRateLimiter;
import src.main.java.com.yuandragon.base.rateLimiter.impl.TokenRateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

//        token(executorService);
        bucket(executorService);
    }

    private static void bucket(ExecutorService executorService) throws InterruptedException {
        RateLimiter tokenRateLimiter = new BucketRateLimiter(5, 1000);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> System.out.println(Thread.currentThread().getName() + "--" + tokenRateLimiter.acquire()));
        }
        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> System.out.println(Thread.currentThread().getName() + "--" + tokenRateLimiter.acquire()));
        }
    }

    private static void token(ExecutorService executorService) throws InterruptedException {
        TokenRateLimiter tokenRateLimiter = new TokenRateLimiter(5);

        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "--" + tokenRateLimiter.acquire());
                }
            });
        }
        Thread.sleep(5000);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "--" + tokenRateLimiter.acquire());
                }
            });
        }
    }
}
