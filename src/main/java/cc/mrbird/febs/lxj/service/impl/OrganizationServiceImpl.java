package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.lxj.entity.OrgDepartment;
import cc.mrbird.febs.lxj.entity.OrgUser;
import cc.mrbird.febs.lxj.entity.OrgUserDepartment;
import cc.mrbird.febs.lxj.mapper.DepartmentMapper;
import cc.mrbird.febs.lxj.mapper.UserDepartmentMapper;
import cc.mrbird.febs.lxj.mapper.UserMapper;
import cc.mrbird.febs.lxj.service.DingSetService;
import cc.mrbird.febs.lxj.service.OrganizationService;
import cc.mrbird.febs.lxj.utils.Constant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentGetRequest;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiUserListbypageRequest;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiUserListbypageResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author:wangyong
 * @Date:2020/4/20 10:09
 * @Description:
 */
@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    DingSetService dingSetService;

    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    UserDepartmentMapper userDepartmentMapper;

    private Map<String, OrgDepartment> departmentMap = new HashMap<>();

    /**
     * @author: wangyong
     * @date: 2020/4/20 10:10
     * @return: void
     * @description: 同步组织架构
     */
    @Override
    @Transactional
    public boolean synchronous() throws ApiException {
        String accessToken = dingSetService.getAccessToken();
//        synchronousDepartment();
//        OapiUserListbypageResponse sourceUser = getSourceUser("130015871");
        synchronousDepartment();
        synchronousUser();
        return true;
    }

    /**
     * 同步主部门
     *
     * @throws ApiException
     */
    @Override
    public void synchronousDepartment() throws ApiException {
        String accessToken = dingSetService.getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(Constant.GET_DEPARTMENT_URL);
        OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
        request.setId("1");
        request.setHttpMethod("GET");
        OapiDepartmentGetResponse response = client.execute(request, accessToken);
        if (response != null) {
            OrgDepartment orgDepartment = new OrgDepartment();
            // 系统部门id
            orgDepartment.setId(UUID.randomUUID().toString().replace("-", ""));
            // 部门名称
            orgDepartment.setName(response.getName());
            // 系统部门父id
            orgDepartment.setParentId(null);
            // 钉钉部门id
            orgDepartment.setSourceId(response.getId() + "");
            // 钉钉部门父id
            orgDepartment.setSourceParentId(response.getParentid() + "");
            // 钉钉部门标识
            orgDepartment.setSourceIdentifier(response.getSourceIdentifier());
            OrgDepartment departmentById = departmentMapper.getDepartmentById(response.getId() + "");
            if (departmentById != null) {
                // 更新部门
                orgDepartment.setId(departmentById.getId());
                departmentMapper.updateDepartment(orgDepartment);
                log.info("更新部门数据：" + orgDepartment);
            } else {
                // 新增部门
                departmentMapper.insertDepartment(orgDepartment);
                log.info("新增部门数据：" + orgDepartment);
            }
            // 递归
            synchronousDepartments(response.getId() + "", orgDepartment.getId());
        }
    }

    /**
     * 同步部门
     *
     * @param sourceId 钉钉部门id
     * @param parentId 系统部门父id
     */
    @Override
    public void synchronousDepartments(String sourceId, String parentId) {
        String accessToken = null;
        try {
            accessToken = dingSetService.getAccessToken();
            if (accessToken == null) {
                log.info("没有获取到accessToken");
            }
            log.info("accessToken:" + accessToken);
            // 获取部门
            DingTalkClient client = new DefaultDingTalkClient(Constant.GET_DEPARTMENT_LIST_URL);
            OapiDepartmentListRequest request = new OapiDepartmentListRequest();
            request.setId(sourceId);
            request.setFetchChild(false);
            request.setHttpMethod("GET");
            OapiDepartmentListResponse response = client.execute(request, accessToken);
            List<OapiDepartmentListResponse.Department> department = response.getDepartment();
            if (department != null && department.size() > 0) {
                for (OapiDepartmentListResponse.Department d : department) {
                    OrgDepartment orgDepartment = new OrgDepartment();
                    // 系统部门id
                    orgDepartment.setId(UUID.randomUUID().toString().replace("-", ""));
                    // 部门名称
                    orgDepartment.setName(d.getName());
                    // 系统部门父id
                    orgDepartment.setParentId(parentId);
                    // 钉钉部门id
                    orgDepartment.setSourceId(d.getId() + "");
                    // 钉钉部门父id
                    orgDepartment.setSourceParentId(d.getParentid() + "");
                    // 钉钉部门标识
                    orgDepartment.setSourceIdentifier(d.getSourceIdentifier());
                    OrgDepartment departmentById = departmentMapper.getDepartmentById(d.getId() + "");
                    if (departmentById != null) {
                        // 更新部门
                        orgDepartment.setId(departmentById.getId());
                        departmentMapper.updateDepartment(orgDepartment);
                        log.info("更新部门数据：" + orgDepartment);
                    } else {
                        // 新增部门
                        departmentMapper.insertDepartment(orgDepartment);
                        log.info("新增部门数据：" + orgDepartment);
                    }
                    // 递归
                    synchronousDepartments(d.getId() + "", orgDepartment.getId());
                }
            } else {
                return;
            }
        } catch (ApiException e) {
            e.printStackTrace();
            log.info("获取accessToken失败");
        }
    }

    /**
     * 同步员工
     */
    @Override
    public void synchronousUser() throws ApiException {
        List<OrgDepartment> allDepartments = departmentMapper.getAllDepartments();
        for (OrgDepartment allDepartment : allDepartments) {
            departmentMap.put(allDepartment.getSourceId(), allDepartment);
        }
        for (OrgDepartment orgDepartment : allDepartments) {
            synchronousUserByDepartmentId(orgDepartment.getSourceId());
        }
    }

    /**
     * 根据部门id同步员工数据
     *
     * @param sourceDepartmentId 部门id
     * @throws ApiException
     * @author wangyong
     */
    @Override
    public void synchronousUserByDepartmentId(String sourceDepartmentId) throws ApiException {
        OapiUserListbypageResponse response = null;
        List<OrgUser> insertUsers = new ArrayList<>();
        List<OrgUser> updateUsers = new ArrayList<>();
        List<OrgUserDepartment> insertUserDepartments = new ArrayList<>();
        List<OrgUserDepartment> updateUserDepartment = new ArrayList<>();
        do {
            response = getSourceUser(sourceDepartmentId);
            if (response != null) {
                List<OapiUserListbypageResponse.Userlist> userlist = response.getUserlist();
                if (userlist != null && userlist.size() > 0) {
                    for (OapiUserListbypageResponse.Userlist user : userlist) {
                        OrgUser orgUser = userSet(sourceDepartmentId, user);
                        String userId = userMapper.getIdBySourceId(orgUser.getSourceId());
                        if (!StringUtils.isEmpty(userId)) {
                            // 用户以及存在系统中，更新
                            orgUser.setId(userId);
                            log.info("更新员工数据：" + orgUser);
                            updateUsers.add(orgUser);
                        } else {
                            // 用户不存在系统中，插入
                            log.info("插入员工数据：" + orgUser);
                            insertUsers.add(orgUser);
                        }
                        String departmentIds = user.getDepartment();
                        if (!StringUtils.isEmpty(departmentIds)) {
                            // 存在部门
                            JSONArray ids = JSON.parseArray(departmentIds);
                            for (Object id : ids) {
                                OrgDepartment departmentById = getOrgDepartmentBySourceId(sourceDepartmentId);
                                OrgUserDepartment orgUserDepartment = new OrgUserDepartment();
                                orgUserDepartment.setId(UUID.randomUUID().toString().replace("-", ""));
                                orgUserDepartment.setSourceId(user.getUserid());
                                orgUserDepartment.setSourceDepartmentId(id + "");
                                orgUserDepartment.setUserId(orgUser.getId());
                                orgUserDepartment.setDepartmentId(departmentById != null ? departmentById.getId() : "");
                                orgUserDepartment.setIsShow(1);
                                String udId = userDepartmentMapper.getIdBySuidAndSdid(user.getUserid(), id + "");
                                if (!StringUtils.isEmpty(udId)) {
                                    // 存在了
                                    orgUserDepartment.setId(udId);
                                    updateUserDepartment.add(orgUserDepartment);
                                    log.info("更新员工部门对应表：" + orgUserDepartment);
                                } else {
                                    insertUserDepartments.add(orgUserDepartment);
                                    log.info("插入员工部门对应表：" + orgUserDepartment);
                                }
                            }
                        }
                    }
                }
            }
        } while (response != null && response.getHasMore());
        if (insertUsers.size() > 0) {
            // 批量插入员工数据
            for (int i = 0; i <= insertUsers.size() / 1000; i++) {
                if (i == insertUsers.size() / 1000) {
                    userMapper.insertUsers(insertUsers.subList(i * 1000, insertUsers.size()));
                } else {
                    userMapper.insertUsers(insertUsers.subList(i * 1000, (i + 1) * 1000));
                }
            }
        }
        if (updateUsers.size() > 0) {
            // 批量更新员工数据
            for (int i = 0; i <= updateUsers.size() / 1000; i++) {
                if (i == insertUsers.size() / 1000) {
                    userMapper.updateUsers(updateUsers.subList(i * 1000, updateUsers.size()));
                } else {
                    userMapper.updateUsers(updateUsers.subList(i * 1000, (i + 1) * 1000));
                }
            }
        }
        if (insertUserDepartments.size() > 0) {
            // 批量插入员工部门对应表数据
            for (int i = 0; i <= insertUserDepartments.size() / 1000; i++) {
                if (i == insertUserDepartments.size() / 1000) {
                    userDepartmentMapper.insertUserDepartments(insertUserDepartments.subList(i * 1000, insertUserDepartments.size()));
                } else {
                    userDepartmentMapper.insertUserDepartments(insertUserDepartments.subList(i * 1000, (i + 1) * 1000));
                }
            }
        }
        if (updateUserDepartment.size() > 0) {
            // 批量更新员工部门对应表数据
            for (int i = 0; i <= updateUserDepartment.size() / 1000; i++) {
                if (i == updateUserDepartment.size() / 1000) {
                    userDepartmentMapper.updateUserDepartments(updateUserDepartment.subList(i * 1000, updateUserDepartment.size()));
                } else {
                    userDepartmentMapper.updateUserDepartments(updateUserDepartment.subList(i * 1000, (i + 1) * 1000));
                }
            }
        }
    }

    /**
     * 设置员工数据
     *
     * @param sourceDepartmentId 部门id
     * @param user               钉钉的员工数据
     * @return 员工数据
     */
    private OrgUser userSet(String sourceDepartmentId, OapiUserListbypageResponse.Userlist user) {
        OrgDepartment orgDepartment = getOrgDepartmentBySourceId(sourceDepartmentId);
        OrgUser orgUser = new OrgUser();
        // id
        orgUser.setId(UUID.randomUUID().toString().replace("-", ""));
        // 员工在当前企业的唯一标识
        orgUser.setUnionid(user.getUnionid());
        // 员工钉钉id
        orgUser.setSourceId(user.getUserid());
        // 员工钉钉部门id
        orgUser.setSourceDepartmentId(orgDepartment != null ? orgDepartment.getId() : null);
        // 员工系统部门id
        orgUser.setDepartmentId(sourceDepartmentId);
        // 部门中的排序
        orgUser.setSortKey(user.getOrder() + "");
        // 手机号
        orgUser.setMobile(user.getMobile());
        // 分机号
        orgUser.setTel(user.getTel());
        // 办公地点
        orgUser.setWorkPlace(user.getWorkPlace());
        // 是否管理员
        orgUser.setIsAdmin(user.getIsAdmin() ? 1 : 0);
        // 是否老板
        orgUser.setIsBoss(user.getIsBoss() ? 1 : 0);
        // 是否隐藏号码
        orgUser.setIsHide(user.getIsHide() ? 1 : 1);
        // 是否部门主管
        orgUser.setIsLeader(user.getIsLeader() ? 1 : 0);
        // 员工名称
        orgUser.setName(user.getName());
        // 是否激活了钉钉
        orgUser.setActive(user.getActive() ? 1 : 0);
        // 职位信息
        orgUser.setPosition(user.getPosition());
        // 员工邮箱
        orgUser.setEmail(user.getEmail());
        // 企业邮箱
        orgUser.setOrgEmail(user.getOrgEmail());
        // 头像
        orgUser.setAvatar(user.getAvatar());
        // 工号
        orgUser.setJobnumber(user.getJobnumber());
        // 入职时间
        orgUser.setHiredDate(user.getHiredDate());
        // 是否隐藏
        orgUser.setIsShow(1);
        return orgUser;
    }

    /**
     * 根据钉钉部门id获取系统的部门数据
     *
     * @param sourceDepartmentId 钉钉部门id
     * @return 系统部门数据
     * @author wangyong
     */
    private OrgDepartment getOrgDepartmentBySourceId(String sourceDepartmentId) {
        OrgDepartment department;
        if (departmentMap.containsKey(sourceDepartmentId)) {
            department = departmentMap.get(sourceDepartmentId);
        } else {
            department = departmentMapper.getDepartmentById(sourceDepartmentId);
            departmentMap.put(sourceDepartmentId, department);
        }
        return department;
    }

    /**
     * 根据部门id获取该部门的员工数据
     *
     * @param departmentId 部门id
     * @return 部门员工数据
     * @throws ApiException
     * @author wangyong
     */
    private OapiUserListbypageResponse getSourceUser(String departmentId) throws ApiException {
        String accessToken = dingSetService.getAccessToken();
        DingTalkClient client = new DefaultDingTalkClient(Constant.GET_USER_LIST_URL);
        OapiUserListbypageRequest request = new OapiUserListbypageRequest();
        request.setDepartmentId(Long.parseLong(departmentId));
        request.setOffset(0L);
        request.setSize(100L);
        request.setOrder("entry_desc");
        request.setHttpMethod("GET");
        OapiUserListbypageResponse execute = client.execute(request, accessToken);
        return execute;
    }
}
