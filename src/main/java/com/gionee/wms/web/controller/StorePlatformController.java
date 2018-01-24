package com.gionee.wms.web.controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.UUIDGenerator;
import com.gionee.wms.common.Util;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.common.excel.excelexport.util.DateUtil;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.entity.StorePlatform;
import com.gionee.wms.entity.Wares;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.stock.StorePlatformService;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.web.extend.DwzMessage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 可销售库存平台分配仅东莞电商仓
 */
@Controller
@RequestMapping("/store/platform")
public class StorePlatformController {
    @Autowired
    private StorePlatformService storePlatformService;
    @Autowired
    private StockService stockService;
    @Autowired
    private WaresService waresService;


    @RequestMapping("/toAdd.do")
    public Object toAdd(StorePlatform record,Model model){
        String skuNo = record.getSkuNo();
        int unuseStore = 0;
        String wareHouseCode = "01";//东莞电商仓
        Stock stock = stockService.getStock(wareHouseCode, record.getSkuNo());
        int totalUseStore= storePlatformService.getTotalUseStoreBySku(record.getSkuNo());
        if(stock!=null && stock.getSalesQuantity()!=null){
            int temp = stock.getSalesQuantity() - totalUseStore;
            unuseStore = temp >=0?temp:0;
        }
        WmsConstants.OrderSource[] values = WmsConstants.OrderSource.values();
        List<WmsConstants.OrderSource> orderSources = excludeExist(values, record.getSkuNo());
        model.addAttribute("platforms",orderSources);
        model.addAttribute("skuNo",skuNo);
        model.addAttribute("unuseStore",unuseStore);
        return "stock/platform/add";
    }


    @RequestMapping("/toUpdate.do")
    public Object toUpdate(StorePlatform record,Model model){
        StorePlatform storePlatform = storePlatformService.selectByPrimaryKey(record.getId());
        int unuseStore = 0;
        String wareHouseCode = "01";//东莞电商仓
        Stock stock = stockService.getStock(wareHouseCode, storePlatform.getSkuNo());
        int totalUseStore= storePlatformService.getTotalUseStoreBySku(storePlatform.getSkuNo());
        if(stock!=null && stock.getSalesQuantity()!=null){
            int temp = stock.getSalesQuantity() - totalUseStore + storePlatform.getTotalNum();
            unuseStore = temp >=0?temp:0;
        }
        WmsConstants.OrderSource[] values = WmsConstants.OrderSource.values();
        model.addAttribute("platforms",values);
        model.addAttribute("storePlatform",storePlatform);
        model.addAttribute("unuseStore",unuseStore);
        return "stock/platform/update";
    }


