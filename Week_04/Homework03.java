package java0.conc0303;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 *
 * 一个简单的代码参考：
 */
public class Homework03 {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException, BrokenBarrierException {
        
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        //1.Future
        start = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Future<Integer> future = threadPool.submit(() -> sum());
        System.out.println("Future---异步计算结果为："+future.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //2.FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(Homework03::sum);
        new Thread(futureTask).start();
        System.out.println("FutureTask---异步计算结果为："+futureTask.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //3.CountDownLatch
        start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            System.out.println("CountDownLatch---异步计算结果为："+sum());
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //4.Semaphore
        start = System.currentTimeMillis();
        Semaphore semaphore = new Semaphore(0);
        threadPool.execute(() -> {
            System.out.println("Semaphore---异步计算结果为："+sum());
            semaphore.release();
        });
        semaphore.acquire();
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //5.CyclicBarrier
        start = System.currentTimeMillis();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        threadPool.execute(() -> {
            System.out.println("CyclicBarrier---异步计算结果为："+sum());
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        cyclicBarrier.await();
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //6.CompletableFuture
        start = System.currentTimeMillis();
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> sum());
        completableFuture.join();
        System.out.println("CompletableFuture---异步计算结果为："+completableFuture.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

        //7.Condition
        start = System.currentTimeMillis();
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        AtomicInteger result = new AtomicInteger();
        threadPool.execute(() -> {
            lock.lock();
            result.set(sum());
            condition.signal();
            lock.unlock();
        });
        lock.lock();
        condition.await();
        lock.unlock();
        System.out.println("Condition---异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");

//        int result = sum(); //这是得到的返回值
        
        // 确保  拿到result 并输出
//        System.out.println("异步计算结果为："+result);
         

        
        // 然后退出main线程
    }
    
    private static int sum() {
        return fibo(36);
    }
    
    private static int fibo(int a) {
        if ( a < 2) 
            return 1;
        return fibo(a-1) + fibo(a-2);
    }
}
