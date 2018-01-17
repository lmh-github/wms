package com.gionee.wms.web.controller;

import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.StorePlatform;
import com.gionee.wms.service.stock.StorePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 可销售库存平台分配仅东莞电商仓
 */
@Controller
@RequestMapping("/store/platform")
public class StorePlatformController {
    @Autowired
    private StorePlatformService storePlatformService;

    @RequestMapping("/create.do")
    @ResponseBody
    public Object create(StorePlatform record){
        return null;
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public Object delete(StorePlatform record){
        return null;
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public Object update(StorePlatform record){
        return null;
    }

    @RequestMapping("/list.do")
    public String list(QueryMap queryMap, Model model){
        List<StorePlatform> list = storePlatformService.getAll(queryMap.getMap());
        model.addAttribute("list",list);
        return "stock/platform/list";
    }


}
