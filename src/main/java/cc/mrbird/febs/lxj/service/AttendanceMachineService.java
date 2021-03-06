package cc.mrbird.febs.lxj.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @ClassName AttendanceMachineService
 * @author: lfh
 * @Date:2020/5/18 15:41
 * @Description:
 **/
public interface AttendanceMachineService {
    void addMachine(String mac);

    void updateMachine(AttendanceMachine attendanceMachine);

    void deleteMachine(List<String> ids);

    AttendanceMachine getMachine(String mac);

    IPage<AttendanceMachine> getAllMachine(AttendanceMachine attendanceMachine, QueryRequest request);

    List<AttendanceMachine> getAllMachine();

    void addMachines(List<String> macList, String teamId);

    List<String> getMachineByTeamId(String id);

    void deleteMachineByMacs(List<String> deletedAttendance);

    void deleteMachineByTeamId(String teamId);

    void updateMachineTeam(List<String> macList,String id);
    //根据班组id跟新解除考勤机绑定
    void updateMachineByTeamIds(List<String> ids);
}
