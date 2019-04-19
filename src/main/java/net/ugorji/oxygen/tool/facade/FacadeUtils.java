/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade;

import java.util.HashMap;
import java.util.Map;

public class FacadeUtils {
  public static Map getConnectionHandlers(FacadeContext ctx) {
    Map m = (Map) ctx.getAttribute(FacadeConstants.CONNECTION_HANDLERS_KEY);
    if (m == null) {
      m = new HashMap();
      ctx.setAttribute(FacadeConstants.CONNECTION_HANDLERS_KEY, m);
    }
    return m;
  }

  public static ConnectionHandler getConnectionHandler(FacadeContext ctx, String id) {
    return (ConnectionHandler) getConnectionHandlers(ctx).get(id);
  }

  public static void addConnectionHandler(FacadeContext ctx, ConnectionHandler conn) {
    getConnectionHandlers(ctx).put(conn.getId(), conn);
  }

  public static void removeConnectionHandler(FacadeContext ctx, String id) {
    getConnectionHandlers(ctx).remove(id);
  }
}
