package org.droiddraw.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class StringHandler extends DefaultHandler
{
    Hashtable<String, String> strings;
    StringBuffer buff;
    String name;
    
    public StringHandler()
    {
        strings = new Hashtable<String,String>();
        buff = new StringBuffer();
    }

    public void characters(char arg0[], int arg1, int arg2)
    {
        buff.append(arg0, arg1, arg2);
    }

    public void startDocument()
    {
        strings.clear();
    }

    public void startElement(String ns, String lName, String qName, Attributes atts)
    {
        buff.setLength(0);
        if(qName.equals("string"))
            name = atts.getValue("name");
    }

    public void endElement(String uri, String localName, String qName)
    {
        if(qName.equals("string"))
            strings.put(name, buff.toString());
    }

    public Hashtable<String,String> getStrings()
    {
        return strings;
    }

    public static Hashtable<String,String> load(InputStream is)
        throws SAXException, ParserConfigurationException, IOException
    {
        return load(new InputSource(is));
    }

    public static Hashtable<String,String> load(InputSource in)
        throws SAXException, ParserConfigurationException, IOException
    {
        StringHandler sh = new StringHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        parser.parse(in, sh);
        return sh.getStrings();
    }

}
