package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.lxj.entity.TeamInfo;
import cc.mrbird.febs.lxj.mapper.TeamInfoMapper;
import cc.mrbird.febs.lxj.service.TeamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TeamServiceImpl
 * @author: lfh
 * @Date:2020/5/18 14:10
 * @Description:
 **/
@Service
public class TeamServiceImpl implements TeamService {
    @Resource
    private TeamInfoMapper teamInfoMapper;
    @Override
    public TeamInfo getTeamInfoById(String id) {
       return teamInfoMapper.getTeamInfoById(id);
    }

    @Override
    public void addTeamInfo(TeamInfo teamInfo) {
        teamInfoMapper.addTeamInfo(teamInfo);
    }

    @Override
    public void updateTeam(TeamInfo teamInfo) {
        teamInfoMapper.updateTeam(teamInfo);
    }

    @Override
    public void deleteTeam(List<String> ids) {
        teamInfoMapper.deleteTeam(ids);
    }

    @Override
    public List<TeamInfo> getAllTeamInfo() {
         return teamInfoMapper.getAllTeamInfo();
    }

    @Override
    public void updateTeamPunchTime(List<TeamInfo> teamInfoList) {
        teamInfoMapper.updateTeamPunchTime(teamInfoList);
    }
}
