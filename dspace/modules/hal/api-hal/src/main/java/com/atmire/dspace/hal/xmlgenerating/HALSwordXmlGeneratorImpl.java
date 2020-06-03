package com.atmire.dspace.hal.xmlgenerating;


import com.atmire.dspace.hal.xmlgenerating.elements.HalSwordDataElementGenerator;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by jonas on 24/04/15.
 */
public class HALSwordXmlGeneratorImpl implements HALSwordXmlGenerator {

    public static Logger log = LoggerFactory.getLogger(HALSwordXmlGeneratorImpl.class);

    private List<HalSwordDataElementGenerator> bodyElementGenerators;
    private List<HalSwordDataElementGenerator> backElementGenerators;
    public static final  String TEI_NAMESPACE = "http://www.tei-c.org/ns/1.0";

    @Override
    public File generateXMLFromItem(Context context, Item item) throws SQLException, IOException {
//        File tempFile = File.createTempFile("hal-sword-export-",".xml");
        File dir = new File (ConfigurationManager.getProperty("dspace.dir")+File.separator+"hal-export");
        //Check if dir exists, if not, make one
        if(!dir.exists()){
            dir.mkdir();
        }
        String fileName= "hal-sword-export-"+item.getID()+".xml";
        File file = new File(ConfigurationManager.getProperty("dspace.dir")+File.separator+"hal-export"+File.separator+fileName);

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


            Document doc = docBuilder.newDocument();

            Element rootElement =  doc.createElementNS(TEI_NAMESPACE, "TEI");

            doc.appendChild(rootElement);

            Element text = doc.createElementNS(TEI_NAMESPACE, "text");
            rootElement.appendChild(text);


            Element body = doc.createElementNS(TEI_NAMESPACE,"body");
            // It doesn't matter that the body is built before it is appended, but it is set here for overview reasons
            buildBody(doc, body,item);
            text.appendChild(body);

            Element back = doc.createElementNS(TEI_NAMESPACE,"back");
            buildBack(doc, back,item);

            text.appendChild(back);

            // Specifically use the "xalan" implementation to avoid DOMSource errors
            org.apache.xalan.processor.TransformerFactoryImpl transformerFactory = (org.apache.xalan.processor.TransformerFactoryImpl) org.apache.xalan.processor.TransformerFactoryImpl.newInstance("org.apache.xalan.processor.TransformerFactoryImpl",null);
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
//            file.deleteOnExit();

        } catch (Exception e) {
            log.error(LogManager.getHeader(context, "Generate sword xml from item", "Item: " + item.getID()),e);
        }
        return file;
    }

    private void buildBack(Document doc, Element back,Item item) throws SQLException {

        for (HalSwordDataElementGenerator generator : getBackElementGenerators()) {
            generator.generateElement(doc,back,item);
        }
    }


    private void buildBody(Document doc, Element body,Item item) throws SQLException {
        for (HalSwordDataElementGenerator generator : getBodyElementGenerators()) {
            generator.generateElement(doc,body,item);
        }
    }

    public List<HalSwordDataElementGenerator> getBodyElementGenerators() {
        return bodyElementGenerators;
    }
    public List<HalSwordDataElementGenerator> getBackElementGenerators() {
        return backElementGenerators;
    }

    @Required
    public void setBodyElementGenerators(List<HalSwordDataElementGenerator> bodyElementGenerators) {
        this.bodyElementGenerators = bodyElementGenerators;
    }
    @Required
    public void setBackElementGenerators(List<HalSwordDataElementGenerator> backElementGenerators) {
        this.backElementGenerators = backElementGenerators;
    }


}
