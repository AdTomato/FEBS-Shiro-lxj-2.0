package cc.mrbird.febs.lxj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ReturnPersonalAttendance
 * @author: lfh
 * @Date:2020/6/4 10:36
 * @Description:
 **/
@Data
public class ReturnAttendance {
    //考勤明细id
    private String id;
    //姓名
    private String name;
    //创建时间
    private Date createdTime;
    //打卡地址
    private String address;
    //考勤机mac地址
    private String BaseMacAddr;
    //打卡时间
    private Date checkTime;

}
