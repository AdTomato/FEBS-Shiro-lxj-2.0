package cc.mrbird.febs.lxj.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.lxj.entity.TeamInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

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

    IPage<TeamInfo> getAllTeamInfo(TeamInfo teamInfo, QueryRequest request);

    void updateTeamPunchTime(List<TeamInfo> teamInfoList);
}
