<?xml version="1.0" encoding="utf-8" ?>
<interface xmlns="" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					 xsi:schemaLocation="http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd" version="DZFP1.0">
	<globalInfo>
		<terminalCode>${terminalCode?if_exists}</terminalCode>
		<appId>ZZS_PT_DZFP</appId>
		<version>2.0</version>
		<interfaceCode>${interfaceCode?if_exists}</interfaceCode>
		<userName>${userName?if_exists}</userName>
		<passWord>${passWord?if_exists}</passWord>
		<taxpayerId>${taxpayerId?if_exists}</taxpayerId>
		<authorizationCode>${authorizationCode?if_exists}</authorizationCode>
		<requestCode>${requestCode?if_exists}</requestCode>
		<requestTime>${.now?string("yyyy-MM-dd HH:mm:ss zz")}</requestTime>
		<responseCode>${responseCode?if_exists}</responseCode>
		<dataExchangeId>${dataExchangeId?if_exists}</dataExchangeId>
	</globalInfo>
	<Data>
		<dataDescription>
			<zipCode>0</zipCode>
			<encryptCode>0</encryptCode>
			<codeType>${codeType?if_exists}</codeType>
		</dataDescription>
		<content><#include "e-invoice-content.ftl"></content>
	</Data>
</interface>