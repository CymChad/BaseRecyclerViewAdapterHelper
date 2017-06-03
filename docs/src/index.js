/**
 * Created by luoxiongwen on 17/6/1.
 */

var app = angular.module('BRAVH', ['ui.bootstrap', 'pascalprecht.translate', 'ngCookies', 'ngSanitize']);
app.controller('mainController', function ($scope, $translate, $cookies) {
  $scope.isNavCollapsed = true;
  $scope.isCollapsed = false;

  $scope.scrollToElem = function (query) {
    var dom = document.querySelector(query);
    var target = dom.offsetTop;
    var doc = document.documentElement;
    var cur = (window.pageYOffset || doc.scrollTop)  - (doc.clientTop || 0);
    var durationTime = 300;
    step(Date.now(), cur, target);

    function step(startTime, start, target) {
      var percent = (Date.now() - startTime) / durationTime;
      var pos = start + (target - start) * percent;
      if (percent < 1) {
        window.scrollTo(0, pos);
        window.requestAnimationFrame(function () {
          step(startTime, start, target);
        });
      } else {
        window.scrollTo(0, target);
      }
    }
  };

  $scope.supported = isSupport();


  function isSupport() {
    var d = document.documentElement.style;
    return (('flex' in d) || ('webkitFlex' in d) || ('msFlex' in d));
  }
  $scope.navbarItems = [
    {
      query: '.section-banner',
      translate: 'nav-index',
    },
    {
      query: '#section1',
      translate: 'nav-introduce',
    },
    {
      query: '#section2',
      translate: 'nav-usage',
    },
    {
      query: '#section3',
      translate: 'nav-aboutUs',
    },
    {
      query: '#section4',
      translate: 'nav-members',
    },
  ];

  $scope.languages = [
    {
      title: '中文-简体',
      language: 'cn',
    },
    {
      title: 'English',
      language: 'en'
    }
  ];

  var languageCookie = $cookies.get('NG_TRANSLATE_LANG_KEY');

  $scope.languageIndex = languageCookie ? (languageCookie === 'en' ? 1 : 0) : defaultLanguage;

  $scope.changeLanguage = function () {
    $scope.languageIndex = ($scope.languageIndex + 1) % 2;
    $translate.use($scope.languages[$scope.languageIndex].language);
  };

  $scope.functionIcons = [
    {
      pic: 'img/function-1.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Optimization-adapter-to-reduce-code',
    },
    {
      pic: 'img/function-2.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Add-OnItemClickLister',
    },
    {
      pic: 'img/function-3.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Add-adaptar-animation',
    },
    {
      pic: 'img/function-4.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Add-footview-and-headview',
    },
    {
      pic: 'img/function-5.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Use-it-drag-and-swipe-item',
    },
    {
      pic: 'img/function-6.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Pull-up-to-loadMore',
    },
    {
      pic: 'img/function-7.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Expandable-Item',
    },
    {
      pic: 'img/function-8.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Use-Custom-BaseViewHolder',
    },
    {
      pic: 'img/function-9.png',
      link: 'https://github.com/CymChad/BaseRecyclerViewAdapterHelper/wiki/Use-it-setEmptyView',
    },
  ];

  $scope.getIconClass = function (icon) {
    return 'iconfont icon-' + icon;
  };

  $scope.gitStatus = [
    {
      icon: 'clock',
      text: 'commits',
      number: 714,
    },
    {
      icon: 'pull-request',
      text: 'pull requests',
      number: 143,
    },
    {
      icon: 'tag',
      text: 'releases',
      number: 138,
    },
    {
      icon: 'contributors',
      text: 'contributors',
      number: 20,
    },
    {
      icon: 'issue',
      text: 'issues',
      number: 843,
    },
    {
      icon: 'star',
      text: 'star',
      number: 6407,
    },
  ];

  $scope.members = [
    {
      avatar: 'img/avatar-1.png',
      name: '上海-陈宇明',
      link: 'https://github.com/CymChad',
    },
    {
      avatar: 'img/avatar-2.png',
      name: '广州-安杰',
      link: 'http://cherylgood.cn/',
    },
    {
      avatar: 'img/avatar-3.png',
      name: '广州-朝辉',
      link: 'http://www.github.com/shichaohui',
    },
    {
      avatar: 'img/avatar-4.png',
      name: '北京-零乱',
      link: 'https://github.com/1109993488',
    },
    {
      avatar: 'img/avatar-5.png',
      name: '绍兴-Tysheng',
      link: 'https://github.com/tysheng',
    },
    {
      avatar: 'img/avatar-6.png',
      name: '广州-曾老师',
      link: 'https://github.com/zeng1990java',
    },
    {
      avatar: 'img/avatar-7.png',
      name: '深圳-Harlan',
      link: 'https://github.com/elfman',
    },
    {
      avatar: 'img/avatar-8.png',
      name: '上海-AllenCoder',
      link: 'http://www.devcoder.cn/',
    },
    {
      avatar: 'img/avatar-9.png',
      name: '苏州-林少',
      link: 'https://lhl-012.github.io/',
    },
    {
      avatar: 'img/avatar-10.png',
      name: '杭州-Frank',
      link: 'https://github.com/frankkwok',
    }
  ];

}).config(['$translateProvider', function ($translateProvider) {
  $translateProvider.translations('en', translationsEN);
  $translateProvider.translations('cn', translationsCN);
  $translateProvider.preferredLanguage(defaultLanguage ? 'en' : 'cn');
  $translateProvider.useCookieStorage();
  $translateProvider.useSanitizeValueStrategy('sanitize');
}]);

var defaultLanguage = (navigator.browserLanguage || navigator.language).toLowerCase().indexOf('en') >= 0 ? 1 : 0;

