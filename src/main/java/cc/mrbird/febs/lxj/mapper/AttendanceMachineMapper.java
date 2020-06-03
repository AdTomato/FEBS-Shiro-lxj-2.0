package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

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

    void addMachine(String mac);

    void updateMachine(AttendanceMachine attendanceMachine);

    void deleteMachine(List<String> ids);

    IPage<AttendanceMachine> getAllMachine(Page<AttendanceMachine> page, AttendanceMachine attendanceMachine);

    long countMachineNum(@Param("attendanceMachine") AttendanceMachine attendanceMachine);

    List<AttendanceMachine> getMachines();

    void addMachines(@Param("macList") List<String> macList, @Param("teamId") String teamId);

    List<String> getMachineByTeamId(String id);

    void deleteMachineByMacs(List<String> deletedAttendance);

    void deleteMachineByTeamId(String teamId);
}
