package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import cc.mrbird.febs.lxj.mapper.AttendanceMachineMapper;
import cc.mrbird.febs.lxj.service.AttendanceMachineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AttendanceMachineServiceImpl
 * @author: lfh
 * @Date:2020/5/18 15:41
 * @Description:
 **/
@Service
public class AttendanceMachineServiceImpl implements AttendanceMachineService {
    @Resource
    private AttendanceMachineMapper attendanceMachineMapper;

    @Override
    public void addMachine(AttendanceMachine attendanceMachine) {
        attendanceMachineMapper.addMachine(attendanceMachine);
    }

    @Override
    public void updateMachine(AttendanceMachine attendanceMachine) {
        attendanceMachineMapper.updateMachine(attendanceMachine);
    }

    @Override
    public void deleteMachine(List<String> ids) {
        attendanceMachineMapper.deleteMachine(ids);
    }

    @Override
    public AttendanceMachine getMachine(String mac) {
      return   attendanceMachineMapper.getAttendanceMachineByMac(mac);
    }
}
