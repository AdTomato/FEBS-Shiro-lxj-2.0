package cc.mrbird.febs.lxj.service;

import com.taobao.api.ApiException;

/**
 * @Author:wangyong
 * @Date:2020/4/20 10:08
 * @Description: 组织架构
 */
public interface OrganizationService {

    /**
     * 同步组织架构
     *
     * @return true 更新成功 false 更新失败
     * @author wangyong
     */
    boolean synchronous() throws ApiException;

    /**
     * 同步根部门
     *
     * @author wangyong
     */
    void synchronousDepartment() throws ApiException;

    /**
     * 同步部门
     *
     * @param sourceId 钉钉部门id
     * @param parentId 系统部门父id
     * @author wangyong
     */
    void synchronousDepartments(String sourceId, String parentId);

    /**
     * 同步员工
     *
     * @author wangyong
     */
    void synchronousUser() throws ApiException;

    /**
     * 根据部门id同步员工
     *
     * @param sourceDepartmentId 部门id
     * @author wangyong
     */
    void synchronousUserByDepartmentId(String sourceDepartmentId) throws ApiException;

}
