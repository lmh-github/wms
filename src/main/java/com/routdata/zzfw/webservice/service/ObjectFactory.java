
package com.routdata.zzfw.webservice.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.routdata.zzfw.webservice.service package. 
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

    private final static QName _BusinessException_QNAME = new QName("http://service.webservice.zzfw.routdata.com/", "BusinessException");
    private final static QName _QueryMail_QNAME = new QName("http://service.webservice.zzfw.routdata.com/", "queryMail");
    private final static QName _SysException_QNAME = new QName("http://service.webservice.zzfw.routdata.com/", "SysException");
    private final static QName _SQLException_QNAME = new QName("http://service.webservice.zzfw.routdata.com/", "SQLException");
    private final static QName _QueryMailResponse_QNAME = new QName("http://service.webservice.zzfw.routdata.com/", "queryMailResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.routdata.zzfw.webservice.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SQLException }
     * 
     */
    public SQLException createSQLException() {
        return new SQLException();
    }

    /**
     * Create an instance of {@link QueryMailResponse }
     * 
     */
    public QueryMailResponse createQueryMailResponse() {
        return new QueryMailResponse();
    }

    /**
     * Create an instance of {@link BusinessException }
     * 
     */
    public BusinessException createBusinessException() {
        return new BusinessException();
    }

    /**
     * Create an instance of {@link SysException }
     * 
     */
    public SysException createSysException() {
        return new SysException();
    }

    /**
     * Create an instance of {@link QueryMail }
     * 
     */
    public QueryMail createQueryMail() {
        return new QueryMail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.webservice.zzfw.routdata.com/", name = "BusinessException")
    public JAXBElement<BusinessException> createBusinessException(BusinessException value) {
        return new JAXBElement<BusinessException>(_BusinessException_QNAME, BusinessException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryMail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.webservice.zzfw.routdata.com/", name = "queryMail")
    public JAXBElement<QueryMail> createQueryMail(QueryMail value) {
        return new JAXBElement<QueryMail>(_QueryMail_QNAME, QueryMail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SysException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.webservice.zzfw.routdata.com/", name = "SysException")
    public JAXBElement<SysException> createSysException(SysException value) {
        return new JAXBElement<SysException>(_SysException_QNAME, SysException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SQLException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.webservice.zzfw.routdata.com/", name = "SQLException")
    public JAXBElement<SQLException> createSQLException(SQLException value) {
        return new JAXBElement<SQLException>(_SQLException_QNAME, SQLException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryMailResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.webservice.zzfw.routdata.com/", name = "queryMailResponse")
    public JAXBElement<QueryMailResponse> createQueryMailResponse(QueryMailResponse value) {
        return new JAXBElement<QueryMailResponse>(_QueryMailResponse_QNAME, QueryMailResponse.class, null, value);
    }

}
