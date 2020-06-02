package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.lxj.entity.TeamInfo;
import cc.mrbird.febs.lxj.mapper.TeamInfoMapper;
import cc.mrbird.febs.lxj.service.TeamService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public IPage<TeamInfo> getAllTeamInfo(TeamInfo teamInfo, QueryRequest request) {
        Page<TeamInfo> page = new Page(request.getPageNum(), request.getPageSize());
        page.setSearchCount(false);
        page.setTotal(teamInfoMapper.countTeamInfoNum(teamInfo));
        SortUtil.handlePageSort(request,page,"name", FebsConstant.ORDER_ASC,false);
        return teamInfoMapper.getAllTeamInfo(page,teamInfo);
    }

    @Override
    public void updateTeamPunchTime(List<TeamInfo> teamInfoList) {
        teamInfoMapper.updateTeamPunchTime(teamInfoList);
    }

    @Override
    public String getTeamIdByName(String name) {
        return teamInfoMapper.getTeamIdByName(name);
    }
}
