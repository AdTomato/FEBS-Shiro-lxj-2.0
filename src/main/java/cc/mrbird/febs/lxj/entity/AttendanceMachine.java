package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wangyong
 * @time: 2020/4/27 15:35
 * @Description: 考勤机管理
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceMachine {

    /**
     * 考勤机id
     */
    private String id;

    /**
     * 考勤机所属班组id
     */
    private String teamInfo;

    /**
     * 考勤机mac地址
     */
    private String mac;

}
