package eu.datlab.worker.downloader;

import eu.datlab.dataaccess.dao.DAOFactory;
import eu.dl.dataaccess.dao.RawDAO;
import eu.dl.dataaccess.dao.TransactionUtils;
import eu.dl.dataaccess.dto.raw.RawData;
import eu.dl.worker.raw.downloader.BaseOnClickHttpDownloader;

/**
 * On Click HTTP Downloader abstract class for Tenders.
 */
public abstract class BaseTenderOnClickHttpDownloader extends BaseOnClickHttpDownloader<RawData> {

    @Override
    public final RawDAO getRawDataDao() {
        return DAOFactory.getDAOFactory().getRawTenderDAO(getName(), getVersion());
    }

    @Override
    protected final TransactionUtils getTransactionUtils() {
        return DAOFactory.getDAOFactory().getTransactionUtils();
    }
}
