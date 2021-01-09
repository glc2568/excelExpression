//package com.excel.readandwrite;
//
//import com.bsteel.scs.common.utils.StringUtils;
//import net.sf.json.JSONObject;
//
//import java.util.HashMap;
//
///**
// * 操作消息提醒
// *
// * @author bsteel
// */
//public class AjaxResult extends HashMap<String, Object> {
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * 状态码
//     */
//    public static final String CODE_TAG = "code";
//
//    /**
//     * 返回内容
//     */
//    public static final String MSG_TAG = "msg";
//
//    /**
//     * 数据对象
//     */
//    public static final String DATA_TAG = "data";
//
//    /**
//     * 成功描述
//     */
//    public static final String SUCCESS_MSG = "操作成功";
//
//    /**
//     * 失败描述
//     */
//    public static final String ERROR_MSG = "操作失败";
//
//    /**
//     * 状态类型
//     */
//    public enum Type {
//
//        /**
//         * 成功
//         */
//        SUCCESS(0),
//        /**
//         * api成功
//         */
//        API_SUCCESS(1),
//        /**
//         * 警告
//         */
//        WARN(301),
//        /**
//         * 错误
//         */
//        ERROR(500);
//
//        private final int value;
//
//        Type(int value) {
//            this.value = value;
//        }
//
//        public int value() {
//            return this.value;
//        }
//    }
//
//    /**
//     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
//     */
//    public AjaxResult() {
//    }
//
//    /**
//     * 初始化一个新创建的 AjaxResult 对象
//     *
//     * @param type 状态类型
//     * @param msg  返回内容
//     */
//    public AjaxResult(Type type, String msg) {
//        super.put(CODE_TAG, type.value);
//        super.put(MSG_TAG, msg);
//    }
//
//    /**
//     * 初始化一个新创建的 AjaxResult 对象
//     *
//     * @param type 状态类型
//     * @param msg  返回内容
//     * @param data 数据对象
//     */
//    public AjaxResult(Type type, String msg, Object data) {
//        super.put(CODE_TAG, type.value);
//        super.put(MSG_TAG, msg);
//        if (StringUtils.isNotNull(data)) {
//            super.put(DATA_TAG, data);
//        }
//    }
//
//    /**
//     * 返回成功消息
//     *
//     * @return 成功消息
//     */
//    public static AjaxResult success() {
//        return success(SUCCESS_MSG, null);
//    }
//
//    /**
//     * Api返回成功消息
//     *
//     * @return 成功消息
//     */
//    public static AjaxResult apiSuccessMsg() {
//        return apiSuccess(SUCCESS_MSG, null);
//    }
//
//    /**
//     * API返回成功数据
//     *
//     * @return 成功消息
//     */
//    public static String apiSuccess(Object data) {
//        AjaxResult success = apiSuccess(SUCCESS_MSG, data);
//        return StringUtils.replaceNullValue(JSONObject.fromObject(success));
//    }
//
//    /**
//     * API返回成功数据
//     *
//     * @return 成功消息
//     */
//    public static String apiSuccess() {
//        return JSONObject.fromObject(apiSuccessMsg()).toString();
//    }
//
//    /**
//     * 返回成功数据
//     *
//     * @return 成功消息
//     */
//    public static AjaxResult success(Object data) {
//        return AjaxResult.success(SUCCESS_MSG, data);
//    }
//
//    /**
//     * 返回成功数据
//     *
//     * @return 成功消息
//     */
//    public static AjaxResult success(String msg) {
//        return AjaxResult.success(msg, null);
//    }
//
//    /**
//     * 返回成功消息
//     *
//     * @param msg  返回内容
//     * @param data 数据对象
//     * @return 成功消息
//     */
//    public static AjaxResult success(String msg, Object data) {
//        return new AjaxResult(Type.SUCCESS, msg, data);
//    }
//
//    /**
//     * API 返回成功消息
//     *
//     * @param msg  返回内容
//     * @param data 数据对象
//     * @return 成功消息
//     */
//    public static AjaxResult apiSuccess(String msg, Object data) {
//        return new AjaxResult(Type.API_SUCCESS, msg, data);
//    }
//
//    /**
//     * 返回警告消息
//     *
//     * @param msg 返回内容
//     * @return 警告消息
//     */
//    public static AjaxResult warn(String msg) {
//        return AjaxResult.warn(msg, null);
//    }
//
//    /**
//     * 返回警告消息
//     *
//     * @param msg  返回内容
//     * @param data 数据对象
//     * @return 警告消息
//     */
//    public static AjaxResult warn(String msg, Object data) {
//        return new AjaxResult(Type.WARN, msg, data);
//    }
//
//    /**
//     * 返回警告消息(返回String)
//     *
//     * @param msg 返回内容
//     * @return 操作错误消息
//     */
//    public static String apiWarn(String msg) {
//        return JSONObject.fromObject(warn(msg, null)).toString();
//    }
//
//    /**
//     * 返回错误消息
//     *
//     * @return
//     */
//    public static String apiError() {
//        return JSONObject.fromObject(error()).toString();
//    }
//
//    /**
//     * 返回错误消息
//     *
//     * @return
//     */
//    public static AjaxResult error() {
//        return AjaxResult.error(ERROR_MSG);
//    }
//
//    /**
//     * 返回错误消息
//     *
//     * @param msg 返回内容
//     * @return 错误消息
//     */
//    public static AjaxResult error(String msg) {
//        return AjaxResult.error(msg, null);
//    }
//
//    /**
//     * 返回操作错误消息
//     *
//     * @param msg 返回内容
//     * @return 操作错误消息
//     */
//    public static AjaxResult openError(String msg) {
//        return AjaxResult.openError(msg, null);
//    }
//
//    /**
//     * 返回错误消息
//     *
//     * @param msg  返回内容
//     * @param data 数据对象
//     * @return 错误消息
//     */
//    public static AjaxResult error(String msg, Object data) {
//        return new AjaxResult(Type.ERROR, msg, data);
//    }
//
//    /**
//     * 返回操作错误消息
//     *
//     * @param msg  返回内容
//     * @param data 数据对象
//     * @return 操作错误消息
//     */
//    public static AjaxResult openError(String msg, Object data) {
//        return new AjaxResult(Type.ERROR, msg, data);
//    }
//}
