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
import net.ugorji.oxygen.util.ObjectWrapper;

public class Help extends FacadePluginAdapter {
  public Object execute() {
    return execute("help");
  }

  public Object execute(String s) {
    return new ObjectWrapper(ctx.getFacadeHelpHelper().getDetailedHelp(s));
  }
}
