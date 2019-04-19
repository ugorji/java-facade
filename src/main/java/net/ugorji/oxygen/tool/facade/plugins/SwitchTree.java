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
import net.ugorji.oxygen.util.StringUtils;

public class SwitchTree extends FacadePluginAdapter {
  public FacadeTreeModel execute() {
    return execute(null);
  }

  public FacadeTreeModel execute(String s) {
    if (!StringUtils.isBlank(s)) {
      TreeUtils.setCurrentTree(ctx, s);
    }
    return TreeUtils.getCurrentTree(ctx);
  }
}
