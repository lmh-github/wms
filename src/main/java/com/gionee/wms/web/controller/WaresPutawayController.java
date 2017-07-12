package com.gionee.wms.web.controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.WaresPutaway;
import com.gionee.wms.service.stock.UcUserService;
import com.gionee.wms.service.wares.WaresPutawayService;
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
 * Created by gionee on 2017/7/12.
 */
@Controller
@RequestMapping("wares/putaway")
public class WaresPutawayController {

    private static Logger logger = LoggerFactory.getLogger(WaresPutawayController.class);

    @Autowired
    private WaresPutawayService waresPutawayService;

    @Autowired
    private UcUserService ucUserService;

    /**
     * @param modelMap
     * @param queryMap
     * @param page
     * @return
     */
    @RequestMapping("list.do")
    public String list(ModelMap modelMap, QueryMap queryMap, Page page) {
        int total = waresPutawayService.queryCount(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();

        List<WaresPutaway> list;
        if (total == 0) {
            list = new ArrayList<>(0);
        } else {
            queryMap.put("page", page);
            list = waresPutawayService.query(queryMap.getMap());
        }

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("currentUser", getCurrentUserName());
        return "wares/putaway/list";
    }


    private String getCurrentUserName() {
        return ucUserService.get(ActionUtils.getLoginName(), null).getUserName();
    }

    /**
     * @param queryMap
     * @param waresPutaway
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public Object save(QueryMap queryMap, WaresPutaway waresPutaway) {
        try {
            ServiceCtrlMessage message;
            if (waresPutaway != null && waresPutaway.getId() == null) {
                waresPutaway.setSponsor(getCurrentUserName());
                message = waresPutawayService.save(waresPutaway);
            } else {
                message = waresPutawayService.update(waresPutaway);
            }
            return message.isResult() ? DwzMessage.success("", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    /**
     * @param modelMap
     * @param id
     * @param to
     * @return
     */
    @RequestMapping("to.do")
    public String to(ModelMap modelMap, Long id, String to) {
        if (id != null) {
            WaresPutaway waresPutaway = waresPutawayService.get(id);
            modelMap.addAttribute("waresPutaway", waresPutaway);
        }
        modelMap.addAttribute("currentUser", getCurrentUserName());
        return "wares/putaway/" + to;
    }

    /**
     * @param waresPutaway
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public Object update(WaresPutaway waresPutaway) {
        return waresPutawayService.update(waresPutaway);
    }


}
