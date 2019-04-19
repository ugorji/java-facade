/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import java.io.PrintWriter;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.tool.facade.trees.FacadeTreeModel;
import net.ugorji.oxygen.tool.facade.trees.TreeUtils;

public class WriteTree extends FacadePluginAdapter {
  public void execute() throws Exception {
    execute(null);
  }

  public void execute(String s) throws Exception {
    PrintWriter pw = new PrintWriter(System.out);
    execute(s, pw);
  }

  public void execute(String s, PrintWriter pw) throws Exception {
    FacadeTreeModel ftm = TreeUtils.getCurrentTree(ctx);
    Object curr = TreeUtils.getChildGivenAbsolutePath(ftm, s);
    curr = (curr == null ? ftm.getCurrentNode() : curr);
    writeChildren(ftm, curr, pw, true);
  }

  private void writeChildren(FacadeTreeModel ftm, Object curr, PrintWriter pw, boolean autoflush)
      throws Exception {
    pw.println(TreeUtils.toAbsolutePath(ftm, curr));
    if (autoflush) pw.flush();
    Object[] kids = ftm.getChildren(curr);
    if (kids != null && kids.length > 0) {
      for (int i = 0; i < kids.length; i++) {
        writeChildren(ftm, kids[i], pw, autoflush);
      }
    }
  }
}
