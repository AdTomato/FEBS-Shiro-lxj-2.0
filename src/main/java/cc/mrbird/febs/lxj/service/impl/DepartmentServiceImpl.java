package cc.mrbird.febs.lxj.service.impl;


import cc.mrbird.febs.lxj.entity.OrgDepartment;
import cc.mrbird.febs.lxj.mapper.DepartmentMapper;
import cc.mrbird.febs.lxj.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:wangyong
 * @Date:2020/4/17 11:54
 * @Description:
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    DepartmentMapper departmentMapper;

    @Override
    public List<OrgDepartment> getAllDepartment() {
//        return departmentMapper.getAllDepartment();
        return null;
    }
}
