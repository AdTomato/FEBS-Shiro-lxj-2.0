package cc.mrbird.febs.lxj.params;

import cc.mrbird.febs.lxj.entity.TeamInfo;
import lombok.Data;

/**
 * @ClassName AddTeamParams
 * @author: lfh
 * @Date:2020/6/2 15:38
 * @Description:
 **/
@Data
public class AddTeamParams {
    private TeamInfo teamInfo;
    private String macs;
}
