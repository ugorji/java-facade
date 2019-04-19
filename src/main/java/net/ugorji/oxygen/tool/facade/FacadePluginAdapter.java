/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade;

public class FacadePluginAdapter {
  protected FacadeContext ctx;

  public void init(FacadeContext _ctx) {
    ctx = _ctx;
  }

  public void close() {
    // do nothing
    ctx = null;
  }
}
