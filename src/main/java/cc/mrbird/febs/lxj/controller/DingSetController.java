package cc.mrbird.febs.lxj.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: wangyong
 * @time: 2020/4/22 13:40
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/dingSet")
public class DingSetController {


    @ApiOperation(value = "钉钉回调异步通知", notes = "钉钉回调异步通知")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "消息体", required = true, dataType = "JSONObject", paramType = "query"),
            @ApiImplicitParam(name = "signature", value = "加密签名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "timestamp", value = "时间戳", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nonce", value = "随机数", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/dingtalk/notify")
    public String dingtalkNotify(@RequestBody JSONObject body, @RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("encrypt", body.getString("encrypt"));
        parameters.put("signature", signature);
        parameters.put("timestamp", timestamp);
        parameters.put("nonce", nonce);
        return String.valueOf(incrementalSync(parameters));
    }

    public Object incrementalSync(Object parameter) {
        if (parameter instanceof Map) {
            Map map = (Map)parameter;
            return this.handleEvent(String.valueOf(map.get("encrypt")), String.valueOf(map.get("signature")), String.valueOf(map.get("timestamp")), String.valueOf(map.get("nonce")));
        } else {
            return "";
        }
    }

    private String handleEvent(String encrypt, String signature, String timestamp, String nonce) {
        String responseEncryMsg = "success";
//        String plainText = getDecryptMsg(encrypt, signature, timestamp, nonce);
//        threadPoolExec.execute(new DingtalkSynchronizeServiceImpl.handleCallBackThread(plainText));
//        return getEncryptedStr(responseEncryMsg, timestamp, nonce);
        return responseEncryMsg;
    }


}
