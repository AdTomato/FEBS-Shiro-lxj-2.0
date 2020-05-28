package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: wangyong
 * @time: 2020/4/24 15:18
 * @Description: 考勤
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {

    /**
     * 数据id
     */
    private String id;

    /**
     * 数据创建时间
     */
    private Date createdTime;

    /**
     * 打卡位置
     */
    private String address;

    /**
     * 考勤机打卡的考勤机MAC地址
     */
    private String baseMacAddr;

    /**
     * 打卡时间
     */
    private Date checkTime;

    /**
     * 企业id
     */
    private String corpId;

    /**
     * 经度信息
     */
    private Double latitude;

    /**
     * 打卡id
     */
    private String bizId;

    /**
     * 打卡方式，MAP定位打卡，ATM考勤机打卡
     */
    private String locationMethod;

    /**
     * 考勤机设备名称
     */
    private String deviceName;

    /**
     * 考勤机设备的SN号
     */
    private String deviceSN;

    /**
     * 打卡人系统id
     */
    private String userId;

    /**
     * 打卡人钉钉id
     */
    private String sourceUserId;

}
