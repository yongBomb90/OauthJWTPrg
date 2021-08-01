package com.homewrk.backpack.common.database;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Master Slave를 감싸기 위한 Routing Database 객체 생성
 */
public class RoutingDataBase extends AbstractRoutingDataSource {

    /**
     * 나누는 기준은 ReadOnly 여부로 한다.
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "slave" : "master";
    }
}
