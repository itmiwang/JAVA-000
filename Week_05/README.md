#### 作业记录：

##### 一、写代码实现 Spring Bean 的装配，方式越多越好（XML、Annotation 都可以）, 提交到 Github。

项目见springHomework01

1、基于XML的装配（构造注入、设值注入）

```
//执行结果
XmlEntityDemo{username='张三', password='123456', list=["constructorvalue1", "constructorvalue2"]}
XmlEntityDemo{username='李四', password='111111', list=["setlistvalue1", "setlistvalue2"]}
```

2、通过注解装配Bean

```
//执行结果
AnnotationEntityDemo{id='1', name='王五'}
```

3、自动装配

```
//执行结果
id值为：2
name值为：赵六
```



##### 二、给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。

项目见spring-homework-02

```
//执行EnableAutoConfigurationTest.java
hello...........
Student(id=101, name=KK101)
```



##### 三、研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：

1）使用 JDBC 原生接口，实现数据库的增删改查操作。
2）使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
3）配置 Hikari 连接池，改进上述操作。提交代码到 Github。

项目见spring-homework-03