/**
 * 树形列表JS
 *
 * @Author ShaoFan
 */
(function ($) {
    $.extend({
        bs_treegrid: {
            _option: {},
            oGridInit: function (parms) {//初始化表单
                $.bs_treegrid._option = parms;
                var oGridInit = new Object();
                //初始化Grid
                oGridInit.Init = function () {
                    $.bs_treegrid.refresh();
                };
                return oGridInit;
            },
            selectColumns: function (column) {
                return $.map($("#dataTable").bootstrapTable("getSelections"), function (row) {
                    return row[column]
                })
            },
            selectFirstColumns: function () {
                return $('#grid').treegrid("getSelected")
            },
            refresh: function () {
                var opts = $('#grid').treegrid("options");
                opts.url = $.bs_treegrid._option.dataUrl + "?parentId=0";
                $('#grid').treegrid("load", bindTreegridMeta2Form("grid", "queryForm"));
                opts.url = $.bs_treegrid._option.dataUrl;
            }
        },
        operate: {
            submit: function (url, type, dataType, data) {
                $.modal.loading("正在处理中，请稍后...");
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    data: data,
                    success: function (result) {
                        $.operate.ajaxSuccess(result)
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        $.modal.alertError(textStatus);
                        $.modal.closeLoading();

                    }
                };
                $.ajax(config)
            },
            add: function (id) {
                var url = $.common.isEmpty(id) ? $.bs_treegrid._option.createUrl : $.bs_treegrid._option.createUrl.replace("{id}", id);
                $.modal.open("添加" + $.bs_treegrid._option.modalName, url);
            },
            edit: function () {
                var row = $.bs_treegrid.selectFirstColumns();
                if (null == row) {
                    $.modal.alertWarning('请选中一条数据');
                    return;
                }
                var url = $.bs_treegrid._option.updateUrl.replace("{id}", row.id);
                $.modal.open("修改" + $.bs_treegrid._option.modalName, url);
            },
            view: function (id) {
                var url = '';
                if($.common.isEmpty(id)){
                    var row = $.bs_treegrid.selectFirstColumns();
                    if (null == row) {
                        $.modal.alertWarning('请选中一条数据');
                        return;
                    }
                    url = $.bs_treegrid._option.viewUrl.replace("{id}", row.id);
                }else{
                    url = $.bs_treegrid._option.viewUrl.replace("{id}", id);
                }

                $.modal.open("查看" + $.bs_treegrid._option.modalName, url);
            },
            split: function () {
                var row = $.bs_treegrid.selectFirstColumns();
                if (null == row) {
                    $.modal.alertWarning('请选中一条数据');
                    return;
                }
                if(row.parentId !=0){
                    $.modal.alertWarning('不能拆分');
                    return;
                }
                var url = $.bs_treegrid._option.splitUrl.replace("{id}", row.id);
                $.modal.open("拆分" + $.bs_treegrid._option.modalName, url);
            },
            changeStatus: function(status){
                var row = $.bs_treegrid.selectFirstColumns();
                if (null == row) {
                    $.modal.alertWarning('请选中一条数据');
                    return;
                }
                var opType = "";
                if (status == 1) {
                    opType = "enable";
                }
                if (status == 2) {
                    opType = "disable";
                }
                var data = {id: row.id, state: status, opType: opType};
                $.operate.submit($.bs_treegrid._option.changeStatusUrl, "post", "json", data);
            },
            remove: function () {
                var row = $.bs_treegrid.selectFirstColumns();
                if (null == row) {
                    $.modal.alertWarning('请选中一条数据');
                    return;
                }
                $.modal.confirm("确定删除该条" + $.bs_treegrid._option.modalName + "信息吗？", function () {
                    var url = $.bs_treegrid._option.removeUrl.replace("{id}", row.id);
                    var data = {
                        "id": row.id
                    };
                    $.operate.submit(url, "post", "json", data);
                })
            },
            batRemove: function () {
                var rows = $.common.isEmpty($.bs_treegrid._option.id) ? $.bs_treegrid.selectFirstColumns() : $.bs_treegrid.selectColumns($.bs_treegrid._option.id);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function () {
                    var url = $.bs_treegrid._option.removeUrl;
                    var data = {
                        "ids": rows.join()
                    };
                    $.operate.submit(url, "post", "json", data);
                })
            },
            ajaxSuccess: function (result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgSuccess(result.message);
                    $.bs_treegrid.refresh();
                } else {
                    $.modal.alertError(result.message);
                }
                $.modal.closeLoading();
            },
            saveSuccess: function (result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS)
                } else {
                    $.modal.alertError(result.message);
                }
                $.modal.closeLoading();
            }, // post请求传输
            post: function (url, data, callback) {
                $.operate.submit(url, "post", "json", data, callback);
            },
            // get请求传输
            get: function (url, callback) {
                $.operate.submit(url, "get", "json", "", callback);
            }
            //其他方法END
        },
        modal: {//模态弹窗
            icon: function (type) {
                var icon = "";
                if (type == modal_status.WARNING) {
                    icon = 'warning';
                } else {
                    if (type == modal_status.SUCCESS) {
                        icon = 'success';
                    } else {
                        if (type == modal_status.FAIL) {
                            icon = 'error';
                        } else {
                            icon = 'info';
                        }
                    }
                }
                return icon
            },
            msg: function (content, type) {
                if (type != undefined) {
                    bs4pop.alert(content, {
                        type: $.modal.icon(type)
                    })
                } else {
                    bs4pop.alert(content)
                }
            },
            msgError: function (content) {
                $.modal.msg(content, modal_status.FAIL)
            },
            msgSuccess: function (content) {
                $.modal.msg(content, modal_status.SUCCESS)
            },
            msgWarning: function (content) {
                $.modal.msg(content, modal_status.WARNING)
            },
            alert: function (content, type) {
                bs4pop.alert(content, {
                    type: $.modal.icon(type),
                    title: "系统提示",
                    btns: [{
                        label: '确认', className: 'btn btn-primary', onClick(e) {
                        }
                    }],
                })
            },
            msgReload: function (msg, type) {
                bs4pop.alert(msg, {type: $.modal.icon(type)}, function () {
                    $.modal.reload();
                });
            },
            alertError: function (content) {
                $.modal.alert(content, modal_status.FAIL);
            },
            alertSuccess: function (content) {
                $.modal.alert(content, modal_status.SUCCESS);
            },
            alertWarning: function (content) {
                $.modal.alert(content, modal_status.WARNING);
            },
            close: function () {
                parent.dia.hide();
            },
            confirm: function (content, callBack) {
                bs4pop.confirm(content, {title: "系统提示"}, function (sure) {
                    if(sure){
                        callBack(true);
                    }
                });
            },
            open: function (title, url, width, height) {
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = "auto";
                    height = "auto";
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                dia = bs4pop.dialog({
                    title: title,
                    content: url,
                    isIframe: true,
                    closeBtn: false,
                    backdrop: 'static',
                    width: width + "px",
                    height: height + "px",
                    btns: []
                });

            },
            openFull: function (title, url, width, height) {
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = "auto";
                    height = "auto"
                }
                if ($.common.isEmpty(title)) {
                    title = false
                }
                if ($.common.isEmpty(url)) {
                    url = "404.html"
                }
                if ($.common.isEmpty(width)) {
                    width = 800
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50)
                }
                $.modal.msg("暂未实现");
            },
            loading: function (message) {
                $.blockUI({
                    message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + "</div>"
                })
            },
            closeLoading: function () {
                setTimeout(function () {
                    $.unblockUI()
                }, 50)
            },
            reload: function () {
                parent.location.reload()
            }
        },
        // 通用方法封装处理
        common: {
            // 判断字符串是否为空
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "") {
                    return true;
                }
                return false;
            },
            // 判断一个字符串是否为非空串
            isNotEmpty: function (value) {
                return !$.common.isEmpty(value);
            },
            // 空对象转字符串
            nullToStr: function (value) {
                if ($.common.isEmpty(value)) {
                    return "-";
                }
                return value;
            },
            // 是否显示数据 为空默认为显示
            visible: function (value) {
                if ($.common.isEmpty(value) || value == true) {
                    return true;
                }
                return false;
            },
            // 空格截取
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            // 比较两个字符串（大小写敏感）
            equals: function (str, that) {
                return str == that;
            },
            // 比较两个字符串（大小写不敏感）
            equalsIgnoreCase: function (str, that) {
                return String(str).toUpperCase() === String(that).toUpperCase();
            },
            // 将字符串按指定字符分割
            split: function (str, sep, maxLen) {
                if ($.common.isEmpty(str)) {
                    return null;
                }
                var value = String(str).split(sep);
                return maxLen ? value.slice(0, maxLen - 1) : value;
            },
            // 字符串格式化(%s )
            sprintf: function (str) {
                var args = arguments, flag = true, i = 1;
                str = str.replace(/%s/g, function () {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return '';
                    }
                    return arg;
                });
                return flag ? str : '';
            },
            // 指定随机数返回
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            // 判断字符串是否是以start开头
            startWith: function (value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            // 判断字符串是否是以end结尾
            endWith: function (value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            },
            // 数组去重
            uniqueFn: function (array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i < array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i]);
                    }
                }
                return result;
            },
            // 数组中的所有元素放入一个字符串
            join: function (array, separator) {
                if ($.common.isEmpty(array)) {
                    return null;
                }
                return array.join(separator);
            },
            // 获取form下所有的字段并转换为json对象
            formToJSON: function (formId) {
                var json = {};
                $.each($("#" + formId).serializeArray(), function (i, field) {
                    json[field.name] = field.value;
                });
                return json;
            }
        },
        form: {
            selectCheckeds: function (name) {
                var checkeds = "";
                $('input:checkbox[name="' + name + '"]:checked').each(function (i) {
                    if (0 == i) {
                        checkeds = $(this).val()
                    } else {
                        checkeds += ("," + $(this).val())
                    }
                });
                return checkeds
            },
            selectSelects: function (name) {
                var selects = "";
                $("#" + name + " option:selected").each(function (i) {
                    if (0 == i) {
                        selects = $(this).val()
                    } else {
                        selects += ("," + $(this).val())
                    }
                });
                return selects
            }
        }

    });

})(jQuery);
web_status = {
    SUCCESS: 200,
    FAIL: 500
};
modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};
