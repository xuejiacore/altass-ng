package org.chim.altass.core.executor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Name: RelatedContext
 * Create Date: 18-1-3 上午10:22
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 * <p>
 * 需要进行分布式序列化存储，map对象使用一个wrapper包装
 */
public class RelatedContextWrapper implements Serializable {

    private static final long serialVersionUID = 3774470353689927098L;

    private Map<String, ExecuteContext> relatedContextMap = null;

    public RelatedContextWrapper() {
        this.relatedContextMap = new HashMap<String, ExecuteContext>();
    }

    public Map<String, ExecuteContext> getRelatedContextMap() {
        return relatedContextMap;
    }

    public void setRelatedContextMap(Map<String, ExecuteContext> relatedContextMap) {
        this.relatedContextMap = relatedContextMap;
    }
}
