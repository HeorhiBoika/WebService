package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Created by PC on 14.04.2017.
 */
public class ParseRequestXML {

    public ParseRequestXML() {
    }

    public Book parseXMLObject(File file) {
        Book book = new Book();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        Document doc = null;
        try {
            doc = dBuilder.parse(file);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList node = doc.getElementsByTagName("book");
        for (int temp = 0; temp < node.getLength(); temp++) {
            Node nNode = node.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                book.setId(Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()));
                book.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                book.setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
            }
        }
        return book;
    }

    public String parseToXMLObject(Book book, File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(book, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bis = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = bis.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
