package com.gionee.wms.service.basis;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Payment;

public interface PaymentService {
	List<Payment> getPaymentList(Map<String, Object> criteria);
}
