/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.FacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class Obj extends FacadePluginAdapter {
  public Object execute() throws Exception {
    FacadeTreeModel ftm = TreeUtils.getCurrentTree(ctx);
    Object curr = ftm.getCurrentNode();
    return ftm.getBackendObject(curr);
  }
}
