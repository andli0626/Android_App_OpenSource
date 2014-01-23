//定义了函数
(function($) {

	//打开URL的函数
    $.openUrl = function(url, opts) {
    	android_log.println("openUrl bridge ");
    	
        if (!window.android_web)
            $.log("$.openUrl: no android web object found", "e");
        
        opts = opts || {};
        var json;
        
        //CONFIG--指向config.js，判断config.js是否定义并且url是否在定义内
        if (typeof CONFIG != "undefined" && url in CONFIG.android.url_activity_map) {
            //转换成JSON字符串
        	json = $.toJSONString(opts);
            
        	//调用打开activity函数，android_web--指向WebBridge
            android_web.openActivity(CONFIG.android.url_activity_map[url], json);
            
        } else {
            opts["url"] = url;
            //转换成JSON字符串
            json = $.toJSONString(opts);
            //输出日志消息
            $.log(json);
            //调用打开webactivity函数，android_js--指向Bridge
            daquan_js.openWebActivity(json);
        }
    };

    $.uiBack = function(data) {
        daquan_js.uiBack($.toJSONString(data));
    };

    /**
     * Page back
     */
    var _page_back_funcs = [];
    window._on_page_back = function(args) {
        var i = 0, l = _page_back_funcs.length, fn;
        for (; i < l; i++) {
            fn = _page_back_funcs[i];
            fn(args);
        }
    };

    $.pageBack = function(fn) {
        _page_back_funcs.push(fn);
    };
    /* end of page back */

    /**
     * Page resume
     */
    var _page_resume_funcs = [];
    window._on_page_resume = function(args) {
        var i = 0, l = _page_resume_funcs.length, fn;
        for (; i < l; i++) {
            fn = _page_resume_funcs[i];
            fn(args);
        }
    };

    $.pageResume = function(fn) {
        _page_resume_funcs.push(fn);
    };
    /* end of page resume */

    //获得当前city
    $.getCity = function(callback) {
    	android_log.println("getCity");	
        var c = $.evalJSON(daquan_js.getCity());
        callback(c);
    };

    //打开等待界面
    $.openWaiting = function(msg) {
    	android_log.println("openWaiting");
        msg = msg || "正在加载...";
        var content = '<div class="loading_content">'
                + '<div class="spinner"></div>'
                + '<span>'+ msg + '</span>'
            + '</div>';
        return $.openDialog({
            content: content
        }).show();
    };

    var alertDialog;
    $.alert = function(msg) {
        if (!alertDialog) {
            alertDialog = $.openDialog({
                buttons: ["close"]
            });
        }
        var content = "<div class='alert_msg'>" + msg + "</div>";
        alertDialog.setContent(content);
        alertDialog.show();
    };

    var onCityListCallback = null;
    $.getCityList = function(callback) {
        onCityListCallback = callback;
        daquan_js.getCityList();
    };

    //获取activity结束的函数
    window._on_citylist_finished = function(list) {
        if (typeof onCityListCallback == "function") {
            onCityListCallback(list.cities);
        }
    };

    function ListDialog(opts) {
        opts = opts || {};
        opts.fillParent = true;
        opts.content = '<div class="scroller list"></div>';
        opts.title = opts.title || "列表";
        opts.buttons = ["close"];
        $.Dialog.call(this, opts);
        var data = opts.data || [],
            itemTpl = opts.itemTpl || "",
            itemSelector = opts.itemSelector || "div",
            itemClick = opts.itemClick,
            self = this,
            contentEl = this.dialogEl.find(".content");
        contentEl.find(".scroller").tmpl({
            tpl: itemTpl,
            data: data,
            rendered: function(dom, context) {
                var data = context.data;
                dom.filter(itemSelector).on("click", function() {
                    self.hide();
                    if (typeof itemClick == "function") {
                        itemClick(data);
                    }
                });
            }
        }).scroller();
    }

    ListDialog.prototype = $.Dialog.prototype;

    $.openListDialog = function(opts) {
        return new ListDialog(opts).show();
    };

    var subCategoryDialog, categoryDialog_tagId;
    $.openSubCategoryDialog = function(opts) {
    	
    	android_log.println("openSubCategoryDialog");
        var tagId = opts.categoryId || 1;
        if (tagId != categoryDialog_tagId)
            subCategoryDialog = null;
        categoryDialog_tagId = tagId;
        if (subCategoryDialog) {
            subCategoryDialog.show();
        } else {
            opts = opts || {};
            $.extend(opts, {
                itemTpl: '<div class="item"><span><[=name]></span></div> ',
                itemSelector: ".item",
                title: "选择类别",
                data: []
            });
            var waiting = $.openWaiting();
            $.ajax({
                url: "http://www.tuan800.com/mobile_api/android/get_sub_tags?tag_id=" + tagId,
                dbLife: 180000,  // use db
                success: function(data) {
                    waiting.hide();
                    var categories = data.categories;
                    categories.splice(0, 0, {
                        name: "【取消分类】",
                        id: tagId
                    });
                    opts.data = categories;
                    subCategoryDialog = $.openListDialog(opts);
                },
                error: function(err) {
                    waiting.hide();
                    $.alert(err);
                }
            });
        }
    };

    var categoryDialog;
    $.openCategoryDialog = function(opts) {
    	android_log.println("openCategoryDialog");
        if (categoryDialog) {
            categoryDialog.show();
        } else {
            opts = opts || {};
            $.extend(opts, {
                itemTpl: '<div class="item"><span><[=name]></span></div> ',
                itemSelector: ".item",
                title: "选择类别",
                data: []
            });
            var waiting = $.openWaiting();
            $.ajax({
                url: "http://api.tuan800.com/mobile_api/android/get_categories",
                dbLife: 180000,  // use db
                success: function(data) {
                    waiting.hide();
                    var categories = data.categories;
                    categories.splice(0, 0, {
                        name: "【取消分类】",
                        id: -1
                    });
                    opts.data = categories;
                    categoryDialog = $.openListDialog(opts);
                },
                error: function(err) {
                    waiting.hide();
                    $.alert(err);
                }
            })
        }
    };

    //获取位置函数
    $.getLatLon = function(callback, repeat) {
        repeat = repeat || 3;
        function get(callback) {
            //获取经纬度
        	var location = android_lbs.getLatitudeAndLongitude();
            
            if (location) {
                callback($.evalJSON(location));
            } else {
                if (repeat > 0) {
                    repeat--;
                    //每隔半分钟检查位置
                    setTimeout(function() {
                        get(callback);
                    }, 30000);
                }
            }
        }
        get(callback);
    };

    //获取具体地址
    $.getAddress = function(callback, repeat) {
        repeat = repeat || 2;
        function get(callback) {
        	//获取当前地址
            var address = android_lbs.getCurrentAddress();
            if (address) {
                callback(address);
            } else {
                if (repeat > 0) {
                    repeat--;
                    setTimeout(function() {
                        get(callback);
                    }, 10000);
                } else {
                    callback(address);
                }
            }
        }

        get(callback);
    };

    $.getLocationCity = function() {
        return android_lbs.getCurrentCity();
    };

    $.umengEvent = function(key){
        daquan_js.umengEvent(key);
    };

    $.getDistance = function(lat1, lng1, lat2, lng2) {
        function rad(d) {
            return d * Math.PI / 180.0;
        }

        if ((Math.abs(lat1) > 90) || (Math.abs(lat2) > 90)) {
            return 1000;
        }

        if ((Math.abs(lng1) > 180) || (Math.abs(lng2) > 180)) {
            return 1000;
        }

        var a = rad(lat1) - rad(lat2),
                b = rad(lng1) - rad(lng2);
        var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378.137; // EARTH_RADIUS
        s = Math.round(s * 1000) / 1000;
        return s;
    };

    $.getPreference = function(key) {
        return daquan_js.getPreference(key);
    };

    $.savePreference = function(key, val) {
    	android_log.println("bridge savaPreference");
    	daquan_js.savePreference(key, val);
    };

    //获取当前网络状态
    $.getNetworkState = function() {
        return android_connect.getNetworkState();
    }
})(tMobile);