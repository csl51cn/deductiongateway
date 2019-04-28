package org.starlightfinancial.deductiongateway.utility;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: xml转json
 * @date: Created in 2019/4/28 10:20
 * @Modified By:
 */
public class XmlUtils {


    /**
     * String 转 org.dom4j.Document
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Document strToDocument(String xml) throws DocumentException {
        return DocumentHelper.parseText(xml);
    }

    /**
     * org.dom4j.Document 转  com.alibaba.fastjson.JSONObject
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static JSONObject documentToJSONObject(String xml) throws DocumentException {
        return elementToJSONObject(strToDocument(xml).getRootElement());
    }

    /**
     * org.dom4j.Element 转  com.alibaba.fastjson.JSONObject
     *
     * @param node
     * @return
     */
    public static JSONObject elementToJSONObject(Element node) {
        JSONObject result = new JSONObject();
        // 当前节点的名称、文本内容和属性

        // 当前节点的所有属性的list
        List<Attribute> listAttr = node.attributes();
        for (Attribute attr : listAttr) {
            // 遍历当前节点的所有属性
            result.put(attr.getName(), attr.getValue());
        }
        // 递归遍历当前节点所有的子节点
        // 所有一级子节点的list
        List<Element> listElement = node.elements();
        if (!listElement.isEmpty()) {
            for (Element e : listElement) {
                // 遍历所有一级子节点
                // 判断一级节点是否有属性和子节点
                if (e.attributes().isEmpty() && e.elements().isEmpty()) {
                    // 没有则将当前节点作为上级节点的属性对待
                    result.put(e.getName(), e.getTextTrim());
                } else {
                    // 判断父节点是否存在该一级节点名称的属性
                    if (!result.containsKey(e.getName())) {
                        // 没有则创建
                        result.put(e.getName(), new JSONArray());
                    }
                    // 将该一级节点放入该节点名称的属性对应的值中
                    ((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));
                }
            }
        }
        return result;
    }
}
