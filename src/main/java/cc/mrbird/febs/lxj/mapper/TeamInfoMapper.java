package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.TeamInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: wangyong
 * @time: 2020/4/27 15:48
 * @Description: 考勤班组mapper
 */
@Repository
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

    IPage<TeamInfo> getAllTeamInfo(@Param("page") Page page, @Param("teamInfo") TeamInfo teamInfo);

    void updateTeamPunchTime(List<TeamInfo> teamInfoList);

    long countTeamInfoNum(@Param("teamInfo") TeamInfo teamInfo);

    String getTeamIdByName(String name);
}
