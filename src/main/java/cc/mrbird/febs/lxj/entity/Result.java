package cc.mrbird.febs.lxj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Result
 * @author: lfh
 * @Date:2020/5/18 14:36
 * @Description:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private boolean flag;//是否成功
    private Integer code;//返回码
    private String message;//返回消息
    private T data;//返回数据


}
