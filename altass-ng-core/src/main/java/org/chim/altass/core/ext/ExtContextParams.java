package org.chim.altass.core.ext;

import org.chim.altass.base.cache.BasicRedisKey;
import org.chim.altass.core.constant.EurekaSystemRedisKey;
import org.chim.altass.core.domain.meta.InputParam;
import org.chim.altass.core.domain.meta.MetaData;
import org.chim.altass.core.domain.meta.OutputParam;
import org.chim.altass.toolkit.RedissonToolkit;
import org.redisson.api.RList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class Name: ExtParamList
 * Create Date: 18-1-5 下午9:09
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@SuppressWarnings("Duplicates")
public class ExtContextParams extends ArrayList<MetaData> {

    private static final long serialVersionUID = 3745674386931285177L;

    private BasicRedisKey key = null;
    private String executorId = null;

    public ExtContextParams(EurekaSystemRedisKey key, String executorId) {
        this.key = key;
        this.executorId = executorId;
    }

    @Override
    public boolean addAll(Collection<? extends MetaData> c) {
        RList<Object> list = RedissonToolkit.getInstance().getList(key, executorId);
        return list.addAll(c);
    }

    public InputParam buildInputParams() {
        InputParam inputParam = new InputParam();
        RList<Object> list = RedissonToolkit.getInstance().getList(key, executorId);
        if (list != null) {
            List<Object> objs = list.readAll();
            for (Object obj : objs) {
                inputParam.addParameter((MetaData) obj);
            }
            return inputParam;
        }
        return null;
    }

    public OutputParam buildOutputParams() {
        OutputParam outputParam = new OutputParam();
        RList<Object> list = RedissonToolkit.getInstance().getList(key, executorId);
        if (list != null) {
            List<Object> objs = list.readAll();
            for (Object obj : objs) {
                outputParam.addParameter((MetaData) obj);
            }
            return outputParam;
        }
        return null;
    }

    public void setParams(InputParam inputParam, OutputParam outputParam) {
        RList<Object> list = RedissonToolkit.getInstance().getList(key, executorId);
        list.clear();
        if (inputParam == null) {
            list.addAll(outputParam.getParams());
        } else {
            list.addAll(inputParam.getParams());
        }
    }
}
