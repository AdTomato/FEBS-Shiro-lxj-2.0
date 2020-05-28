package cc.mrbird.febs.lxj.service;


import cc.mrbird.febs.lxj.entity.AttendanceMachine;

import java.util.List;

/**
 * @ClassName AttendanceMachineService
 * @author: lfh
 * @Date:2020/5/18 15:41
 * @Description:
 **/
public interface AttendanceMachineService {
    void addMachine(AttendanceMachine attendanceMachine);

    void updateMachine(AttendanceMachine attendanceMachine);

    void deleteMachine(List<String> ids);

    AttendanceMachine getMachine(String mac);
}
