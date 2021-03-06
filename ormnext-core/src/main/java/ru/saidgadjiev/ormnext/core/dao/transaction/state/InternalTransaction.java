package ru.saidgadjiev.ormnext.core.dao.transaction.state;

/**
 * Interface for change internal transaction state.
 *
 * @author Said Gadjiev
 */
public interface InternalTransaction {

    /**
     * Change state.
     *
     * @param transactionState target state
     * @see TransactionState
     */
    void changeState(TransactionState transactionState);
}
