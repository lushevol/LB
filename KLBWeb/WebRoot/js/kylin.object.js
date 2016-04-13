/**
 * @fileOverview js组件基础
 * @import jQuery.js
 * @author kylin 
 * @revision 1.1
 */

if (typeof window.kylin == "undefined") {
	window.kylin = {};
}
if (typeof window.kylin.object == "undefined") {
	window.kylin.object = {};
}

(function(package){
	jQuery.extend(package, {
		/** 
		 * @description 创建类
		 * @function
		 * @param {Function} [superClass=null] 所创建类的父类
		 * @param {Map} newMethod 要创建类的方法和属性
		 * @return {Function} 创建的新类
		 *			.[_super] {Object} 新类的父类原型(prototype)
		 */
		createClass : function(superClass, newMethod){
			var parent = typeof superClass == "function" ? jQuery.extend({}, superClass.prototype) : superClass;
			var newClass = function(){
				if(jQuery.isFunction(this.init)){
					this.init.apply(this, arguments);
				}else if(jQuery.isFunction(newClass.init)){
					newClass._super.init.apply(this, arguments);
				}
			};
			newClass._super = parent;
			jQuery.extend(newClass.prototype, parent, newMethod, {
				bind : function (name, data, func) {
					var _j_this = jQuery(this);
					_j_this.bind.apply(_j_this ,arguments);
				},
				one : function (name, data, func) {
					var _j_this = jQuery(this);
					_j_this.one.apply(_j_this ,arguments);
				},
				trigger : function (name, data) {
					var _j_this = jQuery(this);
					_j_this.trigger.apply(_j_this ,arguments);
				},
				triggerHandler : function (name, data) {
					var _j_this = jQuery(this);
					_j_this.triggerHandler.apply(_j_this ,arguments);
				},
				unbind : function (name, data) {
					var _j_this = jQuery(this);
					_j_this.unbind.apply(_j_this ,arguments);
				}
			});
			return newClass;
		},
		/** 
		 * @description 将函数添加到数组
		 * @function
		 * @param {Array} [arr=new Array()] 将function添加到该数组
		 * @param {Function} func 要添加的函数
		 * @return {Array} 函数数组
		 */
		funcToArray : function (arr, func) {
			var array = arr || new Array();
			if (func) {
				if (func instanceof Array) {
					array = array.concact(func);
				} else if(jQuery.isFunction(func)) {
					array.push(func);
				}
			}
			return array;
		}
	});
})(kylin.object);