var translationsEN = {
  'nav-index': 'Index',
  'nav-introduce': 'Introduce',
  'nav-usage': 'Usage',
  'nav-aboutUs': 'About Us',
  'nav-members': 'Members',
  changeLanguage: 'Language',
  recommend: 'as one of the most frequently-used controls in Android, have you ever been vexed by it?<br>BRVAH is targeted at all the Android developers, aiming to help them to work with high efficiency.',
  'section-2-title': 'Function',
  'section-2-brief': 'The efficient use of RECYCLERVIEW meets the common need of Adapter in projects. RECYCLERVIEW has never been so easy',
  'section-2-item-0': 'Optimize<br>adapter code',
  'section-2-item-1': 'Item click<br>listeners',
  'section-2-item-2': 'Adapter <br> animation',
  'section-2-item-3': 'Add Header <br> And Footer',
  'section-2-item-4': 'Drag <br> slide delete',
  'section-2-item-5': 'Auto <br> load more',
  'section-2-item-6': 'Extensional <br> subkey',
  'section-2-item-7': 'Custom <br> project type',
  'section-2-item-8': 'Set <br> empty layout',
  'section-3-title': 'BRVAH HAPPY 1ST BIRTHDAY!',
  'section-3-subtitle-1': 'The first Release Version in April 10th, 2016',
  'section-3-subtitle-2': 'A year later',
  'section-3-summery': 'This year marks the first anniversary of BRVAH.<br>During this year, BRVAH submited code 714 times, which involve 20 people to participate in.<br>It also solved problems for the users 843 times.<br>BRVAH is awarded a total of 6407 stars, gaining NO.1 ranking.',
  'section-4-title': 'Our team',
  'section-4-brief': "It was the reverence to code that makes us treat things with earnestness, responsibility and respect.<br>When faced with challenges, it was the zeal to code that makes the spirits of exploring and seeking become the instincts of each member of BRAVH.<br>We communicate with others through projects. Via BRVAH, we're trying to show our perserverance and convey our faith.<br>A good project, of course, can't be achieved without the trust and affection among the team members. The trust and affection among the team members provide the impetus for BRVAH to further efforts.",
  'section-4-welcome': 'Forward<br>to your join',
  'footer': 'All Right reserved licensed under the Apache license Version 3.0.',
  'not-support': 'This browser is not support by our site, please update your browser. Recommend Chrome and Firefox'
};

var translationsCN = {
  'nav-index': '首页',
  'nav-introduce': '项目介绍',
  'nav-usage': '使用方式',
  'nav-aboutUs': '关于我们',
  'nav-members': '成员介绍',
  changeLanguage: 'Language',
  recommend: '作为Android最常用的控件之一，是否常常为“她”操碎了心<br>BRVAH受益群体是所有Android开发者，希望更多开发者能够一起来把这个项目做得更好帮助更多人',
  'section-2-title': '功能和作用',
  'section-2-brief': '高效的使用RecyclerView应对项目中的常见需求的Adapter，RecycleView从未如此简单！',
  'section-2-item-0': 'Optimize<br>adapter code',
  'section-2-item-1': 'Item click<br>listeners',
  'section-2-item-2': 'Adapter <br> animation',
  'section-2-item-3': 'Add Header <br> And Footer',
  'section-2-item-4': 'Drag <br> slide delete',
  'section-2-item-5': 'Auto <br> load more',
  'section-2-item-6': 'Extensional <br> subkey',
  'section-2-item-7': 'Custom <br> project type',
  'section-2-item-8': 'Set <br> empty layout',
  'section-3-title': 'BRVAH HAPPY 1ST BIRTHDAY!',
  'section-3-subtitle-1': 'The first Release Version in April 10th, 2016',
  'section-3-subtitle-2': 'A year later',
  'section-3-summery': 'BRVAH一周岁了， 在这一年中提交代码714次<br>共有20人参与其中，解决问题843次<br>获得star 6407，同类框架排行第1',
  'section-4-title': '我们的团队',
  'section-4-brief': '正因一颗敬畏代码的心，让我们都以认真、负责和尊重的态度去对待件事情。<br>源于对代码的狂热，因此让我们一次次的直面挑战时，致使求知、探索成为BRVAH每位成员一种内在的本能。<br>我们透过项目去与每一个人沟通，通过BRVAH告诉人们我们的执着与信念。<br>当然，成就一个好的项目，离不开团队成员之间的信任与情感，有了这些内因的推动，才让BRVAH始终努力...',
  'section-4-welcome': '期待<br>您的加入',
  'section-qrcode-title': '公众号「 码个蛋 」',
  'section-qrcode-content-1': '我们不仅仅分享源码，还做了一个公众号——码个蛋<br>分享优秀的文章，每天更新优质文章',
  'section-qrcode-content-2': '文章类型 技术|职场|成长',
  'section-qrcode-tag-0': '热门技术',
  'section-qrcode-tag-1': '代码封装',
  'section-qrcode-tag-2': 'View',
  'section-qrcode-tag-3': '源码分析',
  'section-qrcode-tag-4': '精进之路',
  'section-group-content-1': '入群要求：说出你的目标',
  'section-group-content-2': 'QQ群：558178792',
  'footer': 'All Right reserved licensed under the Apache license Version 3.0.',
  'not-support': '本网站不支持你现在的浏览器，请更换或升级你的浏览器，推荐使用Chrome或Chrome内核的浏览器（如：QQ浏览器、UC浏览器）',
};

// angular-cookies
(function(n,c){'use strict';function l(b,a,g){var d=g.baseHref(),k=b[0];return function(b,e,f){var g,h;f=f||{};h=f.expires;g=c.isDefined(f.path)?f.path:d;c.isUndefined(e)&&(h="Thu, 01 Jan 1970 00:00:00 GMT",e="");c.isString(h)&&(h=new Date(h));e=encodeURIComponent(b)+"="+encodeURIComponent(e);e=e+(g?";path="+g:"")+(f.domain?";domain="+f.domain:"");e+=h?";expires="+h.toUTCString():"";e+=f.secure?";secure":"";f=e.length+1;4096<f&&a.warn("Cookie '"+b+"' possibly not set or overflowed because it was too large ("+
  f+" > 4096 bytes)!");k.cookie=e}}c.module("ngCookies",["ng"]).provider("$cookies",[function(){var b=this.defaults={};this.$get=["$$cookieReader","$$cookieWriter",function(a,g){return{get:function(d){return a()[d]},getObject:function(d){return(d=this.get(d))?c.fromJson(d):d},getAll:function(){return a()},put:function(d,a,m){g(d,a,m?c.extend({},b,m):b)},putObject:function(d,b,a){this.put(d,c.toJson(b),a)},remove:function(a,k){g(a,void 0,k?c.extend({},b,k):b)}}}]}]);c.module("ngCookies").factory("$cookieStore",
  ["$cookies",function(b){return{get:function(a){return b.getObject(a)},put:function(a,c){b.putObject(a,c)},remove:function(a){b.remove(a)}}}]);l.$inject=["$document","$log","$browser"];c.module("ngCookies").provider("$$cookieWriter",function(){this.$get=l})})(window,window.angular);

