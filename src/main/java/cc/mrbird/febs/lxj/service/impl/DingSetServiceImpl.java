package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.lxj.service.DingSetService;
import cc.mrbird.febs.lxj.utils.Constant;
import cc.mrbird.febs.lxj.utils.RedisUtils;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiCallBackUpdateCallBackRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiCallBackUpdateCallBackResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @Author:wangyong
 * @Date:2020/4/20 10:14
 * @Description:
 */
@Service
public class DingSetServiceImpl implements DingSetService {


    @Autowired
    RedisUtils redisUtils;

    /**
     * @Author: wangyong
     * @Date: 2020/4/20 10:14
     * @return: java.lang.String 钉钉的accessToken
     * @Description: 获取钉钉的token
     */
    @Override
    public String getAccessToken() throws ApiException {
        if (redisUtils.hasKey(Constant.ACCESS_TOKEN)) {
            return redisUtils.get(Constant.ACCESS_TOKEN) + "";
        }
        ResourceBundle rb = ResourceBundle.getBundle("dingding");
        String appKey = rb.getString("AppKey");
        String appSecret = rb.getString("AppSecret");
        DefaultDingTalkClient client = new DefaultDingTalkClient(Constant.GET_TOKEN_URL);
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(appKey);
        request.setAppsecret(appSecret);
        request.setHttpMethod("GET");
        OapiGettokenResponse response = client.execute(request);
        if (response != null) {
            redisUtils.set(Constant.ACCESS_TOKEN, response.getAccessToken(), 7100);
            return response.getAccessToken();
        }
        return null;
    }

    /**
     * 注册事件回调函数
     * @return
     */
    @Override
    public OapiCallBackUpdateCallBackResponse registerCallBack() throws ApiException {
        String accessToken = getAccessToken();
        ResourceBundle rb = ResourceBundle.getBundle("dingding");
        String aesKey = rb.getString("aes_key");
        String aesToken = rb.getString("aes_token");
        DingTalkClient client = new DefaultDingTalkClient(Constant.REGISTER_CALL_BACK_URL);
        OapiCallBackUpdateCallBackRequest request = new OapiCallBackUpdateCallBackRequest();
        request.setUrl(Constant.URL_HOST + "/dingtalk/callback");
        request.setAesKey(aesKey);
        request.setToken(aesToken);
        request.setCallBackTag(Arrays.asList("user_add_org", "user_modify_org", "attendance_check_record"));
        OapiCallBackUpdateCallBackResponse response = client.execute(request,accessToken);
        return response;
    }
}
