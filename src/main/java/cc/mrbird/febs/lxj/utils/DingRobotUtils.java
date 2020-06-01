package cc.mrbird.febs.lxj.utils;

import cc.mrbird.febs.lxj.entity.TeamInfo;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DingRobotUtils
 * @author: lfh
 * @Date:2020/5/28 13:12
 * @Description:
 **/
@Slf4j
public class DingRobotUtils {
    /**
     * 钉钉群设置 webhook, 支持重置
     */
    private  static final String ACCESS_TOKEN ="https://oapi.dingtalk.com/robot/send?access_token=993fb19d2b65facda499c00d9d1d8243460bfe78544ac7429abb486451372d21";
    /**
     * 消息类型
     */
    private static final String MSG_TYPE_TEXT = "text";
    private static final String MSG_TYPE_LINK = "link";
    private static final String MSG_TYPE_MARKDOWN = "markdown";
    private static final String MSG_TYPE_ACTION_CARD = "actionCard";
    private static final String MSG_TYPE_FEED_CARD = "feedCard";

    /**
     * 客户端实例
     */
    public static DingTalkClient client = new DefaultDingTalkClient(ACCESS_TOKEN);
    //test
    public void test(List<TeamInfo> allTeamInfo) {
        try {
            //钉钉机器人地址（配置机器人的webhook）
            String dingUrl = ACCESS_TOKEN;

            //是否通知所有人
            boolean isAtAll = false;
            //通知具体人的手机号码列表
            List<String> mobileList = Lists.newArrayList();
            mobileList.add("18551383916");
            //钉钉机器人消息内容
            String content = "查询班组信息" + allTeamInfo.get(0);
            //组装请求内容
            String reqStr = buildReqStr(content, isAtAll, mobileList);

            //推送消息（http请求）
            String result = postJson(dingUrl, reqStr);
            System.out.println("result == " + result);

        } catch (Exception e) {
            e.printStackTrace();

        }


    }
    /**
     * 组装请求报文
     * @param content
     * @return
     */
    private static final int timeout = 10000;

    public static String postJson(String url, String reqStr) {
        String body = null;
        try {
            body = HttpRequest.post(url).body(reqStr).timeout(timeout).execute().body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }
    private static String buildReqStr(String content, boolean isAtAll, List<String> mobileList) {
        //消息内容
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("content", content);
        //通知人
        Map<String, Object> atMap = new HashMap<>();
        //1.是否通知所有人
        atMap.put("isAtAll", isAtAll);
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", mobileList);

        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);

        return JSON.toJSONString(reqMap);
    }