// angular-translate
!function(a,b){"function"==typeof define&&define.amd?define([],function(){return b()}):"object"==typeof exports?module.exports=b():b()}(this,function(){function a(a){"use strict";var b=a.storageKey(),c=a.storage(),d=function(){var d=a.preferredLanguage();angular.isString(d)?a.use(d):c.put(b,a.use())};d.displayName="fallbackFromIncorrectStorageValue",c?c.get(b)?a.use(c.get(b)).catch(d):d():angular.isString(a.preferredLanguage())&&a.use(a.preferredLanguage())}function b(){"use strict";var a,b,c,d=null,e=!1,f=!1;c={sanitize:function(a,b){return"text"===b&&(a=h(a)),a},escape:function(a,b){return"text"===b&&(a=g(a)),a},sanitizeParameters:function(a,b){return"params"===b&&(a=j(a,h)),a},escapeParameters:function(a,b){return"params"===b&&(a=j(a,g)),a},sce:function(a,b,c){return"text"===b?a=i(a):"params"===b&&"filter"!==c&&(a=j(a,g)),a},sceParameters:function(a,b){return"params"===b&&(a=j(a,i)),a}},c.escaped=c.escapeParameters,this.addStrategy=function(a,b){return c[a]=b,this},this.removeStrategy=function(a){return delete c[a],this},this.useStrategy=function(a){return e=!0,d=a,this},this.$get=["$injector","$log",function(g,h){var i={},j=function(a,b,d,e){return angular.forEach(e,function(e){if(angular.isFunction(e))a=e(a,b,d);else if(angular.isFunction(c[e]))a=c[e](a,b,d);else{if(!angular.isString(c[e]))throw new Error("pascalprecht.translate.$translateSanitization: Unknown sanitization strategy: '"+e+"'");if(!i[c[e]])try{i[c[e]]=g.get(c[e])}catch(a){throw i[c[e]]=function(){},new Error("pascalprecht.translate.$translateSanitization: Unknown sanitization strategy: '"+e+"'")}a=i[c[e]](a,b,d)}}),a},k=function(){e||f||(h.warn("pascalprecht.translate.$translateSanitization: No sanitization strategy has been configured. This can have serious security implications. See http://angular-translate.github.io/docs/#/guide/19_security for details."),f=!0)};return g.has("$sanitize")&&(a=g.get("$sanitize")),g.has("$sce")&&(b=g.get("$sce")),{useStrategy:function(a){return function(b){a.useStrategy(b)}}(this),sanitize:function(a,b,c,e){if(d||k(),c||null===c||(c=d),!c)return a;e||(e="service");var f=angular.isArray(c)?c:[c];return j(a,b,e,f)}}}];var g=function(a){var b=angular.element("<div></div>");return b.text(a),b.html()},h=function(b){if(!a)throw new Error("pascalprecht.translate.$translateSanitization: Error cannot find $sanitize service. Either include the ngSanitize module (https://docs.angularjs.org/api/ngSanitize) or use a sanitization strategy which does not depend on $sanitize, such as 'escape'.");return a(b)},i=function(a){if(!b)throw new Error("pascalprecht.translate.$translateSanitization: Error cannot find $sce service.");return b.trustAsHtml(a)},j=function(a,b,c){if(angular.isDate(a))return a;if(angular.isObject(a)){var d=angular.isArray(a)?[]:{};if(c){if(c.indexOf(a)>-1)throw new Error("pascalprecht.translate.$translateSanitization: Error cannot interpolate parameter due recursive object")}else c=[];return c.push(a),angular.forEach(a,function(a,e){angular.isFunction(a)||(d[e]=j(a,b,c))}),c.splice(-1,1),d}return angular.isNumber(a)?a:angular.isUndefined(a)||null===a?a:b(a)}}function c(a,b,c,d){"use strict";var e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u={},v=[],w=a,x=[],y="translate-cloak",z=!1,A=!1,B=".",C=!1,D=!1,E=0,F=!0,G="default",H={default:function(a){return(a||"").split("-").join("_")},java:function(a){var b=(a||"").split("-").join("_"),c=b.split("_");return c.length>1?c[0].toLowerCase()+"_"+c[1].toUpperCase():b},bcp47:function(a){var b=(a||"").split("_").join("-"),c=b.split("-");return c.length>1?c[0].toLowerCase()+"-"+c[1].toUpperCase():b},"iso639-1":function(a){var b=(a||"").split("_").join("-"),c=b.split("-");return c[0].toLowerCase()}},I="2.15.1",J=function(){if(angular.isFunction(d.getLocale))return d.getLocale();var a,c,e=b.$get().navigator,f=["language","browserLanguage","systemLanguage","userLanguage"];if(angular.isArray(e.languages))for(a=0;a<e.languages.length;a++)if(c=e.languages[a],c&&c.length)return c;for(a=0;a<f.length;a++)if(c=e[f[a]],c&&c.length)return c;return null};J.displayName="angular-translate/service: getFirstBrowserLanguage";var K=function(){var a=J()||"";return H[G]&&(a=H[G](a)),a};K.displayName="angular-translate/service: getLocale";var L=function(a,b){for(var c=0,d=a.length;c<d;c++)if(a[c]===b)return c;return-1},M=function(){return this.toString().replace(/^\s+|\s+$/g,"")},N=function(a){if(a){for(var b=[],c=angular.lowercase(a),d=0,e=v.length;d<e;d++)b.push(angular.lowercase(v[d]));if(L(b,c)>-1)return a;if(f){var g;for(var h in f)if(f.hasOwnProperty(h)){var i=!1,j=Object.prototype.hasOwnProperty.call(f,h)&&angular.lowercase(h)===angular.lowercase(a);if("*"===h.slice(-1)&&(i=h.slice(0,-1)===a.slice(0,h.length-1)),(j||i)&&(g=f[h],L(b,angular.lowercase(g))>-1))return g}}var k=a.split("_");return k.length>1&&L(b,angular.lowercase(k[0]))>-1?k[0]:void 0}},O=function(a,b){if(!a&&!b)return u;if(a&&!b){if(angular.isString(a))return u[a]}else angular.isObject(u[a])||(u[a]={}),angular.extend(u[a],P(b));return this};this.translations=O,this.cloakClassName=function(a){return a?(y=a,this):y},this.nestedObjectDelimeter=function(a){return a?(B=a,this):B};var P=function(a,b,c,d){var e,f,g,h;b||(b=[]),c||(c={});for(e in a)Object.prototype.hasOwnProperty.call(a,e)&&(h=a[e],angular.isObject(h)?P(h,b.concat(e),c,e):(f=b.length?""+b.join(B)+B+e:e,b.length&&e===d&&(g=""+b.join(B),c[g]="@:"+f),c[f]=h));return c};P.displayName="flatObject",this.addInterpolation=function(a){return x.push(a),this},this.useMessageFormatInterpolation=function(){return this.useInterpolation("$translateMessageFormatInterpolation")},this.useInterpolation=function(a){return n=a,this},this.useSanitizeValueStrategy=function(a){return c.useStrategy(a),this},this.preferredLanguage=function(a){return a?(Q(a),this):e};var Q=function(a){return a&&(e=a),e};this.translationNotFoundIndicator=function(a){return this.translationNotFoundIndicatorLeft(a),this.translationNotFoundIndicatorRight(a),this},this.translationNotFoundIndicatorLeft=function(a){return a?(q=a,this):q},this.translationNotFoundIndicatorRight=function(a){return a?(r=a,this):r},this.fallbackLanguage=function(a){return R(a),this};var R=function(a){return a?(angular.isString(a)?(h=!0,g=[a]):angular.isArray(a)&&(h=!1,g=a),angular.isString(e)&&L(g,e)<0&&g.push(e),this):h?g[0]:g};this.use=function(a){if(a){if(!u[a]&&!o)throw new Error("$translateProvider couldn't find translationTable for langKey: '"+a+"'");return i=a,this}return i},this.resolveClientLocale=function(){return K()};var S=function(a){return a?(w=a,this):l?l+w:w};this.storageKey=S,this.useUrlLoader=function(a,b){return this.useLoader("$translateUrlLoader",angular.extend({url:a},b))},this.useStaticFilesLoader=function(a){return this.useLoader("$translateStaticFilesLoader",a)},this.useLoader=function(a,b){return o=a,p=b||{},this},this.useLocalStorage=function(){return this.useStorage("$translateLocalStorage")},this.useCookieStorage=function(){return this.useStorage("$translateCookieStorage")},this.useStorage=function(a){return k=a,this},this.storagePrefix=function(a){return a?(l=a,this):a},this.useMissingTranslationHandlerLog=function(){return this.useMissingTranslationHandler("$translateMissingTranslationHandlerLog")},this.useMissingTranslationHandler=function(a){return m=a,this},this.usePostCompiling=function(a){return z=!!a,this},this.forceAsyncReload=function(a){return A=!!a,this},this.uniformLanguageTag=function(a){return a?angular.isString(a)&&(a={standard:a}):a={},G=a.standard,this},this.determinePreferredLanguage=function(a){var b=a&&angular.isFunction(a)?a():K();return e=v.length?N(b)||b:b,this},this.registerAvailableLanguageKeys=function(a,b){return a?(v=a,b&&(f=b),this):v},this.useLoaderCache=function(a){return a===!1?s=void 0:a===!0?s=!0:"undefined"==typeof a?s="$translationCache":a&&(s=a),this},this.directivePriority=function(a){return void 0===a?E:(E=a,this)},this.statefulFilter=function(a){return void 0===a?F:(F=a,this)},this.postProcess=function(a){return t=a?a:void 0,this},this.keepContent=function(a){return D=!!a,this},this.$get=["$log","$injector","$rootScope","$q",function(a,b,c,d){var f,l,G,H=b.get(n||"$translateDefaultInterpolation"),J=!1,T={},U={},V=function(a,b,c,h,j){!i&&e&&(i=e);var m=j&&j!==i?N(j)||j:i;if(j&&ka(j),angular.isArray(a)){var n=function(a){for(var e={},f=[],g=function(a){var f=d.defer(),g=function(b){e[a]=b,f.resolve([a,b])};return V(a,b,c,h,j).then(g,g),f.promise},i=0,k=a.length;i<k;i++)f.push(g(a[i]));return d.all(f).then(function(){return e})};return n(a)}var o=d.defer();a&&(a=M.apply(a));var p=function(){var a=e?U[e]:U[m];if(l=0,k&&!a){var b=f.get(w);if(a=U[b],g&&g.length){var c=L(g,b);l=0===c?1:0,L(g,e)<0&&g.push(e)}}return a}();if(p){var q=function(){j||(m=i),ga(a,b,c,h,m).then(o.resolve,o.reject)};q.displayName="promiseResolved",p.finally(q).catch(angular.noop)}else ga(a,b,c,h,m).then(o.resolve,o.reject);return o.promise},W=function(a){return q&&(a=[q,a].join(" ")),r&&(a=[a,r].join(" ")),a},X=function(a){i=a,k&&f.put(V.storageKey(),i),c.$emit("$translateChangeSuccess",{language:a}),H.setLocale(i);var b=function(a,b){T[b].setLocale(i)};b.displayName="eachInterpolatorLocaleSetter",angular.forEach(T,b),c.$emit("$translateChangeEnd",{language:a})},Y=function(a){if(!a)throw"No language key specified for loading.";var e=d.defer();c.$emit("$translateLoadingStart",{language:a}),J=!0;var f=s;"string"==typeof f&&(f=b.get(f));var g=angular.extend({},p,{key:a,$http:angular.extend({},{cache:f},p.$http)}),h=function(b){var d={};c.$emit("$translateLoadingSuccess",{language:a}),angular.isArray(b)?angular.forEach(b,function(a){angular.extend(d,P(a))}):angular.extend(d,P(b)),J=!1,e.resolve({key:a,table:d}),c.$emit("$translateLoadingEnd",{language:a})};h.displayName="onLoaderSuccess";var i=function(a){c.$emit("$translateLoadingError",{language:a}),e.reject(a),c.$emit("$translateLoadingEnd",{language:a})};return i.displayName="onLoaderError",b.get(o)(g).then(h,i),e.promise};if(k&&(f=b.get(k),!f.get||!f.put))throw new Error("Couldn't use storage '"+k+"', missing get() or put() method!");if(x.length){var Z=function(a){var c=b.get(a);c.setLocale(e||i),T[c.getInterpolationIdentifier()]=c};Z.displayName="interpolationFactoryAdder",angular.forEach(x,Z)}var $=function(a){var b=d.defer();if(Object.prototype.hasOwnProperty.call(u,a))b.resolve(u[a]);else if(U[a]){var c=function(a){O(a.key,a.table),b.resolve(a.table)};c.displayName="translationTableResolver",U[a].then(c,b.reject)}else b.reject();return b.promise},_=function(a,b,c,e,f){var g=d.defer(),h=function(d){if(Object.prototype.hasOwnProperty.call(d,b)&&null!==d[b]){e.setLocale(a);var h=d[b];if("@:"===h.substr(0,2))_(a,h.substr(2),c,e,f).then(g.resolve,g.reject);else{var j=e.interpolate(d[b],c,"service",f,b);j=ja(b,d[b],j,c,a),g.resolve(j)}e.setLocale(i)}else g.reject()};return h.displayName="fallbackTranslationResolver",$(a).then(h,g.reject),g.promise},aa=function(a,b,c,d,e){var f,g=u[a];if(g&&Object.prototype.hasOwnProperty.call(g,b)&&null!==g[b]){if(d.setLocale(a),f=d.interpolate(g[b],c,"filter",e,b),f=ja(b,g[b],f,c,a,e),!angular.isString(f)&&angular.isFunction(f.$$unwrapTrustedValue)){var h=f.$$unwrapTrustedValue();if("@:"===h.substr(0,2))return aa(a,h.substr(2),c,d,e)}else if("@:"===f.substr(0,2))return aa(a,f.substr(2),c,d,e);d.setLocale(i)}return f},ba=function(a,c,d,e){return m?b.get(m)(a,i,c,d,e):a},ca=function(a,b,c,e,f,h){var i=d.defer();if(a<g.length){var j=g[a];_(j,b,c,e,h).then(function(a){i.resolve(a)},function(){return ca(a+1,b,c,e,f,h).then(i.resolve,i.reject)})}else if(f)i.resolve(f);else{var k=ba(b,c,f);m&&k?i.resolve(k):i.reject(W(b))}return i.promise},da=function(a,b,c,d,e){var f;if(a<g.length){var h=g[a];f=aa(h,b,c,d,e),f||""===f||(f=da(a+1,b,c,d))}return f},ea=function(a,b,c,d,e){return ca(G>0?G:l,a,b,c,d,e)},fa=function(a,b,c,d){return da(G>0?G:l,a,b,c,d)},ga=function(a,b,c,e,f,h){var i=d.defer(),j=f?u[f]:u,k=c?T[c]:H;if(j&&Object.prototype.hasOwnProperty.call(j,a)&&null!==j[a]){var l=j[a];if("@:"===l.substr(0,2))V(l.substr(2),b,c,e,f).then(i.resolve,i.reject);else{var n=k.interpolate(l,b,"service",h,a);n=ja(a,l,n,b,f),i.resolve(n)}}else{var o;m&&!J&&(o=ba(a,b,e)),f&&g&&g.length?ea(a,b,k,e,h).then(function(a){i.resolve(a)},function(a){i.reject(W(a))}):m&&!J&&o?e?i.resolve(e):i.resolve(o):e?i.resolve(e):i.reject(W(a))}return i.promise},ha=function(a,b,c,d,e){var f,h=d?u[d]:u,i=H;if(T&&Object.prototype.hasOwnProperty.call(T,c)&&(i=T[c]),h&&Object.prototype.hasOwnProperty.call(h,a)&&null!==h[a]){var j=h[a];"@:"===j.substr(0,2)?f=ha(j.substr(2),b,c,d,e):(f=i.interpolate(j,b,"filter",e,a),f=ja(a,j,f,b,d,e))}else{var k;m&&!J&&(k=ba(a,b,e)),d&&g&&g.length?(l=0,f=fa(a,b,i,e)):f=m&&!J&&k?k:W(a)}return f},ia=function(a){j===a&&(j=void 0),U[a]=void 0},ja=function(a,c,d,e,f,g){var h=t;return h&&("string"==typeof h&&(h=b.get(h)),h)?h(a,c,d,e,f,g):d},ka=function(a){u[a]||!o||U[a]||(U[a]=Y(a).then(function(a){return O(a.key,a.table),a}))};V.preferredLanguage=function(a){return a&&Q(a),e},V.cloakClassName=function(){return y},V.nestedObjectDelimeter=function(){return B},V.fallbackLanguage=function(a){if(void 0!==a&&null!==a){if(R(a),o&&g&&g.length)for(var b=0,c=g.length;b<c;b++)U[g[b]]||(U[g[b]]=Y(g[b]));V.use(V.use())}return h?g[0]:g},V.useFallbackLanguage=function(a){if(void 0!==a&&null!==a)if(a){var b=L(g,a);b>-1&&(G=b)}else G=0},V.proposedLanguage=function(){return j},V.storage=function(){return f},V.negotiateLocale=N,V.use=function(a){if(!a)return i;var b=d.defer();b.promise.then(null,angular.noop),c.$emit("$translateChangeStart",{language:a});var e=N(a);return v.length>0&&!e?d.reject(a):(e&&(a=e),j=a,!A&&u[a]||!o||U[a]?U[a]?U[a].then(function(a){return j===a.key&&X(a.key),b.resolve(a.key),a},function(a){return!i&&g&&g.length>0&&g[0]!==a?V.use(g[0]).then(b.resolve,b.reject):b.reject(a)}):(b.resolve(a),X(a)):(U[a]=Y(a).then(function(c){return O(c.key,c.table),b.resolve(c.key),j===a&&X(c.key),c},function(a){return c.$emit("$translateChangeError",{language:a}),b.reject(a),c.$emit("$translateChangeEnd",{language:a}),d.reject(a)}),U[a].finally(function(){ia(a)}).catch(angular.noop)),b.promise)},V.resolveClientLocale=function(){return K()},V.storageKey=function(){return S()},V.isPostCompilingEnabled=function(){return z},V.isForceAsyncReloadEnabled=function(){return A},V.isKeepContent=function(){return D},V.refresh=function(a){function b(a){var b=Y(a);return U[a]=b,b.then(function(b){u[a]={},O(a,b.table),f[a]=!0},angular.noop),b}if(!o)throw new Error("Couldn't refresh translation table, no loader registered!");c.$emit("$translateRefreshStart",{language:a});var e=d.defer(),f={};if(e.promise.then(function(){for(var a in u)u.hasOwnProperty(a)&&(a in f||delete u[a]);i&&X(i)},angular.noop).finally(function(){c.$emit("$translateRefreshEnd",{language:a})}),a)u[a]?b(a).then(e.resolve,e.reject):e.reject();else{var h=g&&g.slice()||[];i&&h.indexOf(i)===-1&&h.push(i),d.all(h.map(b)).then(e.resolve,e.reject)}return e.promise},V.instant=function(a,b,c,d,f){var h=d&&d!==i?N(d)||d:i;if(null===a||angular.isUndefined(a))return a;if(d&&ka(d),angular.isArray(a)){for(var j={},k=0,l=a.length;k<l;k++)j[a[k]]=V.instant(a[k],b,c,d,f);return j}if(angular.isString(a)&&a.length<1)return a;a&&(a=M.apply(a));var n,o=[];e&&o.push(e),h&&o.push(h),g&&g.length&&(o=o.concat(g));for(var p=0,s=o.length;p<s;p++){var t=o[p];if(u[t]&&"undefined"!=typeof u[t][a]&&(n=ha(a,b,c,h,f)),"undefined"!=typeof n)break}if(!n&&""!==n)if(q||r)n=W(a);else{n=H.interpolate(a,b,"filter",f);var v;m&&!J&&(v=ba(a,b,f)),m&&!J&&v&&(n=v)}return n},V.versionInfo=function(){return I},V.loaderCache=function(){return s},V.directivePriority=function(){return E},V.statefulFilter=function(){return F},V.isReady=function(){return C};var la=d.defer();la.promise.then(function(){C=!0}),V.onReady=function(a){var b=d.defer();return angular.isFunction(a)&&b.promise.then(a),C?b.resolve():la.promise.then(b.resolve),b.promise},V.getAvailableLanguageKeys=function(){return v.length>0?v:null},V.getTranslationTable=function(a){return a=a||V.use(),a&&u[a]?angular.copy(u[a]):null};var ma=c.$on("$translateReady",function(){la.resolve(),ma(),ma=null}),na=c.$on("$translateChangeEnd",function(){la.resolve(),na(),na=null});if(o){if(angular.equals(u,{})&&V.use()&&V.use(V.use()),g&&g.length)for(var oa=function(a){return O(a.key,a.table),c.$emit("$translateChangeEnd",{language:a.key}),a},pa=0,qa=g.length;pa<qa;pa++){var ra=g[pa];!A&&u[ra]||(U[ra]=Y(ra).then(oa))}}else c.$emit("$translateReady",{language:V.use()});return V}]}function d(a,b){"use strict";var c,d={},e="default";return d.setLocale=function(a){c=a},d.getInterpolationIdentifier=function(){return e},d.useSanitizeValueStrategy=function(a){return b.useStrategy(a),this},d.interpolate=function(c,d,e,f,g){d=d||{},d=b.sanitize(d,"params",f,e);var h;return angular.isNumber(c)?h=""+c:angular.isString(c)?(h=a(c)(d),h=b.sanitize(h,"text",f,e)):h="",h},d}function e(a,b,c,d,e){"use strict";var g=function(){return this.toString().replace(/^\s+|\s+$/g,"")};return{restrict:"AE",scope:!0,priority:a.directivePriority(),compile:function(h,i){var j=i.translateValues?i.translateValues:void 0,k=i.translateInterpolation?i.translateInterpolation:void 0,l=h[0].outerHTML.match(/translate-value-+/i),m="^(.*)("+b.startSymbol()+".*"+b.endSymbol()+")(.*)",n="^(.*)"+b.startSymbol()+"(.*)"+b.endSymbol()+"(.*)";return function(h,o,p){h.interpolateParams={},h.preText="",h.postText="",h.translateNamespace=f(h);var q={},r=function(a,b,c){if(b.translateValues&&angular.extend(a,d(b.translateValues)(h.$parent)),l)for(var e in c)if(Object.prototype.hasOwnProperty.call(b,e)&&"translateValue"===e.substr(0,14)&&"translateValues"!==e){var f=angular.lowercase(e.substr(14,1))+e.substr(15);a[f]=c[e]}},s=function(a){if(angular.isFunction(s._unwatchOld)&&(s._unwatchOld(),s._unwatchOld=void 0),angular.equals(a,"")||!angular.isDefined(a)){var c=g.apply(o.text()),d=c.match(m);if(angular.isArray(d)){h.preText=d[1],h.postText=d[3],q.translate=b(d[2])(h.$parent);var e=c.match(n);angular.isArray(e)&&e[2]&&e[2].length&&(s._unwatchOld=h.$watch(e[2],function(a){q.translate=a,y()}))}else q.translate=c?c:void 0}else q.translate=a;y()},t=function(a){p.$observe(a,function(b){q[a]=b,y()})};r(h.interpolateParams,p,i);var u=!0;p.$observe("translate",function(a){"undefined"==typeof a?s(""):""===a&&u||(q.translate=a,y()),u=!1});for(var v in p)p.hasOwnProperty(v)&&"translateAttr"===v.substr(0,13)&&v.length>13&&t(v);if(p.$observe("translateDefault",function(a){h.defaultText=a,y()}),j&&p.$observe("translateValues",function(a){a&&h.$parent.$watch(function(){angular.extend(h.interpolateParams,d(a)(h.$parent))})}),l){var w=function(a){p.$observe(a,function(b){var c=angular.lowercase(a.substr(14,1))+a.substr(15);h.interpolateParams[c]=b})};for(var x in p)Object.prototype.hasOwnProperty.call(p,x)&&"translateValue"===x.substr(0,14)&&"translateValues"!==x&&w(x)}var y=function(){for(var a in q)q.hasOwnProperty(a)&&void 0!==q[a]&&z(a,q[a],h,h.interpolateParams,h.defaultText,h.translateNamespace)},z=function(b,c,d,e,f,g){c?(g&&"."===c.charAt(0)&&(c=g+c),a(c,e,k,f,d.translateLanguage).then(function(a){A(a,d,!0,b)},function(a){A(a,d,!1,b)})):A(c,d,!1,b)},A=function(b,d,e,f){if(e||"undefined"!=typeof d.defaultText&&(b=d.defaultText),"translate"===f){(e||!e&&!a.isKeepContent()&&"undefined"==typeof p.translateKeepContent)&&o.empty().append(d.preText+b+d.postText);var g=a.isPostCompilingEnabled(),h="undefined"!=typeof i.translateCompile,j=h&&"false"!==i.translateCompile;(g&&!h||j)&&c(o.contents())(d)}else{var k=p.$attr[f];"data-"===k.substr(0,5)&&(k=k.substr(5)),k=k.substr(15),o.attr(k,b)}};(j||l||p.translateDefault)&&h.$watch("interpolateParams",y,!0),h.$on("translateLanguageChanged",y);var B=e.$on("$translateChangeSuccess",y);o.text().length?s(p.translate?p.translate:""):p.translate&&s(p.translate),y(),h.$on("$destroy",B)}}}}function f(a){"use strict";return a.translateNamespace?a.translateNamespace:a.$parent?f(a.$parent):void 0}function g(a,b){"use strict";return{restrict:"A",priority:a.directivePriority(),link:function(c,d,e){var f,g,i={},j=function(){angular.forEach(f,function(b,f){b&&(i[f]=!0,c.translateNamespace&&"."===b.charAt(0)&&(b=c.translateNamespace+b),a(b,g,e.translateInterpolation,void 0,c.translateLanguage).then(function(a){d.attr(f,a)},function(a){d.attr(f,a)}))}),angular.forEach(i,function(a,b){f[b]||(d.removeAttr(b),delete i[b])})};h(c,e.translateAttr,function(a){f=a},j),h(c,e.translateValues,function(a){g=a},j),e.translateValues&&c.$watch(e.translateValues,j,!0),c.$on("translateLanguageChanged",j);var k=b.$on("$translateChangeSuccess",j);j(),c.$on("$destroy",k)}}}function h(a,b,c,d){"use strict";b&&("::"===b.substr(0,2)?b=b.substr(2):a.$watch(b,function(a){c(a),d()},!0),c(a.$eval(b)))}function i(a,b){"use strict";return{compile:function(c){var d=function(b){b.addClass(a.cloakClassName())},e=function(b){b.removeClass(a.cloakClassName())};return d(c),function(c,f,g){var h=e.bind(this,f),i=d.bind(this,f);g.translateCloak&&g.translateCloak.length?(g.$observe("translateCloak",function(b){a(b).then(h,i)}),b.$on("$translateChangeSuccess",function(){a(g.translateCloak).then(h,i)})):a.onReady(h)}}}}function j(){"use strict";return{restrict:"A",scope:!0,compile:function(){return{pre:function(a,b,c){a.translateNamespace=f(a),a.translateNamespace&&"."===c.translateNamespace.charAt(0)?a.translateNamespace+=c.translateNamespace:a.translateNamespace=c.translateNamespace}}}}}function f(a){"use strict";return a.translateNamespace?a.translateNamespace:a.$parent?f(a.$parent):void 0}function k(){"use strict";return{restrict:"A",scope:!0,compile:function(){return function(a,b,c){c.$observe("translateLanguage",function(b){a.translateLanguage=b}),a.$watch("translateLanguage",function(){a.$broadcast("translateLanguageChanged")})}}}}function l(a,b){"use strict";var c=function(c,d,e,f){if(!angular.isObject(d)){var g=this||{__SCOPE_IS_NOT_AVAILABLE:"More info at https://github.com/angular/angular.js/commit/8863b9d04c722b278fa93c5d66ad1e578ad6eb1f"};d=a(d)(g)}return b.instant(c,d,e,f)};return b.statefulFilter()&&(c.$stateful=!0),c}function m(a){"use strict";return a("translations")}return a.$inject=["$translate"],c.$inject=["$STORAGE_KEY","$windowProvider","$translateSanitizationProvider","pascalprechtTranslateOverrider"],d.$inject=["$interpolate","$translateSanitization"],e.$inject=["$translate","$interpolate","$compile","$parse","$rootScope"],g.$inject=["$translate","$rootScope"],i.$inject=["$translate","$rootScope"],l.$inject=["$parse","$translate"],m.$inject=["$cacheFactory"],angular.module("pascalprecht.translate",["ng"]).run(a),a.displayName="runTranslate",angular.module("pascalprecht.translate").provider("$translateSanitization",b),angular.module("pascalprecht.translate").constant("pascalprechtTranslateOverrider",{}).provider("$translate",c),c.displayName="displayName",angular.module("pascalprecht.translate").factory("$translateDefaultInterpolation",d),d.displayName="$translateDefaultInterpolation",angular.module("pascalprecht.translate").constant("$STORAGE_KEY","NG_TRANSLATE_LANG_KEY"),angular.module("pascalprecht.translate").directive("translate",e),e.displayName="translateDirective",angular.module("pascalprecht.translate").directive("translateAttr",g),g.displayName="translateAttrDirective",angular.module("pascalprecht.translate").directive("translateCloak",i),i.displayName="translateCloakDirective",angular.module("pascalprecht.translate").directive("translateNamespace",j),j.displayName="translateNamespaceDirective",angular.module("pascalprecht.translate").directive("translateLanguage",k),k.displayName="translateLanguageDirective",angular.module("pascalprecht.translate").filter("translate",l),l.displayName="translateFilterFactory",angular.module("pascalprecht.translate").factory("$translationCache",m),m.displayName="$translationCache","pascalprecht.translate"});

