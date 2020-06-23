package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.OrgUser;
import cc.mrbird.febs.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author:wangyong
 * @Date:2020/4/20 18:05
 * @Description:
 */
@Repository("lxjMapper")
@Mapper
public interface UserMapper {

    /**
     * 批量插入人员数据
     *
     * @param users 人员数据
     * @author wangyong
     */
    void insertUsers(List<OrgUser> users);

    /**
     * 单个插入人员数据
     *
     * @param user 人员数据
     * @author wangyong
     */
    void insertUser(OrgUser user);

    /**
     * 批量更新人员数据
     *
     * @param users 人员数据
     * @author wangyong
     */
    void updateUsers(List<OrgUser> users);

    /**
     * 更新人员数据
     *
     * @param user 人员数据
     * @author wangyong
     */
    void updateUser(OrgUser user);

    /**
     * 根据钉钉的人员id获取系统中的人员id
     *
     * @param sourceId 钉钉人员id
     * @return 系统人员id
     * @author wangyong
     */
    String getIdBySourceId(String sourceId);

    /**
     * @author lfh
     * @Description 根据用户id获取部门id
     * @Date 2020/5/21 10:05
     * @throws
     * @param userId
     * @return {@link String}
     **/
    String getDeptId(String userId);

    List<OrgUser> getUsers();

}
