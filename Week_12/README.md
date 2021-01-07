

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

使用 docker-compose 重新配置主从

```
# Replication
role:master
connected_slaves:2
slave0:ip=192.168.0.130,port=6380,state=online,offset=3486,lag=1
slave1:ip=192.168.0.130,port=6381,state=online,offset=3486,lag=1
master_replid:8ef05a6d8119281fa77506c874425971119577e8
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:3486
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:3486
```

```
# Replication
role:slave
master_host:redis-master
master_port:6379
master_link_status:up
master_last_io_seconds_ago:2
master_sync_in_progress:0
slave_repl_offset:4662
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:8ef05a6d8119281fa77506c874425971119577e8
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:4662
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:4662
```

```
# Replication
role:slave
master_host:redis-master
master_port:6379
master_link_status:up
master_last_io_seconds_ago:9
master_sync_in_progress:0
slave_repl_offset:4732
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:8ef05a6d8119281fa77506c874425971119577e8
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:4732
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:4732
```

主从测试

```
# redis-master
127.0.0.1:6379> set aa 11
OK

# redis-slave-1
127.0.0.1:6379> get aa
"11"
127.0.0.1:6379> set bb 11
(error) READONLY You can't write against a read only replica.

# redis-slave-2
127.0.0.1:6379> get aa
"11"
127.0.0.1:6379> set cc 11
(error) READONLY You can't write against a read only replica.

```



使用 sentinel.conf 配置文件 在Mac  上一直无法成功配置 sentinel高可用，使用 docker-compose 方式在Mac 上也仍然不成功，所以改用虚拟机+docker-compose来完成功能，初步怀疑是Mac 的配置问题，后续继续研究

###### sentinel高可用

sentinel启动后，查看sentinel是否成功

```
root@docker-database:/usr/local/docker/redis-sentinel# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                                NAMES
b4d83e4f91b5        redis               "docker-entrypoint.s…"   5 seconds ago       Up 3 seconds        6379/tcp, 0.0.0.0:26381->26379/tcp   redis-sentinel-3
6e14adf4fe6e        redis               "docker-entrypoint.s…"   5 seconds ago       Up 4 seconds        6379/tcp, 0.0.0.0:26380->26379/tcp   redis-sentinel-2
b7efdcdb6d42        redis               "docker-entrypoint.s…"   5 seconds ago       Up 4 seconds        6379/tcp, 0.0.0.0:26379->26379/tcp   redis-sentinel-1
20ff2fd9711e        redis               "docker-entrypoint.s…"   9 minutes ago       Up 9 minutes        0.0.0.0:6381->6379/tcp               redis-slave-2
ec7e63b78cd6        redis               "docker-entrypoint.s…"   9 minutes ago       Up 9 minutes        0.0.0.0:6380->6379/tcp               redis-slave-1
43bae14e9334        redis               "docker-entrypoint.s…"   9 minutes ago       Up 9 minutes        0.0.0.0:6379->6379/tcp               redis-master
```

```
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=192.168.0.130:6379,slaves=2,sentinels=4
```

sentinel启动日志

