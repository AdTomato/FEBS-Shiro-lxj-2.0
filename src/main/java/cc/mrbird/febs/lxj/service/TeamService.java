package cc.mrbird.febs.lxj.service;


import cc.mrbird.febs.lxj.entity.TeamInfo;

import java.util.List;

/**
 * @ClassName TeamService
 * @author: lfh
 * @Date:2020/5/18 14:09
 * @Description:
 **/
public interface TeamService {
    TeamInfo getTeamInfoById(String id);

    void addTeamInfo(TeamInfo teamInfo);

    void updateTeam(TeamInfo teamInfo);

    void deleteTeam(List<String> ids);

    List<TeamInfo> getAllTeamInfo();

    void updateTeamPunchTime(List<TeamInfo> teamInfoList);
}