    /**
     * @description: 官方演示示例
     *      title 是消息列表下透出的标题
     *      text 是进入群后看到的消息内容
     *
     * @author: niaonao
     * @date: 2019/7/6
     */
    public static void sdkDemoJava() {
        DingTalkClient client = DingRobotUtils.client;
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent("测试文本消息");
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(Arrays.asList("13261303345"));
        request.setAt(at);

        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl("https://www.dingtalk.com/");
        link.setPicUrl("");
        link.setTitle("时代的火车向前开");
        link.setText("这个即将发布的新版本，创始人陈航（花名“无招”）称它为“红树林”。\n" +
                "而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是“红树林");
        request.setLink(link);

        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle("杭州天气");
        markdown.setText("#### 杭州天气 @156xxxx8827\n" +
                "> 9度，西北风1级，空气良89，相对温度73%\n\n" +
                "> ![screenshot](https://gw.alipayobjects.com/zos/skylark-tools/public/files/84111bbeba74743d2771ed4f062d1f25.png)\n"  +
                "> ###### 10点20分发布 [天气](http://www.thinkpage.cn/) \n");
        request.setMarkdown(markdown);
        try {
            client.execute(request);
        } catch (ApiException e) {
            log.error("[ApiException]: 消息发送演示示例, 异常捕获{}", e.getMessage());
        }
    }
    /**
     * @description: 发送普通文本消息
     * @param content   文本消息
     * @param mobileList    指定@ 联系人
     * @param isAtAll       是否@ 全部联系人
     * @return: com.dingtalk.api.response.OapiRobotSendResponse
     * @author: niaonao
     * @date: 2019/7/6
     */
    public static OapiRobotSendResponse sendMessageByText(String content, List<String> mobileList, boolean isAtAll) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        //参数	参数类型	必须	说明
        //msgtype	String	是	消息类型，此时固定为：text
        //content	String	是	消息内容
        //atMobiles	Array	否	被@人的手机号(在content里添加@人的手机号)
        //isAtAll	bool	否	@所有人时：true，否则为：false
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        if (!CollectionUtils.isEmpty(mobileList)) {
            // 发送消息并@ 以下手机号联系人
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(mobileList);
            at.setIsAtAll(isAtAll ? "true" : "false");
            request.setAt(at);
        }
        request.setMsgtype(DingRobotUtils.MSG_TYPE_TEXT);
        request.setText(text);

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingRobotUtils.client.execute(request);
        } catch (ApiException e) {
            log.error("[发送普通文本消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }
    /**
     * @description: 发送link 类型消息
     * @param title 消息标题
     * @param text  消息内容
     * @param messageUrl     点击消息后跳转的url
     * @param picUrl    插入图片的url
     * @return: com.dingtalk.api.response.OapiRobotSendResponse
     * @author: niaonao
     * @date: 2019/7/6
     */
    public static OapiRobotSendResponse sendMessageByLink(String title, String text, String messageUrl, String picUrl) {
        if (!DataValidUtil.checkNotEmpty(title, text, messageUrl)) {
            return null;
        }
        //参数	参数类型	必须	说明
        //msgtype	String	是	消息类型，此时固定为：link
        //title	String	是	消息标题
        //text	String	是	消息内容。如果太长只会部分展示
        //messageUrl	String	是	点击消息跳转的URL
        //picUrl	String	否	图片URL
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setTitle(title);
        link.setText(text);
        link.setMessageUrl(messageUrl);
        link.setPicUrl(picUrl);

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingRobotUtils.MSG_TYPE_LINK);
        request.setLink(link);

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingRobotUtils.client.execute(request);
        } catch (ApiException e) {
            log.error("[发送link 类型消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }


    /**
     * @description: 发送Markdown 编辑格式的消息
     * @param title 标题
     * @param markdownText  支持markdown 编辑格式的文本信息
     * @param mobileList    消息@ 联系人
     * @param isAtAll   是否@ 全部
     * @return: com.dingtalk.api.response.OapiRobotSendResponse
     * @author: niaonao
     * @date: 2019/7/6
     */
    public static OapiRobotSendResponse sendMessageByMarkdown(String title, String markdownText, List<String> mobileList, boolean isAtAll) {
        if (!DataValidUtil.checkNotEmpty(title, markdownText)) {
            return null;
        }
        //参数	类型	必选	说明
        //msgtype	String	是	此消息类型为固定markdown
        //title	String	是	首屏会话透出的展示内容
        //text	String	是	markdown格式的消息
        //atMobiles	Array	否	被@人的手机号(在text内容里要有@手机号)
        //isAtAll	bool	否	@所有人时：true，否则为：false
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(markdownText);

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingRobotUtils.MSG_TYPE_MARKDOWN);
        request.setMarkdown(markdown);
        if (!CollectionUtils.isEmpty(mobileList)) {
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setIsAtAll(isAtAll ? "true" : "false");
            at.setAtMobiles(mobileList);
            request.setAt(at);
        }

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingRobotUtils.client.execute(request);
        } catch (ApiException e) {
            log.error("[发送link 类型消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * @description: 整体跳转ActionCard类型的消息发送
     * @param title 消息标题, 会话消息会展示标题
     * @param markdownText  markdown格式的消息
     * @param singleTitle   单个按钮的标题
     * @param singleURL 单个按钮的跳转链接
     * @param btnOrientation    是否横向排列(true 横向排列, false 纵向排列)
     * @param hideAvatar    是否隐藏发消息者头像(true 隐藏头像, false 不隐藏)
     * @return: com.dingtalk.api.response.OapiRobotSendResponse
     * @author: niaonao
     * @date: 2019/7/6
     */
    public static OapiRobotSendResponse sendMessageByActionCardSingle(String title, String markdownText, String singleTitle, String singleURL, boolean btnOrientation, boolean hideAvatar) {
        if (!DataValidUtil.checkNotEmpty(title, markdownText)) {
            return null;
        }
        //参数	类型	必选	说明
        //    msgtype	string	true	此消息类型为固定actionCard
        //    title	string	true	首屏会话透出的展示内容
        //    text	string	true	markdown格式的消息
        //    singleTitle	string	true	单个按钮的方案。(设置此项和singleURL后btns无效)
        //    singleURL	string	true	点击singleTitle按钮触发的URL
        //    btnOrientation	string	false	0-按钮竖直排列，1-按钮横向排列
        //    hideAvatar	string	false	0-正常发消息者头像，1-隐藏发消息者头像
        OapiRobotSendRequest.Actioncard actionCard = new OapiRobotSendRequest.Actioncard();
        actionCard.setTitle(title);
        actionCard.setText(markdownText);
        actionCard.setSingleTitle(singleTitle);
        actionCard.setSingleURL(singleURL);
        // 此处默认为0
        actionCard.setBtnOrientation(btnOrientation ? "1" : "0");
        // 此处默认为0
        actionCard.setHideAvatar(hideAvatar ? "1" : "0");

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingRobotUtils.MSG_TYPE_ACTION_CARD);
        request.setActionCard(actionCard);
        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingRobotUtils.client.execute(request);
        } catch (ApiException e) {
            log.error("[发送ActionCard 类型消息]: 整体跳转ActionCard类型的发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * @description: 独立跳转ActionCard类型 消息发送
     * @param title 标题
     * @param markdownText  文本
     * @param btns  按钮列表
     * @param btnOrientation    是否横向排列(true 横向排列, false 纵向排列)
     * @param hideAvatar    是否隐藏发消息者头像(true 隐藏头像, false 不隐藏)
     * @return: com.dingtalk.api.response.OapiRobotSendResponse
     * @author: niaonao
     * @date: 2019/7/6
     */
    public static OapiRobotSendResponse sendMessageByActionCardMulti(String title, String markdownText, List<OapiRobotSendRequest.Btns> btns, boolean btnOrientation, boolean hideAvatar) {
        if (!DataValidUtil.checkNotEmpty(title, markdownText) || CollectionUtils.isEmpty(btns)) {
            return null;
        }
        //参数	类型	必选	说明
        //msgtype	string	true	此消息类型为固定actionCard
        //title	string	true	首屏会话透出的展示内容
        //text	string	true	markdown格式的消息
        //btns	array	true	按钮的信息：title-按钮方案，actionURL-点击按钮触发的URL
        //btnOrientation	string	false	0-按钮竖直排列，1-按钮横向排列
        //hideAvatar	string	false	0-正常发消息者头像，1-隐藏发消息者头像
        OapiRobotSendRequest.Actioncard actionCard = new OapiRobotSendRequest.Actioncard();
        actionCard.setTitle(title);
        actionCard.setText(markdownText);
        // 此处默认为0
        actionCard.setBtnOrientation(btnOrientation ? "1" : "0");
        // 此处默认为0
        actionCard.setHideAvatar(hideAvatar ? "1" : "0");

        actionCard.setBtns(btns);

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingRobotUtils.MSG_TYPE_ACTION_CARD);
        request.setActionCard(actionCard);
        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingRobotUtils.client.execute(request);
        } catch (ApiException e) {
            log.error("[发送ActionCard 类型消息]: 独立跳转ActionCard类型发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    /**
     * @description: 发送FeedCard类型消息
     * @param links
     * @return: com.dingtalk.api.response.OapiRobotSendResponse
     * @author: niaonao
     * @date: 2019/7/6
     */
    public static OapiRobotSendResponse sendMessageByFeedCard(List<OapiRobotSendRequest.Links> links) {
        if (CollectionUtils.isEmpty(links)) {
            return null;
        }

        //msgtype	string	true	此消息类型为固定feedCard
        //title	string	true	单条信息文本
        //messageURL	string	true	点击单条信息到跳转链接
        //picURL	string	true	单条信息后面图片的URL
        OapiRobotSendRequest.Feedcard feedcard = new  OapiRobotSendRequest.Feedcard();
        feedcard.setLinks(links);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingRobotUtils.MSG_TYPE_FEED_CARD);
        request.setFeedCard(feedcard);
        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = DingRobotUtils.client.execute(request);
        } catch (ApiException e) {
            log.error("[发送ActionCard 类型消息]: 独立跳转ActionCard类型发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }
/*    版权声明：本文为CSDN博主「闵渭凯」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
    原文链接：https://blog.csdn.net/weixin_41187876/article/details/99959559*/
}
