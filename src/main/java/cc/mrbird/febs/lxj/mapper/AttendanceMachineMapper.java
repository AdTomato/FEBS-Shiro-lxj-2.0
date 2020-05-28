package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.AttendanceMachine;

import java.util.List;

/**
 * @author: wangyong
 * @time: 2020/4/27 15:37
 * @Description: 考勤机mapper
 */
public interface AttendanceMachineMapper {

    /**
     * 根据考勤机的mac地址，获取考勤机信息
     *
     * @param mac mac地址
     * @return 考勤机信息
     * @author wangyong
     */
    AttendanceMachine getAttendanceMachineByMac(String mac);

    void addMachine(AttendanceMachine attendanceMachine);

    void updateMachine(AttendanceMachine attendanceMachine);

    void deleteMachine(List<String> ids);
}
