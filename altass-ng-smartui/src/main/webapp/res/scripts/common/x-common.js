/**
 * 常用的js库
 * Created by Xuejia on 2016/11/2.
 */

// ==============================================================================       浏览器常用包  START

var BrowserCommon = {};

/**
 *
 * @returns {*}
 */
BrowserCommon.browserType = function () {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1;
    //判断是否Opera浏览器
    if (isOpera) {
        return "Opera"
    }
    //判断是否Firefox浏览器
    if (userAgent.indexOf("Firefox") > -1) {
        return "Firefox";
    }
    if (userAgent.indexOf("Chrome") > -1) {
        return "Chrome";
    }
    //判断是否Safari浏览器
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    }
    //判断是否IE浏览器
    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
        return "IE";
    }
};

BrowserCommon.isIE = function () {
    return BrowserCommon.browserType() == 'IE';
};

BrowserCommon.isFireFox = function () {
    return BrowserCommon.browserType() == 'Firefox';
};

BrowserCommon.isChrome = function () {
    return BrowserCommon.browserType() == 'Chrome';
};

// ==============================================================================       浏览器常用包  END


// ==============================================================================       字符串操作常用  START
// 为String 类添加一个format方法，能够对字符串中的数据进行格式化填充
// 使用方法：
// 1、"我是{0}，今年是{1}".format("Xuejia", "2016-12");
// 2、"我是{name}，今年是{date}".format({name: "Xuejia", date: "2016-12"})
String.prototype.format = function (args) {
    var result = this;
    if (arguments.length > 0) {
        if (arguments.length == 1 && typeof (args) == "object") {
            for (var key in args) {
                if (args[key] != undefined) {
                    result = result.replace(new RegExp("({" + key + "})", "g"), args[key]);
                }
            }
        } else {
            for (var i = 0; i < arguments.length; i++) {
                if (arguments[i] != undefined) {
                    result = result.replace(new RegExp("({)" + i + "(})", "g"), arguments[i]);
                }
            }
        }
    }
    return result;
};
// ==============================================================================       字符串操作常用  END


// ==============================================================================       UUID  START
function uuid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16).toUpperCase();
    });
}
// ==============================================================================       UUID  END