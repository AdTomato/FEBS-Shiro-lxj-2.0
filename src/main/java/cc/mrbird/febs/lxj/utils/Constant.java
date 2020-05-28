package cc.mrbird.febs.lxj.utils;

/**
 * @Author:wangyong
 * @Date:2020/4/20 10:31
 * @Description:
 */
public class Constant {

    /**
     * 钉钉的accessToken
     */
    public static final String ACCESS_TOKEN = "accessToken";

    /**
     * 获取钉钉access token的url
     */
    public static final String GET_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

    /**
     * 获取钉钉部门列表的url
     */
    public static final String GET_DEPARTMENT_LIST_URL = "https://oapi.dingtalk.com/department/list";

    /**
     * 获取钉钉部门的url
     */
    public static final String GET_DEPARTMENT_URL = "https://oapi.dingtalk.com/department/get";

    /**
     * 获取钉钉用户列表url
     */
    public static final String GET_USER_LIST_URL = "https://oapi.dingtalk.com/user/listbypage";

    /**
     * 注册事件回调url
     */
    public static final String REGISTER_CALL_BACK_URL = "https://oapi.dingtalk.com/call_back/update_call_back";

    public static final String ATTENDANCE_CHECK_RECORD = "attendance_check_record";

    /**
     * 应用的SuiteKey，登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String SUITE_KEY="ding73b7db4ffb45819f35c2f4657eb6378f";

    /**
     * 应用的SuiteSecret，登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String SUITE_SECRET="";

    /**
     * 回调URL加解密用。应用的数据加密密钥，登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String ENCODING_AES_KEY = "e6e18d0a42dd027cade89c213c75f477e6e18d0a42d";

    /**
     * 回调URL签名用。应用的签名Token, 登录开发者后台，点击应用管理，进入应用详情可见
     */
    public static final String TOKEN = "123456";

    public static final String CORP_ID = "ding73b7db4ffb45819f35c2f4657eb6378f";

    //public static final String URL_HOST = "http://ys.ahdingtalk.com:8080/";
    public static final String URL_HOST = "http://dingtalk.free.idcfengye.com";
}
