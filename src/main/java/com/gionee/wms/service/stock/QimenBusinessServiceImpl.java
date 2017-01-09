package com.gionee.wms.service.stock;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.common.excel.excelexport.util.DateUtil;
import com.gionee.wms.common.excel.excelexport.util.qimen.QimenHttpclient;
import com.gionee.wms.common.excel.excelexport.util.qimen.QimenSign;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.PurPreRecvDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.PurPreRecvGoods;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("qimenBusinessService")
public class QimenBusinessServiceImpl implements QimenBusinessService {
    private static Logger logger = LoggerFactory.getLogger(QimenBusinessServiceImpl.class);

    public String getUrl(String method) {
        String url = "http://qimen.api.taobao.com/router/qimen/service?"  //线上地址     日常地址："http://qimenapi.tbsandbox.com/router/qimen/service?"
                + "method=" + method
                + "&timestamp=" + DateUtil.formate(new Date())
                + "&format=xml"
                + "&app_key=" + WmsConstants.QIMEN_APPKEY
                + "&v=2.0"
                + "&sign_method=md5"
                + "&customerId=" + WmsConstants.QIMEN_CUSTOMERID;
        return url;
    }

    public static String match(String s, String tag) {
        String results = "";
        Pattern p = Pattern.compile("<" + tag + ">(.*)</" + tag + ">");
        Matcher m = p.matcher(s);
        while (!m.hitEnd() && m.find()) {
            results += m.group(1);
        }
        return results;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String s = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><response><flag>failure</flag><code>TOP60</code><message>qimen inernal error | qimen.request-convert-error | [QIMEN-ERROR] 获取不到卖家[tuserid=null, customerId=995566, app_key=21689850]的授权关系</message></response>\n";
        System.out.println(match(s, "flag"));
    }


    public static String replaceContentByPartten(String content,
                                                 String partten, String target) {
        Matcher matcher = Pattern.compile(partten, Pattern.CASE_INSENSITIVE).matcher(content);
        if (matcher.find()) {
            StringBuffer temp = new StringBuffer();
            matcher.appendReplacement(temp, target);
            matcher.appendTail(temp);
            return temp.toString();
        } else {
            return content;
        }
    }

    @Override
    public ServiceCtrlMessage entryOrderCreate(Transfer transfer) {
        String url = getUrl("taobao.qimen.entryorder.create");

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request><entryOrderCode>" + transfer.getTransferId() +
                "</entryOrderCode><ownerCode>O1234</ownerCode><warehouseCode>" + transfer.getWarehouseId() + "</warehouseCode>" +
                "<orderCreateTime>" + DateUtil.formateYMDHMS(transfer.getCreateTime()) + "</orderCreateTime>";
        if (transfer.getTransferTo().equals("顺风仓")) {
            body += "<logisticsCode>SF</logisticsCode>"+
            "<logisticsName>顺风</logisticsName>";
        }
        body+="<operatorName>"+transfer.getConsignee()+"</operatorName>" +
                "<operateTime>"+ DateUtil.formateYMDHMS(transfer.getCreateTime()) + "</operateTime>" +
                "<remark>"+transfer.getRemark()+"</remark>";
        body += "</request>";//请求主题
        String secretKey = WmsConstants.QIMEN_SECRET;
        try {
            String md5 = QimenSign.sign(url, body, secretKey);
            //System.out.println(md5);

            String url1 = url + "&sign=" + md5;
            //System.out.println(url1);

            String response = QimenHttpclient.send(url1, body);       //注意：签名的body和http请求的body必须完全一致，包括空格、换行等

            String flag = match(response, "flag");
            if (flag.equals("failure")) {
                String message = match(response, "message");//得到奇门返回的post值
                return new ServiceCtrlMessage(false, message);
            }
            return new ServiceCtrlMessage(true, "下达成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return new ServiceCtrlMessage(false, "操作异常！");
        }

    }

    @Override
    public ServiceCtrlMessage entryOrderConfirm(Transfer transfer) {
        return null;
    }

    @Override
    public ServiceCtrlMessage orderProcessReport(Transfer transfer) {
        return null;
    }

    @Override
    public ServiceCtrlMessage stockOutCreate(Transfer transfer) {
        return null;
    }

    @Override
    public ServiceCtrlMessage stockOutConfirm(Transfer transfer) {
        return null;
    }
}
