package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author:wangyong
 * @Date:2020/4/17 11:51
 * @Description: 部门实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgDepartment {

    /**
     * 系统部门id
     */
    private String id;

    /**
     * 系统部门名称
     */
    private String name;

    /**
     * 系统父id
     */
    private String parentId;

    /**
     * 部门钉钉id
     */
    private String sourceId;

    /**
     * 部门钉钉父id
     */
    private String sourceParentId;

    /**
     * 钉钉部门标识
     */
    private String sourceIdentifier;

    /**
     * 是否显示
     */
    private Integer isShow;

}
