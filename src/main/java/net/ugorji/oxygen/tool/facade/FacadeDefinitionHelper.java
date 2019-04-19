/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FacadeDefinitionHelper {
  private String srcfile;
  private String name;
  private String classname;
  private String packagee;
  private Properties parameters = new Properties();
  private Properties categoryMapping = new Properties();
  private Properties classnameMapping = new Properties();

  public FacadeDefinitionHelper(String _srcfile) throws Exception {
    srcfile = _srcfile;

    InputStream is = getInputStreamToSrcFile();
    FDHXMLParser parsehdlr = new FDHXMLParser();
    SAXParserFactory fact = SAXParserFactory.newInstance();
    SAXParser sp = fact.newSAXParser();
    sp.parse(is, parsehdlr);
    is.close();
  }

  public String[] getCommands(String commsetname) {
    HashSet hs = new HashSet();
    for (Iterator itr = categoryMapping.entrySet().iterator(); itr.hasNext(); ) {
      Map.Entry me = (Map.Entry) itr.next();
      if (commsetname.equals(me.getValue())) {
        hs.add(me.getKey());
      }
    }
    return (String[]) hs.toArray(new String[0]);
  }

  public String[] getCommands() {
    return (String[]) categoryMapping.keySet().toArray(new String[0]);
  }

  public String[] getCategories() {
    return (String[]) (new HashSet(categoryMapping.values())).toArray(new String[0]);
  }

  public String getParameter(String key) {
    return (String) parameters.get(key);
  }

  private InputStream getInputStreamToSrcFile() throws Exception {
    File f = new File(srcfile);
    if (f.exists()) {
      return new FileInputStream(f);
    } else {
      return getClass().getClassLoader().getResourceAsStream(srcfile);
    }
  }

  private class FDHXMLParser extends DefaultHandler {
    private String cat;

    public void startDocument() throws SAXException {}

    public void endDocument() throws SAXException {}

    public void characters(char[] ch, int start, int length) throws SAXException {}

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      try {
        if ("general".equals(qName)) {
          name = attributes.getValue("name");
          classname = attributes.getValue("classname");
          packagee = attributes.getValue("package");
        } else if ("param".equals(qName)) {
          parameters.setProperty(attributes.getValue("name"), attributes.getValue("value"));
        } else if ("command".equals(qName)) {
          String cat2 = attributes.getValue("category");
          categoryMapping.setProperty(attributes.getValue("name"), (cat2 == null ? cat : cat2));
          classnameMapping.setProperty(
              attributes.getValue("name"), attributes.getValue("classname"));
        } else if ("category".equals(qName)) {
          cat = attributes.getValue("name");
        }
      } catch (Exception exc) {
        exc.printStackTrace();
        throw new SAXException(exc);
      }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
      if ("category".equals(qName)) {
        cat = null;
      }
    }
  }
}
