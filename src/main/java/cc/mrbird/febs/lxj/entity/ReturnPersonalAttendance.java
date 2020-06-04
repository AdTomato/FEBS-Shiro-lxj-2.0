package cc.mrbird.febs.lxj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ReturnPersonalAttendance
 * @author: lfh
 * @Date:2020/6/4 13:22
 * @Description:
 **/
@Data
public class ReturnPersonalAttendance {
    private String id;
    private String name;
    private String dept;
    private Date workTime;
    private Double duration;
}
