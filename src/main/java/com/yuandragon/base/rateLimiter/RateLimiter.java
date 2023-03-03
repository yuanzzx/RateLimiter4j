package src.main.java.com.yuandragon.base.rateLimiter;

/**
 * 限流顶层接口
 */
public interface RateLimiter {

    long acquire();

}
