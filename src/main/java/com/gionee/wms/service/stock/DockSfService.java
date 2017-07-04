package com.gionee.wms.service.stock;

import com.sf.integration.warehouse.response.DockSFResponse;

/**
 * Created by Pengbin on 2017/5/28.
 */
public interface DockSfService {

    DockSFResponse dock(String sailOrderPushInfoXML);
}
