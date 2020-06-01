package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.lxj.entity.AttendanceMachine;
import cc.mrbird.febs.lxj.entity.Result;
import cc.mrbird.febs.lxj.entity.TeamInfo;
import cc.mrbird.febs.lxj.service.AttendanceMachineService;
import cc.mrbird.febs.lxj.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TeamController
 * @Author:lfh
 * @Date:2020/5/18 13:40
 * @Description: 考勤班组管理
 **/
@RestController
@Slf4j
@RequestMapping("/controller/team")
public class TeamController extends BaseController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private AttendanceMachineService attendanceMachineService;
    /**
     * @author lfh
     * @Description 查询所有班组信息
     * @Date 2020/5/26 17:51
     * @throws
     * @param
     * @return {@link Object}
     **/
    @GetMapping("/searchAllTeam")
    public Object searchAllTeam(TeamInfo teamInfo, QueryRequest request){

      /*  List<TeamInfo> allTeamInfo = teamService.getAllTeamInfo();
        if (allTeamInfo.size() == 0 || allTeamInfo == null){
            return new Result(false,500,"班组信息为空","");
        }
        return new Result(true,200,"",allTeamInfo);*/
        Map<String, Object> dataTable = getDataTable(teamService.getAllTeamInfo(teamInfo,request));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * @author lfh
     * @Description 查询班组对应的考勤设备
     * @Date 2020/5/26 17:51
     * @throws
     * @param id 考勤班组id
     * @return {@link Object}
     **/
    @GetMapping("/searchMachineInTeamInfo")
    public Object searchTeamInfo(String id) {
        if (id == null || "".equals(id)){
            return new Result(false,500,"未传入班组id","");
        }
        List<AttendanceMachine> attendancesMachine = new ArrayList<>();
        //查询所有的考勤设备信息
        List<AttendanceMachine> machineList = attendanceMachineService.getAllMachine();
        //遍历考勤设备，将满足条件的考勤机返回
        for (AttendanceMachine attendanceMachine : machineList) {
            if (attendanceMachine.getTeamInfo().equals(id)) {
                attendancesMachine.add(attendanceMachine);
            }
        }
        return new FebsResponse().success().data(attendancesMachine);
    }

    /**
     * @param teamInfo 班组信息
     * @return {@link Object}
     * @throws
     * @author lfh
     * @Description： 新增班组信息
     * @Date 2020/5/18 14:24
     **/
    @PostMapping("/addTeam")
    public Object addTeam(@Validated @RequestBody TeamInfo teamInfo) {
        if (teamInfo == null || "".equals(teamInfo.getName())) {
            return new Result(false, 500, "未传入班组信息", "");
        }
        try {
            teamService.addTeamInfo(teamInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new FebsResponse().fail().message("添加失败");
        }

        return new FebsResponse().success();
    }

    /**
     * @param teamInfo 班组信息
     * @return {@link Object}
     * @throws
     * @author lfh
     * @Description 更新班组信息
     * @Date 2020/5/18 15:30
     **/
    @PostMapping("/updateTeam")
    public Object updateTeam(@RequestBody TeamInfo teamInfo) {
        if (teamInfo == null) {
            return new Result(false, 500, "未传入班组信息", "");
        }
        try {
            teamService.updateTeam(teamInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new FebsResponse().fail().message("修改班组信息失败");
        }

        return new FebsResponse().success();
    }

    /**
     * @param ids
     * @return {@link Object}
     * @throws
     * @author lfh
     * @Description 删除班组信息 可批量
     * @Date 2020/5/18 15:30
     **/
    @GetMapping("/deleteTeam")
    public Object deleteTeam(@RequestBody List<String> ids) {
        if (ids.size() == 0 || ids == null) {
            return new FebsResponse().fail().message("未传入班组信息");
        }
        try {
            teamService.deleteTeam(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new FebsResponse().fail().message("删除失败");
        }
        return new FebsResponse().success();
    }
}
