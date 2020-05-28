package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.TeamInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: wangyong
 * @time: 2020/4/27 15:48
 * @Description: 考勤班组mapper
 */
@Mapper
public interface TeamInfoMapper {

    /**
     * 根据考勤班组id获取考勤班组信息
     *
     * @param id 考勤班组id
     * @return 考勤班组信息
     * @author wangyong
     */
    TeamInfo getTeamInfoById(String id);

    void addTeamInfo(TeamInfo teamInfo);

    void updateTeam(TeamInfo teamInfo);

    void deleteTeam(List<String> ids);

    List<TeamInfo> getAllTeamInfo();

    void updateTeamPunchTime(List<TeamInfo> teamInfoList);
}
