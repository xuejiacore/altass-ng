package org.chim.altass.executor.redis.bean;

/**
 * Class Name: Response
 * Create Date: 11/5/18 11:21 PM
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
public class Response<T> {
    private boolean isAllDown = false;
    private T result = null;

    public Response() {

    }

    public Response(T result) {
        this.result = result;
    }

    public boolean isAllDown() {
        return isAllDown;
    }

    public void setAllDown(boolean allDown) {
        isAllDown = allDown;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RedisResponse{" +
                "isAllDown=" + isAllDown +
                ", result=" + result +
                '}';
    }
}
