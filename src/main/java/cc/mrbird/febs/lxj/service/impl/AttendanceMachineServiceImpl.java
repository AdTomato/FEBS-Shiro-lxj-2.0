package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import cc.mrbird.febs.lxj.entity.TeamInfo;
import cc.mrbird.febs.lxj.mapper.AttendanceMachineMapper;
import cc.mrbird.febs.lxj.mapper.TeamInfoMapper;
import cc.mrbird.febs.lxj.service.AttendanceMachineService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    @Resource
    private TeamInfoMapper teamInfoMapper;

    @Override
    public void addMachine(String mac) {
        attendanceMachineMapper.addMachine(mac);
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

    @Override
    public IPage<AttendanceMachine> getAllMachine(AttendanceMachine attendanceMachine, QueryRequest request) {
        Page<AttendanceMachine> page = new Page<>(request.getPageNum(),request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(attendanceMachineMapper.countMachineNum(attendanceMachine));
        SortUtil.handlePageSort(request,page,"mac", FebsConstant.ORDER_ASC,false);
        IPage<AttendanceMachine> allMachine = attendanceMachineMapper.getAllMachine(page, attendanceMachine);
        List<AttendanceMachine> attendanceMachines = allMachine.getRecords();
        //返回班组名称
        for (AttendanceMachine machine : attendanceMachines) {
            if (machine.getTeamInfo() != null && machine.getTeamInfo() !=""){
                TeamInfo teamInfo = teamInfoMapper.getTeamInfoById(machine.getTeamInfo());
                if (teamInfo != null) {
                    machine.setTeamInfo(teamInfo.getName());
                }
            } else {
                machine.setTeamInfo("");
            }
        }
        allMachine.setRecords(attendanceMachines);
        return allMachine;

    }

    @Override
    public List<AttendanceMachine> getAllMachine() {
        return attendanceMachineMapper.getMachines();
    }

    @Override
    public void addMachines(List<String> macList, String name) {
         attendanceMachineMapper.addMachines(macList, name);
    }

    @Override
    public List<String> getMachineByTeamId(String id) {
        return attendanceMachineMapper.getMachineByTeamId(id);
    }

    @Override
    public void deleteMachineByMacs(List<String> deletedAttendance) {
        attendanceMachineMapper.deleteMachineByMacs(deletedAttendance);
    }

    @Override
    public void deleteMachineByTeamId(String teamId) {
        attendanceMachineMapper.deleteMachineByTeamId(teamId);
    }
}
