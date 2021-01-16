##### 一、搭建ActiveMQ服务， 基于JMS， 写代码分别实现对于queue和topic的消息生产和消费， 代码提交到github。

代码见 jms-activemp 项目

##### 二、搭建一个3节点Kafka集群， 测试功能和性能； 实现spring kafka下对kafka集群的操作， 将代码提交到github。

先使用 docker-compose 搭建一个 zookeeper 集群

创建 docker-compose-zookeeper-cluster.yml 如下：

```yaml
version: '3.7'

networks:
  docker_net:
    external: true

services:
  zoo1:
    image: zookeeper
    restart: unless-stopped
    hostname: zoo1
    container_name: zoo1
    ports:
      - 2181:2181
    environment:
      ZOO_MY_ID: 1
      ZOO_SERVERS: server.1=0.0.0.0:2888:3888;2181 server.2=zoo2:2888:3888;2181 server.3=zoo3:2888:3888;2181
    volumes:
      - ./zookeeper/zoo1/data:/data
      - ./zookeeper/zoo1/datalog:/datalog

  zoo2:
    image: zookeeper
    restart: unless-stopped
    hostname: zoo2
    container_name: zoo2
    ports:
      - 2182:2181
    environment:
      ZOO_MY_ID: 2
      ZOO_SERVERS: server.1=zoo1:2888:3888;2181 server.2=0.0.0.0:2888:3888;2181 server.3=zoo3:2888:3888;2181
    volumes:
      - ./zookeeper/zoo2/data:/data
      - ./zookeeper/zoo2/datalog:/datalog

  zoo3:
    image: zookeeper
    restart: unless-stopped
    hostname: zoo3
    container_name: zoo3
    ports:
      - 2183:2181
    environment:
      ZOO_MY_ID: 3
      ZOO_SERVERS: server.1=zoo1:2888:3888;2181 server.2=zoo2:2888:3888;2181 server.3=0.0.0.0:2888:3888;2181
    volumes:
      - ./zookeeper/zoo3/data:/data
      - ./zookeeper/zoo3/datalog:/datalog
```

执行 `docker-compose -f docker-compose-zookeeper-cluster.yml up -d` 启动 zookeeper 集群。

```
root@docker-database:/usr/local/docker/zookeeper# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS              PORTS                                                  NAMES
dcefbdb980d4        zookeeper           "/docker-entrypoint.…"   About a minute ago   Up 57 seconds       2888/tcp, 3888/tcp, 8080/tcp, 0.0.0.0:2183->2181/tcp   zoo3
6e33edb2b9e5        zookeeper           "/docker-entrypoint.…"   About a minute ago   Up 58 seconds       2888/tcp, 3888/tcp, 0.0.0.0:2181->2181/tcp, 8080/tcp   zoo1
fbeea840d293        zookeeper           "/docker-entrypoint.…"   About a minute ago   Up 59 seconds       2888/tcp, 3888/tcp, 8080/tcp, 0.0.0.0:2182->2181/tcp   zoo2
```

```
root@docker-database:/usr/local/docker/zookeeper# docker exec -it zoo1 /bin/bash
root@zoo1:/apache-zookeeper-3.6.2-bin# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: follower
```

```
root@docker-database:/usr/local/docker/zookeeper# docker exec -it zoo2 /bin/bash
root@zoo2:/apache-zookeeper-3.6.2-bin# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: leader
```

```
root@docker-database:/usr/local/docker/zookeeper# docker exec -it zoo3 /bin/bash
root@zoo3:/apache-zookeeper-3.6.2-bin# zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /conf/zoo.cfg
Client port found: 2181. Client address: localhost. Client SSL: false.
Mode: follower
```

再使用 docker-compose 搭建 kafka 集群

创建 docker-compose-kafka-cluster.yml 如下：

