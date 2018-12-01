package org.chim.altass.toolkit.script;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.testng.Assert;

import java.util.Iterator;
import java.util.Set;

/**
 * Class Name: JsonHelperTest
 * Create Date: 11/24/18 1:34 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class JsonHelperTest {


    @Test
    public void outkey() throws Exception {
        Object outkey1 = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey1", null, false);
        Assert.assertEquals("outkey1val", outkey1);

        Object outkey2 = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey2", null, false);
        assert outkey2 != null;
        Assert.assertEquals(outkey2, 2);
    }

    @Test
    public void simpleStrArray() throws Exception {
        Object array = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey3array", null, false);
        Assert.assertTrue(array != null && ((JSONArray) array).size() == 4);

        for (Object obj : (JSONArray) array) {
            System.out.println(obj.getClass() + "\t" + obj);
        }
    }

    public static void main(String[] args) {
    }

    @Test
    public void objectArray() throws Exception {
        Object objectArray = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey4array", null, false);
        Assert.assertTrue(objectArray instanceof JSONArray);

        for (Object jsonObj : (JSONArray) objectArray) {
            System.out.println(jsonObj);
        }

        Object val1 = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey4array[1].key4arrayitem1key2", null, false);
        Assert.assertEquals(val1, 88);


        Object val2 = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey4array[0].key4arrayitem1key1", null, false);
        Assert.assertEquals(val2, "item1");
    }

    @Test
    public void iteratorObj() throws Exception {
        Object data = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey3array");
        printData(data);

        data = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey1");
        printData(data);

        data = JsonHelper.jsonget(jsonObj(JSON_STR_1), "outkey6objarray");
        printData(data);
    }

    private void printData(Object data) {
        if (data instanceof JSONObject) {
            System.out.println("---------------------------------------------------------- IS JSON_OBJECT");
            Set<String> keySet = ((JSONObject) data).keySet();
            for (String key : keySet) {
                System.out.println(key + " => " + JSON.toJSONString(((JSONObject) data).get(key)));
            }
        } else if (data instanceof JSONArray) {
            System.out.println("========================================== IS JSON_ARRAY");
            int i = 0;
            for (Object d : (JSONArray) data) {
                System.out.println("item[" + (i++) + "] =>" + JSON.toJSONString(d));
            }
        } else {
            System.out.println("========================================== IS BASIC_TYPE");
            System.out.println(data);
        }
    }

    private static final String JSON_STR_1 =
            "{\n" +
                    "  \"outkey1\": \"outkey1val\",\n" +
                    "  \"outkey2\": 2,\n" +
                    "  \"outkey3array\": [\n" +
                    "    \"val1\",\n" +
                    "    \"val2\",\n" +
                    "    999,\n" +
                    "    \"2018-11-24 13:49:20\"\n" +
                    "  ],\n" +
                    "  \"outkey4array\": [\n" +
                    "    {\n" +
                    "      \"key4arrayitem1key1\": \"item1\",\n" +
                    "      \"key4arrayitem1key2\": 77\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"key4arrayitem1key1\": \"item1\",\n" +
                    "      \"key4arrayitem1key2\": 88\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"outkey5obj\": {\n" +
                    "    \"key5objitem1key1\": \"key5objval1\",\n" +
                    "    \"key5objitem1key2\": 5\n" +
                    "  },\n" +
                    "  \"outkey6objarray\": {\n" +
                    "    \"key6objitemkey1\": \"simple\",\n" +
                    "    \"key6objitemkey2\": [\n" +
                    "      \"val1\",\n" +
                    "      \"val2\",\n" +
                    "      \"val3\"\n" +
                    "    ],\n" +
                    "    \"key6objitemkey3\": [\n" +
                    "      {\n" +
                    "        \"key6objitemkey3inner1\": \"innerval1\",\n" +
                    "        \"key6objitemkey3inner2\": 66\n" +
                    "      },\n" +
                    "      {\n" +
                    "        \"key6objitemkey3inner1\": \"innerval2\"\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";

    private JSONObject jsonObj(String json) {
        return JSON.parseObject(json);
    }
}
