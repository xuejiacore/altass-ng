package org.chim.altass.core.executor;

import java.io.Serializable;
import java.util.Map;

/**
 * Class Name: ContextAttrWrapper
 * Create Date: 18-1-4 上午9:38
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class ContextAttrWrapper implements Serializable {

    private static final long serialVersionUID = -7003344064413147044L;
    private Map<Integer, Object> attributes = null;

    public Map<Integer, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<Integer, Object> attributes) {
        this.attributes = attributes;
    }
}
