package cc.mrbird.febs.lxj.service;

import com.dingtalk.api.response.OapiCallBackUpdateCallBackResponse;
import com.taobao.api.ApiException;

/**
 * @Author:wangyong
 * @Date:2020/4/20 10:12
 * @Description: 钉钉设置
 */
public interface DingSetService {

    String getAccessToken() throws ApiException;

    OapiCallBackUpdateCallBackResponse registerCallBack() throws ApiException;

}
