package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.lxj.service.DingSetService;
import cc.mrbird.febs.lxj.service.OrganizationService;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackGetCallBackRequest;
import com.dingtalk.api.response.OapiCallBackGetCallBackResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:wangyong
 * @Date:2020/4/20 10:55
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    DingSetService dingSetService;

    @Autowired
    OrganizationService organizationService;

    @GetMapping("/testOrganization")
    public String testOrganization() throws ApiException {
        boolean synchronous = organizationService.synchronous();
        if (synchronous) {
            return "更新成功";
        }
        return "更新失败";
    }

    @GetMapping("/test")
    public String test() {
        return "e6e18d0a42dd027cade89c213c75f477e6e18d0a42d".length() + "";
    }

    @GetMapping("/getCallBack")
    public Object getCallBack() throws ApiException {
        String accessToken = dingSetService.getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/call_back/get_call_back");
        OapiCallBackGetCallBackRequest request = new OapiCallBackGetCallBackRequest();
        request.setHttpMethod("GET");
        OapiCallBackGetCallBackResponse response = client.execute(request,accessToken);

        return response;
    }
    

}
