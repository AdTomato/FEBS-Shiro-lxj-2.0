package cc.mrbird.febs.lxj.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: wangyong
 * @time: 2020/4/27 15:43
 * @Description: 班组信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamInfo implements Serializable {

    /**
     * 班组id
     */
    private String id;

    /**
     * 班组名称
     */
    private String name;

    /**
     * 班组上班打卡时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date punchTime;

    /**
     * 班组弹性工作时间
     */
    private Integer flexibleWorkTime;

}
