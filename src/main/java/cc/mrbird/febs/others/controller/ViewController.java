package cc.mrbird.febs.others.controller;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.FebsUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrBird
 */
@Controller("othersView")
@RequestMapping(FebsConstant.VIEW_PREFIX + "others")
public class ViewController {

    @GetMapping("febs/form")
    @RequiresPermissions("febs:form:view")
    public String febsForm() {
        return FebsUtil.view("others/febs/form");
    }

    @GetMapping("febs/form/group")
    @RequiresPermissions("febs:formgroup:view")
    public String febsFormGroup() {
        return FebsUtil.view("others/febs/formGroup");
    }

    @GetMapping("febs/tools")
    @RequiresPermissions("febs:tools:view")
    public String febsTools() {
        return FebsUtil.view("others/febs/tools");
    }

    @GetMapping("febs/icon")
    @RequiresPermissions("febs:icons:view")
    public String febsIcon() {
        return FebsUtil.view("others/febs/icon");
    }

    @GetMapping("febs/others")
    @RequiresPermissions("others:febs:others")
    public String febsOthers() {
        return FebsUtil.view("others/febs/others");
    }

    @GetMapping("apex/line")
    @RequiresPermissions("apex:line:view")
    public String apexLine() {
        return FebsUtil.view("others/apex/line");
    }

    @GetMapping("apex/area")
    @RequiresPermissions("apex:area:view")
    public String apexArea() {
        return FebsUtil.view("others/apex/area");
    }

    @GetMapping("apex/column")
    @RequiresPermissions("apex:column:view")
    public String apexColumn() {
        return FebsUtil.view("others/apex/column");
    }

    @GetMapping("apex/radar")
    @RequiresPermissions("apex:radar:view")
    public String apexRadar() {
        return FebsUtil.view("others/apex/radar");
    }

    @GetMapping("apex/bar")
    @RequiresPermissions("apex:bar:view")
    public String apexBar() {
        return FebsUtil.view("others/apex/bar");
    }

    @GetMapping("apex/mix")
    @RequiresPermissions("apex:mix:view")
    public String apexMix() {
        return FebsUtil.view("others/apex/mix");
    }

    @GetMapping("map")
    @RequiresPermissions("map:view")
    public String map() {
        return FebsUtil.view("others/map/gaodeMap");
    }

    @GetMapping("eximport")
    @RequiresPermissions("others:eximport:view")
    public String eximport() {
        return FebsUtil.view("others/eximport/eximport");
    }

    @GetMapping("eximport/result")
    public String eximportResult() {
        return FebsUtil.view("others/eximport/eximportResult");
    }
    /***
     * 班组管理
     */
    @GetMapping("team")
    @RequiresPermissions("team:view")
    public String team() {
        return FebsUtil.view("others/team/team");
    }

    /***
     * 考勤机管理
     */
    @GetMapping("machine")
    @RequiresPermissions("machine:view")
    public String machine() {
        return FebsUtil.view("others/machine/machine");
    }

    /***
     * 人员每日考勤时长
     */
    @GetMapping("attendanceTime")
    @RequiresPermissions("time:view")
    public String statis() {
        return FebsUtil.view("others/time/attend");
    }

    /***
     * 人员每日考勤明细
     */
    @GetMapping("attendanceDetails")
    @RequiresPermissions("time:view")
    public String details() {
        return FebsUtil.view("others/time/attendsDetails");
    }

    /***
     * 考勤明细
     */
    @GetMapping("details")
    @RequiresPermissions("time:view")
    public String attDetails() {
        return FebsUtil.view("others/time/details");
    }
}
