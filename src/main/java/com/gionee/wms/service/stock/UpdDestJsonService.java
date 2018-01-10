package com.gionee.wms.service.stock;

import com.gionee.wms.vo.UpdDestJsonRequestVo;

/**
 * 发送出库的IMEI到第三方业务
 *   调用接口http://www.datatransetl.com:8085/dc/app/updDestJson.json
 * Created by admin on 2018/1/9.
 */
public interface UpdDestJsonService {

    void sendIMEI(UpdDestJsonRequestVo vo);
}
