package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.*;
import cc.mrbird.febs.lxj.params.AttendanceDetailParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: wangyong
 * @time: 2020/4/24 16:19
 * @Description: 考勤的mapper对象
 */
@Repository
@Mapper
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

    long countAttendanceNum(@Param("attendanceDetailParams")AttendanceDetailParams attendanceDetailParams);

    IPage<ReturnAttendance> getAttendanceDetailList(@Param("page")Page<Attendance> page, @Param("attendanceDetailParams")AttendanceDetailParams attendanceDetailParams);

    long countPersonalAttendanceNum(@Param("attendanceDetailParams")AttendanceDetailParams attendanceDetailParams);

    IPage<ReturnPersonalAttendance> getPersonalAttendanceList(@Param("page") Page<PersonalAttendance> page, @Param("attendanceDetailParams") AttendanceDetailParams attendanceDetailParams);

    long countPersonalAttendanceDetailNum(@Param("attendanceDetailParams")AttendanceDetailParams attendanceDetailParams);

    IPage<ResultPersonalAttendanceDetail> getPersonalAttendanceDetailList(@Param("page") Page<PersonalAttendanceDetail> page, @Param("attendanceDetailParams")AttendanceDetailParams attendanceDetailParams);
}
