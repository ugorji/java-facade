/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.plugins;

import java.util.Arrays;
import java.util.List;
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.util.ObjectWrapper;

public class Sample extends FacadePluginAdapter {
  public Object execute() {
    return new ObjectWrapper("Sample");
  }

  public List execute(Object a) {
    return Arrays.asList(new String[] {"Sample1", "Sample2", "Sample3"});
  }
}
