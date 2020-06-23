
package cc.mrbird.febs.lxj.config;

import cc.mrbird.febs.lxj.entity.TeamInfo;
import cc.mrbird.febs.lxj.service.OrganizationService;
import cc.mrbird.febs.lxj.service.TeamService;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    private OrganizationService organizationService;
    //3.添加定时任务
    //@Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() throws ApiException {
        //每天0点同步组织架构
        organizationService.synchronous();
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now()
        );
    }
}
