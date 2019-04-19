/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.util.ObjectWrapper;
import net.ugorji.oxygen.util.StringUtils;

public class LastErrorStackTrace extends FacadePluginAdapter {
  public Object execute() throws Exception {
    Throwable thr = ctx.getLastThrowable();
    Object o = null;
    if (thr != null) {
      String s = StringUtils.toString(thr);
      o = new ObjectWrapper(s);
    }
    return o;
  }
}
