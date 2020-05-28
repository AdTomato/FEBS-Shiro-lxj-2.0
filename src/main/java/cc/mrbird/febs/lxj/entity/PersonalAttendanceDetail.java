package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: wangyong
 * @time: 2020/4/27 16:22
 * @Description: 人员每日考勤明细
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalAttendanceDetail {

    /**
     * 每日考勤时长明细数据id
     */
    private String id;

    /**
     * 数据创建时间
     */
    private Date createdTime;

    /**
     * 人员每日考勤时长id
     */
    private String personalAttendanceId;

    /**
     * 班组id
     */
    private String teamId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 时长
     */
    private Double duration;

    /**
     * 状态
     */
    private String status;

}
