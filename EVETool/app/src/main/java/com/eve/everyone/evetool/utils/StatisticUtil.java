package com.eve.everyone.evetool.utils;

import android.content.Context;

import com.baidu.mobstat.StatService;

/**
 * Created by Kavli on 2015/11/19.
 */
public class StatisticUtil {
    public static final int[] TEST_ENTERPRISE_ID = new int[]{1, 11}; //测试帐号enterprise_id
    //public static final int[] TEST_ENTERPRISE_ID = new int[]{}; //测试帐号enterprise_id

    public static final String BAIDU_EVENT_ID_LOAD_ACTIVITY_TRIGGLE_BTN = "load_activity_triggle_btn";//商户登录查看密码
    public static final String BAIDU_EVENT_ID_LOAD_ACTIVITY_BTN = "load_activity_btn";//商户登录按钮
    public static final String BAIDU_EVENT_ID_EM_OPEN_MENU = "em_main_open_menu";//打开侧栏菜单
    public static final String BAIDU_EVENT_ID_EM_CLICK_OPEN_MENU = "em_main_click_open_menu";//点击左上角图标打开菜单
    public static final String BAIDU_EVENT_ID_EM_MENU_PHONE_SEARCH = "em_main_menu_phone_search";//号码搜索侧栏菜单
    public static final String BAIDU_EVENT_ID_EM_MENU_RECORD = "em_main_menu_record";//店铺记录侧栏菜单
    public static final String BAIDU_EVENT_ID_EM_MENU_SALES_KIT = "em_main_menu_sales_kit";//销售简报侧栏菜单
    public static final String BAIDU_EVENT_ID_EM_MENU_UPGRADE = "em_main_menu_upgrade";//系统升级侧栏菜单
    public static final String BAIDU_EVENT_ID_EM_MENU_EXIT = "em_main_menu_exit";//退出系统侧栏菜单
    public static final String BAIDU_EVENT_ID_PHONE_SEARCH_NUMBER = "phone_search_numbers";//号码搜索数字键集合
    public static final String BAIDU_EVENT_ID_PHONE_SEARCH_DEL = "phone_search_del";//号码搜索删除键
    public static final String BAIDU_EVENT_ID_PHONE_SEARCH_CLR = "phone_search_clr";//号码搜索清空键
    public static final String BAIDU_EVENT_ID_PHONE_SEARCH_CREATE = "phone_search_create";//号码搜索创建用户键
    public static final String BAIDU_EVENT_ID_PHONE_SEARCH_FIND = "phone_search_find";//号码搜索搜索用户键
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_POINT = "em_record_point";//商户积分记录页面显示
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_AMOUNT = "em_record_amount";//商户储值消费记录页面显示
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_COUPON = "em_record_coupon";//商户优惠券记录页面显示
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_TAP_POINT = "em_record_tap_point";//商户积分记录tap
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_TAP_AMOUNT = "em_record_tap_amount";//商户储值消费记录tap
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_TAP_COUPON = "em_record_tap_coupon";//商户优惠券记录tap
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_ITEM_POINT = "em_record_item_point";//商户积分记录条目
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_ITEM_AMOUNT = "em_record_item_amount";//商户储值消费记录条目
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_ITEM_COUPON = "em_record_item_coupon";//商户优惠券记录条目
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_REFRESH_POINT = "em_record_refresh_point";//商户积分记录下拉刷新
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_REFRESH_AMOUNT = "em_record_refresh_amount";//商户储值消费记录下拉刷新
    public static final String BAIDU_EVENT_ID_ENTERPRISE_RECORD_REFRESH_COUPON = "em_record_refresh_coupon";//商户优惠券记录下拉刷新
    public static final String BAIDU_EVENT_ID_ENTERPRISE_SALE_KIT_REFRESH = "em_sale_kit_refresh";//销售简报下拉刷新
    public static final String BAIDU_EVENT_ID_ENTERPRISE_UPGRADE_BUTTON = "em_upgrade_button";//立即升级按钮
    public static final String BAIDU_EVENT_ID_ENTERPRISE_UPGRADE_REFRESH = "em_upgrade_refresh";//系统升级下拉刷新
    public static final String BAIDU_EVENT_ID_CUSTOMER_COUPON_REFRESH = "customer_coupon_list_refresh";//优惠券列表刷新
    public static final String BAIDU_EVENT_ID_CUSTOMER_COUPON_USE = "customer_coupon_use";//优惠券使用
    public static final String BAIDU_EVENT_ID_CUSTOMER_RECORD_POINT = "customer_record_point";//顾客积分记录页面显示
    public static final String BAIDU_EVENT_ID_CUSTOMER_RECORD_AMOUNT = "customer_record_amount";//顾客储值消费记录页面显示
    public static final String BAIDU_EVENT_ID_CUSTOMER_RECORD_TAP_POINT = "customer_record_tap_point";//顾客积分记录tap
    public static final String BAIDU_EVENT_ID_CUSTOMER_RECORD_TAP_AMOUNT = "customer_record_tap_amount";//顾客储值消费记录tap
    public static final String BAIDU_EVENT_ID_CUSTOMER_RECORD_REFRESH_POINT = "customer_record_refresh_point";//顾客积分记录下拉刷新
    public static final String BAIDU_EVENT_ID_CUSTOMER_RECORD_REFRESH_AMOUNT = "customer_record_refresh_amount";//顾客储值消费记录下拉刷新
    public static final String BAIDU_EVENT_ID_CUSTOMER_EDIT_INFO_SAVE = "customer_edit_info_save";//顾客信息修改保存
    public static final String BAIDU_EVENT_ID_CUSTOMER_EDIT_INFO_CANCEL = "customer_edit_info_cancel";//顾客信息修改取消
    public static final String BAIDU_EVENT_ID_CUSTOMER_QR_DIALOG_REFRESH = "customer_qr_dialog_refresh";//顾客二维码刷新
    public static final String BAIDU_EVENT_ID_USER_INFO_ITEM_EDIT_INFO = "user_info_item_modify";//用户信息资料修改条目
    public static final String BAIDU_EVENT_ID_USER_INFO_ITEM_EXCHARGE_RECORD = "info_item_excharge_record";//用户信息积分兑换记录条目
    public static final String BAIDU_EVENT_ID_USER_INFO_ITEM_AMOUNT_RECOUR = "info_item_amount_record";//用户信息储值消费条目
    public static final String BAIDU_EVENT_ID_USER_MAIN_CLICK_RET = "user_main_click_ret";//左上角返回按钮返回
    public static final String BAIDU_EVENT_ID_USER_MAIN_PRESS_RET = "user_main_press_ret";//顾客界面系统返回按键
    public static final String BAIDU_EVENT_ID_USER_MAIN_CLICK_TICKET = "user_main_click_ticket";//按钮进入优惠券
    public static final String BAIDU_EVENT_ID_USER_MAIN_DIALOG_TICKET = "user_main_dialog_ticket";//弹窗进入优惠券
    public static final String BAIDU_EVENT_ID_USER_MAIN_QR_CODE = "user_main_qr_code";//点击头像弹二维码
    public static final String BAIDU_EVENT_ID_USER_MAIN_USER_INFO = "user_main_user_info";//点击用户信息
    public static final String BAIDU_EVENT_ID_USER_MAIN_SLIDER_BTN_POINT = "user_main_slider_btn_point";//积分操作滑块
    public static final String BAIDU_EVENT_ID_USER_MAIN_SLIDER_BTN_AMOUNT = "user_main_slider_btn_amount";//储值消费操作滑块
    public static final String BAIDU_EVENT_ID_USER_MAIN_PRESENT_DEC = "user_main_present_dec";//赠送积分减按钮
    public static final String BAIDU_EVENT_ID_USER_MAIN_PRESENT_INC = "user_main_present_inc";//赠送积分加按钮
    public static final String BAIDU_EVENT_ID_USER_MAIN_PRESENT_EDIT = "user_main_present_edit";//赠送积分编辑点击
    public static final String BAIDU_EVENT_ID_USER_MAIN_PRESENT_BTN = "user_main_present_btn";//赠送积分按钮
    public static final String BAIDU_EVENT_ID_USER_MAIN_PRESENT_EXCHARGE_BTN = "user_main_present_excge_btn";//兑换积分跳转按钮
    public static final String BAIDU_EVENT_ID_USER_MAIN_CONSUME_DISCOUNT_CB = "consume_discount_cb";//折扣优惠选中取消
    public static final String BAIDU_EVENT_ID_USER_MAIN_CONSUME_POINT_CB = "consume_point_cb";//赠送积分选中取消
    public static final String BAIDU_EVENT_ID_USER_MAIN_CONSUME_VAL_EDIT = "consume_val_edit";//折扣编辑
    public static final String BAIDU_EVENT_ID_USER_MAIN_CONSUME_DISCOUNT_EDIT = "consume_discount_edit";//折扣编辑
    public static final String BAIDU_EVENT_ID_USER_MAIN_CONSUME_BTN = "consume_btn";//确认支付按钮
    public static final String BAIDU_EVENT_ID_USER_MAIN_CONSUME_RECHARGE_BTN = "consume_recharge_btn";//充值跳转按钮
    public static final String BAIDU_EVENT_ID_EXCHARGE_DEC_BTN = "excharge_dec_btn";//积分兑换减按钮
    public static final String BAIDU_EVENT_ID_EXCHARGE_INC_BTN = "excharge_inc_btn";//积分兑换加按钮
    public static final String BAIDU_EVENT_ID_EXCHARGE_VAL_EDIT = "recharge_val_edit";//兑换积分值编辑
    public static final String BAIDU_EVENT_ID_EXCHARGE_QUICK_BTN1 = "recharge_quick_btn1";//1倍快速兑换积分按钮
    public static final String BAIDU_EVENT_ID_EXCHARGE_QUICK_BTN2 = "recharge_quick_btn2";//2倍快速兑换积分按钮
    public static final String BAIDU_EVENT_ID_EXCHARGE_QUICK_BTN3 = "recharge_quick_btn3";//3倍快速兑换积分按钮
    public static final String BAIDU_EVENT_ID_EXCHARGE_BTN = "recharge_btn";//兑换积分按钮
    public static final String BAIDU_EVENT_ID_RECHARGE_VAL_EDIT = "recharge_val_edit";//充值金额编辑
    public static final String BAIDU_EVENT_ID_RECHARGE_PRESENT = "recharge_present";//充值赠送勾选取消
    public static final String BAIDU_EVENT_ID_RECHARGE_BTN = "recharge_check";//确认充值按钮

