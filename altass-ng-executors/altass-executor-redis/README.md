# Redis 流处理节点

---

# Changelog

| UpdateTime | Description | Author | Version | Issue |
| :--- | :--- | :--- | :--- | :--- |
| 2018-11-10 17:01:45 | 初始版本 | J.Chim | v1.0.0 | Unstable |

# Performance

| 序号 | 特性 |
| :--- | :--- |
| 1 | 流式处理 |
| 2 | 内置表达式解析引擎 |
| 3 | 支持批处理 |

# Description

redis流式处理节点，可用于接收前驱的输出数据，作为当前节点的输入参数后，利用Altass节点内置的参数表达式解析引擎，对输入的 redis 命令或命
令集进行初步的运算后作为参数传入执行。

其中支持redis命令集的形式处理，同时允许获取执行redis命令后的返回结果，在整个批处理的过程中，返回的结果都是可以作为当前命令集的上下文数据
进行运算的。

并且，在执行完成每一个批处理任务后，单个redis命令或者redis命令集将会以当前节点的输出参数，因此，作为承接redis流处理节点后的任意其他节点，
都是能够获取redis各个命令运算的最终数据的。

Tips: 如果全程已流式处理的方式接入该节点，节点会以持续数据流的形式不断地处理数据，若直接后继也是流式处理节点，同样会将以流式输出的形式往
后继节点不断地输出数据，构成完整的流式处理链。


# Usage

- 支持的redis命令或命令集

```text
    var0 = hget chimtestkey 1key1;
    # 该行是注释行
    var1 = hset chimtestkey@30 1 key1 ${outparam1};
    var2 = hget chimtestkey 1 key1;
    var3 = hset chimtestkey 2 $[var2+34] $[(var2+1)*20+var1];
    var4 = hget chimtestkey 2 key1;
    hdel chimtestkey 2 $[var2+34];
```

待执行的参数支持返回值接收（可选），支持参数表达式运算，在使用参数表达式的同时，整个执行命令集的返回值（如果有定义）也是会作为变量的上下文
进行传递和运算的。

× 如果是需要对操作的key设置超时时间ttl，可以在key用分隔符"@"后缀"20"，如：chimtestkey@29，则说明对chimtestkey@20设置了ttl=20s。

- 注意：
  "${}"包含的变量是运算节点的输入参数表达式，而"$[]"是当前会话的redis批处理的变量占位符，仅运算待执行脚本的上下文内容。
