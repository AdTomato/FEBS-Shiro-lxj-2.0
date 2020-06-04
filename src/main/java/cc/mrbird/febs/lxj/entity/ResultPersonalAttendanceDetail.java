package cc.mrbird.febs.lxj.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ResultPersonalAttendanceDetail
 * @author: lfh
 * @Date:2020/6/4 15:00
 * @Description:
 **/
@Data
public class ResultPersonalAttendanceDetail {
    private String id;
    private String name;
    private String team;
    private String dept;
    private Date startTime;
    private Date endTime;
    private Double duration;
    private String status;
}
