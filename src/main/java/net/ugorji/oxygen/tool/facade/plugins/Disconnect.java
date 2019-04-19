/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import java.util.Iterator;
import java.util.Map;
import net.ugorji.oxygen.tool.facade.ConnectionHandler;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.FacadeUtils;

public class Disconnect extends FacadePluginAdapter {

  public void execute() throws Exception {
    Map m = FacadeUtils.getConnectionHandlers(ctx);
    for (Iterator itr = m.keySet().iterator(); itr.hasNext(); ) {
      execute((String) itr.next());
    }
  }

  public void execute(String connid) throws Exception {
    ConnectionHandler conn = FacadeUtils.getConnectionHandler(ctx, connid);
    if (conn != null) {
      conn.disconnect();
      FacadeUtils.removeConnectionHandler(ctx, connid);
    }
  }
}
