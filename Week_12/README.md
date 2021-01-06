

##### mac 使用 Docker 通过 redis.conf 配置 redis 主从复制，sentinel高可用，Cluster集群

###### 配置 redis 主从复制

```
docker run -p 6379:6379 --name redis-6379 -v /usr/local/docker/redisconfig/redis6379.conf:/etc/redis/redis6379.conf -v /etc/redis/data:/data -d redis redis-server /etc/redis/redis6379.conf --appendonly yes
docker exec -it redis-6379 /bin/bash
```

```
docker run -p 6380:6379 --name redis-6380 -v /usr/local/docker/redisconfig/redis6379.conf:/etc/redis/redis6379.conf -v /etc/redis/data:/data -d redis redis-server /etc/redis/redis6379.conf --appendonly yes
docker exec -it redis-6380 /bin/bash
$redis-cli
slaveof 172.17.0.2 6379
```

```
docker run -p 6381:6379 --name redis-6381 -v /usr/local/docker/redisconfig/redis6379.conf:/etc/redis/redis6379.conf -v /etc/redis/data:/data -d redis redis-server /etc/redis/redis6379.conf --appendonly yes
docker exec -it redis-6381 /bin/bash
$redis-cli
slaveof 172.17.0.2 6379
```

使用命令 info 查看主从是否连接上，简介内容如下，表示已经连接上

```
# Replication
role:master
connected_slaves:2
slave0:ip=172.17.0.4,port=6379,state=online,offset=20901,lag=1
slave1:ip=172.17.0.3,port=6379,state=online,offset=20901,lag=0
```

```
# Replication
role:slave
master_host:172.17.0.2
master_port:6379
master_link_status:up
```

```
# Replication
role:slave
master_host:172.17.0.2
master_port:6379
master_link_status:up
```

```
# master
127.0.0.1:6379> keys *
1) "uptime"
2) "bb"
3) "aa"
4) "cc"
```

```
# slave
127.0.0.1:6379> keys *
1) "aa"
2) "uptime"
3) "bb"
4) "cc"
```

```
# slave
127.0.0.1:6379> keys *
1) "uptime"
2) "bb"
3) "cc"
4) "aa"
```



###### sentinel高可用

```
# redis-sentinel实例1
docker run -it --name redis-sentinel1 -v /root/redis/sentinel1.conf:/usr/local/etc/redis/sentinel.conf -d redis /bin/bash
    
# redis-sentinel实例2
docker run -it --name redis-sentinel2 -v /root/redis/sentinel2.conf:/usr/local/etc/redis/sentinel.conf -d redis /bin/bash
    
# redis-sentinel实例3
docker run -it --name redis-sentinel3 -v /root/redis/sentinel3.conf:/usr/local/etc/redis/sentinel.conf -d redis /bin/bash
```

docker run -it --name redis-sentinel1 -p 26379:26379 -v /usr/local/docker/redisconfig/sentinel-26379.conf:/usr/local/etc/redis/sentinel.conf -d redis /bin/bash





