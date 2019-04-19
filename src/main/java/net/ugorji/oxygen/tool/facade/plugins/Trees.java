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
import java.util.TreeSet;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.FacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;
import net.ugorji.oxygen.util.OxyTable;

public class Trees extends FacadePluginAdapter {
  public OxyTable execute() {
    Map m = TreeUtils.getTrees(ctx);
    String[] headers = new String[] {"Tree", "Separator", "Up Denotation"};
    // System.out.println("Trees: " + m);
    OxyTable tabl = new OxyTable(headers);
    for (Iterator itr = new TreeSet(m.keySet()).iterator(); itr.hasNext(); ) {
      String treename = (String) itr.next();
      FacadeTreeModel tree = (FacadeTreeModel) m.get(treename);
      tabl.addRow(new String[] {treename, tree.getSeparator(), tree.getUpRepresentation()});
    }
    return tabl;
  }
}
