package org.chim.altass.core.domain.buildin.attr;

import org.chim.altass.base.parser.exml.anntation.Attr;
import org.chim.altass.base.parser.exml.anntation.Elem;
import org.chim.altass.core.domain.buildin.entry.Entry;

/**
 * Class Name: AGroup
 * Create Date: 2017/10/27 16:52
 * Creator: Chim·Zigui
 * Version: v1.0
 * Updater:
 * Date Time:
 * Description:
 */
@Elem(alias = "region", version = "1.0")
public class ARegion {

    @Attr(alias = "regionId")
    private String regionId = null;
    @Attr(alias = "regionName")
    private String regionName = null;
    @Attr(alias = "regionMaster")
    private String regionMaster = null;

    public ARegion() {
        this(null);
    }

    public ARegion(String regionId) {
        this(regionId, null);
    }

    public ARegion(String regionId, String regionName) {
        this(regionId, regionName, null);
    }

    public ARegion(String regionId, String regionName, String regionMaster) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.regionMaster = regionMaster;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionMaster() {
        return regionMaster;
    }

    public void setRegionMaster(String regionMaster) {
        this.regionMaster = regionMaster;
    }

    public void bind(Entry... entries) {
        if (entries != null) {
            for (Entry entry : entries) {
                entry.setRegion(this);
            }
        }
    }
}
