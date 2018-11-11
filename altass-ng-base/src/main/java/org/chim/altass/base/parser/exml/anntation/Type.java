package org.chim.altass.base.parser.exml.anntation;

/**
 * Class Name: Type
 * Create Date: 17-12-10 下午11:51
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public enum Type {
    BASIC(0x0, "基本类型"),
    COLLECTION(0x2, "集合类型");

    private String typeName = null;
    private Integer typeId = null;

    Type(Integer typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
}
