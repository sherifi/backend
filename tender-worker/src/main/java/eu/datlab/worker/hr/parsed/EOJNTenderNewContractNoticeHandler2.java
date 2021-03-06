package eu.datlab.worker.hr.parsed;

import eu.dl.dataaccess.dto.parsed.ParsedPrice;
import eu.dl.dataaccess.dto.parsed.ParsedTender;
import eu.dl.worker.utils.jsoup.JsoupUtils;

import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.CHECKBOX_TEXT_SELECTOR;
import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.SUBSECTION_III_1_SELECTOR;
import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.SUBSECTION_III_2_SELECTOR;
import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.SUBSECTION_II_1_SELECTOR;
import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.SUBSECTION_II_2_SELECTOR;
import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.SUBSECTION_II_3_SELECTOR;
import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.SUBSECTION_I_3_SELECTOR;
import static eu.datlab.worker.hr.parsed.EOJNTenderNewFormUtils.SUBSECTION_VI_4_SELECTOR;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * New contract notice form parser for Croatia.
 * It parses specific fields of form "Poziv na nadmetanje"
 *
 * @author Marek Mikes
 */
final class EOJNTenderNewContractNoticeHandler2 {
    /**
     * Private constructor to make this class static.
     */
    private EOJNTenderNewContractNoticeHandler2() {}

    /**
     * Parses form specific data.
     *
     * @param parsedTender
     *         tender to add data to
     * @param document
     *         document to parse data from
     *
     * @return ParsedTender with parsed data
     */
    static ParsedTender parse(final ParsedTender parsedTender, final Document document) {
        final Element subsectionI3 = JsoupUtils.selectFirst(SUBSECTION_I_3_SELECTOR, document);
        final Element subsectionII1 = JsoupUtils.selectFirst(SUBSECTION_II_1_SELECTOR, document);
        final Element subsectionII2 = JsoupUtils.selectFirst(SUBSECTION_II_2_SELECTOR, document);
        final Element subsectionII3 = JsoupUtils.selectFirst(SUBSECTION_II_3_SELECTOR, document);
        final Element subsectionIII1 = JsoupUtils.selectFirst(SUBSECTION_III_1_SELECTOR, document);
        final Element subsectionIII2 = JsoupUtils.selectFirst(SUBSECTION_III_2_SELECTOR, document);
        final Element subsectionVI4 = JsoupUtils.selectFirst(SUBSECTION_VI_4_SELECTOR + "," +
                "p:contains(VI.4) + p + table", document);

        parsedTender
                .setAreVariantsAccepted(EOJNTenderNewFormUtils.parseBooleanFromCheckboxes("AlternPonuda_DA1",
                        "AlternPonuda_NE1", subsectionII1))
                .setEstimatedPrice(parseTenderEstimatedPrice(subsectionII2))
                .setHasOptions(EOJNTenderNewFormUtils.parseBooleanFromCheckboxes("Opcije_DA1", "Opcije_NE1",
                        subsectionII1))
                .setEstimatedDurationInMonths(JsoupUtils.selectText("a[name=TrajRazMj1] + span", subsectionII3))
                .setEstimatedDurationInDays(JsoupUtils.selectText("a[name=TrajRazD1] + span", subsectionII3))
                .setEstimatedStartDate(JsoupUtils.selectText("a[name=TrajPoc1] + span", subsectionII3))
                .setEstimatedCompletionDate(JsoupUtils.selectText("a[name=TrajKraj1] + span", subsectionII3))
                .setDeposits(JsoupUtils.selectText("p:contains(III.1.1) + p + table", subsectionIII1))
                .setPersonalRequirements(JsoupUtils.selectText("tr:contains(III.2.1) + tr > td > table",
                        subsectionIII2))
                .setTechnicalRequirements(JsoupUtils.selectText("tr:contains(III.2.3) + tr + tr", subsectionIII2))
                .setAppealBodyName(EOJNTenderNewFormUtils.parseTenderAppealBodyName(subsectionVI4));

        if (parsedTender.getBuyers() != null) {
            parsedTender.getBuyers().get(0)
                .addMainActivity(JsoupUtils.selectText(CHECKBOX_TEXT_SELECTOR, subsectionI3));
        }

        return parsedTender;
    }

    /**
     * Parse tender estimated price value from document.
     *
     * @param subsectionII2
     *         subsection II.2 to be parsed
     *
     * @return tender estimated price or Null
     */
    private static ParsedPrice parseTenderEstimatedPrice(final Element subsectionII2) {
        final ParsedPrice price = new ParsedPrice()
                .setNetAmount(JsoupUtils.selectText("a[name=ProcVrijednost1] + span", subsectionII2));

        final Element currencyElement = JsoupUtils.selectFirst("input[name=Valuta1]", subsectionII2);
        if (currencyElement != null) {
            return price
                    .setCurrency(currencyElement.val());
        } else {
            // e.g. https://eojn.nn.hr/SPIN/APPLICATION/IPN/DocumentManagement/DokumentPodaciFrm.aspx?id=169765
            return price
                    .setCurrency(JsoupUtils.selectText("td:has(a[name=ProcVrijednost1]) + td > p > span:nth-child(2)",
                            subsectionII2));
        }
    }

}