```yaml
version: '3.7'

networks:
  docker_net:
    external: true

services:
  kafka1:
    image: wurstmeister/kafka
    restart: unless-stopped
    container_name: kafka1
    ports:
      - "9092:9092"
    external_links:
      - zoo1
      - zoo2
      - zoo3
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ADVERTISED_HOST_NAME: 192.168.0.130                   ## 修改:宿主机IP
      KAFKA_ADVERTISED_PORT: 9092                                 ## 修改:宿主机映射port
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://192.168.0.130:9092    ## 绑定发布订阅的端口。修改:宿主机IP
      KAFKA_ZOOKEEPER_CONNECT: "zoo1:2181,zoo2:2181,zoo3:2181"
    volumes:
      - "./kafka/kafka1/docker.sock:/var/run/docker.sock"
      - "./kafka/kafka1/data/:/kafka"

  kafka2:
    image: wurstmeister/kafka
    restart: unless-stopped
    container_name: kafka2
    ports:
      - "9093:9092"
    external_links:
      - zoo1
      - zoo2
      - zoo3
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ADVERTISED_HOST_NAME: 192.168.0.130                 ## 修改:宿主机IP
      KAFKA_ADVERTISED_PORT: 9093                               ## 修改:宿主机映射port
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://192.168.0.130:9093   ## 修改:宿主机IP
      KAFKA_ZOOKEEPER_CONNECT: "zoo1:2181,zoo2:2181,zoo3:2181"
    volumes:
      - "./kafka/kafka2/docker.sock:/var/run/docker.sock"
      - "./kafka/kafka2/data/:/kafka"

  kafka3:
    image: wurstmeister/kafka
    restart: unless-stopped
    container_name: kafka3
    ports:
      - "9094:9092"
    external_links:
      - zoo1
      - zoo2
      - zoo3
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ADVERTISED_HOST_NAME: 192.168.0.130                 ## 修改:宿主机IP
      KAFKA_ADVERTISED_PORT: 9094                              ## 修改:宿主机映射port
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://192.168.0.130:9094   ## 修改:宿主机IP
      KAFKA_ZOOKEEPER_CONNECT: "zoo1:2181,zoo2:2181,zoo3:2181"
    volumes:
      - "./kafka/kafka3/docker.sock:/var/run/docker.sock"
      - "./kafka/kafka3/data/:/kafka"

  kafka-manager:
    image: sheepkiller/kafka-manager:latest
    restart: unless-stopped
    container_name: kafka-manager
    hostname: kafka-manager
    ports:
      - "9000:9000"
    links:            # 连接本compose文件创建的container
      - kafka1
      - kafka2
      - kafka3
    external_links:   # 连接本compose文件以外的container
      - zoo1
      - zoo2
      - zoo3
    environment:
      ZK_HOSTS: zoo1:2181,zoo2:2181,zoo3:2181                 ## 修改:宿主机IP
      TZ: CST-8
```

执行 `docker-compose -f docker-compose-kafka-cluster.yml up -d` 启动 kafka 集群。

```
root@docker-database:/usr/local/docker/kafka# docker ps
CONTAINER ID        IMAGE                              COMMAND                  CREATED             STATUS              PORTS                                                  NAMES
6f6992acb877        sheepkiller/kafka-manager:latest   "./start-kafka-manag…"   3 minutes ago       Up 3 minutes        0.0.0.0:9000->9000/tcp                                 kafka-manager
d1105eff27f0        wurstmeister/kafka                 "start-kafka.sh"         3 minutes ago       Up 8 seconds        0.0.0.0:9094->9092/tcp                                 kafka3
45d379c7dd4e        wurstmeister/kafka                 "start-kafka.sh"         3 minutes ago       Up 8 seconds        0.0.0.0:9093->9092/tcp                                 kafka2
be8b453a6ed8        wurstmeister/kafka                 "start-kafka.sh"         3 minutes ago       Up 4 seconds        0.0.0.0:9092->9092/tcp                                 kafka1
```





