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
import java.util.HashSet;
import java.util.Set;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.util.StringUtils;

public class AvailableVars extends FacadePluginAdapter {
  public Set execute() {
    String s = ctx.getFacadeDefinitionHelper().getParameter("AVAILABLE_VARS");
    Collection c = StringUtils.tokenize(s, ",", true);
    return new HashSet(c);
  }
}
