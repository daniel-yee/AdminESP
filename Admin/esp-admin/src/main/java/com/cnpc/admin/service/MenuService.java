package com.cnpc.admin.service;

import com.cnpc.admin.constant.CommonConstant;
import com.cnpc.admin.entity.Menu;
import com.cnpc.admin.mapper.MenuMapper;
import com.cnpc.common.service.BaseService;
import com.cnpc.common.util.CodeUtil;
import com.cnpc.common.vo.PermissionInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-12
 */
@Service
public class MenuService extends BaseService<MenuMapper, Menu> {

    @Override
    public Menu selectById(Object id) {
        Menu menu = super.selectById(id);
        if (!menu.getParentId().equals(CommonConstant.ROOT)) {
            Menu parentMenu = super.selectById(menu.getParentId());
            menu.setParentName(parentMenu.getName());
        } else {
            menu.setParentName("根节点");
        }
        return menu;
    }


    public String getNextCode(String parentId) {
        String maxCode = this.mapper.selectMaxLevelCodeByParentId(parentId);
        String prefix = "";
        if (!parentId.equals(CommonConstant.ROOT)) {
            Menu menu = super.selectById(parentId);
            prefix = menu.getLevelcode();
        }
        return CodeUtil.nextCode(prefix, maxCode, 6);
    }


    public void menuToPermissionInfo(List<Menu> menus, List<PermissionInfo> permissionInfos) {
        for (Menu menu : menus) {
            /*if(StringUtils.isBlank(menu.getHref())){
                menu.setHref("/"+menu.getCode());
            }*/
            PermissionInfo info =new PermissionInfo();
            info.setCode(menu.getCode());
            info.setType(menu.getType());
            info.setName(menu.getName());
            String uri=menu.getHref();
            info.setUri(uri);
            info.setComponent(menu.getComponent());
            info.setMethod(CommonConstant.RESOURCE_REQUEST_METHOD_GET);
            info.setMenu(menu.getId());
            permissionInfos.add(info);
        }
    }
}
