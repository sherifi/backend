package eu.datlab.worker.ee.parsed;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dl.dataaccess.dto.parsed.ParsedBid;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.dataaccess.dto.parsed.ParsedTenderLot;
import eu.dl.worker.utils.jsoup.JsoupUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Design contest result handler for E-procurement in Estonia.
 *
 * @author Tomas Mrazek
 */
public final class EPEDesignContestResultHandler {

    private static final Logger logger = LoggerFactory.getLogger(EPEDesignContestResultHandler.class);

    /**
     * Supress default constructor for noninstantiability.
     */
    private EPEDesignContestResultHandler() {
        throw new AssertionError();
    }

    /**
     * Parses contract notice specific data.
     *
     * @param doc
     *      parsed document
     * @param publicationDate
     *      publication date
     * @return parsed tender
     */
    public static ParsedTender parse(final Document doc, final String publicationDate) {
        Element context = EPEParserUtils.getDataTable(doc);

        return EPEParserUtils.parsePublicationAndTitle(doc, publicationDate)
            .setCpvs(EPEParserUtils.parseCPVs("^II\\.1\\.3\\)", context))
            .addFunding(EPEParserUtils.parseEUFunding("^VI\\.1\\)", context))
            .setAppealBodyName(EPEParserUtils.parseAppealBodyName("^VI\\.3\\.3\\)", context))
            .setLots(EPEParserUtils.parseLots(
                EPEDesignContestResultHandler::parseLot,
                JsoupUtils.selectFirst("tr:matches(^V OSA:) + tr", context),
                JsoupUtils.selectFirst("tr:matches(^VI OSA:)", context),
                null));
            
    }

    /**
     * @param node
     *      node that includes lot data
     * @param metaData
     *      meta data
     * @return parsed lot or null
     */
    private static List<ParsedTenderLot> parseLot(final Element node, final Map<String, Object> metaData) {
        if (node == null) {
            return null;
        }

        return Arrays.asList(new ParsedTenderLot()
            // text of first row includes number, remove non-digit characters
            .setLotNumber(JsoupUtils.selectText("tr", node).replaceAll("\\D", ""))
            .setTitle(EPEParserUtils.regexValueByLabel("Nimetus", "(?<value>.+)", node))
            .setMaxFrameworkAgreementParticipants(EPEParserUtils.regexValueByLabel("^V\\.1\\.1\\)", "(?<value>\\d+)",
                node))
            .addBid(new ParsedBid()
                .setIsWinning(Boolean.TRUE.toString())
                .addBidder(EPEParserUtils.parseBody("^V\\.1\\.3\\)", node))
                .setPrice(EPEParserUtils.parsePrice("^V\\.2\\)", node))));
    }
}