```
Attaching to redis-sentinel-3, redis-sentinel-2, redis-sentinel-1
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.333 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.338 # Redis version=6.0.9, bits=64, commit=00000000, modified=0, pid=1, just started
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.338 # Configuration loaded
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.340 * Running mode=sentinel, port=26379.
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.342 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.357 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.357 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.357 # Sentinel ID is 113d5de62c2d011a73be826cf62ee0888ac2749d
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.357 # +monitor master mymaster 192.168.0.130 6379 quorum 2
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.358 * +slave slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.382 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.382 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.382 * +slave slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.401 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:07.401 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:09.422 * +sentinel sentinel b6bed57c98f823b13d180c0ccd72a7739571436c 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:09.423 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:09.423 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:10.414 * +sentinel-invalid-addr sentinel b6bed57c98f823b13d180c0ccd72a7739571436c 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:10.414 * +sentinel sentinel 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:10.417 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:10.417 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:11.413 * +sentinel-address-switch master mymaster 192.168.0.130 6379 ip 192.168.0.130 port 26381 for b6bed57c98f823b13d180c0ccd72a7739571436c
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:11.414 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:16:11.414 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.370 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.371 # Redis version=6.0.9, bits=64, commit=00000000, modified=0, pid=1, just started
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.371 # Configuration loaded
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.372 * Running mode=sentinel, port=26379.
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.372 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.383 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.383 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.383 # Sentinel ID is b6bed57c98f823b13d180c0ccd72a7739571436c
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.383 # +monitor master mymaster 192.168.0.130 6379 quorum 2
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.384 * +slave slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6379
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.402 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.402 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.402 * +slave slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.409 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:07.409 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:09.392 * +sentinel sentinel 113d5de62c2d011a73be826cf62ee0888ac2749d 192.168.0.130 26379 @ mymaster 192.168.0.130 6379
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:09.393 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:09.393 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:10.416 * +sentinel sentinel 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:10.417 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-2 | 1:X 07 Jan 2021 18:16:10.418 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.346 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.347 # Redis version=6.0.9, bits=64, commit=00000000, modified=0, pid=1, just started
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.347 # Configuration loaded
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.349 * Running mode=sentinel, port=26379.
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.349 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.371 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.371 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.374 # Sentinel ID is 78a5e5fcd25bf37f32f65db5917efda9f36bfaba
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.374 # +monitor master mymaster 192.168.0.130 6379 quorum 2
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.375 * +slave slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.378 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.378 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.378 * +slave slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.387 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:08.388 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:09.393 * +sentinel sentinel 113d5de62c2d011a73be826cf62ee0888ac2749d 192.168.0.130 26379 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:09.403 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:09.403 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:09.423 * +sentinel sentinel b6bed57c98f823b13d180c0ccd72a7739571436c 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:09.432 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:09.432 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:11.490 * +sentinel-invalid-addr sentinel b6bed57c98f823b13d180c0ccd72a7739571436c 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:11.490 * +sentinel sentinel 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:11.491 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:11.492 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:11.505 * +sentinel-address-switch master mymaster 192.168.0.130 6379 ip 192.168.0.130 port 26381 for b6bed57c98f823b13d180c0ccd72a7739571436c
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:11.507 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:16:11.508 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:19:08.158 * +fix-slave-config slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:19:08.161 * +fix-slave-config slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6379
```

停掉主redis验证sentinel高可用

```
root@docker-database:/usr/local/docker/redis# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                                NAMES
b4d83e4f91b5        redis               "docker-entrypoint.s…"   31 minutes ago      Up 31 minutes       6379/tcp, 0.0.0.0:26381->26379/tcp   redis-sentinel-3
6e14adf4fe6e        redis               "docker-entrypoint.s…"   31 minutes ago      Up 31 minutes       6379/tcp, 0.0.0.0:26380->26379/tcp   redis-sentinel-2
b7efdcdb6d42        redis               "docker-entrypoint.s…"   31 minutes ago      Up 31 minutes       6379/tcp, 0.0.0.0:26379->26379/tcp   redis-sentinel-1
20ff2fd9711e        redis               "docker-entrypoint.s…"   41 minutes ago      Up 41 minutes       0.0.0.0:6381->6379/tcp               redis-slave-2
ec7e63b78cd6        redis               "docker-entrypoint.s…"   41 minutes ago      Up 41 minutes       0.0.0.0:6380->6379/tcp               redis-slave-1
```

30s后3个sentinel选出了新的主

```
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=192.168.0.130:6380,slaves=2,sentinels=4
```

日志如下：

