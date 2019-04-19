/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.trees;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;
import javax.management.MBeanServerConnection;
import net.ugorji.oxygen.tool.facade.FacadeConstants;
import net.ugorji.oxygen.tool.facade.FacadeContext;
import net.ugorji.oxygen.util.StringUtils;

public class TreeUtils {

  public static Map getTrees(FacadeContext ctx) {
    return (Map) ctx.getAttribute(FacadeConstants.TREES_KEY);
  }

  public static FacadeTreeModel getCurrentTree(FacadeContext ctx) {
    return (FacadeTreeModel) ctx.getAttribute(FacadeConstants.CURRENT_TREE_KEY);
  }

  public static void setCurrentTree(FacadeContext ctx, String name) {
    if (name == null) {
      ctx.setAttribute(FacadeConstants.CURRENT_TREE_KEY, null);
    } else {
      FacadeTreeModel tree = (FacadeTreeModel) getTrees(ctx).get(name);
      if (tree == null) {
        throw new RuntimeException("There is no tree bound to the name: " + name);
      }
      ctx.setAttribute(FacadeConstants.CURRENT_TREE_KEY, tree);
    }
  }

  public static void initTrees(FacadeContext ctx) throws Exception {
    if (getTrees(ctx) != null) {
      return;
    }

    Map trees = new HashMap();
    ctx.setAttribute(FacadeConstants.TREES_KEY, trees);

    File[] fileroots = File.listRoots();
    for (int i = 0; i < fileroots.length; i++) {
      addTree(ctx, new FileSystemFacadeTreeModel(fileroots[i]));
    }

    MBeanServerConnection mbs = ManagementFactory.getPlatformMBeanServer();
    String[] sa = mbs.getDomains();
    for (int i = 0; i < sa.length; i++) {
      addTree(ctx, new SimpleJMXFacadeTreeModel(mbs, sa[i], "jmx:" + sa[i], ctx));
    }
  }

  public static void addTree(FacadeContext ctx, FacadeTreeModel ftm) throws Exception {
    Map m = getTrees(ctx);
    m.put(ftm.getName(), ftm);
    if (getCurrentTree(ctx) == null) {
      setCurrentTree(ctx, ftm.getName());
    }
  }

  public static void removeTree(FacadeContext ctx, String name) throws Exception {
    Map m = getTrees(ctx);
    FacadeTreeModel ftm = (FacadeTreeModel) m.get(name);
    if (getCurrentTree(ctx) == ftm) {
      setCurrentTree(ctx, null);
    }
    m.remove(name);
  }

  public static Object getChildGivenAbsolutePath(FacadeTreeModel ftm, String s) throws Exception {
    Object curr = null;
    if (!StringUtils.isBlank(s)) {
      curr = (s.startsWith(ftm.getSeparator()) ? ftm.getRoot() : ftm.getCurrentNode());
      String[] parts = StringUtils.split(s, ftm.getSeparator());
      for (int i = 0; i < parts.length; i++) {
        if (parts[i].equals(ftm.getUpRepresentation())) {
          curr = ftm.getParent(curr);
        } else {
          curr = ftm.getChild(curr, parts[i]);
        }
        if (curr == null) {
          throw new RuntimeException("Could not parse part of the path: " + parts[i]);
        }
      }
    }
    return curr;
  }

  public static String toAbsolutePath(FacadeTreeModel ftm, Object curr) throws Exception {
    LinkedList ll = new LinkedList();
    while (curr != null) {
      ll.addFirst(curr);
      curr = ftm.getParent(curr);
    }
    StringBuffer buf = new StringBuffer();
    boolean firstOne = true;
    for (Iterator itr = ll.iterator(); itr.hasNext(); ) {
      String s = ftm.toPathName(itr.next());
      if (s != null) {
        if (firstOne) {
          // add sep in front of the first one also
          buf.append(ftm.getSeparator());
          firstOne = false;
        } else {
          buf.append(ftm.getSeparator());
        }
        buf.append(s);
      }
    }
    return buf.toString();
  }

  public static boolean useKeyEqualsBeforeValueInPath(FacadeContext ctx, String domain)
      throws Exception {
    // System.out.println("hello " + " HMM: \\
    // java\\..*|JMImplementation|OSGI|msa\\.jetty\\.JettyWebServer");
    // String d = "java\\..*|JMImplementation|OSGI|msa\\.jetty\\.JettyWebServer";
    // System.out.println(d);
    String ss =
        ctx.getFacadeDefinitionHelper()
            .getParameter("DOMAINS_WITH_NO_NEED_FOR_KEY_IN_PATH_PATTERN");
    boolean b = !(Pattern.compile(ss).matcher(domain).matches());
    // System.out.println(ss);
    // System.out.println(domain + " = " + b + " ... " + ss);
    return b;
  }
}
