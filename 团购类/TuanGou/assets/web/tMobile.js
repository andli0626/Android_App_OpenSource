
if (!Array.prototype.reduce) {
    Array.prototype.reduce = function(callback /*, initValue*/) {
        if (this == undefined)
            throw new TypeError("Array.prototype.reduce: no array found.");
        if (typeof callback != "function")
            throw new TypeError("Array.prototype.reduce: callback must be a function.");

        var t = Object(this),
            hasInit = arguments.length > 1;
        if (!t.length && !hasInit)
            throw new TypeError("Reduce of empty array with no initial value");
        var len = t.length,
            ret = hasInit ? arguments[1] : t[0];
        for (var i = hasInit ? 0 : 1; i < len; i++) {
            if (i in t)
                ret = callback(ret, t[i], i, t);
        }

        return ret;
    }
}

if (!Array.prototype.map) {
    Array.prototype.map = function(callback/*, scope */) {
        if (this == undefined)
            throw new TypeError("Array.prototype.map: no array found.");
        if (typeof callback != "function")
            throw new TypeError("Array.prototype.map: callback must be a function.");
        
        var t = Object(this),
            len = t.length,
            ret = new Array(len),
            scope = arguments[1];
        for (var i = 0; i < len; i++) {
            if (i in t)
                ret[i] = callback.call(scope, t[i], i, t);
        }
        return ret;
    }
}

if (!Array.prototype.filter) {
    Array.prototype.filter = function(callback/*, scope */) {
        if (this == undefined)
            throw new TypeError("Array.prototype.map: no array found.");
        if (typeof callback != "function")
            throw new TypeError("Array.prototype.map: callback must be a function.");

        var t = Object(this),
            ret = [],
            scope = arguments[1],
            val;
        for (var i = 0, l = t.length; i < l; i++) {
            if (i in t) {
                val = t[i];
                if (callback.call(scope, val, i, t))
                    ret.push(val);
            }
        }
        return ret;
    }
}

Array.prototype.each = function(fn/*, scope */) {
    if (this === void 0 || this === null || typeof fn != "function")
        throw new TypeError();

    var t = Object(this),
        scope = arguments[1], i, l;
    if(typeof t.forEach == "function") {
        t.forEach(fn, scope);
    } else {
        for (i = 0, l = t.length; i < l ; i++) {
            if (i in t)
                fn.call(scope, t[i], i, t);
        }
    }
};

if (!Function.prototype.bind) {
    Function.prototype.bind = function(scope/*, arg1, arg2, ...*/) {
        var fn = this;
        if (typeof fn != "function")
            throw TypeError("Function.prototype.bind: must be a function.");
        var _slice = Array.prototype.slice,
            args = _slice.call(arguments, 1);
        return function() {
            fn.apply(scope, args.concat(_slice.call(arguments, 0)));   
        }
    }
}
String.prototype.test = function(regex, params) {
    return ((typeof(regex) == 'string') ? new RegExp(regex, params) : regex).test(this);
};
String.prototype.replaceAll = function(AFindText, ARepText) {
    var raRegExp = new RegExp(AFindText, "g");
    return this.replace(raRegExp, ARepText);
};
String.prototype.sliceBetween = function(start, end) {
    return this.slice(this.indexOf(start) + start.length, this.indexOf(end));
};
String.prototype.contains = function(string, s) {
    return(s) ? (s + this + s).indexOf(s + string + s) > -1 : this.indexOf(string) > -1;
};
String.prototype.clean = function() {
    return this.replace(/\s{2,}/g, ' ').trim();
};
if (!String.prototype.trim) {
    String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, '');
    };
}
String.prototype.toInt = function() {
    return parseInt(this);
};
String.prototype.startsWith = function(s) {
    return this.substr(0, s.length) == s;
};

String.prototype.dasherize = function() {
    return this.replace(/[A-Z]/g, function(m){return '-' + m.toLowerCase();});
};
String.prototype.camelize = function() {
    return this.replace(/\-[a-z]/g, function(m) {return m[1].toUpperCase();});
};

/**
 * Format a string
 *
 * syntax:
 * "What the %s are you talking about %s".format("hell", 123)
 * will be:  "What the hell are you talking about 123"
 */
String.prototype.format = function() {
    var args = arguments, i = 0;
    return this.replace(/%s/g, function() {
        return String(args[i++]) || '';   
    });
};
/**
 * Envolving from Zepto
 */
