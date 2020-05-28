package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wangyong
 * @Date: 2020/4/21 9:09
 * @Description: 用户部门映射
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgUserDepartment {

    /**
     * id
     */
    private String id;

    /**
     * 钉钉id
     */
    private String sourceId;

    /**
     * 钉钉部门id
     */
    private String sourceDepartmentId;

    /**
     * 系统用户id
     */
    private String userId;

    /**
     * 系统部门id
     */
    private String departmentId;

    /**
     * 是否显示
     */
    private Integer isShow;

}
