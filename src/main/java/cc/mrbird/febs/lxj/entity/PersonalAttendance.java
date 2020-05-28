package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author: wangyong
 * @time: 2020/4/27 16:02
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalAttendance {

    /**
     * 每日考勤数据id
     */
    private String id;

    /**
     * 考勤人员id
     */
    private String userId;

    /**
     * 考勤人员部门id
     */
    private String departmentId;

    /**
     * 工作时间
     */
    private Date workTime;

    /**
     * 时长
     */
    private Double duration;

}
