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
import net.ugorji.oxygen.tool.facade.FacadePluginAdapter;
import net.ugorji.oxygen.util.OxyTable;

public class QuickHelp extends FacadePluginAdapter {
  public OxyTable execute() {
    return execute(null);
  }

  public OxyTable execute(String section) {
    String[] categories = ctx.getFacadeDefinitionHelper().getCategories();
    String[] headers = new String[] {"Section", "Command", "Quick Help"};
    String[] row = new String[headers.length];
    OxyTable tabl = new OxyTable(headers);
    tabl.setDescription("Showing summary help for wlcommands");
    for (int i = 0; i < categories.length; i++) {
      if (section == null || section.equals(categories[i])) {
        String[] commands = ctx.getFacadeDefinitionHelper().getCommands(categories[i]);
        Arrays.sort(commands);
        for (int j = 0; j < commands.length; j++) {
          row[0] = categories[i];
          row[1] = commands[j];
          row[2] = ctx.getFacadeHelpHelper().getQuickHelp(commands[j]);
          if (row[2] == null || row[2].trim().length() == 0) {
            row[2] = "-";
          }
          tabl.addRow(row);
        }
      }
    }
    tabl.sort();
    return tabl;
  }
}
