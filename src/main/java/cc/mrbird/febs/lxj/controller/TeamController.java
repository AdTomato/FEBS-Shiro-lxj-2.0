package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.lxj.entity.Result;
import cc.mrbird.febs.lxj.entity.TeamInfo;
import cc.mrbird.febs.lxj.service.TeamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName TeamController
 * @Author:lfh
 * @Date:2020/5/18 13:40
 * @Description: 考勤班组管理
 **/
@RestController
@Slf4j
@RequestMapping("/controller/team")
public class TeamController {
    @Autowired
    private TeamService teamService;
    /**
     * @author lfh
     * @Description： 新增班组信息
     * @Date 2020/5/18 14:24
     * @throws
     * @param teamInfo 班组信息
     * @return {@link Object}
     **/
    @PostMapping("/addTeam")
    public Object addTeam(@RequestBody TeamInfo teamInfo){
        if (teamInfo == null || "".equals(teamInfo)){
            return new Result(false,500,"未传入班组信息","");
        }
        try {
            teamService.addTeamInfo(teamInfo);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,500,"插入班组信息失败","");
        }

        return new Result(true,200,"插入班组信息成功","");
    }
    /**
     * @author lfh
     * @Description 更新班组信息
     * @Date 2020/5/18 15:30
     * @throws
     * @param teamInfo 班组信息
     * @return {@link Object}
     **/
    @PostMapping("/updateTeam")
    public Object updateTeam(@RequestBody TeamInfo teamInfo){
        if (teamInfo == null || "".equals(teamInfo)){
            return new Result(false,500,"未传入班组信息","");
        }
        try {
            teamService.updateTeam(teamInfo);
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,500,"更新班组信息失败","");
        }

        return new Result(true,200,"更新班组信息成功","");
    }
    /**
     * @author lfh
     * @Description 删除班组信息 可批量
     * @Date 2020/5/18 15:30
     * @throws
     * @param ids
     * @return {@link Object}
     **/
    @GetMapping("/deleteTeam")
    public Object deleteTeam(@RequestBody List<String> ids){
        if (ids.size() == 0 || "".equals(ids)){
            return new Result(false,500,"未传入班组信息", "");
        }
        try {
            teamService.deleteTeam(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,500,"删除班组信息失败","");
        }
        return new Result(true,200,"删除班组信息成功","");
    }
}
