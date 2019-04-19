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

public class ListMBeanOperations extends FacadePluginAdapter {
  public OxyTable execute() throws Exception {
    return execute(null);
  }

  public OxyTable execute(String regex) throws Exception {
    JMXFacadeTreeModel ftm = (JMXFacadeTreeModel) TreeUtils.getCurrentTree(ctx);
    MBeanServerConnection mbs = ftm.getMBeanServerConnection();
    ObjectName cmo = (ObjectName) ftm.getCurrentNode();
    cmo = JMXUtils.checkRegistered(mbs, cmo);
    // cmo = (cmo == null ? (ObjectName)ftm.getRoot() : cmo);

    MBeanInfo mbinfo = mbs.getMBeanInfo(cmo);
    MBeanOperationInfo[] opsinfo = mbinfo.getOperations();
    Map m0 = new HashMap();
    Map m2 = new HashMap();
    for (int i = 0; i < opsinfo.length; i++) {
      String opsname = opsinfo[i].getName();
      m0.put(opsname, opsinfo[i].getReturnType());
      MBeanParameterInfo[] params = opsinfo[i].getSignature();
      List l1 = new ArrayList();
      for (int j = 0; j < params.length; j++) {
        l1.add(params[j].getName() + "=" + params[j].getType());
      }
      m2.put(opsname, l1);
    }
    String[] headers = new String[] {"Name", "Return Type", "Args"};
    Object[] row = new Object[headers.length];
    OxyTable tabl = new OxyTable(headers);
    tabl.setMainIndex(1);
    // tabl.setInternalObject(opsinfo);
    for (Iterator itr = m0.keySet().iterator(); itr.hasNext(); ) {
      String name = (String) itr.next();
      if (regex != null && !(StringUtils.matchSimpleRegex(name, regex))) {
        continue;
      }
      String returntype = (String) m0.get(name);
      Object args = m2.get(name);
      row[0] = name;
      row[1] = returntype;
      row[2] = args;
      tabl.addRow(row);
    }
    // tabl.printTo(new PrintWriter(System.out));
    tabl.sort();
    return tabl;
  }
}
