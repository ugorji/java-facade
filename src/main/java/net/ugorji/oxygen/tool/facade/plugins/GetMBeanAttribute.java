/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.JMXFacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.JMXUtils;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class GetMBeanAttribute extends FacadePluginAdapter {

  public Object execute(String key) throws Exception {
    JMXFacadeTreeModel ftm = (JMXFacadeTreeModel) TreeUtils.getCurrentTree(ctx);
    MBeanServerConnection mbs = ftm.getMBeanServerConnection();
    ObjectName cmo = (ObjectName) ftm.getCurrentNode();
    cmo = JMXUtils.checkRegistered(mbs, cmo);

    return mbs.getAttribute(cmo, key);
  }
}
