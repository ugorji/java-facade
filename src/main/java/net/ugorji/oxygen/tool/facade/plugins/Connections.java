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
import java.util.Map;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.FacadeUtils;

public class Connections extends FacadePluginAdapter {
  public List execute() throws Exception {
    Map m = FacadeUtils.getConnectionHandlers(ctx);
    return new ArrayList(m.values());
  }
}
