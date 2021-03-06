package eu.datlab.worker.it.master;

import java.util.List;

import eu.datlab.worker.master.BaseDatlabTenderMaster;
import eu.dl.dataaccess.dto.master.MasterTender;
import eu.dl.dataaccess.dto.matched.MatchedTender;

/**
 * Master for "Portale della trasparenza" tenders.
 */
public class ANACPDTTenderMaster extends BaseDatlabTenderMaster {
    private static final String VERSION = "1.0";

    @Override
    protected final String getVersion() {
        return VERSION;
    }

    @Override
    protected final String getIncomingQueueName() {
        return getIncomingQueueNameFromConfig();
    }

    @Override
    protected final void registerSpecificPlugins() {
    }

    @Override
    protected final List<MatchedTender> sourceSpecificPreprocessData(final List<MatchedTender> items) {
        return items;
    }
    
    @Override
    protected final MasterTender sourceSpecificPostprocessData(final MasterTender item) {
        return item;
    }
}
