
package com.sf.integration.warehouse.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sf.integration.warehouse.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OutsideToLscmService_QNAME = new QName("http://service.warehouse.integration.sf.com/", "outsideToLscmService");
    private final static QName _JyToLscmServiceResponse_QNAME = new QName("http://service.warehouse.integration.sf.com/", "jyToLscmServiceResponse");
    private final static QName _JyToLscmService_QNAME = new QName("http://service.warehouse.integration.sf.com/", "jyToLscmService");
    private final static QName _YxToLscmServiceResponse_QNAME = new QName("http://service.warehouse.integration.sf.com/", "yxToLscmServiceResponse");
    private final static QName _YxToLscmService_QNAME = new QName("http://service.warehouse.integration.sf.com/", "yxToLscmService");
    private final static QName _OutsideToLscmServiceResponse_QNAME = new QName("http://service.warehouse.integration.sf.com/", "outsideToLscmServiceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sf.integration.warehouse.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OutsideToLscmServiceResponse }
     * 
     */
    public OutsideToLscmServiceResponse createOutsideToLscmServiceResponse() {
        return new OutsideToLscmServiceResponse();
    }

    /**
     * Create an instance of {@link YxToLscmService }
     * 
     */
    public YxToLscmService createYxToLscmService() {
        return new YxToLscmService();
    }

    /**
     * Create an instance of {@link JyToLscmService }
     * 
     */
    public JyToLscmService createJyToLscmService() {
        return new JyToLscmService();
    }

    /**
     * Create an instance of {@link YxToLscmServiceResponse }
     * 
     */
    public YxToLscmServiceResponse createYxToLscmServiceResponse() {
        return new YxToLscmServiceResponse();
    }

    /**
     * Create an instance of {@link OutsideToLscmService }
     * 
     */
    public OutsideToLscmService createOutsideToLscmService() {
        return new OutsideToLscmService();
    }

    /**
     * Create an instance of {@link JyToLscmServiceResponse }
     * 
     */
    public JyToLscmServiceResponse createJyToLscmServiceResponse() {
        return new JyToLscmServiceResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutsideToLscmService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.warehouse.integration.sf.com/", name = "outsideToLscmService")
    public JAXBElement<OutsideToLscmService> createOutsideToLscmService(OutsideToLscmService value) {
        return new JAXBElement<OutsideToLscmService>(_OutsideToLscmService_QNAME, OutsideToLscmService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JyToLscmServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.warehouse.integration.sf.com/", name = "jyToLscmServiceResponse")
    public JAXBElement<JyToLscmServiceResponse> createJyToLscmServiceResponse(JyToLscmServiceResponse value) {
        return new JAXBElement<JyToLscmServiceResponse>(_JyToLscmServiceResponse_QNAME, JyToLscmServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JyToLscmService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.warehouse.integration.sf.com/", name = "jyToLscmService")
    public JAXBElement<JyToLscmService> createJyToLscmService(JyToLscmService value) {
        return new JAXBElement<JyToLscmService>(_JyToLscmService_QNAME, JyToLscmService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link YxToLscmServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.warehouse.integration.sf.com/", name = "yxToLscmServiceResponse")
    public JAXBElement<YxToLscmServiceResponse> createYxToLscmServiceResponse(YxToLscmServiceResponse value) {
        return new JAXBElement<YxToLscmServiceResponse>(_YxToLscmServiceResponse_QNAME, YxToLscmServiceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link YxToLscmService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.warehouse.integration.sf.com/", name = "yxToLscmService")
    public JAXBElement<YxToLscmService> createYxToLscmService(YxToLscmService value) {
        return new JAXBElement<YxToLscmService>(_YxToLscmService_QNAME, YxToLscmService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OutsideToLscmServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.warehouse.integration.sf.com/", name = "outsideToLscmServiceResponse")
    public JAXBElement<OutsideToLscmServiceResponse> createOutsideToLscmServiceResponse(OutsideToLscmServiceResponse value) {
        return new JAXBElement<OutsideToLscmServiceResponse>(_OutsideToLscmServiceResponse_QNAME, OutsideToLscmServiceResponse.class, null, value);
    }

}
