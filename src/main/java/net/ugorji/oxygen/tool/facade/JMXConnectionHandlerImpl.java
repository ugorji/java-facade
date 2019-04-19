/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade;

import java.util.Iterator;
import java.util.List;
import javax.management.remote.JMXConnector;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class JMXConnectionHandlerImpl implements ConnectionHandler {

  private List trees;
  private JMXConnector jmxconn;
  private FacadeContext ctx;
  private String id;

  public JMXConnectionHandlerImpl(FacadeContext ctx0, JMXConnector jmxconn0, List trees0)
      throws Exception {
    ctx = ctx0;
    trees = trees0;
    jmxconn = jmxconn0;
    id = jmxconn.getConnectionId();
  }

  public String toString() {
    return getId();
  }

  public String getId() {
    return id;
  }

  public void disconnect() throws Exception {
    for (Iterator itr = trees.iterator(); itr.hasNext(); ) {
      String s = (String) itr.next();
      TreeUtils.removeTree(ctx, s);
    }
    jmxconn.close();
  }
}
