package com.cnpc.admin.service;

import com.cnpc.admin.constant.CommonConstant;
import com.cnpc.admin.entity.Element;
import com.cnpc.admin.mapper.ElementMapper;
import com.cnpc.common.message.TableResultResponse;
import com.cnpc.common.service.BaseService;
import com.cnpc.common.util.Query;
import com.cnpc.common.vo.PermissionInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-10-16
 */
@Service
public class ElementService extends BaseService<ElementMapper,Element> {

    @Override
    public TableResultResponse<Element> selectByQuery(Query query) {
        Example example = new Example(Element.class);
        Example.Criteria criteria = example.createCriteria();
        String menuId=query.get("menuId").toString();
        String name= "";
        if(query.get("name")!=null) {
            name = query.get("name").toString();
            criteria.andLike(name, "%" + name + "%");
        }
        criteria.andEqualTo("menuId",menuId);
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<Element> list = mapper.selectByExample(example);
        return new TableResultResponse<>(result.getTotal(), list);
    }


    public TableResultResponse<Element> selectByQueryAll(Query query) {
        Example example = new Example(Element.class);
        Example.Criteria criteria = example.createCriteria();
        String menuId=query.get("menuId").toString();
        criteria.andEqualTo("menuId",menuId);
        List<Element> list = mapper.selectByExample(example);
        return new TableResultResponse<>(list.size(),list);
    }

    public void elementToPermissionInfo(List<Element> elements, List<PermissionInfo> permissionInfos) {
        for (Element element : elements) {
            PermissionInfo info =new PermissionInfo();
            info.setCode(element.getCode());
            info.setUri(element.getUri());
            info.setName(element.getName());
            info.setMenu(element.getMenuId());
            info.setMethod(element.getMethod());
            info.setType(CommonConstant.RESOURCE_TYPE_ELEMENT);
            info.setComponent(element.getUri());
            permissionInfos.add(info);
        }
    }
}
