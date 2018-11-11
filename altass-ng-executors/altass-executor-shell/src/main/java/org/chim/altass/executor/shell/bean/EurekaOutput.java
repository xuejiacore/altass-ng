package org.chim.altass.executor.shell.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class Name: EurekaOutput
 * Create Date: 2017/9/6 19:59
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class EurekaOutput {
    private Map<String, Object> eurekaOutput = null;

    public EurekaOutput() {

    }

    public EurekaOutput(String jsonData) {
        parseJsonResult(jsonData);
    }

    public void parseJsonResult(String jsonData) {
        JSONObject jsonObject;
        try {
            jsonObject = JSON.parseObject(jsonData);
        } catch (JSONException e) {
            return;
        }
        JSONObject eurekaReturn = jsonObject.getJSONObject("eureka_output");

        if (eurekaReturn != null) {
            this.eurekaOutput = new HashMap<String, Object>();
            Set<String> returnKeySet = eurekaReturn.keySet();
            for (String key : returnKeySet) {
                this.eurekaOutput.put(key, eurekaReturn.get(key));
            }
        }
    }

    public Map<String, Object> getEurekaOutput() {
        return eurekaOutput;
    }

    public void setEurekaOutput(Map<String, Object> eurekaOutput) {
        this.eurekaOutput = eurekaOutput;
    }

}
