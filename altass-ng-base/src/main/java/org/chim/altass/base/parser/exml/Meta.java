package org.chim.altass.base.parser.exml;

/**
 * Class Name: Meta
 * Create Date: 17-12-12 下午10:25
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * xml 解析器的辅助元数据标记
 * <p>
 * 如果一个数据是泛型或接口类型的，那么需要进行元数据声明，以便可以准确地重新序列化为对应的实体数据
 */
public class Meta {

    /**
     * 泛型或者是接口对应的实现类类型
     */
    private Class typeClz = null;

    /**
     * 构造一个元数据
     *
     * @param typeClz 元数据类型
     */
    public Meta(Class typeClz) {
        this.typeClz = typeClz;
    }

    public Class getTypeClz() {
        return typeClz;
    }

    public void setTypeClz(Class typeClz) {
        this.typeClz = typeClz;
    }
}
