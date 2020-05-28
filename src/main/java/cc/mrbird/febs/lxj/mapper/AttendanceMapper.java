package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.Attendance;

import java.util.List;

/**
 * @author: wangyong
 * @time: 2020/4/24 16:19
 * @Description: 考勤的mapper对象
 */
public interface AttendanceMapper {

    /**
     * 单个插入考勤数据
     *
     * @param attendance 考勤数据
     * @author wangyong
     */
    void insertAttendance(Attendance attendance);

    /**
     * 批量插入考勤数据
     *
     * @param attendances 考勤数据
     * @author wangyong
     */
    void insertAttendances(List<Attendance> attendances);

}