    /**
     * 排除已存在的平台
     * @param values
     * @param skuNo
     * @return
     */
    private List<WmsConstants.OrderSource> excludeExist(WmsConstants.OrderSource[] values,String skuNo){
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("skuNo",skuNo);
        List<StorePlatform> list = storePlatformService.getAll(queryMap);
        List<WmsConstants.OrderSource> platforms = new ArrayList<WmsConstants.OrderSource>();

        for(WmsConstants.OrderSource os:values){
            boolean flag = true;
            for(StorePlatform sf:list){
                if(os.getCode().equals(sf.getPlatformNo())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                platforms.add(os);
            }

        }
       return platforms;
    }


    @RequestMapping("/create.json")
    @ResponseBody
    public Object create(StorePlatform record,QueryMap queryMap){
        String loginName = ActionUtils.getLoginName();
        Date now = new Date();
        String wareHouseCode = "01";//东莞电商仓
        Sku sku = storePlatformService.getSku(record);
        Wares wares = waresService.getWares(sku.getWaresId());
        Stock stock = stockService.getStock(wareHouseCode, record.getSkuNo());
        int totalUseStore= storePlatformService.getTotalUseStoreBySku(record.getSkuNo());
        if(stock==null){
            return DwzMessage.error("库存不存在！",queryMap);
        }
        int temp = stock.getSalesQuantity() - totalUseStore -record.getTotalNum();
        if(temp<0){
            return DwzMessage.error("库存不足！",queryMap);
        }
        record.setId(UUIDGenerator.getUUID());
        record.setSkuUnit(wares.getMeasureUnit());
        record.setSkuName(sku.getSkuName());
        record.setPlatformName(Util.getNameByCode(record.getPlatformNo()));
        record.setCreateBy(loginName);
        record.setCreateDate(now);
        record.setUpdateBy(loginName);
        record.setUpdateDate(now);
        storePlatformService.insertSelective(record);
        return DwzMessage.success("操作成功！",queryMap);
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public Object delete(StorePlatform record,QueryMap queryMap){
        String loginName = ActionUtils.getLoginName();
        StorePlatform storePlatform = storePlatformService.selectByPrimaryKey(record.getId());
        if(storePlatform==null){
            return DwzMessage.error("操作失败！已删除或不存在！",queryMap);
        }
        if(!storePlatform.getCreateBy().equals(loginName)){
            return DwzMessage.error("操作失败！没有删除权限，请联系该设置人！",queryMap);
        }
        storePlatformService.deleteByPrimaryKey(record.getId());
        return DwzMessage.success("操作成功！",queryMap);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public Object update(StorePlatform record,QueryMap queryMap){
        String loginName = ActionUtils.getLoginName();
        StorePlatform storePlatform = storePlatformService.selectByPrimaryKey(record.getId());
        if(storePlatform==null){
            return  DwzMessage.error("操作失败！已删除或不存在",queryMap);
        }
        if(!storePlatform.getCreateBy().equals(loginName)){
            return  DwzMessage.error("操作失败！没有修改权限，请联系该设置人！",queryMap);
        }
        String wareHouseCode = "01";//东莞电商仓
        Stock stock = stockService.getStock(wareHouseCode, storePlatform.getSkuNo());
        int totalUseStore= storePlatformService.getTotalUseStoreBySku(storePlatform.getSkuNo());
        if(stock==null){
            return DwzMessage.error("操作失败！库存不存在！",queryMap);
        }
        int temp = stock.getSalesQuantity() - totalUseStore -record.getTotalNum() + storePlatform.getTotalNum();
        if(temp<0){
            return DwzMessage.error("操作失败！库存不足！",queryMap);
        }
        Date now = new Date();
//        record.setPlatformName(WmsConstants.getOrderSource(storePlatform.getPlatformNo()));
        record.setUpdateBy(loginName);
        record.setUpdateDate(now);
        storePlatformService.updateByPrimaryKeySelective(record);
        return  DwzMessage.success("操作成功！",queryMap);
    }

    @RequestMapping("/list.do")
    public String list(StorePlatform record,QueryMap queryMap, Model model){
        // 获取库存，计算可分配库存
        String wareHouseCode = "01";//东莞电商仓
        Sku sku = storePlatformService.getSku(record);
        Wares wares = waresService.getWares(sku.getWaresId());
        Stock stock = stockService.getStock(wareHouseCode, record.getSkuNo());
        int unuseStore = 0;
        int totalUseStore= storePlatformService.getTotalUseStoreBySku(record.getSkuNo());
        if(stock!=null && stock.getSalesQuantity()!=null){
            int temp = stock.getSalesQuantity() - totalUseStore;
            unuseStore = temp >=0?temp:0;
        }
        queryMap.put("skuNo",record.getSkuNo());
        List<StorePlatform> list = storePlatformService.getAll(queryMap.getMap());
        model.addAttribute("list",list);
        model.addAttribute("sku",sku);
        model.addAttribute("stock",stock);
        model.addAttribute("totalUseStore",totalUseStore);
        model.addAttribute("unuseStore",unuseStore);
        model.addAttribute("wares",wares);
        return "stock/platform/list";
    }

    @RequestMapping("/export.do")
    public void export(StorePlatform record, QueryMap queryMap, HttpServletResponse response){
        queryMap.put("skuNo",record.getSkuNo());
        List<StorePlatform> list = storePlatformService.getAll(queryMap.getMap());
        String wareHouseCode = "01";//东莞电商仓
        Stock stock = stockService.getStock(wareHouseCode, record.getSkuNo());
        StorePlatform storePlatform = new StorePlatform();
        storePlatform.setSkuNo(record.getSkuNo());
        storePlatform.setPlatformNo("");
        storePlatform.setPlatformName("总库存");
        storePlatform.setTotalNum(stock.getSalesQuantity());
        storePlatform.setCreateBy("");
        storePlatform.setUpdateDate(new Date());
        list.add(storePlatform);
        List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
        for (StorePlatform item : list) {
            Map<String, String> repeatData = new HashMap<String, String>();
            repeatData.put("SKU_NO", item.getSkuNo());
            repeatData.put("PLATFORM_NO", item.getPlatformNo());
            repeatData.put("PLATFORM_NAME", item.getPlatformName());
            repeatData.put("TOTAL_NUM", item.getTotalNum().toString());
            repeatData.put("CREATE_BY", item.getCreateBy());
            repeatData.put("UPDATE_DATE", DateUtil.formateYMD(item.getUpdateDate()));
            sheetData.add(repeatData);
        }
        ExcelModule excelModule = new ExcelModule(sheetData);
        // 清空输出流
        response.reset();
        // 设置响应头和下载保存的文件名
        response.setHeader("content-disposition", "attachment;filename=store_platform.xls");
        // 定义输出类型
        response.setContentType("APPLICATION/msexcel");
        OutputStream out = null;
        try {
            String templeteFile = ActionUtils.getProjectPath() + "/export/store_platform_exp_template.xls";
            String tempFile = ActionUtils.getProjectPath() + "/export/store_platform.xls";
            System.out.println(templeteFile + " " + tempFile);
            File file = ExcelExpUtil.expExcel(excelModule, templeteFile, tempFile);
            out = response.getOutputStream();
            FileUtils.copyFile(file, out);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
