### Week02-01

##### 1、使用 GCLogAnalysis.java 自己演练一遍串行/并行/CMS/G1的案例

<img src="/Users/miwang/Library/Application Support/typora-user-images/image-20201025112727833.png" alt="image-20201025112727833" style="zoom:50%;" />

##### 串行GC

![Xnip2020-10-25_11-51-37](/Users/miwang/javaWeb/advancement-workspace/exercise/02/screenshot/Xnip2020-10-25_11-51-37.png)

调大堆内存，GC时间变长，GC次数降低，运行次数也增加，运行效率提升

![Xnip2020-10-25_11-53-43](/Users/miwang/javaWeb/advancement-workspace/exercise/02/screenshot/Xnip2020-10-25_11-53-43.png)



##### 并行GC（默认）

![Xnip2020-10-25_12-11-42](/Users/miwang/javaWeb/advancement-workspace/exercise/02/screenshot/Xnip2020-10-25_12-11-42.png)

![image-20201025121411504](/Users/miwang/Library/Application Support/typora-user-images/image-20201025121411504.png)

注：如果去掉-Xms配置，即没有-Xms = -Xmx，默认-Xms的配置较小时，则理论上第一次发生GC的时间会提前

##### CMS

![image-20201025174205528](/Users/miwang/Library/Application Support/typora-user-images/image-20201025174205528.png)

​	阶段 1：Initial Mark（初始标记）
​	阶段 2：Concurrent Mark（并发标记）
​	阶段 3：Concurrent Preclean（并发预清理）
​	阶段 4： Final Remark（最终标记）
​	阶段 5： Concurrent Sweep（并发清除）
​	阶段 6： Concurrent Reset（并发重置）

##### G1

![image-20201025175143927](/Users/miwang/Library/Application Support/typora-user-images/image-20201025175143927.png)

​	Evacuation Pause: young（纯年轻代模式转移暂停）
​	Concurrent Marking（并发标记）
​	阶段 1: Initial Mark（初始标记）
​	阶段 2: Root Region Scan（Root区扫描）
​	阶段 3: Concurrent Mark（并发标记）
​	阶段 4: Remark（再次标记）
​	阶段 5: Cleanup（清理）
​	Evacuation Pause (mixed)（转移暂停: 混合模式）
​	Full GC (Allocation Failure)

![image-20201025175457224](/Users/miwang/Library/Application Support/typora-user-images/image-20201025175457224.png)



##### 不同GC的总结(使用多次平均值进行统计)

|      |                  -Xmx512m -Xms512m                  | -Xmx1g -Xms1g                                       | -Xmx2g -Xms2g                      | -Xmx4g -Xms4g                      |
| ---- | :-------------------------------------------------: | --------------------------------------------------- | ---------------------------------- | ---------------------------------- |
| 串行 |         生成对象次数:7942，youngGC平均14次          | 生成对象次数:9488，youngGC平均9次                   | 生成对象次数:10097，youngGC平均4次 | 生成对象次数:8455，youngGC平均2次  |
| 并行 | 生成对象次数:7023，执行FullGC 5次，YoungGC 24次左右 | 生成对象次数:8430，执行FullGC 1次，YoungGC 12次左右 | 生成对象次数:9851，youngGC平均5次  | 生成对象次数:7757，youngGC平均2次  |
| CMS  | 生成对象次数:8933，执行FullGC 次，YoungGC 27次左右  | 生成对象次数:9411，执行FullGC 1次，YoungGC 9次左右  | 生成对象次数:10460，youngGC平均5次 | 生成对象次数:10022，youngGC平均3次 |
| G1   |                  生成对象次数:8319                  | 生成对象次数:8978                                   | 生成对象次数:9633                  | 生成对象次数:9310                  |

感觉内存在2g后，增加内存，产生对象数量反而有所下降



##### 2、使用压测工具（wrk或sb），演练gateway-server-0.0.1-SNAPSHOT.jar 示例

![image-20201028155423822](/Users/miwang/Library/Application Support/typora-user-images/image-20201028155423822.png)

wrk各项参数说明：

​	Latency：响应时间

​	Req/Sec：单个线程处理请求数

​	Avg:平均值

​	Stdev:标准差，值越大说明数据分布均匀，可能是机器或服务性能不稳定导致。

​	Max:最大值

​	+/- Stdev：正负标准差比例，差值比标准差大或小的数据比率

​	Latency Distribution：延时分布多少ms一下请求数比例

​	Requests/sec：平均每秒处理请求数

​	Transfer/sec：平均每秒传输数据量

|      | Avg(512m) | Stdev(512m) | Max(512m) | Avg(1g) | Stdev(1g) | Max(1g) | Avg(2g) | Stdev(2g) | Max(2g) |
| ---- | --------- | ----------- | --------- | ------- | --------- | ------- | ------- | --------- | ------- |
| 串行 |           |             |           |         |           |         |         |           |         |
| 并行 |           |             |           |         |           |         |         |           |         |
| CMS  |           |             |           |         |           |         |         |           |         |
| G1   |           |             |           |         |           |         |         |           |         |

相同GC下，内存增大，延时和吞吐都会变好，并行和CMS差别不大。

