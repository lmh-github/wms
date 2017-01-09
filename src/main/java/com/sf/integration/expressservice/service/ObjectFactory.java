
package com.sf.integration.expressservice.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sf.integration.expressservice.service package. 
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

    private final static QName _SfexpressService_QNAME = new QName("http://service.expressservice.integration.sf.com/", "sfexpressService");
    private final static QName _SfexpressServiceResponse_QNAME = new QName("http://service.expressservice.integration.sf.com/", "sfexpressServiceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.sf.integration.expressservice.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SfexpressService }
     * 
     */
    public SfexpressService createSfexpressService() {
        return new SfexpressService();
    }

    /**
     * Create an instance of {@link SfexpressServiceResponse }
     * 
     */
    public SfexpressServiceResponse createSfexpressServiceResponse() {
        return new SfexpressServiceResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SfexpressService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.expressservice.integration.sf.com/", name = "sfexpressService")
    public JAXBElement<SfexpressService> createSfexpressService(SfexpressService value) {
        return new JAXBElement<SfexpressService>(_SfexpressService_QNAME, SfexpressService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SfexpressServiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.expressservice.integration.sf.com/", name = "sfexpressServiceResponse")
    public JAXBElement<SfexpressServiceResponse> createSfexpressServiceResponse(SfexpressServiceResponse value) {
        return new JAXBElement<SfexpressServiceResponse>(_SfexpressServiceResponse_QNAME, SfexpressServiceResponse.class, null, value);
    }

}
