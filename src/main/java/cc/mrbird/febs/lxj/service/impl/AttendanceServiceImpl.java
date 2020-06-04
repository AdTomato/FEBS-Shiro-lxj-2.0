package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.lxj.entity.*;
import cc.mrbird.febs.lxj.mapper.*;
import cc.mrbird.febs.lxj.params.AttendanceDetailParams;
import cc.mrbird.febs.lxj.service.AttendanceService;
import cc.mrbird.febs.lxj.utils.DoubleUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetByMobileRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetByMobileResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author:wangyong
 * @Date:2020/4/17 13:50
 * @Description:
 */
@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Resource
    UserMapper userMapper;  // 用户

    @Resource
    AttendanceMapper attendanceMapper;  // 考勤

    @Resource
    AttendanceMachineMapper attendanceMachineMapper;  // 考勤机

    @Resource
    TeamInfoMapper teamInfoMapper;  // 考勤班组

    @Resource
    PersonalAttendanceMapper personalAttendanceMapper;  // 考勤时长

    @Resource
    PersonalAttendanceDetailMapper personalAttendanceDetailMapper;  // 考勤时长明细

    @Override
    public String getAccessToken() throws ApiException {
        ResourceBundle rb = ResourceBundle.getBundle("dingding");
        String appKey = rb.getString("AppKey");
        String appSecret = rb.getString("AppSecret");
        DefaultDingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        String accessToken = response.getAccessToken();
        return accessToken;
    }

    @Override
    public OapiAttendanceListRecordResponse getAttendanceListRecord(List<String> ids, String startTime, String endTime, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/attendance/listRecord");
        OapiAttendanceListRecordRequest request = new OapiAttendanceListRecordRequest();
        request.setCheckDateFrom(startTime);
        request.setCheckDateTo(endTime);
        request.setUserIds(ids);
        OapiAttendanceListRecordResponse execute = client.execute(request, accessToken);
        return execute;
    }

    @Override
    public String getUserIdByMobile(String mobile, String accessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get_by_mobile");
        OapiUserGetByMobileRequest request = new OapiUserGetByMobileRequest();
        request.setMobile(mobile);

        OapiUserGetByMobileResponse execute = client.execute(request, accessToken);
        return execute.getUserid();
    }

    /**
     * 处理考勤数据
     *
     * @param attendanceData 考勤数据
     * @author wangyong
     */
    @Async
    @Override
    public void dealWithAttendanceData(JSONObject attendanceData) {
        log.info("开始处理考勤数据");
        if (attendanceData == null) {
            log.info("考勤数据为空，不做任何处理");
            return;
        }
        if (attendanceData.containsKey("DataList")) {
            // 存在考勤数据键
            JSONArray dataList = attendanceData.getJSONArray("DataList");
            if (dataList.isEmpty()) {
                // JSONArray为空
                return;
            }
            // 将json数组封装成对象存储
            Attendance attendance = getAttendance(dataList);

            // 获取考勤机信息
            AttendanceMachine attendanceMachine = attendanceMachineMapper.getAttendanceMachineByMac(attendance.getBaseMacAddr().replace(":", ""));

            // 获取考勤班组信息
            TeamInfo teamInfo = teamInfoMapper.getTeamInfoById(attendanceMachine.getTeamInfo());

            // 进行数据的处理
            dealWithAttendanceData(attendance, teamInfo);

            attendanceMapper.insertAttendance(attendance);
            log.info("考勤数据处理完毕");
        }
    }

    @Override
    public IPage<ReturnAttendance> getAttendanceDetailList(AttendanceDetailParams attendanceDetailParams, QueryRequest request) {
        Page<Attendance> page  = new Page<>(request.getPageNum(),request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(attendanceMapper.countAttendanceNum(attendanceDetailParams));
        SortUtil.handlePageSort(request,page,"name", FebsConstant.ORDER_ASC,false);
        return attendanceMapper.getAttendanceDetailList(page,attendanceDetailParams);
    }

    @Override
    public IPage<ReturnPersonalAttendance> getPersonalAttendance(AttendanceDetailParams attendanceDetailParams, QueryRequest request) {
        Page<PersonalAttendance> page  = new Page<>(request.getPageNum(),request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(attendanceMapper.countPersonalAttendanceNum(attendanceDetailParams));
        SortUtil.handlePageSort(request,page,"work_time", FebsConstant.ORDER_ASC,false);
        return attendanceMapper.getPersonalAttendanceList(page,attendanceDetailParams);

    }

    @Override
    public IPage<PersonalAttendanceDetail> getPersonalAttendanceDetail(AttendanceDetailParams attendanceDetailParams, QueryRequest request) {
        Page<PersonalAttendanceDetail> page  = new Page<>(request.getPageNum(),request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(attendanceMapper.countPersonalAttendanceDetailNum(attendanceDetailParams));
        SortUtil.handlePageSort(request,page,"work_time", FebsConstant.ORDER_ASC,false);
        return attendanceMapper.getPersonalAttendanceDetailList(page,attendanceDetailParams);
    }

    /**
     * 将考勤json数据封装成考勤对象
     *
     * @param dataList 考勤json数据
     * @return 考勤对象
     * @author wangyong
     */
    private Attendance getAttendance(JSONArray dataList) {
        JSONObject jsonObject = dataList.getJSONObject(0);
        Attendance attendance = new Attendance();
        // 考勤数据id
        attendance.setId(UUID.randomUUID().toString().replace("-", ""));

        // 考勤数据创建时间
        attendance.setCreatedTime(new Date());

        // 打卡地址
        attendance.setAddress(jsonObject.getString("address"));

        // 考勤机打卡的考勤机MAC地址
        attendance.setBaseMacAddr(jsonObject.getString("baseMacAddr").replace(":", ""));

        // 打卡时间
        Long checkTime = jsonObject.getLong("checkTime");
        if (checkTime != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(checkTime);
            attendance.setCheckTime(calendar.getTime());
        }

        // 企业id
        attendance.setCorpId(jsonObject.getString("corpId"));

        // 经度信息
        attendance.setLatitude(jsonObject.getDouble("latitude"));

        // 打卡id
        attendance.setBizId(jsonObject.getString("bizId"));

        // 打卡方式
        attendance.setLocationMethod(jsonObject.getString("locationMethod"));

        // 考勤机设备名称
        attendance.setDeviceName(jsonObject.getString("deviceName"));

        // 考勤机设备SN号
        attendance.setDeviceSN(jsonObject.getString("deviceSN"));

        // 打卡人钉钉id
        String sourceUserId = jsonObject.getString("userId");
        attendance.setSourceUserId(sourceUserId);

        // 打卡人系统id
        String userId = userMapper.getIdBySourceId(sourceUserId);

        attendance.setUserId(userId);
        log.info("插入打卡人考勤数据：" + attendance);
        return attendance;
    }

    /**
     * 处理考勤数据
     *
     * @param attendance 原始考勤数据
     * @param teamInfo   考勤班组
     * @author wangyong
     */
    private void dealWithAttendanceData(Attendance attendance, TeamInfo teamInfo){
        // 重构今日时间，设置工作时间为年-月-日，不获取时分秒
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //班组打卡时间
        //Calendar startWorkTime = Calendar.getInstance();
        //startWorkTime.setTime(teamInfo.getPunchTime());
        //if (startWorkTime.get(Calendar.get)calendar.get(Calendar.DATE))
        // 根据userid和当天时间获取今日的人员每日考勤时长数据
        log.info(calendar.getTime().toString());
        PersonalAttendance personalAttendanceByWorkTime = personalAttendanceMapper.getPersonalAttendanceByWorkTime(calendar.getTime(),attendance.getUserId());
        if (personalAttendanceByWorkTime == null) {
            // 人员每日考勤时长数据不存在，存在两种情况
            // 1. 加班到第二天凌晨以后
            // 2. 第二天刚开始上班
            calendar.add(Calendar.DATE, -1);
            // 获取上一天的人员每日考勤时长数据
            personalAttendanceByWorkTime = personalAttendanceMapper.getPersonalAttendanceByWorkTime(calendar.getTime(),attendance.getUserId());
            if (personalAttendanceByWorkTime == null) {
                // 前一天没有考勤数据，本次为上班时间
                // 创建今日人员每日考勤时长数据
//                personalAttendanceByWorkTime = new PersonalAttendance();
//                personalAttendanceByWorkTime.setId(UUID.randomUUID().toString().replace("-", ""));
//                personalAttendanceByWorkTime.setUserId(attendance.getUserId());
//                calendar.add(Calendar.DATE, 1);
//                personalAttendanceByWorkTime.setWorkTime(calendar.getTime());
//                personalAttendanceByWorkTime.setDuration(0D);
                calendar.add(Calendar.DATE, 1);
                personalAttendanceByWorkTime = createPersonalAttendance(attendance, calendar);
                log.info("创建今日人员每日考勤时长数据：" + personalAttendanceByWorkTime);
                personalAttendanceMapper.insertPersonalAttendance(personalAttendanceByWorkTime);
                // 创建人员每日考勤时长明细数据
//                PersonalAttendanceDetail personalAttendanceDetail = new PersonalAttendanceDetail();
//                personalAttendanceDetail.setId(UUID.randomUUID().toString().replace("-", ""));
//                personalAttendanceDetail.setPersonalAttendanceId(personalAttendanceByWorkTime.getId());
//                personalAttendanceDetail.setTeamId(teamInfo.getId());
//                personalAttendanceDetail.setStartTime(getWorkTime(teamInfo, attendance.getCheckTime()));
//                personalAttendanceDetail.setEndTime(null);
//                personalAttendanceDetail.setDuration(0D);
//                personalAttendanceDetail.setStatus("正常");
                PersonalAttendanceDetail personalAttendanceDetail = createPersonalAttendanceDetail(attendance, personalAttendanceByWorkTime, teamInfo);
                log.info("创建人员每日考勤时长明细数据：" + personalAttendanceDetail);
                personalAttendanceDetailMapper.insertPersonalAttendanceDetail(personalAttendanceDetail);
            } else {
                //前一天有考勤时长数据
                PersonalAttendanceDetail personalAttendanceDetail = personalAttendanceDetailMapper.getPersonalAttendanceDetailByPersonalAttendanceId(personalAttendanceByWorkTime.getId());
                if (personalAttendanceDetail.getEndTime() != null) {
                    // 本次为上班时间
                    calendar.add(Calendar.DATE, 1);
                    personalAttendanceByWorkTime = createPersonalAttendance(attendance, calendar);
                    log.info("创建今日人员每日考勤时长数据：" + personalAttendanceByWorkTime);
                    personalAttendanceMapper.insertPersonalAttendance(personalAttendanceByWorkTime);

                    personalAttendanceDetail = createPersonalAttendanceDetail(attendance, personalAttendanceByWorkTime, teamInfo);
                    log.info("创建人员每日考勤时长明细数据：" + personalAttendanceDetail);
                    personalAttendanceDetailMapper.insertPersonalAttendanceDetail(personalAttendanceDetail);
                } else {
                    // 上次没有下班，需要判断本次是否为上班时间
                    if (whetherWork(teamInfo, attendance.getCheckTime())) {
                        //在上班前打卡
                        // 上班时间打卡
                        // 本次打卡为当天的上班卡，前一天的打卡为异常打卡，缺少下班卡
                        personalAttendanceDetail.setStatus("异常");
                        log.info("前一天没有打下班卡，更新异常打卡数据");
                        personalAttendanceDetailMapper.updatePersonalAttendanceDetail(personalAttendanceDetail);
                        //创建今日考勤数据
                        // 本次为上班时间
                        calendar.add(Calendar.DATE, 1);
                        personalAttendanceByWorkTime = createPersonalAttendance(attendance, calendar);
                        log.info("创建今日人员每日考勤时长数据：" + personalAttendanceByWorkTime);
                        personalAttendanceMapper.insertPersonalAttendance(personalAttendanceByWorkTime);
                        personalAttendanceDetail = createPersonalAttendanceDetail(attendance, personalAttendanceByWorkTime, teamInfo);
                        log.info("创建人员每日考勤时长明细数据：" + personalAttendanceDetail);
                        personalAttendanceDetailMapper.insertPersonalAttendanceDetail(personalAttendanceDetail);
                    } else {
                        // 上班前时间打卡
                        // 本次打卡为上一天的下班卡
                        log.info("加班到第二天上班前");
                        AttendanceMachine attendanceMachine = attendanceMachineMapper.getAttendanceMachineByMac(attendance.getBaseMacAddr().replace(":", ""));
                        if (attendanceMachine.getTeamInfo().equals(personalAttendanceDetail.getTeamId())) {
                            // 打卡班组相同
                            personalAttendanceDetail.setEndTime(attendance.getCheckTime());
                            personalAttendanceDetail.setDuration(calculationDuration(personalAttendanceDetail.getStartTime(), personalAttendanceDetail.getEndTime()));
                            personalAttendanceDetail.setStatus("正常");
                            // 更新人员每日考勤数据
                        } else {
                            // 打卡班组不同，不是同一个班组
                            personalAttendanceDetail.setStatus("异常");
                            // 更新人员每日考勤数据
                            //创建不同班组的考勤详情数据
                            log.info("创建人员每日考勤时长明细数据：" + personalAttendanceDetail);
                            personalAttendanceDetailMapper.insertPersonalAttendanceDetail(personalAttendanceDetail);
                        }
                        personalAttendanceDetailMapper.updatePersonalAttendanceDetail(personalAttendanceDetail);
                    }

                }
            }
        } else {
            // 存在
            // 根据人员每日考勤时长获取人员考勤时长明细表
            List<PersonalAttendanceDetail> personalAttendanceDetails = personalAttendanceDetailMapper.getPersonalAttendancesDetailByPersonalAttendanceId(personalAttendanceByWorkTime.getId());
            PersonalAttendanceDetail personalAttendanceDetail = personalAttendanceDetails.get(0);

            if (personalAttendanceDetail.getEndTime() != null) {
                // 上一次打卡为下班卡，本次打卡为上班卡
                // 上班卡只需要插入一条考勤数据即可
                PersonalAttendanceDetail p = new PersonalAttendanceDetail();
                p.setId(UUID.randomUUID().toString().replace("-", ""));
                p.setTeamId(teamInfo.getId());
                p.setPersonalAttendanceId(personalAttendanceByWorkTime.getId());
                if ( whetherWork(teamInfo, attendance.getCheckTime())){
                    //上班时间打卡
                    p.setStartTime(attendance.getCheckTime());
                }else {
                    //上班前打卡
                    Calendar teamTime = Calendar.getInstance();
                    Date punchTime = teamInfo.getPunchTime();
                    teamTime.setTime(punchTime);
                    teamTime.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),teamTime.get(Calendar.HOUR_OF_DAY),0,0);
                    p.setStartTime(teamTime.getTime());
                }
                p.setCreatedTime(new Date());
                p.setDuration(0D);
                p.setStatus("正常");
                // 插入考勤数据
                personalAttendanceDetailMapper.insertPersonalAttendanceDetail(p);
            } else {
                // 上一次打卡为上班卡，本次打卡为下班卡
                if (teamInfo.getId().equals(personalAttendanceDetail.getTeamId())) {
                    // 打卡班组相同
                    //判断第二次打卡的时间是否在上班前
                    if (whetherWork(teamInfo,attendance.getCheckTime())){
                        //上班时间后打卡
                        personalAttendanceDetail.setEndTime(attendance.getCheckTime());

                    }else {
                        //上班时间前打卡
                        Calendar teamTime = Calendar.getInstance();
                        Date punchTime = teamInfo.getPunchTime();
                        teamTime.setTime(punchTime);
                        teamTime.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),teamTime.get(Calendar.HOUR_OF_DAY),0,0);
                        personalAttendanceDetail.setEndTime(teamTime.getTime());
                    }
                    personalAttendanceDetail.setDuration(calculationDuration(personalAttendanceDetail.getStartTime(), personalAttendanceDetail.getEndTime()));
                    personalAttendanceDetail.setStatus("正常");
                    personalAttendanceDetailMapper.updatePersonalAttendanceDetail(personalAttendanceDetail);
                    // 更新人员每日考勤数据
                } else {
                    // 打卡班组不同，不是同一个班组
                    personalAttendanceDetail.setStatus("异常");
                    personalAttendanceDetailMapper.updatePersonalAttendanceDetail(personalAttendanceDetail);
                    //在新的班组打卡创建一条数据
                    personalAttendanceDetail = createPersonalAttendanceDetail(attendance, personalAttendanceByWorkTime, teamInfo);
                    log.info("创建人员每日考勤时长明细数据：" + personalAttendanceDetail);
                    personalAttendanceDetailMapper.insertPersonalAttendanceDetail(personalAttendanceDetail);
                    // 更新人员每日考勤数据
                }

            }
        }
    }

    /**
     * 判断是否在上班时间打卡
     *
     * @param teamInfo  考勤班组
     * @param checkDate 打卡时间
     * @return true 上班时间打卡 false 上班时间前打卡
     * @author wangyong
     */
    private boolean whetherWork(TeamInfo teamInfo, Date checkDate) {
        Calendar startWorkTime = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Calendar teamTime = Calendar.getInstance();
        teamTime.setTime(teamInfo.getPunchTime());
        //班组打卡事案件
        startWorkTime.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE),teamTime.get(Calendar.HOUR_OF_DAY),0,0);
        //startWorkTime.setTime(teamInfo.getPunchTime());
        //员工打卡时间
        Calendar checkTime = Calendar.getInstance();
        checkTime.setTime(checkDate);

        return startWorkTime.getTimeInMillis() > checkTime.getTimeInMillis() ? false : true;
    }

    /**
     * 创建人员每日考勤时长明细数据
     *
     * @param attendance         原始打卡数据
     * @param personalAttendance 人员每日考勤时长数据
     * @param teamInfo           班组信息
     * @return 人员每日考勤时长明细数据
     * @author wangyong
     */
    private PersonalAttendanceDetail createPersonalAttendanceDetail(Attendance attendance, PersonalAttendance personalAttendance, TeamInfo teamInfo) {
        PersonalAttendanceDetail personalAttendanceDetail = new PersonalAttendanceDetail();
        personalAttendanceDetail.setId(UUID.randomUUID().toString().replace("-", ""));
        personalAttendanceDetail.setCreatedTime(new Date());
        personalAttendanceDetail.setPersonalAttendanceId(personalAttendance.getId());
        personalAttendanceDetail.setTeamId(teamInfo.getId());
        personalAttendanceDetail.setStartTime(getWorkTime(teamInfo, attendance.getCheckTime()));
        personalAttendanceDetail.setEndTime(null);
        personalAttendanceDetail.setDuration(0D);
        personalAttendanceDetail.setStatus("正常");
        return personalAttendanceDetail;
    }

    /**
     * 创建今日人员每日考勤时长数据
     *
     * @param attendance 打卡原始数据
     * @param calendar   时间
     * @return 今日人员每日考勤时长
     * @author wangyong
     */
    private PersonalAttendance createPersonalAttendance(Attendance attendance, Calendar calendar) {
        PersonalAttendance personalAttendance = new PersonalAttendance();
        personalAttendance.setId(UUID.randomUUID().toString().replace("-", ""));
        personalAttendance.setUserId(attendance.getUserId());
        //calendar.add(Calendar.DATE, 1);
        String deptId = userMapper.getDeptId(attendance.getUserId());
        personalAttendance.setDepartmentId(deptId);
        personalAttendance.setWorkTime(calendar.getTime());
        //personalAttendance.setWorkTime(attendance.getCheckTime());
        personalAttendance.setDuration(0D);
        return personalAttendance;
    }

    /**
     * 计算考勤时长
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时长
     * @author wangyong
     */
    private Double calculationDuration(Date startTime, Date endTime) {
        Double result = 0D;
        //Double second = (startTime.getTime() - endTime.getTime()) / 1000D;
        Double second = (endTime.getTime() - startTime.getTime()) / 1000D;
        result = DoubleUtils.doubleRound(second / 3600D, 2);
        return result;
    }

    /**
     * 根据班组的弹性工作时间去处理上班打卡时间
     *
     * @param teamInfo 班组
     * @param time     上班打卡时间
     * @return 处理后的上班打卡时间
     * @author wangyong
     */
    private Date getWorkTime(TeamInfo teamInfo, Date time) {
        Date result = time;
        Date startWorkTime = teamInfo.getPunchTime();
        Integer flexibleWorkTime = teamInfo.getFlexibleWorkTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startWorkTime);
        Calendar checkCalendar = Calendar.getInstance();
        checkCalendar.setTime(time);

        //班组打卡小时 > 员工打卡时间的小时  或者  班组打卡小时=员工打卡时间的小时 || 班组打卡时间分钟 > 员工打卡时间的分钟
       if (calendar.get(Calendar.HOUR_OF_DAY) > checkCalendar.get(Calendar.HOUR_OF_DAY) || (calendar.get(Calendar.HOUR_OF_DAY) == checkCalendar.get(Calendar.HOUR_OF_DAY)&&calendar.get(Calendar.MINUTE) > checkCalendar.get(Calendar.MINUTE))) {
            // 时
            checkCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            // 分
            checkCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            // 秒
            checkCalendar.set(Calendar.SECOND, 0);
            // 毫秒
            checkCalendar.set(Calendar.MILLISECOND, 0);
            return checkCalendar.getTime();
        }

        boolean flag = true;
       //班组打卡时间后打卡
        while (flag) {
            if (flexibleWorkTime == 0){
                return  checkCalendar.getTime();
            }
            calendar.add(Calendar.MINUTE, flexibleWorkTime);
            if (calendar.get(Calendar.HOUR_OF_DAY) == 0 || calendar.get(Calendar.HOUR_OF_DAY) > checkCalendar.get(Calendar.HOUR_OF_DAY) || calendar.get(Calendar.MINUTE) > checkCalendar.get(Calendar.MINUTE)) {
                calendar.add(Calendar.MINUTE, -flexibleWorkTime);
                // 时
                checkCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
                // 分
                checkCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
                // 秒
                checkCalendar.set(Calendar.SECOND, 0);
                // 毫秒
                checkCalendar.set(Calendar.MILLISECOND, 0);
                result = checkCalendar.getTime();
                flag = false;
            }
        }
        return result;
    }

    /**
     * 检查打卡时间是否在班组上班时间之前
     *
     * @param teamInfo 班组
     * @param time     打卡时间
     * @return 处理后的时间
     * @author wangyong
     */
    private Date checkTime(TeamInfo teamInfo, Date time) {
        Date result = time;
        Date startWorkTime = teamInfo.getPunchTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startWorkTime);
        Calendar checkCalendar = Calendar.getInstance();
        checkCalendar.setTime(time);
        if ((calendar.get(Calendar.HOUR) > checkCalendar.get(Calendar.HOUR)) || (calendar.get(Calendar.HOUR) == checkCalendar.get(Calendar.HOUR) && calendar.get(Calendar.MINUTE) > checkCalendar.get(Calendar.MINUTE))) {
            // 时
            checkCalendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR));
            // 分
            checkCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            // 秒
            checkCalendar.set(Calendar.SECOND, 0);
            // 毫秒
            checkCalendar.set(Calendar.MILLISECOND, 0);
            result = checkCalendar.getTime();
        }
        return result;
    }
}
