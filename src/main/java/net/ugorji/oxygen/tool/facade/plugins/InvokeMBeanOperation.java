/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import java.util.ArrayList;
import java.util.List;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.JMXFacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.JMXUtils;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class InvokeMBeanOperation extends FacadePluginAdapter {

  public Object execute(String opname) throws Exception {
    return execute(opname, null);
  }

  public Object execute(String opname, List argslist) throws Exception {
    if (argslist == null) {
      argslist = new ArrayList();
    }

    JMXFacadeTreeModel ftm = (JMXFacadeTreeModel) TreeUtils.getCurrentTree(ctx);
    MBeanServerConnection mbs = ftm.getMBeanServerConnection();
    ObjectName cmo = (ObjectName) ftm.getCurrentNode();
    cmo = JMXUtils.checkRegistered(mbs, cmo, null);

    Object[] args = argslist.toArray();
    String[] signature = new String[args.length];
    for (int i = 0; i < signature.length; i++) {
      signature[i] = args[i].getClass().getName();
    }

    return mbs.invoke(cmo, opname, args, signature);
  }
}
