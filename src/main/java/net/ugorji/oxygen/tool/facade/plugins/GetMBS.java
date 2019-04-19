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
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.JMXFacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class GetMBS extends FacadePluginAdapter {

  public MBeanServerConnection execute() throws Exception {
    JMXFacadeTreeModel ftm = (JMXFacadeTreeModel) TreeUtils.getCurrentTree(ctx);
    return ftm.getMBeanServerConnection();
  }
}
