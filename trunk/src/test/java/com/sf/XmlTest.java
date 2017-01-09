/**
* @author jay_liang
* @date 2014-3-24 上午11:30:43
*/
package com.sf;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gionee.wms.common.JsonUtils;
import com.sf.integration.expressservice.bean.OrderFilterRespBean;
import com.sf.integration.expressservice.bean.RouteRespBean;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-24 上午11:30:43
 * @=======================================
 */
public class XmlTest {
	
	private JAXBContext context;
	private StringWriter writer = null;
    private StringReader reader = null;
    private JsonUtils jsonUtils = null;
    
    @Before
    public void init() {        
        try {
           jsonUtils = new JsonUtils(Inclusion.NON_NULL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void destory() {
        context = null;
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }
	
	@Test
	public void testXml() throws JAXBException {
		RouteRespBean respBean = new RouteRespBean();
//		respBean.setService("aaa");
//		respBean.setHead("ERR");
//		SFError error = new SFError();
//		error.setCode("123");
//		error.setValue("AAAA");
//		respBean.setError(error);
//		SFBody respBody = new SFBody();
//		RouteResponse rr = new RouteResponse();
//		rr.setMailno("mailto");
//		rr.setOrderid("orderid");
//		List<Route> rlist = new ArrayList<Route>();
//		Route route=new Route();
//		route.setAcceptAddress("address");
//		route.setAcceptTime("2014-03-25");
//		route.setOpcode("opcode");
//		route.setRemark("remark");
//		rlist.add(route);
//		rr.setRoute(rlist);
//		List<RouteResponse> rrlist = new ArrayList<RouteResponse>();
//		rrlist.add(rr);
//		respBody.setRouteResponse(rrlist);
//		respBean.setBody(respBody);
//		
		context = JAXBContext.newInstance(RouteRespBean.class);
//        //下面代码演示将对象转变为xml
//        Marshaller mar = context.createMarshaller();
//        writer = new StringWriter();
//        mar.marshal(respBean, writer);
//        System.out.println(writer);
        
        //下面代码演示将上面生成的xml转换为对象
        String xml="<?xml version='1.0' encoding='UTF-8'?><Response service=\"RouteService\"><Head>OK</Head><Body><RouteResponse mailno=\"587000256204\"><Route remark=\"已收件\" accept_time=\"2014-03-13 14:10:39\" accept_address=\"上海市\" opcode=\"50\"/><Route remark=\"海关扣件\" accept_time=\"2014-03-13 15:15:39\" accept_address=\"上海市\" opcode=\"18\"/><Route remark=\"海关已放行\" accept_time=\"2014-03-13 15:25:39\" accept_address=\"上海市\" opcode=\"14\"/><Route remark=\"代理收件\" accept_time=\"2014-03-13 15:40:39\" accept_address=\"上海市\" opcode=\"607\"/><Route remark=\"正在派件..\" accept_time=\"2014-03-13 15:45:39\" accept_address=\"上海市\" opcode=\"44\"/><Route remark=\"快件到达便利店 \" accept_time=\"2014-03-13 15:50:39\" accept_address=\"上海市\" opcode=\"130\"/><Route remark=\"快件转寄EMS,单号99945678899\" accept_time=\"2014-03-13 15:55:39\" accept_address=\"上海市\" opcode=\"627\"/><Route remark=\"快件转寄EMS,单号99945678899\" accept_time=\"2014-03-13 16:00:39\" accept_address=\"上海市\" opcode=\"626\"/><Route remark=\"快件准备送往 便利店\" accept_time=\"2014-03-13 16:05:39\" accept_address=\"上海市\" opcode=\"123\"/><Route remark=\"派件已签收\" accept_time=\"2014-03-13 16:10:39\" accept_address=\"上海市\" opcode=\"80\"/></RouteResponse></Body></Response>";
        System.out.println(xml);
        reader = new StringReader(xml);
        Unmarshaller unmar = context.createUnmarshaller();
        respBean = (RouteRespBean)unmar.unmarshal(reader);
        System.out.println(jsonUtils.toJson(respBean));
	}
	
	@Test
	public void testXml1() throws JAXBException {
		OrderFilterRespBean respBean = new OrderFilterRespBean();
		context = JAXBContext.newInstance(OrderFilterRespBean.class);
		String xml="<?xml version='1.0' encoding='UTF-8'?><Response service=\"OrderFilterService\"><Head>OK</Head><Body><OrderFilterResponse filter_result=\"2\" destcode=\"755\" orderid=\"1234567890\"/></Body></Response>";
		System.out.println(xml);
        reader = new StringReader(xml);
        Unmarshaller unmar = context.createUnmarshaller();
        respBean = (OrderFilterRespBean)unmar.unmarshal(reader);
        System.out.println(jsonUtils.toJson(respBean));
	}

}
