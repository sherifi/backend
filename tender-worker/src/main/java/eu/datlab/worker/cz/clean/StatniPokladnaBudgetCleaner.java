package eu.datlab.worker.cz.clean;

import eu.datlab.dataaccess.dao.DAOFactory;
import eu.datlab.dataaccess.dto.clean.CleanBudgetItem;
import eu.datlab.dataaccess.dto.codetables.BudgetItemReportType;
import eu.datlab.dataaccess.dto.parsed.ParsedBudgetItem;
import eu.datlab.worker.clean.BudgetItemPlugin;
import eu.datlab.worker.cz.clean.plugin.StatniPokladnaBodyIdPlugin;
import eu.dl.dataaccess.dao.CleanDAO;
import eu.dl.dataaccess.dao.ParsedDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.worker.clean.BaseCleaner;
import eu.dl.worker.clean.utils.CodeTableUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Cleans budgets from Statni Pokladna and stores them into database.
 */
public final class StatniPokladnaBudgetCleaner extends BaseCleaner<ParsedBudgetItem, CleanBudgetItem> {

    private static final String VERSION = "1.0";

    private static final Locale LOCALE = new Locale("en");

    private static final NumberFormat NUMBER_FORMAT;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(LOCALE);
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator(' ');
        NUMBER_FORMAT = new DecimalFormat("#,##0.###", formatSymbols);
    }
    
    @Override
    protected void registerCommonPlugins() {
    }

    @Override
    protected void registerSpecificPlugins() {        
        Map<String, Map<Enum, List<String>>> mappings = new HashMap<>();
        mappings.put(BudgetItemPlugin.BUDGET_ITEM_REPORT_TYPE_MAPPING, reportTypeMapping());
        
        pluginRegistry.registerPlugin("budgetItem", new BudgetItemPlugin(NUMBER_FORMAT, mappings));
        pluginRegistry.registerPlugin("budgetBodyIds", new StatniPokladnaBodyIdPlugin());
    }

    /**
     * @return budget report type mapping
     */
    private Map<Enum, List<String>> reportTypeMapping() {
        return CodeTableUtils.enumToMapping(BudgetItemReportType.class);
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    protected CleanBudgetItem postProcessCommonRules(
            final CleanBudgetItem cleanItem, final ParsedBudgetItem parsedItem) {
        return cleanItem;
    }

    @Override
    protected CleanBudgetItem postProcessSourceSpecificRules(final ParsedBudgetItem parsedItem,
        final CleanBudgetItem cleanItem) {
        return cleanItem;
    }

    @Override
    protected CleanDAO<CleanBudgetItem> getCleanDAO() {
        return DAOFactory.getDAOFactory().getCleanBudgetItemDAO(getName(), getVersion());
    }

    @Override
    protected ParsedDAO<ParsedBudgetItem> getParsedDAO() {
        return DAOFactory.getDAOFactory().getParsedBudgetItemDAO(getName(), getVersion());
    }

    @Override
    protected ParsedBudgetItem preProcessParsedItem(final ParsedBudgetItem parsedItem) {
        return parsedItem;
    }

    @Override
    protected TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
