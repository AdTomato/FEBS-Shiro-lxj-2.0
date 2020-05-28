package cc.mrbird.febs.lxj.controller;


import cc.mrbird.febs.lxj.service.AttendanceService;
import cc.mrbird.febs.lxj.service.DingSetService;
import cc.mrbird.febs.lxj.utils.Constant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiCallBackUpdateCallBackResponse;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.dingtalk.oapi.lib.aes.Utils;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: wangyong
 * @time: 2020/4/22 13:53
 * @Description:
 */
@RestController
@RequestMapping("/dingtalk")
@Slf4j
public class CallbackController {


    @Autowired
    DingSetService dingSetService;

    @Autowired
    AttendanceService attendanceService;

    private static final Logger bizLogger = LoggerFactory.getLogger("BIZ_CALLBACKCONTROLLER");
    private static final Logger mainLogger = LoggerFactory.getLogger(CallbackController.class);
    /**
     * 创建套件后，验证回调URL创建有效事件（第一次保存回调URL之前）
     */
    private static final String EVENT_CHECK_CREATE_SUITE_URL = "check_create_suite_url";
    /**
     * 创建套件后，验证回调URL变更有效事件（第一次保存回调URL之后）
     */
    private static final String EVENT_CHECK_UPADTE_SUITE_URL = "check_update_suite_url";

    /**
     * suite_ticket推送事件
     */
    private static final String EVENT_SUITE_TICKET = "suite_ticket";
    /**
     * 企业授权开通应用事件
     */
    private static final String EVENT_TMP_AUTH_CODE = "tmp_auth_code";

    /**
     * 相应钉钉回调时的值
     */
    private static final String CALLBACK_RESPONSE_SUCCESS = "success";

    @GetMapping("/registerCallBack")
    public Object registerCallBack() throws ApiException {
        OapiCallBackUpdateCallBackResponse oapiCallBackUpdateCallBackResponse = dingSetService.registerCallBack();
        return oapiCallBackUpdateCallBackResponse;
    }

    /**
     *
     * @param signature 加密标签
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param json
     * @return
     */
    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> callback(@RequestParam(value = "signature", required = false) String signature,
                                        @RequestParam(value = "timestamp", required = false) String timestamp,
                                        @RequestParam(value = "nonce", required = false) String nonce,
                                        @RequestBody(required = false) JSONObject json) {
        String params = " signature:"+signature + " timestamp:"+timestamp +" nonce:"+nonce+" json:"+json;
        try {
            bizLogger.info("begin /callback"+params);
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(Constant.TOKEN, Constant.ENCODING_AES_KEY,
                    Constant.SUITE_KEY);

            //从post请求的body中获取回调信息的加密数据进行解密处理
            String encryptMsg = json.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encryptMsg);
            JSONObject obj = JSON.parseObject(plainText);
            bizLogger.info("打卡信息：" + obj);

            //根据回调数据类型做不同的业务处理
            String eventType = obj.getString("EventType");
            if (EVENT_CHECK_CREATE_SUITE_URL.equals(eventType)) {
                bizLogger.info("验证新创建的回调URL有效性: " + plainText);
            } else if (EVENT_CHECK_UPADTE_SUITE_URL.equals(eventType)) {
                bizLogger.info("验证更新回调URL有效性: " + plainText);
            } else if (EVENT_SUITE_TICKET.equals(eventType)) {
                //suite_ticket用于用签名形式生成accessToken(访问钉钉服务端的凭证)，需要保存到应用的db。
                //钉钉会定期向本callback url推送suite_ticket新值用以提升安全性。
                //应用在获取到新的时值时，保存db成功后，返回给钉钉success加密串（如本demo的return）
                bizLogger.info("应用suite_ticket数据推送: " + plainText);
            } else if (EVENT_TMP_AUTH_CODE.equals(eventType)) {
                //本事件应用应该异步进行授权开通企业的初始化，目的是尽最大努力快速返回给钉钉服务端。用以提升企业管理员开通应用体验
                //即使本接口没有收到数据或者收到事件后处理初始化失败都可以后续再用户试用应用时从前端获取到corpId并拉取授权企业信息，
                // 进而初始化开通及企业。
                bizLogger.info("企业授权开通应用事件: " + plainText);
            } else if (Constant.ATTENDANCE_CHECK_RECORD.equals(eventType)) {
                // 考勤打卡
                log.info("时间为考勤时间，进行考勤事件处理");
                attendanceService.dealWithAttendanceData(obj);
            } else {
                // 其他类型事件处理
            }

            // 返回success的加密信息表示回调处理成功
            Map<String, String> encryptedMap = dingTalkEncryptor.getEncryptedMap(CALLBACK_RESPONSE_SUCCESS, System.currentTimeMillis(), Utils.getRandomStr(8));
            return encryptedMap;
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            mainLogger.error("process callback failed！"+params,e);
            return null;
        }

    }

}
