# 脚本表达式解释器

- 用于动态脚本值的解析，如：SELECT * FROM TABLE_NAME_${op_time} 将对应的op_time进行实值替换
- 提供表达式解析：
```javascript 1.8
if (a == b) {
    c = a + b;
} else {
    c = a - b;
}
```