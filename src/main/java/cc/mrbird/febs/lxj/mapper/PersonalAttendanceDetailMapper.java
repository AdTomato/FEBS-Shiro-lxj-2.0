package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.PersonalAttendanceDetail;

import java.util.List;

/**
 * @author: wangyong
 * @time: 2020/4/27 16:43
 * @Description:
 */
public interface PersonalAttendanceDetailMapper {

    /**
     * 根据人员每日考勤时长数据id获取人员每日考勤明细
     *
     * @param personalAttendanceId 人员每日考勤时长数据id
     * @return 人员每日考勤明细
     * @author wangyong
     */
    List<PersonalAttendanceDetail> getPersonalAttendancesDetailByPersonalAttendanceId(String personalAttendanceId);

    PersonalAttendanceDetail getPersonalAttendanceDetailByPersonalAttendanceId(String personalAttendance);

    /**
     * 插入一条人员每日考勤明细
     *
     * @param personalAttendanceDetail 人员每日考勤明细数据
     * @author wangyong
     */
    void insertPersonalAttendanceDetail(PersonalAttendanceDetail personalAttendanceDetail);

    /**
     * 单个更新人员每日考勤明细
     *
     * @param personalAttendanceDetail 人员每日考勤明细
     * @author wangyong
     */
    void updatePersonalAttendanceDetail(PersonalAttendanceDetail personalAttendanceDetail);

    /**
     * 批量更新人员每日考勤明细
     *
     * @param personalAttendanceDetails 人员每日考勤明细
     * @author wangyong
     */
    void updatePersonalAttendanceDetails(List<PersonalAttendanceDetail> personalAttendanceDetails);

    /**
     * @author lfh
     * @Description 获取第一条考勤数据
     * @Date 2020/5/19 13:13
     * @throws
     * @param personalAttendanceId 每日考勤时长id
     * @return
     **/
    PersonalAttendanceDetail getFirstPersonalAttendanceDetailByPersonalAttendanceId(String personalAttendanceId);
}
