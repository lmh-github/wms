package com.gionee.wms.web.ws;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.PurchaseOrderStatus;
import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.PurPreRecvGoods;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.basis.SupplierService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.PurchaseService;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.web.ws.response.WSResult;
import com.gionee.wms.web.ws.response.dto.PurPreRecvDTO;
import com.gionee.wms.web.ws.response.dto.PurPreRecvGoodsDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @ClassName: PurchaseSoapServiceImpl
 * @Description: 采购服务soap接口实现类
 * @author Kevin
 * @date 2013-8-5 下午05:35:35
 * 
 */
@WebService(serviceName = "PurchaseSoapService", portName = "PurchaseSoapServicePort", endpointInterface = "com.gionee.wms.web.ws.PurchaseSoapService", targetNamespace = WmsConstants.TARGET_NS)
public class PurchaseSoapServiceImpl implements PurchaseSoapService {
	private static Logger logger = LoggerFactory.getLogger(PurchaseSoapServiceImpl.class);
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private WaresService waresService;

	@Autowired
	private Validator validator;
	private DozerBeanMapper dozer = new DozerBeanMapper();

	@Override
	public String helloWorld() {
		return "Hello World";
	}

	@Override
	public WSResult putPurPreRecv(PurPreRecvDTO purPreRecv) {
		WSResult result = new WSResult();
		try {
			// 校验入参
			Validate.notNull(purPreRecv, "采购预收单为空");
			if(dozer == null){
				dozer = new DozerBeanMapper();
			}
			PurPreRecv purPreRecvEntity = dozer.map(purPreRecv, PurPreRecv.class);
			purPreRecvEntity.setPurPreparedTime(purPreRecv.getPreparedTime());
			purPreRecvEntity.setPurPreparedBy(purPreRecv.getPreparedBy());
			Set constraintViolations = validator.validate(purPreRecvEntity);
			if (!constraintViolations.isEmpty()) {
				throw new ConstraintViolationException(constraintViolations);
			}
			// List<PurchaseGoodsDTO> goodsList = purchase.getGoodsList();
			if (CollectionUtils.isEmpty(purPreRecv.getGoodsList())) {
				result.setCode(WSResult.GOODS_LIST_IS_NULL);
				return result;
			}
			List<PurPreRecvGoods> goodsEntityList = Lists.newArrayList();
			Set<String> materialCodeList = Sets.newHashSet();
			for (PurPreRecvGoodsDTO goodsDTO : purPreRecv.getGoodsList()) {
				PurPreRecvGoods goodsEntity = dozer.map(goodsDTO, PurPreRecvGoods.class);
				constraintViolations = validator.validate(goodsEntity);
				if (!constraintViolations.isEmpty()) {
					throw new ConstraintViolationException(constraintViolations);
				}
				goodsEntityList.add(goodsEntity);
				materialCodeList.add(goodsEntity.getMaterialCode());
			}
			// 设置采购预收单相关信息
			Warehouse warehouse = warehouseService.getWarehouseByCode(purPreRecvEntity.getWarehouseCode());
			if (warehouse == null) {
				result.setCode(WSResult.WAREHOUSE_CODE_NOT_EXISTS);
				result.setMessage(purPreRecvEntity.getWarehouseCode());
				return result;
			}
			if (WmsConstants.ENABLED_FALSE == warehouse.getEnabled()) {
				result.setCode(WSResult.WAREHOUSE_IS_DISABLED);
				result.setMessage(purPreRecvEntity.getWarehouseCode());
				return result;
			}
			purPreRecvEntity.setWarehouseId(warehouse.getId());
			purPreRecvEntity.setWarehouseName(warehouse.getWarehouseName());
			Supplier defaultSupplier = supplierService.getDefaultSupplier();
			purPreRecvEntity.setSupplierId(defaultSupplier.getId());
			purPreRecvEntity.setSupplierCode(defaultSupplier.getSupplierCode());
			purPreRecvEntity.setSupplierName(defaultSupplier.getSupplierName());
			purPreRecvEntity.setPreparedTime(new Date());
			purPreRecvEntity.setHandlingStatus(PurchaseOrderStatus.UNRECEIVED.getCode());

			// 设置采购预收商品相关信息
			List<Sku> skuList = waresService.getSkuListByMaterialCodes(Lists.newArrayList(materialCodeList));
			Map<String, Sku> skuMap = Maps.newHashMap();
			for (Sku sku : skuList) {
				skuMap.put(sku.getMaterialCode(), sku);
			}
			for (PurPreRecvGoods goodsEntity : goodsEntityList) {
				if (!skuMap.containsKey(goodsEntity.getMaterialCode())) {
					result.setCode(WSResult.MATERAIL_CODE_NOT_EXISTS);
					result.setMessage(goodsEntity.getMaterialCode());
					return result;
				}
				Sku sku = skuMap.get(goodsEntity.getMaterialCode());
				if (WmsConstants.ENABLED_TRUE == sku.getWares().getIndivEnabled()) {
					if (CollectionUtils.isEmpty(goodsEntity.getIndivCodeList())) {
						result.setCode(WSResult.INDIV_CODES_IS_NULL);
						result.setMessage(goodsEntity.getMaterialCode());
						return result;
					}
					if (goodsEntity.getIndivCodeList().size() != goodsEntity.getQuantity()) {
						result.setCode(WSResult.INDIV_CODES_QUANTITY_MISMATCHING);
						result.setMessage(goodsEntity.getMaterialCode());
						return result;
					}
				}
				goodsEntity.setSkuId(sku.getId());
				goodsEntity.setSkuCode(sku.getSkuCode());
				goodsEntity.setSkuName(sku.getSkuName());
				goodsEntity.setMeasureUnit(sku.getWares().getMeasureUnit());
				goodsEntity.setIndivEnabled(sku.getWares().getIndivEnabled());

			}
			if (purchaseService.getPurPreRecvByPostingNo(purPreRecvEntity.getPostingNo()) == null) {
				// 保存采购单及商品清单
				purchaseService.addPreRecvWithGoodsList(purPreRecvEntity, goodsEntityList);
			} else {
				result.setCode(WSResult.DUPLICATE_PUR_RERECEIVE);
			}
			return result;
		} catch (ConstraintViolationException e) {
			List<String> errorMessages = Lists.newArrayList();
			for (ConstraintViolation violation : e.getConstraintViolations()) {
				errorMessages.add(violation.getPropertyPath() + " " + violation.getMessage());
			}
			String message = StringUtils.join(errorMessages, "\n");
			logger.error(message, e.getMessage());
			result.setResult(WSResult.PARAMETER_ERROR, message);
			return result;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			result.setSystemError();
			return result;
		}
	}
}
