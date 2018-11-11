package org.chim.altass.smartui.common;

import java.io.Serializable;

public class CommonResult implements Serializable {
	
	private static final long serialVersionUID = 4127518004111628422L;

	private boolean flag;
	
	private int code;

	private String errorCode;
	
	private String msg;
	
	private String errorDetail;

	private Object result;
	
	protected CommonResult(boolean flag, int code, String msg, Object result){
		this.flag = flag;
		this.code = code;
		this.msg = msg;
		this.result = result;
	}
	
	public CommonResult() {
		
	}
	
	public CommonResult(boolean flag) {
		this.flag = flag;
	}
	
	public static CommonResult success(){
		CommonResult result =  new CommonResult();
		result.flag = true;
		return result;
	}
	
	public static CommonResult success(Object rest){
		CommonResult result =  new CommonResult();
		result.flag = true;
		result.setResult(rest);
		return result;
	}
	
	public static CommonResult success(String msg){
		CommonResult result = new CommonResult();
		result.flag = true;
		result.msg = msg;
		return result;
	}
	
	public static CommonResult fail(){
		CommonResult result =  new CommonResult();
		result.flag = false;
		return result;
	}
	
	public static CommonResult fail(String msg){
		CommonResult result =  new CommonResult();
		result.flag = false;
		result.msg = msg;
		return result;
	}
	
	public static CommonResult fail(String errorCode, String msg){
		CommonResult result =  new CommonResult();
		result.flag = false;
		result.msg = msg;
		result.errorCode = errorCode;
		return result;
	}
	
	public static CommonResult fail(String errorCode, String msg, String errorDetail){
		CommonResult result =  new CommonResult();
		result.flag = false;
		result.msg = msg;
		result.errorCode = errorCode;
		result.errorDetail = errorDetail;
		return result;
	}
	
    public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    
    public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}

    
}