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
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import net.ugorji.oxygen.util.CloseUtils;
import net.ugorji.oxygen.util.FreemarkerTemplateHelper;
import net.ugorji.oxygen.util.OxygenUtils;
import net.ugorji.oxygen.util.StringUtils;

public class FacadeCreator {

  private File outputDir;
  private String srcFile;
  private boolean verbose;

  public void createFacade() throws Exception {
    // parse the srcFile into a TemplateModel
    // merge that into a template
    // write output file into the outputDir (honoring the package name passed)
    TemplateModel tmplModel = getTemplateModel();
    Map tmplctx = new HashMap();
    tmplctx.put("model", tmplModel);

    FreemarkerTemplateHelper tmplhdlr =
        new FreemarkerTemplateHelper(new String[] {"/net/ugorji/oxygen/tool/facade/resources"}, null);

    File outDir = new File(outputDir, tmplModel.getPackage().replace('.', '/'));
    outDir.mkdirs();

    File outFile = null;
    FileWriter stw = null;

    outFile = new File(outDir, tmplModel.getClassName() + ".java");
    if (verbose) System.out.println("Writing to: " + outFile);
    stw = new FileWriter(outFile);
    tmplhdlr.write("facadecreator.static.java.tmpl", tmplctx, stw);
    CloseUtils.close(stw);

    outFile = new File(outDir, tmplModel.getClassName() + "Session.java");
    if (verbose) System.out.println("Writing to: " + outFile);
    stw = new FileWriter(outFile);
    tmplhdlr.write("facadecreator.session.java.tmpl", tmplctx, stw);
    CloseUtils.close(stw);
  }

  public TemplateModel getTemplateModel() throws Exception {
    // parse the xml file using SAX, and populate the model.
    InputStream is = OxygenUtils.searchForResourceAsStream(srcFile, getClass());
    FCXMLParser parsehdlr = new FCXMLParser();
    SAXParserFactory fact = SAXParserFactory.newInstance();
    SAXParser sp = fact.newSAXParser();
    sp.parse(is, parsehdlr);
    CloseUtils.close(is);
    return parsehdlr.tm;
  }

  public static class TemplateModel {
    public String name;
    public String packagee;
    public String classname;
    public boolean static0;

    public String helpresource;
    public String definitionresource;

    public List templateElements = new ArrayList();
    public Properties parameters = new Properties();
    public Properties categoryMapping = new Properties();

    public String getHelpresource() {
      return helpresource;
    }

    public String getDefinitionresource() {
      return definitionresource;
    }

    public String getPackage() {
      return packagee;
    }

    public String getName() {
      return name;
    }

    public String getClassName() {
      return classname;
    }

    public TemplateElement[] getTemplateElements() {
      return (TemplateElement[]) templateElements.toArray(new TemplateElement[0]);
    }

    public Properties getParameters() {
      return parameters;
    }

    public Properties getCategoryMapping() {
      return categoryMapping;
    }

    public boolean getStatic() {
      return static0;
    }
  }

  public static class TemplateElement {
    public String exceptionPhrase;
    public String returnObject;
    public String parametersSignature;
    public String parametersCall;
    public String classname;
    public String methodname;

    public String getExceptionPhrase() {
      return exceptionPhrase;
    }

    public String getReturnObject() {
      return returnObject;
    }

    public String getMethodName() {
      return methodname;
    }

    public String getParametersSignature() {
      return parametersSignature;
    }

    public String getParametersCall() {
      return parametersCall;
    }

    public String getClassName() {
      return classname;
    }
  }

  private static class FCXMLParser extends DefaultHandler {
    public TemplateModel tm = new TemplateModel();
    private String category = null;

    public void startDocument() throws SAXException {}

    public void endDocument() throws SAXException {}

    public void endElement(String uri, String localName, String qName) throws SAXException {}

    public void characters(char[] ch, int start, int length) throws SAXException {}

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
      try {
        if ("general".equals(qName)) {
          tm.static0 = "true".equals(attributes.getValue("static"));
          tm.name = attributes.getValue("name");
          tm.classname = attributes.getValue("classname");
          tm.packagee = attributes.getValue("package");
          tm.helpresource = attributes.getValue("helpresource");
          tm.definitionresource = attributes.getValue("definitionresource");
        } else if ("param".equals(qName)) {
          tm.parameters.setProperty(attributes.getValue("name"), attributes.getValue("value"));
        } else if ("category".equals(qName)) {
          category = attributes.getValue("name");
        } else if ("command".equals(qName)) {
          tm.categoryMapping.setProperty(attributes.getValue("name"), category);
          String classname1 = attributes.getValue("classname");
          String methodname1 = attributes.getValue("name");

          Class clazz = Class.forName(classname1);
          Method[] methods = clazz.getMethods();
          for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("execute")) {
              TemplateElement te = new TemplateElement();
              te.classname = classname1;
              te.methodname = methodname1;
              te.returnObject = methods[i].getReturnType().getName();
              Class[] excTypes = methods[i].getExceptionTypes();
              List excTypesStr = new ArrayList();
              for (int j = 0; j < excTypes.length; j++) {
                excTypesStr.add(excTypes[j].getName());
              }
              if (excTypesStr.size() > 0) {
                te.exceptionPhrase = "throws " + StringUtils.toString(excTypesStr, ", ");
              } else {
                te.exceptionPhrase = "";
              }
              Class[] paramTypes = methods[i].getParameterTypes();
              List paramTypesStr = new ArrayList();
              List paramCallStr = new ArrayList();
              for (int j = 0; j < paramTypes.length; j++) {
                paramTypesStr.add(paramTypes[j].getName() + " arg" + j);
                paramCallStr.add("arg" + j);
              }
              te.parametersSignature = StringUtils.toString(paramTypesStr, ", ");
              te.parametersCall = StringUtils.toString(paramCallStr, ", ");
              tm.templateElements.add(te);
            }
          }
        }
      } catch (Exception exc) {
        exc.printStackTrace();
        throw new SAXException(exc);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    FacadeCreator fc = new FacadeCreator();
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-outputdir")) {
        fc.outputDir = new File(args[++i]);
      } else if (args[i].equals("-srcfile")) {
        fc.srcFile = args[++i];
      } else if (args[i].equals("-verbose")) {
        fc.verbose = true;
      }
    }
    fc.createFacade();
  }
}
