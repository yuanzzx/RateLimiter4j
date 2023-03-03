package src.main.java.com.yuandragon;

import src.main.java.com.yuandragon.base.rateLimiter.impl.TokenRateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

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
