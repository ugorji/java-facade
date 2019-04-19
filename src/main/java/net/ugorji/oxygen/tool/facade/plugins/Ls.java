/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

/** List will take a matchexpr, and only return those which match that expr e.g. ls 'Server' */
package net.ugorji.oxygen.tool.facade.plugins;

import java.util.regex.Pattern;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.FacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;
import net.ugorji.oxygen.util.OxyBitMask;
import net.ugorji.oxygen.util.OxyTable;
import net.ugorji.oxygen.util.StringUtils;

public class Ls extends FacadePluginAdapter {
  public OxyTable execute() throws Exception {
    return execute(null);
  }

  public OxyTable execute(String regex) throws Exception {
    Pattern patt = null;
    if (!StringUtils.isBlank(regex)) {
      patt = Pattern.compile(regex);
    }
    FacadeTreeModel ftm = TreeUtils.getCurrentTree(ctx);
    Object curr = ftm.getCurrentNode();
    Object[] kids = ftm.getChildren(curr);
    String[] headers = new String[] {"Mask", "Child"};
    String[] row = new String[headers.length];
    OxyTable tabl = new OxyTable(headers);
    OxyBitMask obm = new OxyBitMask(ftm.getChildBitMaskRepresentation());
    for (int i = 0; i < kids.length; i++) {
      row[1] = ftm.toPathName(kids[i]);
      if (patt == null || patt.matcher(row[1]).find()) {
        row[0] =
            (new StringBuffer().append(obm.getCharArray(ftm.getChildBitMask(kids[i]), '-')))
                .toString();
        tabl.addRow(row);
      }
    }
    return tabl;
  }
}
