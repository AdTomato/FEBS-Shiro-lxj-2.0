package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.lxj.entity.Attendance;
import cc.mrbird.febs.lxj.params.AttendanceDetailParams;
import cc.mrbird.febs.lxj.service.AttendanceService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackGetCallBackRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiCallBackGetCallBackResponse;
import com.taobao.api.ApiException;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Author:wangyong
 * @Date:2020/4/17 14:02
 * @Description: 考勤
 */
@RestController
@RequestMapping("/controller/attendance")
public class AttendanceController extends BaseController {

    @Autowired
    AttendanceService attendanceService;

    @GetMapping("/attendance")
    public Object getAttendance() throws ApiException {
        String accessToken = attendanceService.getAccessToken();
        String userId = attendanceService.getUserIdByMobile("13084042075", accessToken);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        String startTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE) + " 00:00:00";
        calendar.add(Calendar.DATE, 3);
        String endTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE) + " 00:00:00";
        OapiAttendanceListRecordResponse attendanceListRecord = attendanceService.getAttendanceListRecord(Arrays.asList(userId), startTime, endTime, accessToken);
        return attendanceListRecord;
    }
    /**
     * @author lfh
     * @Description  考勤明细数据
     * @Date 2020/6/3 17:40
     * @throws
     * @param attendanceDetailParams
     * @param request
     * @return {@link FebsResponse}
     **/
    @PostMapping("/attendanceDetailList")
    public FebsResponse attendanceDetailList(@RequestBody(required = false) AttendanceDetailParams attendanceDetailParams, QueryRequest request){
        Map<String,Object> dataTable  = getDataTable(attendanceService.getAttendanceDetailList(attendanceDetailParams,request));
        return new FebsResponse().success().data(dataTable);
    }
    /**
     * @author lfh
     * @Description  人员每日考勤时长
     * @Date 2020/6/3 17:40
     * @throws
     * @param attendanceDetailParams
     * @param request
     * @return {@link FebsResponse}
     **/
    @PostMapping("/personalAttendance")
    public FebsResponse personalAttendance(@RequestBody AttendanceDetailParams attendanceDetailParams, QueryRequest request){
        Map<String,Object> dataTable  = getDataTable(attendanceService.getPersonalAttendance(attendanceDetailParams,request));
        return new FebsResponse().success().data(dataTable);
    }
    @PostMapping("/personalAttendanceDetail")
    public FebsResponse personalAttendanceDetail(@RequestBody AttendanceDetailParams attendanceDetailParams, QueryRequest request){
        Map<String,Object> dataTable  = getDataTable(attendanceService.getPersonalAttendanceDetail(attendanceDetailParams,request));
        return new FebsResponse().success().data(dataTable);
    }
    @GetMapping("/test")
    public Object test() throws ApiException {
        String accessToken = attendanceService.getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/get_call_back");
        OapiCallBackGetCallBackRequest request = new OapiCallBackGetCallBackRequest();
        request.setHttpMethod("GET");
        OapiCallBackGetCallBackResponse response = client.execute(request,accessToken);
        return response;
    }

}
