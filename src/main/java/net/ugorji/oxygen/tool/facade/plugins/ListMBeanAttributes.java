/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import java.util.*;
import javax.management.*;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.JMXFacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.JMXUtils;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;
import net.ugorji.oxygen.util.OxyTable;
import net.ugorji.oxygen.util.StringUtils;

public class ListMBeanAttributes extends FacadePluginAdapter {
  public OxyTable execute() throws Exception {
    return execute(null);
  }

  public OxyTable execute(String regex) throws Exception {
    JMXFacadeTreeModel ftm = (JMXFacadeTreeModel) TreeUtils.getCurrentTree(ctx);
    MBeanServerConnection mbs = ftm.getMBeanServerConnection();
    ObjectName cmo = (ObjectName) ftm.getCurrentNode();
    // cmo = (cmo == null ? (ObjectName)ftm.getRoot() : cmo);
    cmo = JMXUtils.checkRegistered(mbs, cmo);

    MBeanInfo mbinfo = mbs.getMBeanInfo(cmo);
    MBeanAttributeInfo[] attsinfo = mbinfo.getAttributes();

    Map map = new HashMap();
    TreeSet al = new TreeSet();
    for (int i = 0; i < attsinfo.length; i++) {
      String attname = attsinfo[i].getName();
      al.add(attname);
      map.put(attname, attsinfo[i]);
    }

    String[] headers = new String[] {"", "Name", "Type", "Value"};
    OxyTable tabl = new OxyTable(headers);
    tabl.setDescription(
        "Attributes: 1st column Legend: D=dynamic, C=configurable, *=encrypted, r=readable, w=writable");
    tabl.setMainIndex(1);
    // tabl.setInternalObject(attsinfo);
    Object[] row = new Object[headers.length];
    String[] attnames = (String[]) al.toArray(new String[0]);
    AttributeList attlist = mbs.getAttributes(cmo, attnames);
    for (Iterator itr = attlist.iterator(); itr.hasNext(); ) {
      Attribute att = (Attribute) itr.next();
      MBeanAttributeInfo attinfo = (MBeanAttributeInfo) map.get(att.getName());
      addAtt(att, attinfo, regex, tabl, row);
    }
    // tabl.printTo(new PrintWriter(System.out));
    tabl.sort();
    return tabl;
  }

  private static void addAtt(
      Attribute att, MBeanAttributeInfo attinfo, String regex, OxyTable tabl, Object[] row)
      throws Exception {
    String attname = att.getName();
    if (regex != null && !(StringUtils.matchSimpleRegex(attname, regex))) {
      return;
    }
    boolean isDynamic = false;
    boolean isConfigurable = false;
    boolean isExcluded = false;
    boolean isEncrypted = false;
    // if(attinfo instanceof ExtendedAttributeInfo) {
    //  ExtendedAttributeInfo extattinfo = (ExtendedAttributeInfo)attinfo;
    //  isDynamic = extattinfo.isDynamic();
    //  isConfigurable = extattinfo.isConfigurable();
    //  isExcluded = extattinfo.isExcluded();
    //  isEncrypted = extattinfo.isEncrypted();
    // }
    // if(isExcluded) {
    //  return;
    // }

    StringBuffer buf = new StringBuffer();
    // addAttAppend(buf, false, " ", " ");
    addAttAppend(buf, isDynamic, "D", "-");
    addAttAppend(buf, isConfigurable, "C", "-");
    // addAttAppend(buf, isExcluded, "E", "-");
    addAttAppend(buf, isEncrypted, "*", "-");
    addAttAppend(buf, false, " ", " ");
    // addAttAppend(buf, attinfo.isIs(), "i", "-");
    addAttAppend(buf, attinfo.isReadable(), "r", "-");
    addAttAppend(buf, attinfo.isWritable(), "w", "-");
    // addAttAppend(buf, false, " ", " ");

    row[0] = buf.toString();
    row[1] = attname;
    row[2] = attinfo.getType();
    Object value = att.getValue();
    if (value != null && value instanceof Object[]) {
      value = Arrays.asList((Object[]) value).toString();
    }
    row[3] = value;
    tabl.addRow(row);
  }

  private static void addAttAppend(StringBuffer buf, boolean b, String s0, String s1) {
    if (b) {
      buf.append(s0);
    } else {
      buf.append(s1);
    }
  }
}
