package com.gionee.wms.web.controller;

import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.Attachment;
import com.gionee.wms.service.stock.AttachmentService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.extend.DwzMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Pengbin on 2017/6/8.
 */
@Controller
@RequestMapping("/atta/")
public class AttachmentController {

    private final Logger logger = LoggerFactory.getLogger(AttachmentController.class);

    @Autowired
    private AttachmentService attachmentService;

    /**
     * @param queryMap
     * @param attachment
     * @return
     */
    @RequestMapping("up.json")
    @ResponseBody
    public Object up(QueryMap queryMap, Attachment attachment, MultipartFile uploadFile) {
        try {
            String basePath = System.getProperty("WEBCONTENT.BASE.PASH");
            String dirPath = "workorder/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/";
            File dir = new File(basePath + dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String abstractPath = dirPath + System.currentTimeMillis() + ".f";
            File file = new File(basePath + abstractPath);
            uploadFile.transferTo(file);

            attachment.setFileName(uploadFile.getOriginalFilename());
            attachment.setAbstractPath(abstractPath);
            attachment.setStatus("有效");
            ServiceCtrlMessage message = attachmentService.add(attachment);
            // ServiceCtrlMessage message = new ServiceCtrlMessage(true, "");
            return message.isResult() ? DwzMessage.success("上传成功！", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    @RequestMapping("down/{id}/.do")
    public ResponseEntity<?> down(@PathVariable("id") Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Attachment attachment = attachmentService.get(id);
            if (attachment == null) {
                httpHeaders.add("Content-Type", "text/html; charset=utf-8");
                return new ResponseEntity<>("您要下载的附件找不到了！", httpHeaders, HttpStatus.OK);
            }
            File file = new File(System.getProperty("WEBCONTENT.BASE.PASH") + attachment.getAbstractPath());
            FileSystemResource resource = new FileSystemResource(file);

            httpHeaders.add("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity(resource, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            httpHeaders.add("Content-Type", "text/html; charset=utf-8");
            return new ResponseEntity<>("下载出现异常！", httpHeaders, HttpStatus.OK);
        }
    }

}
