/**
 * Project: x-framework
 * Package Name: org.ike.eurekacore.core.domain.job
 * Author: Xuejia
 * Date Time: 2016/12/27 10:46
 * Copyright: 2016 www.zigui.site. All rights reserved.
 **/
package org.chim.altass.core.domain.buildin.entry;


import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.Element;
import org.chim.altass.core.domain.IEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Name: Entries
 * Create Date: 2016/12/27 10:46
 * Creator: Xuejia
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "entries")
public class Entries extends Element {

    private static final long serialVersionUID = 7892283690376361906L;
    @Elem(alias = "entry")
    private List<IEntry> entries = null;

    public boolean addEntry(Entry entry) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        return this.entries.add(entry);
    }

    public boolean removeEntry(IEntry entry) {
        if (this.entries == null) {
            return false;
        }
        IEntry rmItem = null;

        for (IEntry iEntry : entries) {
            if (iEntry.getNodeId().equals(entry.getNodeId())) {
                rmItem = iEntry;
                break;
            }
        }
        if (rmItem != null) {
            return this.entries.remove(rmItem);
        }
        return false;
    }

    public List<IEntry> getEntries() {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        return this.entries;
    }

    public void setEntries(List<IEntry> entries) {
        this.entries = entries;
    }
}
