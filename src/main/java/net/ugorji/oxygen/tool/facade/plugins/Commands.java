/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import java.util.Arrays;
import java.util.List;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;

public class Commands extends FacadePluginAdapter {
  public List execute() {
    return Arrays.asList(ctx.getFacadeDefinitionHelper().getCommands());
  }
}
