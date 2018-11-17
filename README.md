
![](https://img.shields.io/github/issues/xuejiacore/altass-ng.svg)
![](https://img.shields.io/github/forks/xuejiacore/altass-ng.svg)
![](https://img.shields.io/github/stars/xuejiacore/altass-ng.svg)
![](https://img.shields.io/github/license/xuejiacore/altass-ng.svg)

# Welcome to Altass Cluster!

              _ _                     _____ _           _
        /\   | | |                   / ____| |         | |
       /  \  | | |_ __ _ ___ ___    | |    | |_   _ ___| |_ ___ _ __
      / /\ \ | | __/ _` / __/ __|   | |    | | | | / __| __/ _ \ '__|
     / ____ \| | || (_| \__ \__ \   | |____| | |_| \__ \ ||  __/ |
    /_/    \_\_|\__\__,_|___/___/    \_____|_|\__,_|___/\__\___|_|
      ::   J.Chim   ::


Altass Cluster is a distributed, reliable, and available service for efficiently collecting, aggregating. It provided a
powerful, flexible architecture based on streaming data flows. And support basic interaction with Apache Flume, Spring 
Cloud. Provided many mature solutions through build-in components, such as redis node, http node, flume, rpc and so on.


Altass Cluster is open-sourced under the Apache Software Foundation License v2.0.

## Documentation

#### Architecture

<img width="100%" src="https://github.com/xuejiacore/altass-ng/blob/master/doc/images/screenshot_20181112_01.png"/>

Example:


<img width="100%" src="https://github.com/xuejiacore/altass-ng/blob/master/doc/images/screenshot_20181112_02.png"/>

<img width="100%" src="https://github.com/xuejiacore/altass-ng/blob/master/doc/images/screenshot_20181117_03.png"/>

<img width="100%" src="https://github.com/xuejiacore/altass-ng/blob/master/doc/images/screenshot_20181117_04.png"/>

<img width="100%" src="https://github.com/xuejiacore/altass-ng/blob/master/doc/images/screenshot_20181117_05.png"/>

#### Supported Components

The Altass 1.x had supported some components list lower at flowing table.

| Name | Description | Features | Class |
| --- | --- | --- | --- |
| Debug | Base Debug Node | Basic | org.chim.altass.core.executor.debug.DebugExecutor |
| DebugStream | Base Stream Debug Node | Stream, Distribution | org.chim.altass.core.executor.debug.DebugStreamExecutor |
| FileInput | Read and Output Data | Stream, Distribution | org.chim.altass.core.executor.io.FileInputStreamExecutor |
| Http | Accept Http Request | Stream, Distribution | org.chim.altass.executor.HttpExecutor |
| Redis | Execute Single or Batch Redis cmd | Stream, Distribution | org.chim.altass.executor.RedisExecutor |
| SequenceGenerator | To Generate Sequence | Pipeline | org.chim.altass.core.executor.toolkit.GenSequenceExecutor |
| JdbcExecutor | To Execute Basic Sql Scripts | Stream | org.chim.altass.executor.JdbcExecutor |

#### How does a cluster work?

Every node are single server for executor.

##### AltassNode Init
```java
AltassNode altassNode;
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
    obtainXmlApplicationConfigLocation()
);
context.start();
altassNode = context.getBean(AltassNode.class);
```

```java
Job job = new Job("START", "END");
job.setJobId(DateUtil.format(new Date(), DateUtil.SDFYYYYMMDD));
job.setExecutorClz(JobExecutor.class);

Entry debugEntryA = new Entry("DEBUG_A");
debugEntryA.setExecutorClz(DebugExecutor.class);
job.addEntry(debugEntryA);

Entry debugEntryB = new Entry("DEBUG_B");
debugEntryB.setExecutorClz(DebugExecutor.class);
job.addEntry(debugEntryB);

Entry debugEntryB1 = new Entry("DEBUG_B1");
debugEntryB1.setExecutorClz(DebugExecutor.class);
job.addEntry(debugEntryB1);

Entry blockA = new Entry("Block");
blockA.setExecutorClz(SimpleBlockingExecutor.class);
job.addEntry(blockA);

Entry debugEntryC = new Entry("DEBUG_C");
debugEntryC.setExecutorClz(DebugExecutor.class);
job.addEntry(debugEntryC);

job.connect(start, debugEntryA);
job.connect(start, debugEntryB);
job.connect(debugEntryB, debugEntryB1);
job.connect(debugEntryA, blockA);
job.connect(debugEntryB1, blockA);
job.connect(blockA, debugEntryC);
job.connect(debugEntryC, end);

altassNode.run(new JDFWrapper(job));
```

## Contact us!

- Mail: xuejiacore@sina.com

Bug and Issue tracker.

## Compiling Altass

* Oracle Java JDK 1.8
* Apache Maven 3.x
