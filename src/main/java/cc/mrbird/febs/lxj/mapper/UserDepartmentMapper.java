package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.OrgUserDepartment;

import java.util.List;

/**
 * @author: wangyong
 * @time: 2020/4/21 13:06
 * @Description:
 */
public interface UserDepartmentMapper {

    /**
     * 批量插入人员部门对应表
     *
     * @param userDepartments 人员部门对应
     * @author wangyong
     */
    void insertUserDepartments(List<OrgUserDepartment> userDepartments);

    /**
     * 单个插入人员部门对应表
     *
     * @param userDepartment 人员部门对应
     * @author wangyong
     */
    void insertUserDepartment(OrgUserDepartment userDepartment);

    /**
     * 批更新人员部门对应表
     *
     * @param userDepartments 人员部门对应
     * @author wangyong
     */
    void updateUserDepartments(List<OrgUserDepartment> userDepartments);

    /**
     * 单个更新人员部门对应表
     *
     * @param userDepartment 人员部门对应
     * @author wangyong
     */
    void updateUserDepartment(OrgUserDepartment userDepartment);

    /**
     * 根据钉钉的员工id和员工部门id，获取系统人员部门对应表id
     *
     * @param sourceId           员工id
     * @param sourceDepartmentId 员工部门id
     * @return 系统人员部门对应表id
     * @author wangyong
     */
    String getIdBySuidAndSdid(String sourceId, String sourceDepartmentId);

}
