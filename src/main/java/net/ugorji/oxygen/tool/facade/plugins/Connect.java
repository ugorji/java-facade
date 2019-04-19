/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

/**
 * When you connect, we give the connection an Id. You pass that id when you disconnect. We allow
 * the ability to list the current connections. For each connection, we put the names of its trees
 * in the facade context also. That way, we can always get a map with keys which are the connection
 * ids, and the values be a list of tree names.
 */
package net.ugorji.oxygen.tool.facade.plugins;

import java.util.*;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import net.ugorji.oxygen.tool.facade.ConnectionHandler;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.FacadeUtils;
import net.ugorji.oxygen.tool.facade.JMXConnectionHandlerImpl;
import net.ugorji.oxygen.tool.facade.trees.FacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.SimpleJMXFacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class Connect extends FacadePluginAdapter {
  public ConnectionHandler execute(String host, int port) throws Exception {
    return execute(host, port, null, null);
  }

  public ConnectionHandler execute(String host, int port, String username, String password)
      throws Exception {
    // JMXServiceURL url = new JMXServiceURL("rmi", host, port, "/jmxrmi");
    JMXServiceURL jmxurl = new JMXServiceURL(getJMXURL(host, port));
    Map env = getJMXConnEnv(username, password);

    JMXConnector jmxc = JMXConnectorFactory.connect(jmxurl, env);
    // System.out.println("JMXConnector connected: "+jmxc.getConnectionId());
    MBeanServerConnection mbs = jmxc.getMBeanServerConnection();

    List treesAdded = new ArrayList();

    doConnect(host, port, mbs, treesAdded);

    ConnectionHandler connhdlr = new JMXConnectionHandlerImpl(ctx, jmxc, treesAdded);
    FacadeUtils.addConnectionHandler(ctx, connhdlr);
    return connhdlr;
  }

  protected void doConnect(String host, int port, MBeanServerConnection mbs, List treesAdded)
      throws Exception {
    String[] sa = mbs.getDomains();
    for (int i = 0; i < sa.length; i++) {
      FacadeTreeModel ftm =
          new SimpleJMXFacadeTreeModel(mbs, sa[i], "jmx:" + host + ":" + port + ":" + sa[i], ctx);
      addTree(ftm, treesAdded);
    }
  }

  protected void addTree(FacadeTreeModel ftm, List treesAdded) throws Exception {
    TreeUtils.addTree(ctx, ftm);
    treesAdded.add(ftm.getName());
    // now add the names to a list in the facadecontext, so that disconnect can remove them all.
  }

  protected Map getJMXConnEnv(String username, String password) throws Exception {
    Map env = new HashMap();
    if (username != null && password != null) {
      Hashtable credentials = new Hashtable();
      credentials.put("login", username);
      credentials.put("password", password);
      env.put(JMXConnector.CREDENTIALS, credentials);
    }
    return env;
  }

  protected String getJMXURL(String host, int port) throws Exception {
    return "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
  }
}
