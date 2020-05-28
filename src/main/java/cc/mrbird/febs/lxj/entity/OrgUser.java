package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author:wangyong
 * @Date:2020/4/20 18:05
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgUser {

    /**
     * id
     */
    private String id;

    /**
     * 员工在当前企业内的唯一标识
     */
    private String unionid;

    /**
     * 员工钉钉id
     */
    private String sourceId;

    /**
     * 员工钉钉部门id
     */
    private String sourceDepartmentId;

    /**
     * 钉钉系统部门id
     */
    private String departmentId;

    /**
     * 部门中的排序
     */
    private String sortKey;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 分机号
     */
    private String tel;

    /**
     * 办公地点
     */
    private String workPlace;

    /**
     * 是否是企业的管理员
     */
    private Integer isAdmin;

    /**
     * 是否为企业的老板
     */
    private Integer isBoss;

    /**
     * 是否隐藏号码
     */
    private Integer isHide;

    /**
     * 是否是部门的主管
     */
    private Integer isLeader;

    /**
     * 员工名称
     */
    private String name;

    /**
     * 是否激活了钉钉
     */
    private Integer active;

    /**
     * 职位信息
     */
    private String position;

    /**
     * 员工的邮箱
     */
    private String email;

    /**
     * 员工的企业邮箱
     */
    private String orgEmail;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 员工工号
     */
    private String jobnumber;

    /**
     * 入职时间
     */
    private Date hiredDate;

    /**
     * 国家地区码
     */
    private String stateCode;

    /**
     * 是否显示
     */
    private Integer isShow;

}
