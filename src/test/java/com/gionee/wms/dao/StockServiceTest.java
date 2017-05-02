package com.gionee.wms.dao;

import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.wares.WaresService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.gionee.wms.common.WmsConstants.StockBizType.OUT_TRANSFER;
import static com.gionee.wms.common.WmsConstants.StockType.STOCK_SALES;

/**
 * Created by Pengbin on 2017/4/29.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring*.xml"})
public class StockServiceTest extends TestCase {

    @Autowired
    private StockService stockService;
    @Autowired
    private WaresService waresService;

    /**
     * 人工调整库存
     */
    @Test
    public void outStock() {
        try {
            Sku sku = waresService.getSkuByCode("1256");
            System.out.println(sku.getId());
            String code = "1717042914659";
            Integer qty = 130;
            StockRequest stockRequest = new StockRequest(1643L, sku.getId(), STOCK_SALES, qty, OUT_TRANSFER, code);
            stockService.decreaseStock(stockRequest);    // 扣减可销库存
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
