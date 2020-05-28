/*
package cc.mrbird.febs.lxj.config;

import cc.mrbird.febs.lxj.entity.TeamInfo;
import cc.mrbird.febs.lxj.service.TeamService;
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
    private TeamService teamService;
    //3.添加定时任务
    //@Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {

        Calendar calendar = Calendar.getInstance();
        Calendar nowDayCalendar = Calendar.getInstance();
        nowDayCalendar.setTime(new Date());

        List<TeamInfo> teamInfoList = teamService.getAllTeamInfo();
        for (TeamInfo info : teamInfoList) {
            Date punchTime = info.getPunchTime();
            calendar.setTime(punchTime);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), nowDayCalendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date time = calendar.getTime();
            info.setPunchTime(time);
        }
       teamService.updateTeamPunchTime(teamInfoList);
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now()
        );
    }
}*/
