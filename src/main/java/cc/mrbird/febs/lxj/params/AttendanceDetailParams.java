package cc.mrbird.febs.lxj.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

/**
 * @ClassName AttendanceDetailParams
 * @author: lfh
 * @Date:2020/6/3 15:13
 * @Description:
 **/
@Data
public class AttendanceDetailParams implements Serializable {
    //姓名
    private String name;

    //打卡时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;




}