(function() {
    
var document = window.document,
    undefined,
    _push = [].push,
    _slice = [].slice,
    tagExpr = /<([\w:]+)/,
    classRE = function(name){ return new RegExp("(^|\\s)"+name+"(\\s|$)") },
    _ = function(q, context) {
        var els = [], tmpNode, list, i, l;
        
        if (!q)
            return this;
        if (typeof q == "string") {
            if (tagExpr.test(q)) {
                tmpNode = document.createElement("i");
                tmpNode.innerHTML = q;
                list = tmpNode.getElementsByTagName('SCRIPT');
                for (i = 0, l = list.length; i < l; i++) {
                    $.globalEval(list[i].text);
                }
                _push.apply(els, _slice.call(tmpNode.childNodes));
            } else {
                els = _query(q, context);
            }
        } else if (q instanceof Array) {
            els = q;
        } else if (q.toString() == '[object NodeList]') {
            els = _slice.call(q);
        } else if (q.nodeName && q.nodeType || q === window) {
            els = [q];
        } else if (q instanceof _) {
            return q;
        }

        _push.apply(this, els);
    },
    _query = function(q, context) {
        context = context || document;
        return _slice.call(context.querySelectorAll(q));
    },
    extend = function(target, src) {
        var k;
        for (k in src) {target[k] = src[k];}
        return target;
    },
    generateUniqueId = function() {
        return "" + Date.now() + (Math.round(Math.random() * 89999) + 10000);  
    },
    getElementPPYId = function(el) {
        return el.__ppyid == undefined ? (el.__ppyid = generateUniqueId()) : el.__ppyid;
    },
    // A crude way of determining if an object is a window
	isWindow = function( obj ) {
		return obj && typeof obj === "object" && "setInterval" in obj && "setTimeout" in obj;
	};

/**
 * Interface object of tMobile lib
 */
window.tMobile = window.$ = function(q, context) {
    return new _(q, context);   
};

$.extend = extend;

$.generateUniqueId = generateUniqueId;

$.getElementPPYId = getElementPPYId;

/**
 * Some verify methods
 */
$.isFunction = function(value) { return ({}).toString.call(value) == "[object Function]" };
$.isObject = function(value) { return value instanceof Object };
$.isArray = function(value) { return value instanceof Array };

/**
 * Empty fn
 */
$.emptyFn = function(){};

// A safely toString method
$.toString = function(s) {
    if (s && typeof s != "string" || s == 0) return String(s);
    return s;
};

$.fn = $.prototype = _.prototype = {
    length: 0,

    extend: function(o) {
        for (var i in o) {tMobile.fn[i] = o[i];}
    },
    
    find: function(q) {
        var els = [];
        this.each(function(el) {
            els = els.concat(_query(q, el));
        });
        return new _(els);
    },

    /**
     * traverse the elements, if callback return false, stop loop
     * @param callback
     */
    each: function(callback) {
        for (var i = 0, l = this.length, el; i < l; i++) {
            el = this[i];
            if (el && callback.call(el, el, i, this) === false) break;
        }
        return this;
    },

    map: function(callback) {
        return new _([].map.call(this, callback), null);
    },

    /**
     * Get elements from current collection filter by selector or function
     * @param s     selector or function
     */
    filter: function(s) {
        var isString = typeof s == "string";
        return new _([].filter.call(this, function(el, i, self) {
            return isString ? _query(s, el.parentNode).indexOf(el) >= 0 : s.call(el, el, i, self);
        }), null);
    },

    /**
     * Get a single element by index
     * @param idx
     */
    eq: function(idx) {
        return new _(this[idx] || null);   
    },

    /**
     * Check if all selected elements match the given selector
     * @param s     selector
     */
    is: function(s) {
        var ret = true;
        this.each(function(el) {
            return (ret = _query(s, el.parentNode).indexOf(el) >= 0);
        });
        return ret
    },

    /***************************************************
     * Traverse 
     ***************************************************/

    parent: function() {
        return this[0] ? new _(this[0].parentNode) : this;
    },

    parents: function() {
        var els = [], p = this[0] && this[0].parentNode;
        while(p && p !== document.documentElement) {
            els.push(p);
            p = p.parentNode;
        }
        return new _(els);
    },

    children: function() {
        var els = [], p = this[0];
        if (p && p.hasChildNodes())
            els = _slice.call(p.childNodes);
        return new _(els);
    },

    /***************************************************
     * Attribute and CSS style
     ***************************************************/

    /**
     * Get or set html elements' css style
     * @param n     style name
     * @param v     style value
     */
    css: function(n, v) {
        if (v == undefined && typeof n == "string")
            return document.defaultView.getComputedStyle(this[0], null).getPropertyValue(n.dasherize());
        if (typeof n == "string") {
            n = n.camelize();
            return this.each(function(el) {
                if (el.style)
                    el.style[n] = v
            });
        } else if(Object.prototype.toString.call(n) == "[object Object]") {
            for (var k in n)
                this.css(k, n[k]);
        }
        return this;
    },

    /**
     * Get or set html elements' attribute
     * @param n     attribute name
     * @param val   attribute value
     */
    attr: function(n, val) {
        return typeof n == "string" && val == undefined ?
            (this[0] && this[0][n]) :
            this.each(typeof n == "object" ? function(el) {
                for (var k in n) {el.setAttribute(k, n[k])}
            } : function(el) {el.setAttribute(n, val);});
    },

    /**
     * Get or set a html input's value
     * @param v
     */
    val: function(v) {
        return typeof v === "undefined" ?
            (this[0] && this[0].value) :
            this.each(function(el) {
                el.value = v;
            });
    },

    /**
     * Set the all matched elements' inner html if val is not null,
     * or return the first matched element's inner html
     * @param val
     */
    //html函数
    html: function(val) {
    	android_log.println("function html");
    	
        if (val === undefined) {
            return this[0] && this[0].innerHTML || null;
        }
        //判断是否为字符串
        else if (typeof val == "string") {
            var list, i, l;
            this.empty().each(function(el) {
                el.innerHTML = val;
                // eval script
                list = el.getElementsByTagName('SCRIPT');
                for (i = 0, l = list.length; i < l; i++) {
                    $.globalEval(list[i].text);
                }
            });   
        } else {
            this.empty().append(val);
        }
        return this;
    },

    hasClass: function(c) {
        return this[0] && classRE(c).test(this[0].className) || false;   
    },

    addClass: function(c) {
        return this.each(function(el) {
            if (!$(el).hasClass(c)) {
                el.className = (el.className + " " + c).trim();
            }
        });
    },

    removeClass: function(c) {
        return this.each(function(el) {
            el.className = el.className.replace(classRE(c), ' ').trim();
        });  
    },

    saveStyle: function(n) {
        return typeof n == "string" ? this.each(function(el) {
            el.__savedStyle = el.__savedStyle || {};
            el.__savedStyle[n] = $(el).css(n);
        }) : this;
    },

    restoreStyle: function(n) {
        return typeof n == "string" ? this.each(function(el) {
            el.__savedStyle && el.__savedStyle[n]
                && $(el).css(n, el.__savedStyle[n]);
        }) : this;
    },

    /**
     * Get an element's offset position(left, top) from rel
     * @param rel   relative element
     */
    pos: function(rel) {
        if (!this[0]) return null;
        var el = this[0], ret = {left: 0, top: 0}, body = document.body;
        if (el == body || el == document || el == window || el == document.documentElement)
            return ret;
        if (rel) {
            if (rel == document || rel == window) rel = document.body;
            do {
                ret.left += el.offsetLeft;
                ret.top += el.offsetTop;
                el = el.offsetParent;
            } while (el && el != rel);
            return ret;
        } else {
            return {left: this[0].offsetLeft, top: this[0].offsetTop};
        }
    },

    /**
     * Get an element's offset width and height
     */
    dim: function() {
        var el = this[0];
        if (!el) return null;

        var getByName = function(name) {
            if (isWindow(el)) {
                var docElemProp = el.document.documentElement[ "client" + name ];
                return el.document.compatMode === "CSS1Compat" && docElemProp ||
                    el.document.body[ "client" + name ] || docElemProp;
            // Get document width or height
            } else if ( el.nodeType === 9 ) {
                // Either scroll[Width/Height] or offset[Width/Height], whichever is greater
                return Math.max(
                    el.documentElement["client" + name],
                    el.body["scroll" + name], el.documentElement["scroll" + name],
                    el.body["offset" + name], el.documentElement["offset" + name]
                );

            // Get width or height on the element
            } else {
                return el["offset" + name];
            }
        }, width = getByName("Width"), height = getByName("Height");

        return {width: width, height: height};
    },

    /**
     * Check if an element's box area contains a certain point
     * @param x     x of point
     * @param y     y of point
     */
    containPos: function(x, y) {
        if (!this[0]) return false;
        var p = this.pos(document), d = this.dim(), left = p.left, top = p.top;
        return x > left && x < left + d.width && y > top && y < top + d.height;
    },

    /***************************************************
     * Manipulate 
     ***************************************************/

    append: function(elements) {
        var p = this[0];
        if (!p) return this;
        $(elements).each(function(el) {
            p.appendChild(el);
        });
        return this;
    },

    appendTo: function(el) {
        var p = $(el)[0];
        if (!p) return this;
        return this.each(function(el) {
            p.appendChild(el);   
        });
    },

    prepend: function(elements) {
        var p = this[0];
        if (!p) return this;
        var f = p.firstChild;
        $(elements).each(function(el) {
            p.insertBefore(el, f);
        });
        return this;
    },

    prependTo: function(el) {
        var p = $(el)[0];
        if (!p) return this;
        var f = p.firstChild;
        return this.each(function(el) {
            p.insertBefore(el, f);  
        });
    },

    clone: function() {
        return this.map(function(el) {
            return el.cloneNode(true);   
        });
    },

    remove: function() {
        return this.each(function(el) {
            if (el && el.parentNode)
                el.parentNode.removeChild(el);
        });  
    },

    /**
     * Remove all child nodes of the matched elements
     */
    empty: function() {
        for (var i = 0, elem; (elem = this[i]) != null; i++) {
            while(elem.firstChild)
                elem.removeChild(elem.firstChild);
        }
        return this;
    }
};

// Detect the os version, from zepto
function detect(ua){
    var os = {},
        android = ua.match(/(Android)\s+([0-9a-zA-Z\.]+)/),
        iphone = ua.match(/(iPhone\sOS)\s([0-9_]+)/),
        ipad = ua.match(/(iPad).*OS\s([0-9_]+)/),
        webos = ua.match(/(webOS)\/([0-9\.]+)/);
    if(android) os.android = true, os.version = android[2];
    if(iphone) os.ios = true, os.version = iphone[2].replace(/_/g,'.'), os.iphone = true;
    if(ipad) os.ios = true, os.version = ipad[2].replace(/_/g,'.'), os.ipad = true;
    if(webos) os.webos = true, os.version = webos[2];
    return os;
}
$.os = detect(navigator.userAgent);

// Add scrollTop and scrollLeft method
["Left", "Top"].each(function(name, i) {
    var method = "scroll" + name;

	$.fn[ method ] = function(val) {
		var elem = this[0], win;

		if ( !elem ) {
			return null;
		}

		if ( val !== undefined ) {
			// Set the scroll offset
			return this.each(function() {
				win = getWindow( this );

				if ( win ) {
					win.scrollTo(
						!i ? val : $(win).scrollLeft(),
						i ? val : $(win).scrollTop()
					);

				} else {
					this[ method ] = val;
				}
			});
		} else {
			win = getWindow( elem );

			// Return the scroll offset
			return win ? ("pageXOffset" in win) ? win[ i ? "pageYOffset" : "pageXOffset" ] :
				win.document.documentElement[ method ] || win.document.body[ method ] :
				elem[ method ];
		}
	};
});

function getWindow( elem ) {
	return isWindow( elem ) ?
		elem :
		elem.nodeType === 9 ?
			elem.defaultView || elem.parentWindow :
			false;
}

// Convert an object to json string
var _strre = /[\x00-\x1f\\"]/,
    stringescape = {
        '\b': '\\b',
        '\t': '\\t',
        '\n': '\\n',
        '\f': '\\f',
        '\r': '\\r',
        '"' : '\\"',
        '\\': '\\\\'
    }, toJSONString = function(obj, secure) {
        var a, i, l;
        switch(typeof obj) {
            case "string":
                return ['"', (_strre.test(obj) ? encodeString(obj) : obj), '"'].join('');
            case 'number':
            case 'boolean':
                return String(obj);
            case "object":
                if (obj) {
                    switch (obj.constructor) {
                        case Array:
                            a = [];
                            for (i = 0, l = obj.length; i < l; i++)
                                a.push(toJSONString(obj[i], secure));
                            return ['[', a.join(','), ']'].join('');
                        case Object:
                            a = [];
                            if (secure) {
                                for (i in obj)
                                    if (obj.hasOwnProperty(i))
                                        a[a.length] = ['"', _strre.test(i) ? encodeString(i) : i, '":', toJSONString(obj[i], secure)].join('');
                            } else {
                                for (i in obj)
                                    a[a.length] = ['"', i, '":', toJSONString(obj[i], secure)].join('');
                            }
                            return ['{', a.join(','), '}'].join('');
                        case String:
                            return ['"', (_strre.test(obj) ? encodeString(obj) : obj), '"'].join('');
                        case Number:
                        case Boolean:
                            return String(obj);
                        case Function:
                        case Date:
                        case RegExp:
                            return 'undefined';
                    }
                }
                return 'null';
            case 'function':
            case 'undefined':
            case 'unknown':
                return 'undefined';
            default:
                return 'null';
        }
    }, encodeString = function(string) {
        return string.replace(
            /[\x00-\x1f\\"]/g, function(a) {
                var b = stringescape[a];
                if (b) return b;
                b = a.charCodeAt();
                return ['\\u00' , Math.floor(b / 16).toString(16) , (b % 16).toString(16)].join('');
            });
    };
$.toJSONString = toJSONString;
$.evalJSON = function(str, secure) {
	
    if (secure){
    	android_log.println("function evalJSON secure");
    	return ((typeof(str) != 'string') || !str.test(/^("(\\.|[^"\\\n\r])*?"|[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t])+?$/)) ? str : eval('(' + str + ')');
    }
    else{
    	android_log.println("function evalJSON");
    	return eval(['(', str, ')'].join(''));
    }
};

$.globalEval = function(data) {
    if (data && /\S/.test(data)) {
        var head = document.getElementsByTagName("head")[0] || document.documentElement,
            script = document.createElement("script");
        script.type = "text/javascript";
        script.appendChild(document.createTextNode(data));
        head.insertBefore(script, head.firstChild);
        head.removeChild(script);
    }
}
})();

(function($) {

    var eventCache = {},
        getElementEventCache = function(el) {
            var id = $.getElementPPYId(el);
            return eventCache[id] || (eventCache[id] = {});
        },
        getDelegates = function(el, type) {
            var c = getElementEventCache(el);
            return c[type] || (c[type] = []);
        },
        createDelegate = function(el, type, fn) {
            var t = getDelegates(el, type),
                delegate = function(e) {
                    if (fn.call(el, e) === false) {
                        e.stopPropagation();
                        e.preventDefault();
                    }
                };
            delegate.handler = fn;
            t.push(delegate);
            return delegate;
        };

    $.fn.extend({
        /**
         * Bind event handler to element
         * @param type   event type
         * @param fn   event handler, must be a function
         */
        on: function(type, fn) {
            return this.each(function(el) {
                var delegate = createDelegate(el, type, fn);
                el.addEventListener(type, delegate, false);
            });
        },

        /**
         * Unbind event handler from element
         * @param type
         * @param fn
         */
        un: function(type, fn) {
            if (typeof type != "string") return;
            return this.each(function(el) {
                var delegates = getDelegates(el, type), i = delegates.length, d;
                if (!delegates.length) return;
                while(i--) {
                    d = delegates[i];
                    if (fn == undefined || d.handler === fn) {
                        delegates.splice(i, 1);
                        el.removeEventListener(type, d, false);
                    }
                }
            });
        },

        /**
         * Fire a given event from element
         * @param type
         */
        fire: function(type) {
            return this.each(function(el) {
                if (el == document && !el.dispatchEvent)
                    el = document.documentElement;
                var event = document.createEvent(type);
                event.initEvent(type, true, true);
                event.eventName = type;
                el.dispatchEvent(event);
            });
        }
    });
})(tMobile);
(function($) {

    /**
     * changed per step: timer cur
     * need to keep: elements
     */
    var fxElements = {},    // fxId: elements
        fxTimer = {},   // fxId: timer
        fxState = {},   // fxId: state [elStyle, prop, start, end, fn, unit]
        interval = 30,
        normalProps = ('backgroundColor borderBottomColor borderBottomWidth borderLeftColor borderLeftWidth '+
            'borderRightColor borderRightWidth borderSpacing borderTopColor borderTopWidth bottom color fontSize '+
            'fontWeight height left letterSpacing lineHeight marginBottom marginLeft marginRight marginTop maxHeight '+
            'maxWidth minHeight minWidth opacity outlineColor outlineOffset outlineWidth paddingBottom paddingLeft '+
            'paddingRight paddingTop right textIndent top width wordSpacing zIndex').split(' '),
        parseEl = document.createElement("div"),
        clearCache = function(id) {
            delete fxElements[id];
            delete fxTimer[id];
            delete fxState[id];
        },
        findAndCancelCacheByElId = function(elId) {
            var k, f;
            for (k in fxElements) {
                if (fxElements[k].indexOf(elId) >= 0 && (f = k)) break;
            }
            if (f) {
                clearTimeout(fxTimer[f].timer);
                clearCache(f);
            }
        },
        interpolate = function(source,target,pos){ return (source+(target-source)*pos).toFixed(3); },
        s = function(str, p, c){ return str.substr(p,c||1); },
        color = function(source,target,pos){
            var i = 2, j, c, tmp, v = [], r = [];
            while(j=3,c=arguments[i-1],i--)
                if(s(c,0)=='r') { c = c.match(/\d+/g); while(j--) v.push(~~c[j]); } else {
                    if(c.length==4) c='#'+s(c,1)+s(c,1)+s(c,2)+s(c,2)+s(c,3)+s(c,3);
                    while(j--) v.push(parseInt(s(c,1+j*2,2), 16));
                }
                while(j--) { tmp = ~~(v[j+3]+(v[j]-v[j+3])*pos); r.push(tmp<0?0:tmp>255?255:tmp); }
                return 'rgb('+r.join(',')+')';
        },
        parse = function(prop) {
            var p = parseFloat(prop), q = prop.replace(/^[\-\d\.]+/,'');
            return isNaN(p) ? { v: q, f: color, u: ''} : { v: p, f: interpolate, u: q };
        },
        prepare = function(fx){
            var parsed = parse(fx.start);
            fx.start = parsed.v;
            fx.unit = parsed.u;
            fx.fn = parsed.f;
            parsed = parse(fx.end);
            fx.end = parsed.v;
        },
        normalize = function(style){
            var css, rules = {}, i = normalProps.length, v, serialisedStyle = [], key;
            if (typeof style != "string") {
                for (key in style) {
                    serialisedStyle.push(key.dasherize() + ':' + style[key]);
                }
                serialisedStyle = serialisedStyle.join(';');
            } else {
                serialisedStyle = style;
            }
            parseEl.innerHTML = '<div style="'+serialisedStyle+'"></div>';
            css = parseEl.childNodes[0].style;
            while(i--) if(v = css[normalProps[i]]) rules[normalProps[i]] = v;
            return rules;
        },
        go = function(id, fxList, opts) {
            opts = opts || {};
            var timer, duration = opts.duration || 1000,
                startTime = +new Date,
                endTime = startTime + duration,
                easing = opts.easing || function(pos){ return (-Math.cos(pos*Math.PI)/2) + 0.5; },
                step = function() {
                    var time = +new Date(), pos = time > endTime ? 1 : (time - startTime) / duration,
                        len = fxList.length, i = 0, fx;
                    for (;i < len;i++) {
                        fx = fxList[i];
                        fx.elStyle[fx.prop] = fx.fn(fx.start, fx.end, easing(pos)) + fx.unit;
                    }
                    if (time < endTime) {
                        timer = setTimeout(step, interval);
                        fxTimer[id] = timer;
                    } else {
                        clearCache(id);
                        if (typeof opts.onafter == "function")
                            opts.onafter.call(null);
                    }
                };
            fxState[id] = fxList;
            timer = setTimeout(step, interval);
            fxTimer[id] = timer;
        };

    $.fn.extend({
        /**
         * Effects
         * @param props     The properties to be changed
         * @param options   duration, easing, onafter
         */
        anim: function(props, options) {
            if (!this[0]) return;
            options = options || {};
            var fxList = [],
                cancelPre =  true,  // Just cancel all, since I have not implement fx chain
                k, id = $.generateUniqueId(), els = [];

            // Normalize the props first
            props = normalize(props);

            this.each(function(el) {
                var elId = $.getElementPPYId(el), fx;
                cancelPre && findAndCancelCacheByElId(elId);
                els.push(elId);
                for (k in props) {
                    fx = {
                        elStyle: el.style,
                        prop: k.camelize(),
                        start: $(el).css(k),
                        end: props[k]
                    };
                    prepare(fx);
                    fxList.push(fx);
                }
            });
            fxElements[id] = els;
            go(id, fxList, options);

            return this;
        },

        /**
         * Display the matched elements or fading them to opaque. 
         * @param opts
         */
        show: function(opts) {
            opts = opts || {};
            if (opts.duration && opts.duration > 0)
                this.css("opacity", 0);
            if (this.restoreStyle("display").css("display") == "none")
                this.css("display", "block");
            if (opts.duration && opts.duration > 0) {
                this.anim({
                    opacity: 1
                }, opts);
            } else {
                this.css("opacity", 1);
            }
            return this;
        },

        /**                                                                                        
         * Hide the matched elements or fading them to transparent.
         * @param opts  see anim options
         * if opts.duration > 0, it will be faded out
         */
        hide: function(opts) {
            opts = opts || {};
            var onafter = opts.onafter || function() {}, self = this;
            opts.onafter = function() {
                onafter(arguments);
                self.css("display", "none");
            };
            this.saveStyle("display");
            if (opts.duration && opts.duration > 0) {
                this.anim({
                    opacity: 0
                }, opts);
            } else {
                opts.onafter();  
            }
            return this;
        }
    });
})(tMobile);

(function($) {
    $.fn.extend({
        /**
         * Make an element draggable
         * if you use beginDrag to start a drag, you can only drag one element
         * @param opts
         * trigger      use what to trigger drag? can be mouse and finger, default to finger
         * onbefore     callback before drag starting
         * onmove       callback when element is moving
         * onafter      callback after drag ended
         */
        draggable: function(opts) {
            opts = opts || {};
            var trigger = opts.trigger || "finger",
                start = trigger == "finger" ? "touchstart" : "mousedown",
                move = trigger == "finger" ? "touchmove" : "mousemove",
                end = trigger == "finger" ? "touchend" : "mouseup",
                moveFn,
                onbefore = opts.onbefore || false,
                onmove = opts.onmove || false,
                onafter = opts.onafter || false,
                bindDocEvent = function(el) {
                    var endFn = function(e) {
                        onafter && onafter.call(el, e);
                        $(document).un(move, moveFn).un(end, endFn);
                        return false;
                    };
                    $(document).on(move, moveFn).on(end, endFn);
                };

            // Set a handler to begin drag, this method can only drag one element
            this.beginDrag = function() {
                var startTriggerX = false, startTriggerY = false,
                    self = this[0],
                    selfStyle = self.style,
                    startElPosLeft = $(self).pos().left,
                    startElPosTop = $(self).pos().top;
                moveFn = function(e) {
                    onmove && onmove.call(self, e);
                    startTriggerX === false && (startTriggerX = e.pageX);
                    startTriggerY === false && (startTriggerY = e.pageY);
                    selfStyle.left = startElPosLeft + e.pageX - startTriggerX + 'px';
                    selfStyle.top = startElPosTop + e.pageY - startTriggerY + 'px';
                    return false;
                };
                bindDocEvent(self);
            };

            this.eq(0).css("position", "absolute").on(start, function(e) {
                onbefore && onbefore.call(this, e);
                var startTriggerX= e.pageX,
                    startTriggerY = e.pageY,
                    self = this,
                    selfStyle = this.style,
                    startElPosLeft = $(this).pos().left,
                    startElPosTop = $(this).pos().top;
                moveFn = function(e) {
                    onmove && onmove.call(self, e);
                    selfStyle.left = startElPosLeft + e.pageX - startTriggerX + 'px';
                    selfStyle.top = startElPosTop + e.pageY - startTriggerY + 'px';
                    return false;
                };
                bindDocEvent(self);
                return false;
            });
            return this;
        }
    });
})(tMobile);
(function($) {

    $.mimeTypes = {
        script: 'text/javascript, application/javascript',
        json:   'application/json',
        xml:    'application/xml, text/xml',
        html:   'text/html',
        text:   'text/plain'
    };

    $.param = function(obj, v){
        var s = [],
            add = function(key, value){
                s.push(encodeURIComponent(v ? v + '[' + key +']' : key)
                  + '=' + encodeURIComponent(value));
            },
            isObject = $.isObject,
            isArray = $.isArray(obj);

        for(var i in obj){
            if(isObject(obj[i]))
                s.push($.param(obj[i], (v ? v + '[' + i + ']' : i)));
            else
                add(isArray ? '' : i, obj[i]);
        }
        return s.join('&').replace('%20', '+');
    };

    /**
     * Start a remote call directly from xhr
     * @param opts
     * url:         remote url, required
     * type:        get or post
     * onsuccess:    callback when remote calling finished successfully
     * onerror:     callback when error occurs
     * id:          request id, optional
     * data
     * async        default to true
     * dataType:    json, script, xml, html, text
     * contentType: default to 'application/x-www-form-urlencoded',
     * headers:     optional, object
     */
    $.ajax = function(options) {
        options = options || {};
        var url = options.url,
            type = options.type || "get",
            success = options.success || $.emptyFn,
            error = options.error || $.emptyFn,
            data = options.data,
            async = options.async !== false,
            dataType = options.dataType || "json",
            headers = options.headers || {},
            contentType = options.contentType || data && 'application/x-www-form-urlencoded';

        if (!url)
            throw Error("$.ajax: url is required.");

        if (typeof data == "object") data = $.param(data);

        if (type.match(/get/i) && data) {
            var qStr = data;
            if (url.match(/\?.*=/)) {
                qStr = '&' + qStr;
            } else if (qStr[0] != '?') {
                qStr = '?' + qStr;
            }
            url += qStr;
        }

        var mime = $.mimeTypes[dataType],
            xhr = new XMLHttpRequest();
        headers = $.extend({'X-Requested-With': 'XMLHttpRequest'}, headers);
        if (mime) headers['Accept'] = mime;

        xhr.onreadystatechange = function(){
            if (xhr.readyState == 4) {
                var result, err = false;
                if ((xhr.status >= 200 && xhr.status < 300) || xhr.status == 0) {
                    if (mime == 'application/json') {
                        try { result = JSON.parse(xhr.responseText); }
                        catch (e) { err = e; }
                    }
                    else result = xhr.responseText;
                    if (err) error(err, 'parsererror', xhr);
                    else success(result, 'success', xhr);
                } else {
                    error('error', 'requesterror', xhr);
                }
            }
        };

        xhr.open(type, url, async);
        if (contentType) headers['Content-Type'] = contentType;
        for (var k in headers) xhr.setRequestHeader(k, headers[k]);
        xhr.send(data);
    };

})(tMobile);
(function($) {

    function Scroller(el, opts) {
        opts = opts || {};
        var wrapper = el.parentNode,
            isDecelerating = false,
            startY = 0,
            startTime = 0,
            THRESHOLD = 8,
            contentStartOffsetY = 0,
            contentOffsetY = 0,
            self = this;
        this.onbottom = opts.onbottom;

        this.el = el;
        this.wrapper = wrapper;

        animateTo(0);

        el.addEventListener("touchstart", onTouchStart, false);
        el.addEventListener("touchmove", onTouchMove, false);
        el.addEventListener("touchend", onTouchEnd, false);

        function onTouchStart(e) {
            stopMomentum();
            startY = e.touches[0].clientY;
            startTime = e.timeStamp;
            contentStartOffsetY = contentOffsetY;
        }

        function onTouchMove(e) {
            e.preventDefault();
            var deltaY = e.touches[0].clientY - startY;
            if (isDragging(deltaY)) {
                animateTo(deltaY + contentStartOffsetY);
            }
        }

        function onTouchEnd(e) {
            var delta = e.changedTouches[0].clientY - startY;
            if (isDragging(delta)) {
                if (typeof self.onbottom == "function" && el.offsetHeight + contentOffsetY <= wrapper.clientHeight) {
                    self.onbottom();
                }
                doMomentum(e);
            }
        }

        function doMomentum(e) {
            isDecelerating = true;
            var velocity = (e.changedTouches[0].clientY - startY ) / (e.timeStamp - startTime),
                acceleration = velocity < 0 ? 0.0005 : -0.0005,
                displacement = - (velocity * velocity) / (2 * acceleration),
                time = - velocity / acceleration,
                newY = contentOffsetY + displacement,
                h1 = el.offsetHeight,
                h2 = wrapper.clientHeight;

            contentOffsetY = newY;

            if(newY >= 0) {
                if(h2 - newY <= h2 ) {time = 300;newY = 0;}
            } else {
                if(Math.abs(newY) >= h1 - h2) {
                    time = 300; newY = -h1 + h2;
                }
            }

            el.style.webkitTransition = '-webkit-transform ' + time +
                    'ms cubic-bezier(0.33,0.66,0.66,1)';
            el.style.webkitTransform = 'translate3d(0, ' + newY + 'px,0)';
        }

        function stopMomentum() {
            if (isDecelerating) {
                isDecelerating = false;
                var style = document.defaultView.getComputedStyle(el, null),
                    transform = new WebKitCSSMatrix(style.webkitTransform);
                el.style.webkitTransition = '';
                animateTo(transform.m42 || 0);
            }
        }

        function animateTo(offsetY) {
            contentOffsetY = offsetY;
            el.style.webkitTransform = 'translate3d(0, ' + offsetY + 'px,0)';
        }

        function isDragging(delta) {
            return Math.abs(delta) >= THRESHOLD;
        }
    }

    Scroller.prototype.resetPosition = function() {
        this.el.style.webkitTransform = 'translate3d(0, 0 ,0)';
    };

    Scroller.prototype.resetCallback = function(opts) {
        if (opts.onbottom) this.onbottom = opts.onbottom;
    };

    $.scroller = Scroller;

    var _scrollerCache = {};
    $.fn.extend({
        scroller: function(opts) {
            opts = opts || {};
            var reset = opts.reset !== false;
            this.each(function(el) {
                var id = $.getElementPPYId(el);
                if (_scrollerCache[id] == null) {
                    _scrollerCache[id] = new Scroller(el, opts);
                } else if (reset) {
                    _scrollerCache[id].resetPosition();
                    _scrollerCache[id].resetCallback(opts);
                }
            });
        }
    });

})(tMobile);
(function($){
    var cache = {}, tmpls = {}, elTmpls = {},
        _tmpl = function(str, data, tmplCache) {
            tmplCache = tmplCache == undefined ? true : tmplCache;
            var fn = tmplCache ? tmpls[str] : null;

            if (!fn) {
                fn = !/\W/.test(str) ?
                         cache[str] = cache[str] ||
                                      _tmpl(str) :
                         new Function("$data",
                                 "var p=[],print=function(){p.push.apply(p,arguments);};" +
                                 "with($data){p.push('" +
                                 str.replace(/[\r\t\n]/g, " ")
                                         .replace(/'(?=[^\]]*\]>)/g, "\t")
                                         .split("'").join("\\'")
                                         .split("\t").join("'")
                                         .replace(/<\[=(.+?)\]>/g, "',$1,'")
                                         .split("<[").join("');")
                                         .split("]>").join("p.push('")
                                         + "');}return p.join('');");
                if (tmplCache) tmpls[str] = fn;
            }
            return data ? fn(data) : fn;
        };

    $.tmpl = function(cid, data, tmplCache) {
        var tpl = document.getElementById(cid).innerHTML;
        return _tmpl(tpl, data, tmplCache);
    };

    $.fn.extend({
        /**
         * $(selector).tmpl({...options...})
         * options:
         * str:             template container's id (discarded)
         * tpl:             template string
         * tplContainerId:  template container's id
         * data:            data apply to template
         * iterateArray:    true to iterate an array data, default to true
         * append           true to append new html elemnts, false to replace, default to false
         * rendering:       callback when data is rendering
         * rendered:        callback when data has been rendered
         * @param opts
         */
        tmpl: function(opts) {
            opts = opts || {};
            var cid = opts.str,
                tpl = opts.tpl,
                data = opts.data,
                iterateArray = opts.iterateArray !== false,
                append = opts.append || false,
                rendering = opts.rendering,
                rendered = opts.rendered,
                el = this[0], dom, i, l,
                applyData = function(context) {
                    if (typeof rendering == "function" && rendering.call(this, context) === false) {
                        return;
                    }
                    dom = $(fn(context.data));
                    this.append(dom);
                    if (typeof rendered == "function")
                        rendered.call(this, dom, context);   
                };
            if (!el)
                throw Error("$.tmpl: no element found.");
            cid = opts.tplContainerId || cid;
            tpl = tpl || document.getElementById(cid).innerHTML;
            var elID = $.getElementPPYId(el),
                fn = tpl ? _tmpl(tpl) : elTmpls[elID];
            if (!fn)
                throw Error("$.tmpl: no template found for element.");
            else
                elTmpls[elID] = fn;
            if (data) {
                el = $(el);
                if (!append) el.empty();
                if (iterateArray && data.constructor == Array && data.length) {
                    for (i = 0, l = data.length; i < l; i++) {
                        applyData.call(el, {data: data[i], index: i});
                    }
                } else {
                    applyData.call(el, {data: data});  
                }
            }
            return this;
        }
    });

})(tMobile);
(function($) {

    var flexTpl = '<div class="flex_col">'
                + '<div></div>'
                + '<div class="dialog">'
                    + '<div class="content"></div>'
                + '</div>'
                + '<div></div>'
            + '</div>',
        fillParentTpl = '<div class="flex_col">'
                + '<div class="flex_margin_row"></div>'
                + '<div class="dialog flex_col">'
                    + '<div class="content"></div>'
                + '</div>'
                + '<div class="flex_margin_row"></div>'
            + '</div>';


    function Popup(opts) {
        if (!opts) return;
        var parent = $(opts.parent || "body"),
            zIndex = opts.zIndex || 101,
            top = opts.top || 0,
            left = opts.left || 0,
            tpl = opts.tpl,
            el = opts.el || "<div></div>";
        this.el = $(el).addClass("popup");
        if (opts.fillParent) this.el.addClass("fill_parent");
        this.el.css("z-index", zIndex)
            .css("top", top + "px").css("left", left + "px")
            .css("display", "none").appendTo(parent)
            .tmpl({tpl: tpl, data: {}});
    }

    Popup.prototype.show = function() {
        this.el.css("display", "block");
        return this;
    };

    Popup.prototype.hide = function() {
        this.el.css("display", "none");
        return this;
    };

    function Dialog(opts) {
        opts = opts || {};
        if (!opts.tpl) opts.tpl = opts.fillParent ? fillParentTpl : flexTpl;
        Popup.call(this, opts);
        this.dialogEl = this.el.find(".dialog");
        var self = this;
        if (opts.buttons && opts.buttons.length) {
            var buttonsEl = $("<div></div>").addClass("buttons").addClass("flex_row").appendTo(this.dialogEl),
                buttonsArr = opts.buttons, btn, text, onclick;
            for (var i = 0, l = buttonsArr.length; i < l; i++) {
                btn = buttonsArr[i];
                if (typeof btn == "string") {
                    if (btn.trim() == "close") {
                        text = "关闭";
                        onclick = function() {
                            self.hide();
                        }
                    }
                } else {
                    text = btn.text || "";
                    onclick = btn.onclick || $.emptyFn;
                }
                $("<div></div>").addClass("button_col").append('<div class="button">' + text + '</div>')
                    .on("click", onclick).appendTo(buttonsEl);
            }
        }
        if (typeof opts.title == "string") {
            var headerEl = $("<div></div>").addClass("header").prependTo(this.dialogEl),
                titleEl = $("<span></span>").addClass("title").appendTo(headerEl);
            titleEl.html(opts.title);
        }
        if (typeof opts.content == "string") {
            var contentEl = this.dialogEl.find(".content");
            contentEl.html(opts.content);
        }
    }

    Dialog.prototype = new Popup();

    Dialog.prototype.setContent = function(content) {
        var contentEl = this.dialogEl.find(".content");
        contentEl.html(content);
    };

    $.Dialog = Dialog;
    $.Popup = Popup;

    $.openDialog = function(opts) {
        return new Dialog(opts);
    };

})(tMobile);(function($){
    if (!$.adapters) $.adapters = {};

    function webAdapter() {
        
/**
 * Start a remote call directly from xhr
 * @param opts
 * url:         remote url, required
 * type:        get or post
 * onsuccess:    callback when remote calling finished successfully
 * onerror:     callback when error occurs
 * id:          request id, optional
 * data
 * async        default to true
 * dataType:    json, script, xml, html, text
 * contentType: default to 'application/x-www-form-urlencoded',
 * headers:     optional, object
 */
$.ajax = function(options) {
    options = options || {};
    var url = options.url,
        type = options.type || "get",
        success = options.success || $.emptyFn,
        error = options.error || $.emptyFn,
        data = options.data,
        async = options.async !== false,
        dataType = options.dataType || "json",
        headers = options.headers || {},
        contentType = options.contentType || data && 'application/x-www-form-urlencoded';

    if (!url)
        throw Error("$.ajax: url is required.");

    if (typeof data == "object") data = $.param(data);

    if (type.match(/get/i) && data) {
        var qStr = data;
        if (url.match(/\?.*=/)) {
            qStr = '&' + qStr;
        } else if (qStr[0] != '?') {
            qStr = '?' + qStr;
        }
        url += qStr;
    }

    var mime = $.mimeTypes[dataType],
        xhr = new XMLHttpRequest();
    headers = $.extend({'X-Requested-With': 'XMLHttpRequest'}, headers);
    if (mime) headers['Accept'] = mime;

    xhr.onreadystatechange = function(){
        if (xhr.readyState == 4) {
            var result, err = false;
            if ((xhr.status >= 200 && xhr.status < 300) || xhr.status == 0) {
                if (mime == 'application/json') {
                    try { result = JSON.parse(xhr.responseText); }
                    catch (e) { err = e; }
                }
                else result = xhr.responseText;
                if (err) error(err, 'parsererror', xhr);
                else success(result, 'success', xhr);
            } else {
                error('error', 'requesterror', xhr);
            }
        }
    };

    xhr.open(type, url, async);
    if (contentType) headers['Content-Type'] = contentType;
    for (var k in headers) xhr.setRequestHeader(k, headers[k]);
    xhr.send(data);
};
$.openUrl = function(url) {
};

$.loadUrl = function(url) {

};

$.pageLoaded = function(fn) {
    $(window).on("load", fn);
};


$.pageAppeared = function(fn) {
    $(window).on("load", fn);
};
    }

    $.adapters["web"] = webAdapter;
})(tMobile);(function($){
    if (!$.adapters) $.adapters = {};

    function androidAdapter() {
        /**
 * Android http response callback
 * @param id
 * @param status        http response status
 * @param responseText
 */
 //ajax请求结束函数   	
window._on_ajax_finished = function(id, status, result) {
	android_log.println("_on_ajax_finished");
	
    if (!_ajaxCache[id])
        $.log("_on_ajax_finished: no ajax cache found", "e");
    var ctx = _ajaxCache[id], err = false,
        success = ctx.success, error = ctx.error;
    delete _ajaxCache[id];
    if ((status >= 200 && status < 300) || status == 0) {
        if (ctx.dataType == "json" && typeof result == "string") {
            try { result = JSON.parse(result); }
            catch (e) { err = e; }
        }
        if (err) error(err, 'parsererror', result);
        else  {
            // use db if needed
            if (ctx.db != null) {
            	//保存到数据库
                ctx.db.kvSave(ctx.dbKey, $.toJSONString(result), ctx.dbLife);
            }
            //调用成功函数
            success(result, 'success');
        }
    } else {
        error(result, 'requesterror');
    }
};

if (typeof _ajaxCache == "undefined") {
    _ajaxCache = {};
}

/**
 * http请求
 * Start a remote call from android http request
 * @param opts
 * url:         remote url, required
 * onsuccess:    callback when remote calling finished successfully
 * onerror:     callback when error occurs
 * id:          request id, optional
 * data
 * async        default to true
 * dataType:    json, script, xml, html, text
 * contentType: default to 'application/x-www-form-urlencoded',
 * headers:     optional, object
 *
 * db:          true to use db cache, default to false
 * dbLife:      db cache exists time in secounds, default to 3600(1 hour)
 */
$.ajax = function(options) {
	
    if (!window.android_net)
        $.log("$.ajax: no android javascript object found.", "e");

    options = options || {};
    var ajaxId = $.generateUniqueId();   // ajax cache id
    var url = options.url,
        type = options.type || "get",
        success = options.success || $.emptyFn,
        error = options.error || $.emptyFn,
        data = options.data,
        async = options.async !== false,
        dataType = options.dataType || "json",
        headers = options.headers || {},
        contentType = options.contentType || data && 'application/x-www-form-urlencoded',
        db = (options.db === true || options.dbLife != null),
        dbLife = options.dbLife == null ? 3600 : options.dbLife;
    
    if (!url)
        $.log("$.ajax: url is required.", "e");
    
    // Add to cache
    var cache = _ajaxCache[ajaxId] = {
        url: url,
        data: data,
        success: success,
        error: error,
        dataType: dataType
    };

    if (typeof data == "object") data = $.param(data);

    if (type.match(/get/i) && data) {
        var qStr = data;
        if (url.match(/\?.*=/)) {
            qStr = '&' + qStr;
        } else if (qStr[0] != '?') {
            qStr = '?' + qStr;
        }
        url += qStr;
    }

    // use db if needed
    if (db) {
        var key = type.match(/get/i) ? url : url + data;
        $.openDatabase(function(db) {
            cache.db = db;
            cache.dbKey = key;
            cache.dbLife = dbLife;
            db.kvLoad(key, function(val) {
                if (val == "" || val == null) {
                    request();
                } else {
                    _on_ajax_finished(ajaxId, 200, $.evalJSON(val));
                }
            });
        });
        return;
    }

    function request() {
    	android_log.println("request: url = ",url);
    	android_log.println("request: dataType = ",dataType);
    	android_log.println("request: ajaxId = ",ajaxId);
    	
        if (type.match(/get/i)) {
            if (async){
            	android_log.println("android_net.get");
            	android_net.get(ajaxId, url, dataType);
            }
            else{
            	android_log.println("_on_ajax_finished");
            	_on_ajax_finished(ajaxId, 200, android_net.getSync(url));
            }
        } else {
            if (async){
            	android_log.println("android_net.post");
            	android_net.post(ajaxId, url, data, dataType);
            }
            else{
            	android_log.println("_on_ajax_finished");
            	_on_ajax_finished(ajaxId, 200, android_net.postSync(url, data));
            }
        }
    }
    request();

};/**
 * Android logcat
 * @param log
 * @param type      d - debug, e - error
 */
$.log = function(log, type) {
    if (!window.android_log)
        throw Error("$.log: no android logcat found");
    type = type || "d";
    android_log[type](log);
};

$.print = function(log) {
    android_log.println(log);
};
/**
 * open a new ui
 * @param url
 * @param opts
 */
$.openUrl = function(url, opts) {
	android_log.println("openUrl tMobile 1");
	
    if (!window.android_web)
        $.log("$.openUrl: no android web object found", "e");
    var json = $.toJSONString(opts || {});
    if (CONFIG && url in CONFIG.android.url_activity_map) {
        android_web.openActivity(CONFIG.android.url_activity_map[url], json);
    } else {
        android_web.openUrl(url, json);
    }
};

$.loadUrl = function(url) {
    if (!window.android_web)
        $.log("$.openUrl: no android web object found", "e");
    android_web.loadUrl(url);
};

$.refresh = function() {
    android_web.refresh();
}

/**
 * Page loaded callback
 */

var _page_loaded_funcs = [];
window._page_loaded = function() {
    var i = 0, l = _page_loaded_funcs.length, fn;
    android_log.println("_page_loaded",l);
    
    for (; i < l; i++) {
        fn = _page_loaded_funcs[i];
        android_log.println("_page_loaded for",i);
        fn();
        android_log.println("_page_loaded for fn()",i);
    }
    android_log.println("_page_loaded out for");
};

$.pageLoaded = function(fn) {
    _page_loaded_funcs.push(fn);
};

/**
 * Page appeared callback
 */

var _page_appeared_funcs = [];

/**
 * Android will call _page_appeared when webactivity loaded or resumed
 * @param load  true - page is loaded from page source; false - web activity resumed
 */
window._page_appeared = function(load) {
    var i = 0, l = _page_appeared_funcs.length, fn;
    for (; i < l; i++) {
        fn = _page_appeared_funcs[i];
        fn(load);
    }
};

/**
 *
 * @param fn    fn will be called twice when a page is loaded from source
 */
$.pageAppeared = function(fn) {
    _page_appeared_funcs.push(fn);
};

var _queryParams;
$.getQueryParams = function() {
    if (!window.android_web)
        $.log("$.openUrl: no android web object found", "e");
    _queryParams = $.evalJSON(android_web.getQueryParams());
    return _queryParams;
};

$.getQueryParam = function(name, defaultValue) {
    if (!_queryParams)
        _queryParams = $.getQueryParams();
    var v = _queryParams[name];
    return v == undefined ? defaultValue : v;
};

$.getContextData = function(name) {
    if (!window.android_web)
        $.log("$.openUrl: no android web object found", "e");
    return android_web.getContextData(name);
};

$.getContextDataObj = function(name) {
    var json = $.getContextData(name);
    return $.evalJSON(json);
}
function Dialog(opts) {
    opts = opts || {};
    var id = opts.id == undefined ? $.generateUniqueId() : opts.id,
        cls = opts.type;
    this.id = id;
    this.type = cls;
    this.loaded = function() {
        if (this.webviewCallBack) {
            this.webviewCallBack();
        }
    };
    this.onLoaded = function(_eHandler) {
        this.webviewCallBack = _eHandler;
        return this;
    };
    android_view.createDialog(id, cls);
    this.loaded();
}

Dialog.prototype.show = function() {
    android_view.showDialog(this.id);
    return this;
};

Dialog.prototype.setProperty = function(key, val) {
    var prop = key;
    if (typeof key == "string") {
        prop = {};
        prop[key] = val == undefined ? "" : val;
    }
    android_view.setProperty(this.id, $.toJSONString(prop));
};

Dialog.prototype.hide = function() {
    android_view.hideDialog(this.id);
    return this;
};

// Dialog type
$.DIALOG_STYLE = {};
$.DIALOG_STYLE.loading = "android.source.tuangou.framework.ui.LoadingDialog";
$.DIALOG_STYLE.alert = "android.source.tuangou.framework.ui.WebAlertDialog";

$.createDialog = function(opts) {
    return new Dialog(opts);
};

$.createLoadingDialog = function(opts) {
    opts = opts || {};
    opts.type = $.DIALOG_STYLE.loading;
    return $.createDialog(opts);
};

$.alert = function(msg) {
    var dialog = $.createDialog({type: $.DIALOG_STYLE.alert});
    dialog.setProperty("text", msg);
    dialog.show();
};

$.setUITitle = function(title) {
    android_view.setTitle(title);
};

$.setWebActivityProperty = function(name, value) {
    android_view.setWebActivityProperty(name, value);
};

// Phone call
$.callPhone = function(num) {
    android_web.callPhone(num);
};

$.viewMap = function(shop) {
    var coordinate = shop.latitude + "," + shop.longitude;
    var shop_name = shop.name;
    android_web.viewMap(coordinate, shop_name);
};

$.toDealSite = function(url) {
    android_web.toDealSite(url);
};

$.share = function(content) {
    android_web.share(content);
};

$.getTimeLeft = function (closeTime) {
    if (!closeTime) {
        return "";
    }
    closeTime = new Date(Date.parse(closeTime.replace(/-/g, "/")));
    var currentTime = new Date(),
            interval = closeTime.getTime() - currentTime.getTime();
    if (interval < 0) {
        return "团购已过期";
    }
    var minute = Math.round(interval / 60000),
            _m = minute % 60,
            hour = (minute - _m) / 60,
            _h = hour % 24,
            _d = (hour - _h) / 24;
    if (_d == 0 && _h != 0) return timeleft = "剩余" + _h + "小时" + _m + "分钟";
    if (_d == 0 && _h == 0) return timeleft = "剩余" + _m + "分钟";
    return timeleft = "剩余" + _d + "天" + _h + "小时" + _m + "分钟";
};
window._on_deals_finished = function(id, dealsJson) {
    var callback = callCache[id];
    if (typeof callback == "function") {
        callback(dealsJson);
    }
};

$.getLocation = function(callback) {
    var loc = android_lbs.getLocation();
    if (!loc || loc == "undefined") loc = "";
    callback(loc);
};

var callCache = {};

$.getNearbyDeals = function(opts) {
    opts = opts || {};
    var pageset = opts.pageset || 10,
        page = opts.page || 1,
        callback = opts.callback || $.emptyFn(),
        id = $.generateUniqueId();
    callCache[id] = callback;
    android_lbs.getNearbyDeals(id, pageset, page);
};

//判断是否登录函数
$.isLogin = function() {
    return android_session.isLogin();
};

//登录函数
$.login = function(userName, password, callback) {
	
	//调用登录函数，android_session指向SessionBridge
    var ret = android_session.login(userName, password);
    if (typeof callback == "function") {
        callback(ret);
    }
};

$.logout = function(callback) {
    var ret = android_session.logout();
    if (typeof callback == "function") {
        callback(ret);
    }
};
function DB(name) {
    if (!name)
        $.log("No db name founded.", "e");
    this.name = name;
    $.log("Creating db: " + name);
    android_store.openDatabase(name);
}

/**
 * Save a key-value data
 * @param key
 * @param val
 * @param life      exists time in seconds.
 */
DB.prototype.kvSave = function(key, val, life) {
    life = life == undefined ? -1 : life;
    var expireTime = life < 0 ? -1 : Math.ceil(Date.now() / 1000) + life;
    android_store.kvSave(this.name, key, val, expireTime);
    return this;
};

DB.prototype.kvLoad = function(key, callback) {
    var val = android_store.kvLoad(this.name, key);
    if (typeof callback == "function") {
        callback(val);
    }
    return this;
};

DB.prototype.getPreference = function(key, callback) {
    var val = android_store.getPreference(key);
    if (typeof callback == "function") {
        callback(val);
    }
    return this;
};

DB.prototype.savePreference = function(key, val, life) {
	android_log.println("tMobile savaPreference");
	
    life = life == undefined ? -1 : life;
    var expireTime = life < 0 ? -1 : Math.ceil(Date.now() / 1000) + life;
    android_store.savePreference(key, val, expireTime);
    return this;
};

/**
 *
 * @param sql
 * @param callback      optional, if successed, callback will has a argument 1, else 0
 */
DB.prototype.execSql = function(sql, callback) {
    var ret = android_store.execSql(this.name, sql);
    if (typeof callback == "function") {
        callback(ret);
    }
};

DB.prototype.query = function(sql, callback) {
    var ret = android_store.query(this.name, sql);
    callback(ret);
};

// current page's db
var _db = {};

$.openDatabase = function(callback, name) {
	 android_log.println("openDatabase");
	 
    name = typeof name == "string" ? name : CONFIG && CONFIG.db.db_name;
    $.log(name);
    if (!(name in _db)) {
        _db[name] = new DB(name);
    }
    if (typeof callback == "function") {
        callback(_db[name]);
    }
};

    }

    $.adapters["android"] = androidAdapter;
})(tMobile);(function($) {
    if (!$.adapters) $.adapters = {};

    function iosAdapter() {

        window._jsbridge_callobj = {order:0};

        window.JSBridge_getAllJsonStringObject = function() {
            var _sortArr = [];

            var obj;
            for (var key in window._jsbridge_callobj) {
                if (key == "order") continue;

                obj = window._jsbridge_callobj[key];
                if (obj.status > 0) continue;

                var b = {
                    jsonid: key,
                    order:obj.order
                };
                _sortArr.push(b);
            }

            //asc
            _sortArr.sort(function(a, b) {
                return a.order - b.order;
            });
            var arr = [];
            var len = _sortArr.length;
            for (var i=0; i<len; i++) {
                arr[i] = _sortArr[i].jsonid;
            }
            return arr.join(",");
        };

        window.JSBridge_getJsonStringForObjectWithId = function(id) {
            var obj = _jsbridge_callobj[id],
                    needCallback = typeof obj.callback == "function" ? 1 : 0;
            if (obj.status > 0) {
                return;
            }
            obj.status = 1;
            var b = {
                jsonid: id,
                task: obj.task,
                callback: needCallback,
                args: obj.args || []
            };

            return $.toJSONString(b);
        };

        /**
         * js callback for objective c
         * @param id    call uid
         * @param opts
         * opts:
         * args: callback arguments, Array type
         */
        window._jsbridge_callback = function(id, opts) {
            var args = opts.args || [],
                    b = _jsbridge_callobj[id];

            if (b && typeof b.callback == "function") {
                b.callback.apply(null, args);
            }
            _jsbridge_callobj[id] = null;
            delete _jsbridge_callobj[id];
        };

        /**
         * Call objective-c method
         * @param task      method name
         * @param args   arguments, optional
         */
        function _call_native(opts) {
            var task = opts.task,
                    callback = opts.callback,
                    args = opts.args,
                    id = $.generateUniqueId();
            if (!task)
                $.log("call native: no task found.", "e");

            _jsbridgeCallForward(id, task, args, callback);
        }

        function _call_native_log(opts) {
            var task = opts.task,
                    args = opts.args,
                    id = $.generateUniqueId();
            _jsbridgeCallForward(id, task, args, 0);
        }

        //no log
        //don't log in this function
        function _jsbridgeCallForward(id, task, args, callback) {
            _jsbridge_callobj.order++;

            _jsbridge_callobj[id] = {
                task: task,
                callback: callback,
                args: args,
                status:0,
                order:_jsbridge_callobj.order
            };

            window.location.href = "JSBridge://ReadNotificationWithId=" + id;
        }

        var _ajaxCache = {};

/**
 * Start a remote call from android http request
 * @param opts
 * url:         remote url, required
 * onsuccess:    callback when remote calling finished successfully
 * onerror:     callback when error occurs
 * id:          request id, optional
 * data
 * async        default to true
 * dataType:    json, script, xml, html, text
 * contentType: default to 'application/x-www-form-urlencoded',
 * headers:     optional, object
 */
$.ajax = function(options) {
    options = options || {};
    var ajaxId = $.generateUniqueId();   // ajax cache id
    var url = options.url,
            type = options.type || "get",
            success = options.success || $.emptyFn,
            error = options.error || $.emptyFn,
            data = options.data,
            async = options.async !== false,
            dataType = options.dataType || "json",
            headers = options.headers || {},
            contentType = options.contentType || data && 'application/x-www-form-urlencoded';

    if (!url)
        $.log("$.ajax: url is required.", "e");

    // Add to cache
    _ajaxCache[ajaxId] = {
        url: url,
        data: data,
        success: success,
        error: error,
        dataType: dataType
    };

    var posturl = url;
    var postdata = data;

    if (typeof data == "object") data = $.param(data);

    if (type.match(/get/i) && data) {
        var qStr = data;
        if (url.match(/\?.*=/)) {
            qStr = '&' + qStr;
        } else if (qStr[0] != '?') {
            qStr = '?' + qStr;
        }
        url += qStr;
    }

    if (type.match(/get/i)) {
        httpGetCmd();
    } else {
        httpPostCmd();
    }
    function httpGetCmd() {
        _call_native({task:"HttpGetCmd", callback:function(id, result, errorCode) {
            if (!_ajaxCache[id])
                $.log("_on_ajax_finished: no ajax cache found", "e");
            var ctx = _ajaxCache[id], err = false,
                    success = ctx.success, error = ctx.error;
            delete _ajaxCache[id];

            if (errorCode) {
                error('网络连接错误');
                return;
            }

            if (ctx.dataType == "json" && typeof result == "string") {
                try {
                    result = JSON.parse(result);
                }
                catch (e) {
                    err = e;
                }
            }
            if (err) error(err, 'parsererror', result);
            else success(result, 'success');

        },
            args:[ajaxId, url, dataType]});
    }

    function httpPostCmd() {
        _call_native({task:"HttpPostCmd", callback:function(id, result, errorCode) {
            if (!_ajaxCache[id])
                $.log("_on_ajax_finished: no ajax cache found", "e");
            var ctx = _ajaxCache[id], err = false,
                    success = ctx.success, error = ctx.error;
            delete _ajaxCache[id];

            if (errorCode) {
                error('网络连接错误');
                return;
            }

            if (ctx.dataType == "json" && typeof result == "string") {
                try {
                    result = JSON.parse(result);
                }
                catch (e) {
                    err = e;
                }
            }
            if (err) error(err, 'parsererror', result);
            else success(result, 'success');

        },
            args:[ajaxId, posturl, postdata, dataType]});
    }

};/**
 * iOS logcat
 * @param log
 * @param type      d - debug, e - error
 */
$.log = function(log, type) {
    type = type || "d";
    _call_native_log({task:"LogCmd", args:[type, log]});
};
var UIWebViewClass = function() {
};

UIWebViewClass.prototype = {
    loaded:function() {
        if (this.alreadyLoad) {
            return;
        } else {
            this.alreadyLoad = true;
        }
        if (this.onshow) {
            for (var i = 0; i < this.onshow.length; i++) {
                this.onshow[i]();
            }
        }
    },
    onLoaded:function(_eHandler) {
        if (!this.onshow)this.onshow = [];
        this.onshow.push(_eHandler);
    }
};
var uIWebViewClassObj = new UIWebViewClass();
window.uIWebViewClassObj = uIWebViewClassObj;

$.openUrl = function(url, opts) {
    var classParam = "";
    if (CONFIG && CONFIG.ios_config.url_activity_map
            && url in CONFIG.ios_config.url_activity_map) {
        classParam = CONFIG.ios_config.url_activity_map[url];
    }
    var paramObj = opts || {};
    paramObj["_class_"] = classParam;
    var json = $.toJSONString(paramObj);
    _call_native({task:"OpenUrlCmd", args:[url, json]});
};

$.loadUrl = function(url) {

};

$.pageLoaded = function(fn) {
    uIWebViewClassObj.onLoaded(fn);
};

$.pageAppeared = function(fn) {
    uIWebViewClassObj.onLoaded(fn);
};

//ios will initialize the params on load
//window.iosWebViewUrlQueryParams = null;

var _queryParams;
$.getQueryParams = function() {
//    var lf = window.location+"";
//    var inx = lf.indexOf("?");
//    var queryStr = lf.substring(inx+1);
//
//    var arr = queryStr.split("&");
//    var obj = {};
//    var len = arr.length;
//    for (var i=0;i<len;i++) {
//        var arr2 = arr[i].split("=");
//        obj[arr2[0]] = arr2[1];
//    }
//
//    _queryParams = obj;
//    return _queryParams;
    if (window.ios_web == undefined) {
        return {};
    }
    _queryParams = $.evalJSON(ios_web.getQueryParams());
    return _queryParams;
};

$.getQueryParam = function(name, defaultValue) {
    if (!_queryParams)
        _queryParams = $.getQueryParams();
    var v = _queryParams[name];
    return v == undefined ? defaultValue : v;
};

$.getContextDataObj = function(name) {
    return ios_web.contextdatas[name];
};


//function Dialog(opts) {
//    opts = opts || {};
//    var id = opts.id == undefined ? $.generateUniqueId() : opts.id,
//            cls = opts.type;
//    this.id = id;
//    this.type = cls;
//    this.loaded = function() {
//        if (this.webviewCallBack) {
//            this.webviewCallBack();
//        }
//    };
//    this.onLoaded = function(_eHandler) {
//        this.webviewCallBack = _eHandler;
//        return this;
//    };
//    var diaObj = this;
//    _call_native({task:"DialogCmd", callback:function() {
//        diaObj.loaded();
//    }, args:["createDialog", id, cls]});
//
//}
//
//Dialog.prototype.show = function() {
//    var diaObj = this;
//    _call_native({task:"DialogCmd", callback:function() {
//        diaObj.loaded();
//    }, args:["showDialog", this.id]});
//    return this;
//};
//
//Dialog.prototype.setProperty = function(key, val) {
//    var prop = key;
//    if (typeof key == "string") {
//        prop = {};
//        prop[key] = val == undefined ? "" : val;
//    }
//    var diaObj = this;
//    _call_native({task:"DialogCmd", callback:function() {
//        diaObj.loaded();
//    }, args:["setProperty", this.id, $.toJSONString(prop)]});
//    return this;
//};
//
//Dialog.prototype.hide = function() {
//    var diaObj = this;
//    _call_native({task:"DialogCmd", callback:function() {
//        diaObj.loaded();
//    }, args:["hideDialog", this.id]});
//    return this;
//};

function Dialog(opts) {

}

Dialog.prototype.show = function() {
    return this;
};

Dialog.prototype.setProperty = function(key, val) {
    return this;
};

Dialog.prototype.hide = function() {
    return this;
};

// Dialog type
$.DIALOG_STYLE = {};
$.DIALOG_STYLE.loading = "LoadingDialog";
$.DIALOG_STYLE.alert = "WebAlertDialog";

$.createDialog = function(opts) {
    return new Dialog(opts);
};

$.createLoadingDialog = function(opts) {
    opts = opts || {};
    opts.type = $.DIALOG_STYLE.loading;
    return $.createDialog(opts);
};

$.alert = function(msg) {
    var dialog;

    function show() {
        dialog.show().onLoaded(function() {  });
    }

    function setProp() {
        dialog.setProperty("text", msg).onLoaded(show)
    }

    dialog = $.createDialog({type: $.DIALOG_STYLE.alert}).onLoaded(setProp);
};

$.callPhone = function(num) {
    _call_native({task:"CommonWebCmd", args:["callPhone", {phoneNum:num}]});
};

$.viewMap = function(shop) {
    _call_native({task:"CommonWebCmd", args:["viewMap", shop]});
};

$.toDealSite = function(url) {
    _call_native({task:"CommonWebCmd", args:["toDealSite", {url:url}]});
};

$.share = function(content) {
    _call_native({task:"CommonWebCmd", args:["share", {content:content}]});
};

$.setUITitle = function(title) {

};

$.setWebActivityProperty = function(name, value) {

};window._on_deals_finished = function(id, dealsJson) {
    var callback = callCache[id];
    if (typeof callback == "function") {
        callback(dealsJson);
    }
};

var callCache = {};


$.getCity = function(callback) {
    var c = ios_location.getCity();
    callback(c);
};


$.getLocation = function(callback) {

    callback("");
};

$.getNearbyDeals = function(opts) {
    opts = opts || {};
    var pageset = opts.pageset || 10,
            page = opts.page || 1,
            callback = opts.callback || $.emptyFn(),
            id = $.generateUniqueId();
    _call_native({task:"NearByCmd", args:[page, pageset], callback:callback});
};

$.isLogin = function() {
    return ios_session.isLogin();
};

    }

    $.adapters["ios"] = iosAdapter;
})(tMobile);(function($) {
    /**
     * For test
     */
    if (!$.adapters)
        $.adapters = {
            "web": function() {},
            "android": function(){},
            "ios": function(){}
        };

    var adapter = "web";
    // Inject from android js bridge
    if (typeof android_config != "undefined") {
        adapter = android_config.getJsAdapter();
    } else if ($.os.ios) {
        adapter = "ios";
    } else if ($.os.android) {
        adapter = "android";
    }

    /**
     * Placeholder
     */
    $.log = $.emptyFn;

    /**
     * Apply adapter;
     */
    $.adapters[adapter]();

})(tMobile);