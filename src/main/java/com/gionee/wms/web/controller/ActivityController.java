package com.gionee.wms.web.controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.Activity;
import com.gionee.wms.service.activity.ActivityService;
import com.gionee.wms.service.stock.UcUserService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.extend.DwzMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gionee on 2017/7/4.
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {

    private static Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityService activityService;
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
        int total = activityService.queryCount(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();

        List<Activity> list;
        if (total == 0) {
            list = new ArrayList<>(0);
        } else {
            queryMap.put("page", page);
            list = activityService.query(queryMap.getMap());
        }

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("currentUser", getCurrentUserName());
        modelMap.addAttribute("count", initActionTotal(queryMap));
        return "activity/list";
    }

    // 获取进行中活动数量
    private int initActionTotal(QueryMap queryMap) {
        queryMap.put("status", "活动中");
        return activityService.queryCount(queryMap.getMap());
    }

    private String getCurrentUserName() {
        return ucUserService.get(ActionUtils.getLoginName(), null).getUserName();
    }

    /**
     * @param queryMap
     * @param activity
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public Object save(QueryMap queryMap, Activity activity, MultipartFile uploadFile) {
        try {
            ServiceCtrlMessage message;
            if (uploadFile != null && !StringUtils.isEmpty(uploadFile.getOriginalFilename())) {
                activity.setAccessory(uploadFile.getBytes());
                activity.setFileName(uploadFile.getOriginalFilename());
            }
            if (activity != null && activity.getId() == null) {
                activity.setSponsor(getCurrentUserName());
                message = activityService.save(activity);
            } else {
                message = activityService.update(activity);
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
            Activity activity = activityService.get(id);
            modelMap.addAttribute("activity", activity);
        }
        modelMap.addAttribute("currentUser", getCurrentUserName());
        return "activity/" + to;
    }

    /**
     * @param activity
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public Object update(Activity activity) {
        return activityService.update(activity);
    }


    @RequestMapping("down.do")
    public ResponseEntity<byte[]> upload(Long id) throws UnsupportedEncodingException {
        Activity activity = activityService.get(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", new String(activity.getFileName().getBytes("UTF-8"), "iso-8859-1"));  //解决文件名中文乱码问题
        return new ResponseEntity<>(activity.getAccessory(), headers, HttpStatus.CREATED);
    }

}
