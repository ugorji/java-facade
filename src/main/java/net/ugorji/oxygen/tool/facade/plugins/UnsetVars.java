/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import java.util.Collection;
import java.util.Set;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;

public class UnsetVars extends FacadePluginAdapter {

  public Set execute(Collection c) {
    ctx.unsetVars(c);
    return ctx.getVars();
  }

  public Set execute(String s) {
    ctx.unsetVar(s);
    return ctx.getVars();
  }
}
