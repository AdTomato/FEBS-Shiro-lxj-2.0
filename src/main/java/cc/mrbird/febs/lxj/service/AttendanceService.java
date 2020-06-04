package cc.mrbird.febs.lxj.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.lxj.entity.PersonalAttendance;
import cc.mrbird.febs.lxj.entity.PersonalAttendanceDetail;
import cc.mrbird.febs.lxj.entity.ReturnAttendance;
import cc.mrbird.febs.lxj.entity.ReturnPersonalAttendance;
import cc.mrbird.febs.lxj.params.AttendanceDetailParams;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
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


    IPage<ReturnAttendance> getAttendanceDetailList(AttendanceDetailParams attendanceDetailParams, QueryRequest request);

    IPage<ReturnPersonalAttendance> getPersonalAttendance(AttendanceDetailParams attendanceDetailParams, QueryRequest request);

    IPage<PersonalAttendanceDetail> getPersonalAttendanceDetail(AttendanceDetailParams attendanceDetailParams, QueryRequest request);
}