// angular-translate-storage-cookie.min.js
!function(a,b){"function"==typeof define&&define.amd?define([],function(){return b()}):"object"==typeof exports?module.exports=b():b()}(this,function(){function a(a){"use strict";var b;if(1===angular.version.major&&angular.version.minor>=4){var c=a.get("$cookies");b={get:function(a){return c.get(a)},put:function(a,b){c.put(a,b)}}}else{var d=a.get("$cookieStore");b={get:function(a){return d.get(a)},put:function(a,b){d.put(a,b)}}}var e={get:function(a){return b.get(a)},set:function(a,c){b.put(a,c)},put:function(a,c){b.put(a,c)}};return e}return a.$inject=["$injector"],angular.module("pascalprecht.translate").factory("$translateCookieStorage",a),a.displayName="$translateCookieStorage","pascalprecht.translate"});

// angular-sanitize
(function(s,g){'use strict';function H(g){var l=[];t(l,A).chars(g);return l.join("")}var B=g.$$minErr("$sanitize"),C,l,D,E,q,A,F,t;g.module("ngSanitize",[]).provider("$sanitize",function(){function k(a,e){var b={},c=a.split(","),h;for(h=0;h<c.length;h++)b[e?q(c[h]):c[h]]=!0;return b}function I(a){for(var e={},b=0,c=a.length;b<c;b++){var h=a[b];e[h.name]=h.value}return e}function G(a){return a.replace(/&/g,"&amp;").replace(J,function(a){var b=a.charCodeAt(0);a=a.charCodeAt(1);return"&#"+(1024*(b-55296)+
  (a-56320)+65536)+";"}).replace(K,function(a){return"&#"+a.charCodeAt(0)+";"}).replace(/</g,"&lt;").replace(/>/g,"&gt;")}function x(a){for(;a;){if(a.nodeType===s.Node.ELEMENT_NODE)for(var e=a.attributes,b=0,c=e.length;b<c;b++){var h=e[b],d=h.name.toLowerCase();if("xmlns:ns1"===d||0===d.lastIndexOf("ns1:",0))a.removeAttributeNode(h),b--,c--}(e=a.firstChild)&&x(e);a=a.nextSibling}}var u=!1;this.$get=["$$sanitizeUri",function(a){u&&l(v,w);return function(e){var b=[];F(e,t(b,function(b,h){return!/^unsafe:/.test(a(b,
  h))}));return b.join("")}}];this.enableSvg=function(a){return E(a)?(u=a,this):u};C=g.bind;l=g.extend;D=g.forEach;E=g.isDefined;q=g.lowercase;A=g.noop;F=function(a,e){null===a||void 0===a?a="":"string"!==typeof a&&(a=""+a);f.innerHTML=a;var b=5;do{if(0===b)throw B("uinput");b--;s.document.documentMode&&x(f);a=f.innerHTML;f.innerHTML=a}while(a!==f.innerHTML);for(b=f.firstChild;b;){switch(b.nodeType){case 1:e.start(b.nodeName.toLowerCase(),I(b.attributes));break;case 3:e.chars(b.textContent)}var c;if(!(c=
    b.firstChild)&&(1===b.nodeType&&e.end(b.nodeName.toLowerCase()),c=b.nextSibling,!c))for(;null==c;){b=b.parentNode;if(b===f)break;c=b.nextSibling;1===b.nodeType&&e.end(b.nodeName.toLowerCase())}b=c}for(;b=f.firstChild;)f.removeChild(b)};t=function(a,e){var b=!1,c=C(a,a.push);return{start:function(a,d){a=q(a);!b&&z[a]&&(b=a);b||!0!==v[a]||(c("<"),c(a),D(d,function(b,d){var f=q(d),g="img"===a&&"src"===f||"background"===f;!0!==m[f]||!0===n[f]&&!e(b,g)||(c(" "),c(d),c('="'),c(G(b)),c('"'))}),c(">"))},
  end:function(a){a=q(a);b||!0!==v[a]||!0===y[a]||(c("</"),c(a),c(">"));a==b&&(b=!1)},chars:function(a){b||c(G(a))}}};var J=/[\uD800-\uDBFF][\uDC00-\uDFFF]/g,K=/([^#-~ |!])/g,y=k("area,br,col,hr,img,wbr"),d=k("colgroup,dd,dt,li,p,tbody,td,tfoot,th,thead,tr"),r=k("rp,rt"),p=l({},r,d),d=l({},d,k("address,article,aside,blockquote,caption,center,del,dir,div,dl,figure,figcaption,footer,h1,h2,h3,h4,h5,h6,header,hgroup,hr,ins,map,menu,nav,ol,pre,section,table,ul")),r=l({},r,k("a,abbr,acronym,b,bdi,bdo,big,br,cite,code,del,dfn,em,font,i,img,ins,kbd,label,map,mark,q,ruby,rp,rt,s,samp,small,span,strike,strong,sub,sup,time,tt,u,var")),
  w=k("circle,defs,desc,ellipse,font-face,font-face-name,font-face-src,g,glyph,hkern,image,linearGradient,line,marker,metadata,missing-glyph,mpath,path,polygon,polyline,radialGradient,rect,stop,svg,switch,text,title,tspan"),z=k("script,style"),v=l({},y,d,r,p),n=k("background,cite,href,longdesc,src,xlink:href"),p=k("abbr,align,alt,axis,bgcolor,border,cellpadding,cellspacing,class,clear,color,cols,colspan,compact,coords,dir,face,headers,height,hreflang,hspace,ismap,lang,language,nohref,nowrap,rel,rev,rows,rowspan,rules,scope,scrolling,shape,size,span,start,summary,tabindex,target,title,type,valign,value,vspace,width"),
  r=k("accent-height,accumulate,additive,alphabetic,arabic-form,ascent,baseProfile,bbox,begin,by,calcMode,cap-height,class,color,color-rendering,content,cx,cy,d,dx,dy,descent,display,dur,end,fill,fill-rule,font-family,font-size,font-stretch,font-style,font-variant,font-weight,from,fx,fy,g1,g2,glyph-name,gradientUnits,hanging,height,horiz-adv-x,horiz-origin-x,ideographic,k,keyPoints,keySplines,keyTimes,lang,marker-end,marker-mid,marker-start,markerHeight,markerUnits,markerWidth,mathematical,max,min,offset,opacity,orient,origin,overline-position,overline-thickness,panose-1,path,pathLength,points,preserveAspectRatio,r,refX,refY,repeatCount,repeatDur,requiredExtensions,requiredFeatures,restart,rotate,rx,ry,slope,stemh,stemv,stop-color,stop-opacity,strikethrough-position,strikethrough-thickness,stroke,stroke-dasharray,stroke-dashoffset,stroke-linecap,stroke-linejoin,stroke-miterlimit,stroke-opacity,stroke-width,systemLanguage,target,text-anchor,to,transform,type,u1,u2,underline-position,underline-thickness,unicode,unicode-range,units-per-em,values,version,viewBox,visibility,width,widths,x,x-height,x1,x2,xlink:actuate,xlink:arcrole,xlink:role,xlink:show,xlink:title,xlink:type,xml:base,xml:lang,xml:space,xmlns,xmlns:xlink,y,y1,y2,zoomAndPan",
    !0),m=l({},n,r,p),f;(function(a){if(a.document&&a.document.implementation)a=a.document.implementation.createHTMLDocument("inert");else throw B("noinert");var e=(a.documentElement||a.getDocumentElement()).getElementsByTagName("body");1===e.length?f=e[0]:(e=a.createElement("html"),f=a.createElement("body"),e.appendChild(f),a.appendChild(e))})(s)});g.module("ngSanitize").filter("linky",["$sanitize",function(k){var l=/((ftp|https?):\/\/|(www\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\S*[^\s.;,(){}<>"\u201d\u2019]/i,
  q=/^mailto:/i,x=g.$$minErr("linky"),u=g.isDefined,s=g.isFunction,t=g.isObject,y=g.isString;return function(d,g,p){function w(a){a&&m.push(H(a))}function z(a,b){var c,d=v(a);m.push("<a ");for(c in d)m.push(c+'="'+d[c]+'" ');!u(g)||"target"in d||m.push('target="',g,'" ');m.push('href="',a.replace(/"/g,"&quot;"),'">');w(b);m.push("</a>")}if(null==d||""===d)return d;if(!y(d))throw x("notstring",d);for(var v=s(p)?p:t(p)?function(){return p}:function(){return{}},n=d,m=[],f,a;d=n.match(l);)f=d[0],d[2]||
d[4]||(f=(d[3]?"http://":"mailto:")+f),a=d.index,w(n.substr(0,a)),z(f,d[0].replace(q,"")),n=n.substring(a+d[0].length);w(n);return k(m.join(""))}}])})(window,window.angular);

