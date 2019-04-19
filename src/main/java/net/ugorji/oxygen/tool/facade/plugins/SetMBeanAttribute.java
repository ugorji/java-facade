/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.JMXFacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.JMXUtils;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class SetMBeanAttribute extends FacadePluginAdapter {

  public Object execute(String attkey, Object attval) throws Exception {
    JMXFacadeTreeModel ftm = (JMXFacadeTreeModel) TreeUtils.getCurrentTree(ctx);
    MBeanServerConnection mbs = ftm.getMBeanServerConnection();
    ObjectName cmo = (ObjectName) ftm.getCurrentNode();
    cmo = JMXUtils.checkRegistered(mbs, cmo);

    Attribute att = new Attribute(attkey, attval);
    mbs.setAttribute(cmo, att);
    return mbs.getAttribute(cmo, attkey);
  }
}
