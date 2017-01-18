
package com.gionee.dc.web.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.gionee.dc.web.service package. 
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

    private final static QName _AddSaleInvoice_QNAME = new QName("http://service.web.dc.gionee.com/", "addSaleInvoice");
    private final static QName _Exception_QNAME = new QName("http://service.web.dc.gionee.com/", "Exception");
    private final static QName _AddSaleInvoiceResponse_QNAME = new QName("http://service.web.dc.gionee.com/", "addSaleInvoiceResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.gionee.dc.web.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddSaleInvoice }
     * 
     */
    public AddSaleInvoice createAddSaleInvoice() {
        return new AddSaleInvoice();
    }

    /**
     * Create an instance of {@link AuthInfo }
     * 
     */
    public AuthInfo createAuthInfo() {
        return new AuthInfo();
    }

    /**
     * Create an instance of {@link ImeidtlSale }
     * 
     */
    public ImeidtlSale createImeidtlSale() {
        return new ImeidtlSale();
    }

    /**
     * Create an instance of {@link AddSaleInvoiceResponse }
     * 
     */
    public AddSaleInvoiceResponse createAddSaleInvoiceResponse() {
        return new AddSaleInvoiceResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSaleInvoice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.web.dc.gionee.com/", name = "addSaleInvoice")
    public JAXBElement<AddSaleInvoice> createAddSaleInvoice(AddSaleInvoice value) {
        return new JAXBElement<AddSaleInvoice>(_AddSaleInvoice_QNAME, AddSaleInvoice.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.web.dc.gionee.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddSaleInvoiceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.web.dc.gionee.com/", name = "addSaleInvoiceResponse")
    public JAXBElement<AddSaleInvoiceResponse> createAddSaleInvoiceResponse(AddSaleInvoiceResponse value) {
        return new JAXBElement<AddSaleInvoiceResponse>(_AddSaleInvoiceResponse_QNAME, AddSaleInvoiceResponse.class, null, value);
    }

}
