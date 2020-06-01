package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import cc.mrbird.febs.lxj.service.AttendanceMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName AttendanceMachineController
 * @author: lfh
 * @Date:2020/5/18 15:39
 * @Description: 考勤机管理
 **/
@RestController
@Slf4j
@RequestMapping("/controller/attendanceMachine")
public class AttendanceMachineController extends BaseController {
    @Autowired
    private AttendanceMachineService attendanceMachineService;

    @GetMapping("/getAllAttendanceMachine")
    public Object getAllAttendanceMachine(AttendanceMachine attendanceMachine, QueryRequest request){
        /*List<AttendanceMachine> allMachine = attendanceMachineService.getAllMachine();
        if (allMachine.isEmpty() || allMachine == null){
            return new Result(false,500,"没有考勤设备数据","");
        }
        return new Result(true,200,"",allMachine);*/
        Map<String, Object> dataTable = getDataTable(attendanceMachineService.getAllMachine(attendanceMachine, request));
        return new FebsResponse().success().data(dataTable);
    }
    /**
     * @author lfh
     * @Description 添加考勤机
     * @Date 2020/5/18 15:55
     * @throws
     * @param attendanceMachine
     * @return {@link Object}
     **/
    @PostMapping("/addMachine")
    public Object addMachine(@Validated @RequestBody AttendanceMachine attendanceMachine){
        if (attendanceMachine == null || "".equals(attendanceMachine)){
            return new FebsResponse().fail().message("未传入考勤机信息");
        }
        try {
            attendanceMachineService.addMachine(attendanceMachine);
        } catch (Exception e){
            e.printStackTrace();
            return new FebsResponse().fail().message("插入考勤机信息失败");
        }
        return new FebsResponse().success();
    }

    @PostMapping("/updateMachine")
    public Object updateMachine(@RequestBody AttendanceMachine attendanceMachine){
        if (attendanceMachine == null || "".equals(attendanceMachine)){
            return new FebsResponse().fail().message("未传入考勤机信息");
        }
        try {
            attendanceMachineService.updateMachine(attendanceMachine);
        } catch (Exception e){
            e.printStackTrace();
            return new FebsResponse().fail().message("更新考勤机信息失败");
        }

        return new FebsResponse().success();
    }

    @PostMapping("/deleteMachine")
    public Object deleteMachine(@RequestBody List<String> ids){
        if (ids.size() == 0 || "".equals(ids)){
            return new FebsResponse().fail().message("未传入考勤机信息");
        }
        try {
            attendanceMachineService.deleteMachine(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new FebsResponse().fail().message("删除考勤机信息失败");

        }
        return new FebsResponse().success();
    }
    @GetMapping("/getMachine")
    public Object getMachine(String mac){
       AttendanceMachine attendanceMachine =  attendanceMachineService.getMachine(mac);
        return new FebsResponse().success().data(attendanceMachine);
    }
}
