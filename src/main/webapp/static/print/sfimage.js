function ExecSfPrint(order_list) {
	var LODOP = getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	LODOP.PRINT_INIT("顺丰电子快递单");
	var width = 100;
	var height = 150;
	var cxw = 0;
	LODOP.SET_PRINT_PAGESIZE(1, (width + cxw) + "mm", (height + cxw) + "mm", "");
	LODOP.NewPage();
	LODOP.ADD_PRINT_IMAGE("0mm", "0mm", (width + cxw) + "mm", (height + cxw) + "mm",
	'<img border="0" src="../static/print/sf-template.jpg"/>');
	LODOP.SET_PRINT_STYLEA(0, "Stretch", 1);
	/*order_list = [];
	var obj = {
				"address":"test",
				"consignee":"test",
				"mobile":"test",
				"tel":"test",
				"sf":{
					"mailno": "12312312212",
					"destcode": "test",
					"origincode": "test"
				},
				"seller":{
					"shop_address": "test",
					"shop_tel": "test"
				}
			};
	order_list.push(obj);
	LODOP.PRINT_INIT("顺丰电子快递单");
	LODOP.SET_PRINT_PAGESIZE(1, "96mm", "136mm", "");

	for (var i = 0; i < order_list.length; i++) {
		LODOP.NewPage();
		LODOP.SET_PRINT_STYLE("FontName", "黑体");

		LODOP.ADD_PRINT_IMAGE("0mm", "0mm", "96mm", "136mm",
				'<img border="0" src="../static/print/sf-express.jpg"/>');
		LODOP.SET_PRINT_STYLEA(0, "Stretch", 1);// (可变形)扩展缩放模式

		LODOP.ADD_PRINT_BARCODE(10, 175, 153, 40, "128C",
				order_list[i]['sf']['mailno']);

		LODOP.SET_PRINT_STYLE("FontSize", 7);
		LODOP.ADD_PRINT_TEXT(73, 6, 240, 46,
				order_list[i]['seller']['shop_address'] + ' '
						+ order_list[i]['seller']['shop_tel']);

		LODOP.SET_PRINT_STYLE("FontSize", 27);
		LODOP.ADD_PRINT_TEXT(73, 280, 108, 38,
				order_list[i]['sf']['origincode']);

		LODOP.SET_PRINT_STYLE("FontSize", 7);
		LODOP.ADD_PRINT_TEXT(132, 6, 240, 46, order_list[i]['address'] + ' '
				+ order_list[i]['consignee'] + ' ' + order_list[i]['mobile']
				+ ' ' + order_list[i]['tel']);

		LODOP.SET_PRINT_STYLE("FontSize", 27);
		LODOP
				.ADD_PRINT_TEXT(132, 280, 108, 38,
						order_list[i]['sf']['destcode']);

		LODOP.SET_PRINT_STYLE("FontSize", 7);
		LODOP.ADD_PRINT_TEXT(189, 6, 240, 14, 'xx大客户');
		LODOP.ADD_PRINT_TEXT(189, 250, 111, 14, '标准快递');

		LODOP.SET_PRINT_STYLE("Alignment", 2);
		LODOP.ADD_PRINT_TEXT(224, 2, 68, 14, order_list[i]['sl']);
		// LODOP.ADD_PRINT_TEXT(510,216,78,30,'实重');
		// LODOP.ADD_PRINT_TEXT(510,370,78,30,'1.32');
		// LODOP.ADD_PRINT_TEXT(510,515,78,30,'费用');
		// LODOP.ADD_PRINT_TEXT(510,690,78,30,'合计');

		LODOP.SET_PRINT_STYLE("Alignment", 1);
		LODOP.ADD_PRINT_TEXT(240, 50, 130, 14, '寄付');
		LODOP.ADD_PRINT_TEXT(254, 50, 130, 14, '7553038568');

		// LODOP.ADD_PRINT_TEXT(720,25,378,30,'代收贷款:100 卡号:000000000001');
		// LODOP.ADD_PRINT_TEXT(750,25,378,30,'声明价值:100 保费费用:800');

		LODOP.ADD_PRINT_TEXT(379, 190, 176, 14, order_list[i]['sf']['mailno']);

		LODOP.ADD_PRINT_TEXT(402, 6, 240, 46,
				order_list[i]['seller']['shop_address'] + ' '
						+ order_list[i]['seller']['shop_tel']);
		LODOP.ADD_PRINT_TEXT(402, 250, 113, 14, 'xx大客户');

		LODOP.ADD_PRINT_TEXT(460, 6, 240, 46, order_list[i]['address'] + ' '
				+ order_list[i]['consignee'] + ' ' + order_list[i]['mobile']
				+ ' ' + order_list[i]['tel']);
		//LODOP.ADD_PRINT_TEXT(1043,562,192,30,'合计2');
		LODOP.ADD_PRINT_TEXT(490, 250, 113, 14, '寄付');
	}*/

	//LODOP.SELECT_PRINTER();
	//LODOP.PREVIEW();
	LODOP.PRINT_DESIGN();
//	LODOP.PRINT();
}
