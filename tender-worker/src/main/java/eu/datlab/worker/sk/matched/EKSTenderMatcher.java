package eu.datlab.worker.sk.matched;

import eu.datlab.worker.matched.BaseDatlabTenderMatcher;
import eu.dl.core.UnrecoverableException;
import eu.dl.dataaccess.dto.matched.MatchedBody;
import eu.dl.dataaccess.dto.matched.MatchedTender;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import static eu.dl.dataaccess.utils.DigestUtils.bodyHash;

/**
 * Matcher for EKS.
 *
 * @author Tomas Mrazek
 */
public class EKSTenderMatcher extends BaseDatlabTenderMatcher {
    private static final String VERSION = "1.0";

    @Override
    protected final String getVersion() {
        return VERSION;
    }

    @Override
    protected final void registerBodyPlugins() {
    }

    @Override
    protected final void registerTenderPlugins() {
    }

    @Override
    protected final String generateBodyHash(final MatchedBody matchedBody) {
        return bodyHash(matchedBody);
    }

    @Override
    protected final String generateTenderHash(final MatchedTender matchedTender) {
        String publication = "";

        try {
            // there is no source id assigned, the hash cannot be reasonably
            // calculated
            publication = UUID.randomUUID().toString();

            byte[] data = UUID.randomUUID().toString().getBytes("UTF-8");
            return DigestUtils.sha1Hex(data);
        } catch (UnsupportedEncodingException e) {
            logger.error("Unable to convert \"{}\" to UTF-8", publication);
            throw new UnrecoverableException("Unable to convert data to UTF-8", e);
        }
    }

    @Override
    protected final String getTenderMessagingTag() {
        return this.getName() + TENDER_MESSAGING_TAG_SUFFIX;
    }

    @Override
    protected final String getBodyMessagingTag() {
        return this.getName() + BODY_MESSAGING_TAG_SUFFIX;
    }
}