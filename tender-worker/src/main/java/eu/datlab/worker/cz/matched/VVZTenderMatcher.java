package eu.datlab.worker.cz.matched;

import eu.datlab.worker.matched.BaseDatlabTenderMatcher;
import eu.dl.dataaccess.dto.matched.MatchedBody;
import eu.dl.dataaccess.dto.matched.MatchedTender;
import eu.dl.dataaccess.utils.TenderUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

import static eu.dl.dataaccess.utils.DigestUtils.bodyHash;

/**
 * Matcher for VVZ (new Vestnik).
 */
public class VVZTenderMatcher extends BaseDatlabTenderMatcher {

    protected static final String VERSION = "1.0";

    private static final List<Pair<String, String>> additionalMatchers = Collections.singletonList(
            Pair.of(VestnikTenderMatcher.class.getCanonicalName(), VestnikTenderMatcher.VERSION));

    /**
     * Constructor.
     */
    public VVZTenderMatcher() {
        super(additionalMatchers);
    }

    @Override
    protected final String getVersion() {
        return VERSION;
    }

    @Override
    protected void registerBodyPlugins() {
    }

    @Override
    protected void registerTenderPlugins() {
        // no other matching plugins needed, because source tender id (used for hash) is never missing
    }

    @Override
    protected final String generateBodyHash(final MatchedBody matchedBody) {
        return bodyHash(matchedBody);
    }

    @Override
    protected final String generateTenderHash(final MatchedTender matchedTender) {
        return TenderUtils.generateTenderHash(matchedTender);
    }

    @Override
    protected final String getTenderMessagingTag() {
        return this.getClass().getName() + TENDER_MESSAGING_TAG_SUFFIX;
    }

    @Override
    protected final String getBodyMessagingTag() {
        return this.getClass().getName() + BODY_MESSAGING_TAG_SUFFIX;
    }
}
