package com.gionee.wms.web.controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.UcUser;
import com.gionee.wms.service.stock.UcUserService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.extend.DwzMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pengbin on 2017/6/7.
 */
@Controller
@RequestMapping("/uc/")
public class UcUserController {

    private final Logger logger = LoggerFactory.getLogger(UcUserController.class);

    @Autowired
    private UcUserService ucUserService;

    @RequestMapping("list.do")
    public String list(ModelMap modelMap, QueryMap queryMap, Page page) {
        int total = ucUserService.queryCount(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();

        List<UcUser> list;
        if (total == 0) {
            list = new ArrayList<>(0);
        } else {
            queryMap.put("page", page);
            list = ucUserService.query(queryMap.getMap());
        }

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("page", page);
        return "uc/list_user";
    }

    /**
     * @param modelMap
     * @param id
     * @param to
     * @return
     */
    @RequestMapping("to.do")
    public String to(ModelMap modelMap, QueryMap queryMap, Long id, String to) {
        if (id != null) {
            UcUser ucUser = ucUserService.get(id);
            modelMap.addAttribute("ucUser", ucUser);
        }
        modelMap.addAllAttributes(queryMap.getMap());
        return "uc/" + to;
    }

    /**
     * @param queryMap
     * @param ucUser
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public Object save(QueryMap queryMap, UcUser ucUser) {
        try {
            ServiceCtrlMessage message = ucUserService.save(ucUser);
            //return message.isResult() ? DwzMessage.success("", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
            return DwzMessage.success("成功", queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

}
