package src.main.java.com.yuandragon.base.sync;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public abstract class Sync extends AbstractQueuedSynchronizer {

    /**
     * 自旋锁阈值
     */
    final int threshold = 16;

    Sync(int permits) {
        setState(permits);
    }

   public final int getPermits() {
        return getState();
    }

    protected final boolean tryRelease(int releases) {
        for (;;) {
            int current = getState();
            int next = current + releases;
            if (next < current) // overflow
                throw new Error("Maximum permit count exceeded");
            if (compareAndSetState(current, next))
                return true;
        }
    }

    public static final class NonFairSync extends Sync {
        private static final long serialVersionUID = -2694183684443567898L;

        NonFairSync(int permits) {
            super(permits);
        }

        protected boolean tryAcquire(int acquires) {
            int count = 0;
            for (; ; ) {
                int available = getState();
                int remaining = available - acquires;
                if (remaining > -1 &&
                        compareAndSetState(available, remaining))
                    return true;
                if (count > threshold) {
                    return false;
                }
                count++;
            }
        }
    }

    /**
     * Fair version
     */
    public static final class FairSync extends Sync {
        private static final long serialVersionUID = 2014338818796000944L;

        public FairSync(int permits) {
            super(permits);
        }

        protected boolean tryAcquire(int acquires) {
            int count = 1;
            for (;;) {
                if (hasQueuedPredecessors())
                    return false;
                int available = getState();
                int remaining = available - acquires;
                if (remaining > -1 &&
                        compareAndSetState(available, remaining))
                    return true;
                if (count > threshold) {
                    return false;
                }
                count++;
            }
        }
    }
}
