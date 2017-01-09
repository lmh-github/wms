
package com.gionee.dc.web.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for imeidtlSale complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="imeidtlSale">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consignee" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consigneeTel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="deliverAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="imei" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="matlid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="matlname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="meid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="model" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="source_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "imeidtlSale", propOrder = {
    "addition",
    "color",
    "consignee",
    "consigneeTel",
    "createDate",
    "deliverAddress",
    "id",
    "imei",
    "matlid",
    "matlname",
    "meid",
    "model",
    "source",
    "sourceDate"
})
public class ImeidtlSale {

    protected String addition;
    protected String color;
    protected String consignee;
    protected String consigneeTel;
    @XmlElement(name = "create_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    protected String deliverAddress;
    protected Long id;
    protected String imei;
    protected String matlid;
    protected String matlname;
    protected String meid;
    protected String model;
    protected String source;
    @XmlElement(name = "source_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar sourceDate;

    /**
     * Gets the value of the addition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddition() {
        return addition;
    }

    /**
     * Sets the value of the addition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddition(String value) {
        this.addition = value;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    /**
     * Gets the value of the consignee property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * Sets the value of the consignee property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsignee(String value) {
        this.consignee = value;
    }

    /**
     * Gets the value of the consigneeTel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConsigneeTel() {
        return consigneeTel;
    }

    /**
     * Sets the value of the consigneeTel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConsigneeTel(String value) {
        this.consigneeTel = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the deliverAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverAddress() {
        return deliverAddress;
    }

    /**
     * Sets the value of the deliverAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverAddress(String value) {
        this.deliverAddress = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the imei property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImei() {
        return imei;
    }

    /**
     * Sets the value of the imei property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImei(String value) {
        this.imei = value;
    }

    /**
     * Gets the value of the matlid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatlid() {
        return matlid;
    }

    /**
     * Sets the value of the matlid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatlid(String value) {
        this.matlid = value;
    }

    /**
     * Gets the value of the matlname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMatlname() {
        return matlname;
    }

    /**
     * Sets the value of the matlname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMatlname(String value) {
        this.matlname = value;
    }

    /**
     * Gets the value of the meid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMeid() {
        return meid;
    }

    /**
     * Sets the value of the meid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMeid(String value) {
        this.meid = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the sourceDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSourceDate() {
        return sourceDate;
    }

    /**
     * Sets the value of the sourceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSourceDate(XMLGregorianCalendar value) {
        this.sourceDate = value;
    }

}
