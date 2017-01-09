/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gionee.wms.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

/**
 * <p>
 * Title:读取XML文件的实用类
 * </p>
 * @version 1.0
 */

public class XMLUtil
{

    private static final Log log = LogFactory.getLog(XMLUtil.class);

    private XMLUtil()
    {
    }
    
    public static Document parseText(String xmlText)
    {
        try
        {
            if (null != xmlText)
            {
                return DocumentHelper.parseText(xmlText);
            }
        } catch (Exception e)
        {
            log.error(e);
        }
        
        return null;
    }

    /**
     * 从文件名初始化，如初始化成功返回ROOT，否则返回NULL
     * @param xmlSource
     */
    public static Document parseXml(String xmlSource)
    {
        try
        {
            if (null != xmlSource)
            {
                InputStream in = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(xmlSource);
                return parseXml(in);
            }
        } catch (Exception e)
        {
            log.error(e);
        }
        
        return null;
    }

    /**
     * 从文件名初始化，如初始化成功返回ROOT，否则返回NULL
     * @param xmlSource
     */
    static public Document parseXml(byte[] xmlData) throws Exception
    {
        if (xmlData == null)
            return null;

        java.io.ByteArrayInputStream in = new ByteArrayInputStream(xmlData);
        return parseXml(in);
    }

    /**
     * 从XML文件输入流初始化，如初始化成功返回true，否则返回false
     */
    static public Document parseXml(File xmlFile) throws Exception
    {
        if (xmlFile == null || !xmlFile.exists())
            return null;
        Document root = null;
        try
        {
            SAXReader saxReader = new SAXReader();
            saxReader.setValidation(false);
            root = saxReader.read(xmlFile);
            return root;
        } catch (Exception e)
        {
            log.error("解析XML输入流出现异常，请检查XML输入流是否正确:" + xmlFile.getName());
            throw e;
        }
    }

    /**
     * 从XML文件输入流初始化，如初始化成功返回true，否则返回false
     */
    static public Document parseXml(InputStream in) throws Exception
    {
        if (in == null)
            return null;
        Document root = null;
        try
        {
            SAXReader saxReader = new SAXReader();
            saxReader.setValidation(false);
            root = saxReader.read(in);
            return root;
        } catch (Exception e)
        {
            log.error("解析XML输入流出现异常，请检查XML输入流是否正确:" + in);
            throw e;
        } finally
        {
            if (in != null)
                try
                {
                    in.close();
                } catch (Exception e)
                {
                }
        }
    }

    /**
     * 检查某路径的节点配置是否存在
     * @param xql
     * @return
     */
    static public boolean isNodeExists(Node startNode, String xql)
    {
        if (startNode == null || xql == null)
        {
            return false;
        }

        Node node = startNode.selectSingleNode(xql);
        return (node != null);
    }

    /**
     * 根据相对节点，获取配置项值。
     * @param startNode 相对节点
     * @param xql 相对startNode表示的配置项路径
     */
    static public String getString(Node startNode, String xql)
    {
        if (startNode == null || xql == null)
        {
            return null;
        }

        Node node = startNode.selectSingleNode(xql);
        if (node != null)
        {
            return node.getText();
        } else
        {
            return null;
        }
    }

    /**
     * 获取配置项
     * @param xql
     * @return
     */
    static public String getString(Node startNode, String xql, String def)
    {
        if (startNode == null || xql == null)
        {
            return def;
        }

        Node node = startNode.selectSingleNode(xql);
        if (node != null)
        {
            return node.getText();
        } else
        {
            return def;
        }
    }

    /**
     * 获取int类型的配置项，如该项不存在或不能转为int，则返回默认值。
     *
     * @param xql
     *            XPATH表示的配置项路径
     * @param def
     *            默认值
     * @return
     */
    static public int getInt(Node startNode, String xql, int def)
    {
        String nValue = getString(startNode, xql);
        try
        {
            return Integer.parseInt(nValue);
        } catch (Exception e)
        {
            return def;
        }
    }

    /**
     * 获取float类型的配置项，如该项不存在或不能转为float，则返回默认值。
     *
     * @param xql
     *            XPATH表示的配置项路径
     * @param def
     *            默认值
     * @return
     */
    static public float getFloat(Node startNode, String xql, float def)
    {
        String nValue = getString(startNode, xql);
        try
        {
            return Float.parseFloat(nValue);
        } catch (Exception e)
        {
            return def;
        }
    }

    /**
     * 获取long类型的配置项，如该项不存在或不能转为long，则返回默认值。
     *
     * @param xql XPATH表示的配置项路径
     * @param def 默认值
     * @return
     */
    static public long getLong(Node startNode, String xql, long def)
    {
        String nValue = getString(startNode, xql);
        try
        {
            return Long.parseLong(nValue);
        } catch (Exception e)
        {
            return def;
        }
    }

