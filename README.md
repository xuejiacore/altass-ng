
![](https://img.shields.io/github/issues/xuejiacore/altass-ng.svg)
![](https://img.shields.io/github/forks/xuejiacore/altass-ng.svg)
![](https://img.shields.io/github/stars/xuejiacore/altass-ng.svg)
![](https://img.shields.io/github/license/xuejiacore/altass-ng.svg)

# Welcome to Altass Cluster!

Altass Cluster is a distributed, reliable, and available service for efficiently collecting, aggregating. It provided a
powerful, flexible architecture based on streaming data flows. And support basic interaction with Apache Flume, Spring 
Cloud. Provided many mature solutions through build-in components, such as redis node, http node, flume, rpc and so on.


Altass Cluster is open-sourced under the Apache Software Foundation License v2.0.

## Documentation

#### Architecture

![](https://github.com/xuejiacore/altass-ng/blob/master/doc/images/screenshot_20181112_01.png)

Example:

![](https://github.com/xuejiacore/altass-ng/blob/master/doc/images/screenshot_20181112_02.png)

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

#### How does a cluster work?

Every node are single server for executor.

## Contact us!

- Mail: xuejiacore@sina.com

Bug and Issue tracker.

## Compiling Altass

* Oracle Java JDK 1.8
* Apache Maven 3.x
