package org.linlinjava.litemall.wx;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class test1 {


    public static void main(String[] args) throws InterruptedException, ExecutionException {

         ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(9);  //队列长度

         RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
                                                                            //核心线程数      //最大线程数
         ThreadPoolExecutor executorService = new ThreadPoolExecutor(9, 9, 1000, TimeUnit.MILLISECONDS, WORK_QUEUE, HANDLER);

         //以上线程池配置
    //         ThreadPoolExecutor executorService = new ThreadPoolExecutor(9, 9, 1000, TimeUnit.MILLISECONDS, workQueue );
        Callable<List> bannerListCallable = () -> tt();    //执行方法1
        Callable<List> bannerListCallable2 = () -> tt3();  //执行方法2


        FutureTask<List> bannerTask = new FutureTask<>(bannerListCallable);     //固定步骤
        FutureTask<List> bannerTask2 = new FutureTask<>(bannerListCallable2);

        executorService.submit(bannerTask);    //提交执行
        executorService.submit(bannerTask2);
        List list = bannerTask.get();
        for(int i=0;i<list.size();i++){
            System.out.println(list.get(i));

        }


    }

    public static List <String> tt() throws InterruptedException {
        ArrayList<String> ts = new ArrayList<>();
        ts.add("1");
        System.out.println("我线程1《《《《《《《");
        ts.add("w");
        System.out.println("我线程1《《《《《《《88888");
        ts.add("e");
        return ts;
    }

    public static  List <String> tt3(){
        ArrayList<String> ts5 = new ArrayList<>();
        ts5.add("9");
        System.out.println("我线程2《《《《《《《");
        ts5.add("w");
        System.out.println("我线程2《《《《《《《88888");
        ts5.add("e");
        return ts5;
    }
}
