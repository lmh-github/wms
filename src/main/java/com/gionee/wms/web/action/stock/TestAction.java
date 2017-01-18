package com.gionee.wms.web.action.stock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.SpContactsEditModel;
import com.gionee.wms.dto.SpMContactsPhoneValue;
import com.gionee.wms.entity.Check;
import com.gionee.wms.service.stock.CheckService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.opensymphony.xwork2.Action;

@Controller("TestAction")
@Scope("prototype")
public class TestAction extends CrudActionSupport<SpContactsEditModel> {
	private static final long serialVersionUID = 2566942049805660757L;

	private CheckService stockCheckService;

	/** 页面相关属性 **/
	private List<Check> stockChecks;
	
	private SpContactsEditModel model;
	
	public SpContactsEditModel getModel() {
        return model;
    }

	/**
	 * 查询盘点任务列表
	 */
	@Override
	public String list() throws Exception {
		return SUCCESS;
	}

	/**
	 * 更新盘点单
	 */
	@Override
	public String update() throws Exception {
		 // debug用
        if (model.getPhoneList() != null) {
            for (SpMContactsPhoneValue phoneInfo : model.getPhoneList()) {
                if (phoneInfo == null) {
                    continue;
                }
                String debugMsg = "phoneType:" + phoneInfo.getPhoneType();
                debugMsg += "   phoneLabel:" + phoneInfo.getPhoneLabel();
                debugMsg += "   phoneNumber:" + phoneInfo.getPhoneNumber();
                System.out.println(debugMsg);
            }
        }
        // 属性初始化
        this.init();
        return Action.SUCCESS;
	}
	private void init() {
//		model = new SpContactsEditModel();
        model.setPhoneList(new ArrayList());
    }
	/**
	 * 上传实盘明细数据
	 */
	public String uploadPhysicalDetail() {
		System.out.println("上传成功");
		return SUCCESS;
	}

	/**
	 * 下载盘点单
	 */
	public String downloadStockCheck() {
		return SUCCESS;
	}

	/**
	 * 创建盘点单
	 */
	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 删除盘点单
	 */
	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 打开创建或编辑页面
	 */
	@Override
	public String input() throws Exception {
		// 属性初始化
//        this.init();
        
        return SUCCESS;
	}

	@Override
	public void prepareAdd() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepareInput() throws Exception {
		prepareModel();

	}

	@Override
	public void prepareUpdate() throws Exception {
//		prepareModel();
		model = new SpContactsEditModel();

	}
	
	private void prepareModel() throws Exception {
		 // debug用
		model = new SpContactsEditModel();
        SpMContactsPhoneValue phoneInfo1 = new SpMContactsPhoneValue();
        phoneInfo1.setPhoneType("0");
        phoneInfo1.setPhoneLabel("this is custom");
        phoneInfo1.setPhoneNumber("13044445555");
        model.getPhoneList().add(phoneInfo1);
        SpMContactsPhoneValue phoneInfo2 = new SpMContactsPhoneValue();
        phoneInfo2.setPhoneType("3");
        phoneInfo2.setPhoneNumber("02111112222");
        model.getPhoneList().add(phoneInfo2);
        SpMContactsPhoneValue phoneInfo3 = new SpMContactsPhoneValue();
        phoneInfo3.setPhoneType("5");
        phoneInfo3.setPhoneNumber("01033334444");
        model.getPhoneList().add(phoneInfo3);

	}

	

//	@Override
//	public StockCheck getModel() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	public List<Check> getStockChecks() {
		return stockChecks;
	}

	@Autowired
	public void setStockCheckService(CheckService stockCheckService) {
		this.stockCheckService = stockCheckService;
	}
}
