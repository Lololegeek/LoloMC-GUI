package dev.lolomc.gui.parser;

import dev.lolomc.gui.model.UiNode;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public final class XmlUiParser {
    public UiNode parse(InputStream input) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {
                public void warning(SAXParseException error) throws SAXParseException { throw error; }
                public void error(SAXParseException error) throws SAXParseException { throw error; }
                public void fatalError(SAXParseException error) throws SAXParseException { throw error; }
            });
            return convert(builder.parse(input).getDocumentElement());
        } catch (Exception error) {
            throw new IllegalArgumentException("Invalid LoloMC GUI XML: " + error.getMessage(), error);
        }
    }

    private UiNode convert(Element element) {
        UiNode result = new UiNode(element.getTagName());
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            result.getAttributes().put(attribute.getNodeName(), attribute.getNodeValue());
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node child = element.getChildNodes().item(i);
            if (child instanceof Element) result.add(convert((Element) child));
            else if (child.getNodeType() == Node.TEXT_NODE) text.append(child.getTextContent());
        }
        result.setText(text.toString().trim());
        return result;
    }
}