```
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:43.424 # +sdown master mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.470 # +sdown master mymaster 192.168.0.130 6379
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.498 # +sdown master mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.547 # +odown master mymaster 192.168.0.130 6379 #quorum 4/2
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.547 # +new-epoch 1
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.547 # +try-failover master mymaster 192.168.0.130 6379
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.550 # +odown master mymaster 192.168.0.130 6379 #quorum 3/2
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.550 # +new-epoch 1
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.550 # +try-failover master mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.552 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.552 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.553 # +vote-for-leader 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 1
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.554 # 78a5e5fcd25bf37f32f65db5917efda9f36bfaba voted for 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 1
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.554 # b6bed57c98f823b13d180c0ccd72a7739571436c voted for 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 1
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:43.559 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:43.559 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:43.559 # +new-epoch 1
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.559 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.559 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.559 # +vote-for-leader b6bed57c98f823b13d180c0ccd72a7739571436c 1
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.563 # 78a5e5fcd25bf37f32f65db5917efda9f36bfaba voted for 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 1
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:43.568 # 113d5de62c2d011a73be826cf62ee0888ac2749d voted for 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 1
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:43.568 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:43.568 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:43.568 # +vote-for-leader 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 1
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.570 # 113d5de62c2d011a73be826cf62ee0888ac2749d voted for 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 1
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.608 # +elected-leader master mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.608 # +failover-state-select-slave master mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.691 # +selected-slave slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.691 * +failover-state-send-slaveof-noone slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:43.768 * +failover-state-wait-promotion slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.509 # +odown master mymaster 192.168.0.130 6379 #quorum 3/2
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.509 # Next failover delay: I will not start a failover before Thu Jan  7 18:48:44 2021
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:44.538 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:44.538 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:44.538 # +promoted-slave slave 192.168.0.130:6380 192.168.0.130 6380 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:44.538 # +failover-state-reconf-slaves master mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:44.620 * +slave-reconf-sent slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.620 # +config-update-from sentinel 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.620 # +switch-master mymaster 192.168.0.130 6379 192.168.0.130 6380
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.620 * +slave slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6380
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.620 * +slave slave 192.168.0.130:6379 192.168.0.130 6379 @ mymaster 192.168.0.130 6380
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.622 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-1 | 1:X 07 Jan 2021 18:42:44.622 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:44.626 # +config-update-from sentinel 78a5e5fcd25bf37f32f65db5917efda9f36bfaba 192.168.0.130 26381 @ mymaster 192.168.0.130 6379
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:44.626 # +switch-master mymaster 192.168.0.130 6379 192.168.0.130 6380
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:44.626 * +slave slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6380
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:44.626 * +slave slave 192.168.0.130:6379 192.168.0.130 6379 @ mymaster 192.168.0.130 6380
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:44.630 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-2 | 1:X 07 Jan 2021 18:42:44.630 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.555 * +slave-reconf-inprog slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.555 * +slave-reconf-done slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.654 # +failover-end master mymaster 192.168.0.130 6379
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.654 # +switch-master mymaster 192.168.0.130 6379 192.168.0.130 6380
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.654 * +slave slave 192.168.0.130:6381 192.168.0.130 6381 @ mymaster 192.168.0.130 6380
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.654 * +slave slave 192.168.0.130:6379 192.168.0.130 6379 @ mymaster 192.168.0.130 6380
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.655 # Could not rename tmp config file (Device or resource busy)
redis-sentinel-3 | 1:X 07 Jan 2021 18:42:45.655 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Device or resource busy
```

再启动redis-master，原来的6379已经变成了6380的从

```
# Replication
role:master
connected_slaves:2
slave0:ip=192.168.0.130,port=6381,state=online,offset=533145,lag=0
slave1:ip=192.168.0.130,port=6379,state=online,offset=533145,lag=0
```



###### Cluster集群

创建redis集群

```
redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1
```

cluster info

```
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
cluster_slots_pfail:0
cluster_slots_fail:0
cluster_known_nodes:6
cluster_size:3
cluster_current_epoch:6
cluster_my_epoch:1
cluster_stats_messages_ping_sent:682
cluster_stats_messages_pong_sent:701
cluster_stats_messages_sent:1383
cluster_stats_messages_ping_received:696
cluster_stats_messages_pong_received:682
cluster_stats_messages_meet_received:5
cluster_stats_messages_received:1383
```