    /**
     * 设置节点的值
     * @param startNode 相对节点
     * @param xql 相对startNode表示的配置项路径
     */
    static public boolean setNodeValue(Node startNode, String xql,
            String nodeValue)
    {
        if (startNode == null || xql == null)
        {
            return false;
        }

        Node node = startNode.selectSingleNode(xql);
        if (node != null)
        {
            node.setText(nodeValue);
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 设置节点的值
     * @param startNode 相对节点
     * @param xql 相对startNode表示的配置项路径
     */
    static public boolean setNodePropertyValue(Node startNode, String xql,
            String propertyName, String nodeValue)
    {
        if (startNode == null || xql == null)
        {
            return false;
        }

        Element node = (Element) startNode.selectSingleNode(xql);
        if (node != null)
        {
            node.attribute(propertyName).setText(nodeValue);
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 输出节点的内容并关闭输出流
     * @param root Node
     * @param out OutputStream
     * @param indent boolean 是否要输出空格
     * @param newLine boolean 是否要换行
     */
    static public void printAndClose(Document document, OutputStream out,
            boolean indent, boolean newLine)
    {
        org.dom4j.io.XMLWriter writer = null;
        try
        {
            org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat
                    .createPrettyPrint();
            format.setEncoding("GBK");
            format.setIndent(indent);
            format.setNewlines(newLine);
            // 以下两行设为TRUE则没有<?xml version="1.0" encoding="GBK"?>
            format.setOmitEncoding(false);
            format.setSuppressDeclaration(false);
            /*
            System.out.println(format.isExpandEmptyElements());
            System.out.println(format.isNewlines());
            System.out.println(format.isOmitEncoding());
            System.out.println(format.isPadText());
            System.out.println(format.isSuppressDeclaration());
            System.out.println(format.isTrimText());
            System.out.println(format.isXHTML());
            */
            writer = new org.dom4j.io.XMLWriter(out, format);
            writer.write(document);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (out != null)
                try
                {
                    out.close();
                } catch (Exception e)
                {
                }
        }
    }

    /**
     * 输出节点的内容并关闭输出流
     * @param root Node
     * @param out OutputStream
     */
    static public void printAndClose(Document document, OutputStream out)
    {
        org.dom4j.io.XMLWriter writer = null;
        try
        {
            org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat
                    .createPrettyPrint();
            format.setEncoding("GBK");
            format.setTrimText(true);
            format.setIndent(true);
            format.setNewlines(true);
            // 以下两行设为TRUE则没有<?xml version="1.0" encoding="GBK"?>
            format.setOmitEncoding(false);
            format.setSuppressDeclaration(false);
            writer = new org.dom4j.io.XMLWriter(out, format);
            writer.write(document);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (out != null)
                try
                {
                    out.close();
                } catch (Exception e)
                {
                }
        }
    }

    /**
     * 输出节点的内容并关闭输出流
     * @param root Node
     * @param out OutputStream
     */
    static public void printAndClose(Node root, OutputStream out, String charSet)
    {
        org.dom4j.io.XMLWriter writer = null;
        try
        {
            org.dom4j.io.OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(charSet);
            writer = new org.dom4j.io.XMLWriter(out, format);
            writer.write(root);
        } catch (Exception e)
        {
            log.error(e);
        } finally
        {
            if (out != null)
                try
                {
                    out.close();
                } catch (Exception e)
                {
                }
        }
    }

    /**
     * 输出节点的内容
     * @param root Node
     * @param out OutputStream
     */
    static public void print(Document document, OutputStream out)
    {
        org.dom4j.io.XMLWriter writer = null;
        org.dom4j.io.OutputFormat format = new org.dom4j.io.OutputFormat();
        format.setEncoding("GBK");
        format.setTrimText(true);
        format.setIndent(false);
        // 以下两行设为TRUE则没有<?xml version="1.0" encoding="GBK"?>
        format.setOmitEncoding(false);
        format.setSuppressDeclaration(false);
        try
        {
            writer = new org.dom4j.io.XMLWriter(out, format);
            writer.write(document);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                out.flush();
            } catch (Exception e)
            {
            }
        }
    }

    /**
     * 输出节点的内容
     * @param root Node
     * @param out OutputStream
     */
    static public void print(Document document, OutputStream out, String charSet)
    {
        org.dom4j.io.XMLWriter writer = null;
        org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat
                .createPrettyPrint();
        format.setNewlines(true);
        format.setEncoding(charSet);
        format.setTrimText(true);
        format.setIndent(true);
        // 以下两行设为TRUE则没有<?xml version="1.0" encoding="GBK"?>
        format.setOmitEncoding(false);
        format.setSuppressDeclaration(false);

        try
        {
            writer = new org.dom4j.io.XMLWriter(out, format);
            writer.write(document);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                out.flush();
            } catch (Exception e)
            {
            }
        }
    }

    @SuppressWarnings("unchecked")
	static public void main(String[] args)
    {
        try
        {
            Document document = org.dom4j.DocumentHelper.createDocument();// .parseText("<?xml version=\"1.0\" encoding=\"GBK\"?><system/>") ;
            org.dom4j.Element root = document.addElement("system");

            java.util.Properties properties = System.getProperties();
            for (java.util.Enumeration elements = properties.propertyNames(); elements
                    .hasMoreElements();)
            {
                String name = (String) elements.nextElement();
                String value = properties.getProperty(name);
                org.dom4j.Element element = root.addElement("property");
                element.addAttribute("name", name);
                element.addText(value);
            }
            root.addElement("tree").addAttribute("text", "测试")
                    .addAttribute("src", "./admin/createMenu.do?pId=12");
            XMLUtil.printAndClose(document, System.out, true, true);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "unused" })
	static private Document createDocument()
    {
        try
        {
            Document document = org.dom4j.DocumentHelper.createDocument();// .parseText("<?xml version=\"1.0\" encoding=\"GBK\"?><system/>") ;
            org.dom4j.Element root = document.addElement("system");

            java.util.Properties properties = System.getProperties();
            for (java.util.Enumeration elements = properties.propertyNames(); elements
                    .hasMoreElements();)
            {
                String name = (String) elements.nextElement();
                String value = properties.getProperty(name);
                org.dom4j.Element element = root.addElement("property");
                element.addAttribute("name", name);
                element.addText(value);
            }
            root.addElement("tree").addAttribute("text", "测试")
                    .addAttribute("src", "./admin/createMenu.do?pId=12");

            return document;
        } catch (Exception ex)
        {
            return null;
        }
    }

}
