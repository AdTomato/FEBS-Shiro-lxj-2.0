package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import cc.mrbird.febs.lxj.mapper.AttendanceMachineMapper;
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

    @Override
    public IPage<AttendanceMachine> getAllMachine(AttendanceMachine attendanceMachine, QueryRequest request) {
        Page<AttendanceMachine> page = new Page<>(request.getPageNum(),request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(attendanceMachineMapper.countMachineNum(attendanceMachine));
        SortUtil.handlePageSort(request,page,"mac", FebsConstant.ORDER_ASC,false);

        return attendanceMachineMapper.getAllMachine(page,attendanceMachine);

    }

    @Override
    public List<AttendanceMachine> getAllMachine() {
        return attendanceMachineMapper.getMachines();
    }
}
