package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import cc.mrbird.febs.lxj.entity.Result;
import cc.mrbird.febs.lxj.service.AttendanceMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName AttendanceMachineController
 * @author: lfh
 * @Date:2020/5/18 15:39
 * @Description: 考勤机管理
 **/
@RestController
@Slf4j
@RequestMapping("/controller/attendanceMachine")
public class AttendanceMachineController {
    @Autowired
    private AttendanceMachineService attendanceMachineService;
    /**
     * @author lfh
     * @Description 添加考勤机
     * @Date 2020/5/18 15:55
     * @throws
     * @param attendanceMachine
     * @return {@link Object}
     **/
    @PostMapping("/addMachine")
    public Object addMachine(@RequestBody AttendanceMachine attendanceMachine){
        if (attendanceMachine == null || "".equals(attendanceMachine)){
            return new Result(false,500,"未传入考勤机信息","");
        }
        try {
            attendanceMachineService.addMachine(attendanceMachine);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,500,"插入考勤机信息失败","");
        }
        return new Result(true,200,"插入考勤机信息成功","");
    }

    @PostMapping("/updateMachine")
    public Object updateMachine(@RequestBody AttendanceMachine attendanceMachine){
        if (attendanceMachine == null || "".equals(attendanceMachine)){
            return new Result(false,500,"未传入考勤机信息","");
        }
        try {
            attendanceMachineService.updateMachine(attendanceMachine);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,500,"更新考勤机信息失败","");
        }

        return new Result(true,200,"更新考勤机信息成功","");
    }

    @PostMapping("/deleteMachine")
    public Object deleteMachine(@RequestBody List<String> ids){
        if (ids.size() == 0 || "".equals(ids)){
            return new Result(false,500,"为传入考勤机信息", "");
        }
        try {
            attendanceMachineService.deleteMachine(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,500,"删除考勤机信息失败","");
        }
        return new Result(true,200,"删除考勤机信息成功","");
    }
    @GetMapping("/getMachine")
    public Object getMachine(String mac){
       AttendanceMachine attendanceMachine =  attendanceMachineService.getMachine(mac);
       return new Result(true,200,"获取考勤机信息成功",attendanceMachine);
    }
}
