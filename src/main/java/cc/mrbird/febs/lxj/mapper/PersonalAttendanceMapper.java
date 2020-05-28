package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.PersonalAttendance;

import java.util.Date;

/**
 * @author: wangyong
 * @time: 2020/4/27 16:05
 * @Description: 每日考勤时长mapper
 */
public interface PersonalAttendanceMapper {

    /**
     * 根据工作时间获取人员每日考勤时长数据
     *
     * @param workTime 工作时间
     * @return 人员每日考勤时长
     * @author wangyong
     */
    PersonalAttendance getPersonalAttendanceByWorkTime(Date workTime, String userId);


    /**
     * 插入人员每日考勤时长数据
     *
     * @param personalAttendance 考勤时长数据
     * @author wangyong
     */
    void insertPersonalAttendance(PersonalAttendance personalAttendance);

}