    public static final String BAIDU_EVENT_ID_FACE_LOGIN_CLICK = "face_login_click";//点击人脸登录
    public static final String BAIDU_EVENT_ID_FACE_LOGIN2_CLICK = "face_login2_click";//点击人脸绑定
    public static final String BAIDU_EVENT_ID_FACE_LOGIN_1_N = "face_login_1_n";//通过人脸登录成功
    public static final String BAIDU_EVENT_ID_FACE_LOGIN_1_1 = "face_login_1_1";//通过人脸绑定成功登录
    public static final String BAIDU_EVENT_ID_FACE_CAMERA_SEL = "face_camera_sel";//人脸登录切换摄像头


    public static final String BAIDU_PAGE_NAME_APP_UPGRADE = "APP升级页面";
    public static final String BAIDU_PAGE_NAME_COUPON_RECORD = "商户页面优惠券使用记录";
    public static final String BAIDU_PAGE_NAME_CREDITS_EXCHARGE = "商户页面积分记录";
    public static final String BAIDU_PAGE_NAME_STORED_CONSUME = "商户页面储值消费记录";
    public static final String BAIDU_PAGE_NAME_CUSTOMER_EXCHARGE = "顾客页面积分记录";
    public static final String BAIDU_PAGE_NAME_CUSTOMER_CONSUME = "顾客页面储值记录";
    public static final String BAIDU_PAGE_NAME_SALE_KIT = "销售简报页面";
    public static final String BAIDU_PAGE_NAME_PHNONE_SEARCH = "用户号码查询页面";

    public static boolean isNotTestEnterpriseId(long id) {
        for (int i = 0; i < TEST_ENTERPRISE_ID.length; i++) {
            if (id == TEST_ENTERPRISE_ID[i]) {
                return false;
            }
        }
        return true;
    }

    public static void onEvent(Context context, long enterpriseId, String eventId, String label) {
        if (isNotTestEnterpriseId(enterpriseId)) {
            StatService.onEvent(context, eventId, label);
        }
    }

    public static void onPageStart(Context context, long enterpriseId, String pageId) {
        if (isNotTestEnterpriseId(enterpriseId)) {
            StatService.onPageStart(context, pageId);
        }
    }

    public static void onPageEnd(Context context, long enterpriseId, String pageId) {
        if (isNotTestEnterpriseId(enterpriseId)) {
            StatService.onPageEnd(context, pageId);
        }
    }

    public static void onPause(Context context, long enterpriseId) {
        if (isNotTestEnterpriseId(enterpriseId)) {
            StatService.onPause(context);
        }
    }

    public static void onResume(Context context, long enterpriseId) {
        if (isNotTestEnterpriseId(enterpriseId)) {
            StatService.onResume(context);
        }
    }
}
