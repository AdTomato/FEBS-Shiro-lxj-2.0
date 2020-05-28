package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.lxj.service.AttendanceService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackGetCallBackRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiCallBackGetCallBackResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author:wangyong
 * @Date:2020/4/17 14:02
 * @Description: 考勤
 */
@RestController
@RequestMapping("/controller/attendance")
public class AttendanceController {

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
