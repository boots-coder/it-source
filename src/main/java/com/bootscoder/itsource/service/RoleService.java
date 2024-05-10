package com.bootscoder.itsource.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bootscoder.itsource.bean.PermissionWithStatus;
import com.bootscoder.itsource.mapper.PermissionMapper;
import com.bootscoder.itsource.mapper.RoleMapper;
import com.bootscoder.itsource.pojo.Permission;
import com.bootscoder.itsource.pojo.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public Page<Role> findPage(int page,int size){
        return roleMapper.selectPage(new Page(page,size),null);
    }

    public void add(Role role){
        roleMapper.insert(role);
    }

    public Role findById(Integer rid){
        return roleMapper.selectById(rid);
    }

    public void update(Role role){
        roleMapper.updateById(role);
    }

    public void delete(Integer rid){
        roleMapper.deleteById(rid);
    }

    // 查询角色的权限情况
    public List<PermissionWithStatus> findPermission(Integer rid){
        // 查询所有权限
        List<Permission> permissions = permissionMapper.selectList(null);
        // 查询角色拥有的权限id
        List<Integer> pids = permissionMapper.findPermissionIdByRole(rid);
        // 构建带有状态的权限结合
        List<PermissionWithStatus> permissionList = new ArrayList();
        for (Permission permission : permissions) {
            // 创建带有状态的权限
            PermissionWithStatus permissionWithStatus = new PermissionWithStatus();
            BeanUtils.copyProperties(permission,permissionWithStatus);
            // 判断角色是否拥有该权限
            if(pids.contains(permission.getPid())){
                permissionWithStatus.setRoleHas(true);
            }else{
                permissionWithStatus.setRoleHas(false);
            }
            permissionList.add(permissionWithStatus);
        }
        return permissionList;
    }

    // 给角色重新分配权限
    public void updatePermissions(Integer rid,Integer[] ids){
        // 删除角色的所有权限
        roleMapper.deleteRoleAllPermission(rid);

        int i = 1/0;

        // 重新给角色添加权限
        for (Integer pid : ids) {
            roleMapper.addRolePermission(rid,pid);
        }
    }
}
