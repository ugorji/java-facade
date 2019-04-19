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

public class Exit extends FacadePluginAdapter {
  public void execute() {
    System.exit(0);
  }
}
