package com.jkcq.util;



/**
 * Created by huashao on 2018/4/11.
 */

public class MessageEvent {

    public static final String BIND_DEVICE_SUCCESS = "bind_device_success";
    public static final String BIND_DEVICE_SUCCESS_WITH_PROGRESS = "bind_device_success_with_progress";
    public static final String UNBIND_DEVICE_SUCCESS = "unbind_device_success";
    public static final String ADD_DEVICE_FIRST_PRESS = "add_device_first_press";
    public static final String DEVICE_SETTING_FIRST_PRESS = "device_setting_first_press";
    public static final String GET_BIND_DEVICELIST_SUCCESS = "get_bind_devicelist_success";
    public static final String ROPE_DATA_UPGRADE_SUCCESS = "rope_data_upgrade_success";
    public static final String WHEN_CONNECTSTATE_CHANGED = "when_connectstate_changed";
    public static final String UPDATE_SCALE_DATA_SUCCESS = "update_scale_data_success";
    public static final String UPDATE_SLEEP_DATA_SUCCESS = "update_sleep_data_success";
    public static final String UPDATE_WATCH_DATA_SUCCESS = "UPDATE_WATCH_DATA_SUCCESS";
    public static final String UPDATE_BRACELET_DATA_SUCCESS = "UPDATE_BRACELET_DATA_SUCCESS";
    public static final String UPDATE_CLOCKTIME_SUCCESS = "update_clocktime_success";
    public static final String SCALE_HISTORYDATA = "scale_historydata";
    public static final String SPORT_UPDATE_SUCESS = "sport_update_success";
    public static final String UNBIND_DEVICE_SUCCESS_EVENT = "unbind_device_success_event";
    public static final String SHOW_SPORT = "show_sport";
    public static final String SYNC_WATCH_SUCCESS = "sync_watch_success";
    public static final String SYNC_W311_SUCCESS = "sync_w311_success";
    public static final String NEED_LOGIN = "need_login";
    public static final String UPDATE_WATCH_TARGET = "update_watch_target";
    public static final String WeChat_NULL = "wechat_null";
    public static final String EXIT_SCALESCAN = "exit_scalescan";//退出绑定体脂秤页面
    public static final String EXIT_SCALEREALTIME = "exit_scalerealtime";//退出体脂秤称重页面
    public static final String EXIT_SCALECONNECTTING = "exit_scaleconnectting";//退出体脂秤称上称页面
    public static final String isChange = "isChange";
    public static final String viewPageChage = "viewPageChage";
    public static final String DayAdd = "dayAdd";
    public static final String DaySub = "daySub";
    public static final String endHr = "end_hr";
    public static final String measure_end = "measure_end";
    public static final String syncTodayData = "syncTodayData";
    public static final String share = "share";
    public static final String calender = "calender";
    public static final String reconnect_device = "reconnect_device";
    public static final String scale_device_success = "scale_device_success";
    public static final String update_step = "update_step";
    public static final String update_sleep = "update_sleep";
    public static final String update_hr = "update_hr";
    public static final String update_oxygen = "update_oxygen";
    public static final String update_oncehr = "update_oncehr";
    public static final String update_bloodpre = "update_bloodpre";
    public static final String update_temp = "update_temp";
    public static final String update_exercise = "update_exercise";
    public static final String update_location = "update_location";
    public static final String update_location_error = "update_location_error";
    public static final String clikeview_stop = "clikeview_stop";
    public static final String send_dynamic = "send_dynamic";
    public static final String update_dynamic_follow_state = "update_dynamic_follow_state";
    public static final String update_dynamic_like_state = "update_dynamic_like_state";
    public static final String update_dynamic_personal_vedio = "update_dynamic_personal_vedio";
    public static final String show_fragment_community = "show_fragment_community";
    public static final String update_progress = "update_progress";
    public static final String show_fragment_other = "show_fragment_other";
    public static final String del_dynamicId = "del_dynamicId";
    public static final String show_iv_add = "show_iv_add";
    public static final String hide_iv_add = "hide_iv_add";
    public static final String newwork_change = "newwork_change";
    public static final String video_all = "video_all";
    public static final String video_follow = "video_follow";
    public static final String video_start = "video_start";
    public static final String hide_radioButton = "hide_radioButton";
    public static final String show_radioButton = "show_radioButton";
    public static final String all_video_exception = "all_video_exception";
    public static final String follow_video_exception = "follow_video_exception";
    public static final String main_report_update="main_report_update";
    public static final String main_dynicid_update="main_dynicid_update";
    public static final String main_report_like_update="main_report_like_update";

    private Object obj = null;

    private String msg;
    private int eventType;


    public int getEventType() {
        return eventType;
    }


    //构造函数
    public MessageEvent(String msg) {
        this.msg = msg;
    }


    public MessageEvent(Object obj, String msg) {
        this.msg = msg;
        this.obj = obj;
    }

    public MessageEvent(String msg, Object obj) {
        this.msg = msg;
        this.obj = obj;
    }

    public MessageEvent(String msg, int eventType) {
        this.msg = msg;
        this.eventType = eventType;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
