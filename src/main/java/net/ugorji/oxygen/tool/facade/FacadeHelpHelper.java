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
import java.util.Properties;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import net.ugorji.oxygen.util.StringUtils;

public class FacadeHelpHelper {
  private String srcfile;
  private Properties summaryMapping = new Properties();
  private Properties detailsMapping = new Properties();

  public FacadeHelpHelper(String _srcfile) throws Exception {
    srcfile = _srcfile;

    InputStream is = getInputStreamToSrcFile();
    FHHXMLParser parsehdlr = new FHHXMLParser();
    SAXParserFactory fact = SAXParserFactory.newInstance();
    SAXParser sp = fact.newSAXParser();
    sp.parse(is, parsehdlr);
    is.close();
  }

  public String getQuickHelp(String comm) {
    return StringUtils.nonNullString(summaryMapping.get(comm));
  }

  public String getDetailedHelp(String comm) {
    return StringUtils.nonNullString(detailsMapping.get(comm));
  }

  private InputStream getInputStreamToSrcFile() throws Exception {
    File f = new File(srcfile);
    if (f.exists()) {
      return new FileInputStream(f);
    } else {
      return getClass().getClassLoader().getResourceAsStream(srcfile);
    }
  }

  private class FHHXMLParser extends DefaultHandler {
    private StringBuffer helpbuf = new StringBuffer();
    private String commname = null;
    private boolean addinghelp = false;

    public void startDocument() throws SAXException {}

    public void endDocument() throws SAXException {}

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      try {
        if ("help".equals(qName)) {
          commname = attributes.getValue("command");
          String summary = attributes.getValue("summary");
          summaryMapping.setProperty(commname, summary);
          addinghelp = true;
          helpbuf = new StringBuffer();
        }
      } catch (Exception exc) {
        exc.printStackTrace();
        throw new SAXException(exc);
      }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
      if ("help".equals(qName)) {
        detailsMapping.put(commname, helpbuf.toString());
        addinghelp = false;
      }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
      if (addinghelp) {
        helpbuf.append(ch, start, length);
      }
    }
  }
}
