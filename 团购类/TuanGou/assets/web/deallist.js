(function($) {

    var _tpl = '<div class="deal">'
        + '<a class="link" href="deal.html">'
            + '<div class="site">'
                + '<span class="name"><[=site.name]></span>'
                + '<img src="<[=_text_mode ? \"text-mode.png\" : imgUrlSmall]>">'
            + '</div>'
            + '<div class="content">'
                + '<span class="desc"><[=name]></span>'
                + '<span class="price">¥<[=price]></span>'
                + '<span class="discount"><[=discount]>折</span>'
                + '<span class="area"><[=shangquanName]></span>'
            + '</div>'
        + '</a>'
        + '</div>', db;

    function setTextMode() {
        if (db) {
            db.getPreference("text_mode", function(val) {
                window._text_mode = val == "1";
            });
        }
    }

    $.fn.extend({
        deallist: function(opts) {
        	android_log.println("deallist");
        	
            // Get image mode
            window._text_mode = false;
            if (!db) {
                $.openDatabase(function(database) {
                    db = database;
                    setTextMode();
                });
            } else {
                setTextMode();
            }
            
            opts = opts || {};
            var urlGenerator = opts.urlGenerator || function(pageset, page, categoryId) {
                    return "http://api.tuan800.com/mobile_api/android/get_deals?pageset=" + pageset +
                                "&page=" + page + "&cityId=1&categoryId=" + categoryId;
                },
                dataGenerator = opts.dataGenerator || function(pageset, page) {
                    return null;
                },
                dealRendering = opts.dealRendering || $.emptyFn,
                dealRendered = opts.dealRendered || function(dom, context) {
                    var data = context.data,
                        link = dom.filter("div").find("a.link").on("click", function(e) {
                            e.preventDefault();
                            $.umengEvent("deal_detail");
                            $.openUrl("deal.html", {
                                deal: data,
                                ui_title: "团购详情"
                            });
                        });
                },
                getDealsFromData = opts.getDealsFromData || function(data) {
                    return data.deals;
                },
                defaultLoadingDialog,
                loadingOperation = opts.loadingOperation || {
                    loading: function() {
                        if (!defaultLoadingDialog)
                            defaultLoadingDialog = $.openWaiting();
                        else
                            defaultLoadingDialog.show();
                    },
                    end: function() {
                        if (defaultLoadingDialog)
                            defaultLoadingDialog.hide();
                    }
                },
                emptyHtml = opts.emptyHtml || "<p style='font-size: 16px;text-align:center;margin-top:5px;'>没有团购</p>",
                loadTipHtml = opts.loadTipHtml || "<div class='load-tip'></div>",
                loadTip = opts.loadTip || "轻轻提起，继续读取...",
                meetTheEndTip = opts.meetTheEndTip || "没有相关团购了...",
                loadTipEl = $(loadTipHtml),
                ajaxType = opts.ajaxType || "get",
                pageset = opts.pageset || 10,
                categoryId = opts.categoryId || null,
                tpl = opts.tpl || _tpl,
                curPage = 1,
                meetTheEnd = false, self = this, loading = false;
                
            function load(page) {
                if (meetTheEnd || loading) return;
                loading = true;
                loadingOperation.loading();
                setTextMode();
                $.ajax({
                    url: urlGenerator(pageset, page, categoryId),
                    type: ajaxType,
                    data: dataGenerator(pageset, page),
                    success: function(data) {
                        var deals = getDealsFromData(data);
                        if (deals && deals.length) {
                            curPage = page;
                            $(self).tmpl({
                                tpl: tpl,
                                append: true,
                                data: deals,
                                rendering: dealRendering,
                                rendered: dealRendered
                            });
                            loadTipEl.html(loadTip).appendTo(self);
                        } else {
                            loadTipEl.html(meetTheEndTip);
                            meetTheEnd = true;
                            if (page == 1)
                                self.html(emptyHtml);
                        }
                        loading = false;
                        loadingOperation.end();
                    },
                    error: function(err) {
                        loadingOperation.end();
                        loading = false;
                        $.alert("出错啦！");
                        $.log(err, "e");
                    }
                });
            }
            load(1);
            var scroller = this.hasClass("scroller") ? this : this.parents().filter(".scroller");
            scroller.scroller({
                onbottom: function() {
                    load(curPage + 1);
                }
            });
        }
    });
})(tMobile);