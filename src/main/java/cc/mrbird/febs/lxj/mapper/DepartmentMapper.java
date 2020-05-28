package cc.mrbird.febs.lxj.mapper;


import cc.mrbird.febs.lxj.entity.OrgDepartment;

import java.util.List;

/**
 * @Author:wangyong
 * @Date:2020/4/17 11:50
 * @Description: 部门
 */
public interface DepartmentMapper {

    List<OrgDepartment> getAllDepartments();

    /**
     * 批量插入部门
     *
     * @param departments 部门集合
     */
    void insertDepartments(List<OrgDepartment> departments);

    /**
     * 插入部门
     *
     * @param department 部门
     */
    void insertDepartment(OrgDepartment department);

    /**
     * 批量更新部门
     *
     * @param departments 部门集合
     */
    void updateDepartments(List<OrgDepartment> departments);

    /**
     * 更新部门
     *
     * @param department 部门
     */
    void updateDepartment(OrgDepartment department);

    /**
     * 根据钉钉部门id获取部门sourceId
     *
     * @param sourceId 钉钉部门id
     * @return 系统部门数据
     */
    OrgDepartment getDepartmentById(String sourceId);


}
