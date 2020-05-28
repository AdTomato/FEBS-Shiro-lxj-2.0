package cc.mrbird.febs.lxj.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.taobao.api.ApiException;

import java.util.List;

/**
 * @Author:wangyong
 * @Date:2020/4/17 13:48
 * @Description: 考勤数据
 */
public interface AttendanceService {

    String getAccessToken() throws ApiException;

    OapiAttendanceListRecordResponse getAttendanceListRecord(List<String> ids, String startTime, String endTime, String accessToken) throws ApiException;

    String getUserIdByMobile(String mobile, String accessToken) throws ApiException;

    /**
     * 处理考勤数据
     *
     * @param attendanceData 考勤数据
     * @author wangyong
     */
    void dealWithAttendanceData(JSONObject attendanceData);

}
