/**
 * Created by Xuejia on 2016/11/14.
 */
// 节点信息
var NodeInfo = function (id, name, vec2, x, y) {
    this._id = id;
    this._name = name;
    this._x = x;
    this._y = y;
    this._vec2 = vec2;
};

// 获得节点的X坐标
NodeInfo.getX = function () {
    return this._x;
};

// 获得节点的Y坐标
NodeInfo.getY = function () {
    return this._y;
};

NodeInfo.getCoordinate = function () {
    return this._vec2;
};

// 获得节点的ID
NodeInfo.getId = function () {
    return this._id;
};

// 获得节点的名称
NodeInfo.getName = function () {
    return this._name;
};