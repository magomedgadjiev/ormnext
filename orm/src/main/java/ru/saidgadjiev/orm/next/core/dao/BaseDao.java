package ru.saidgadjiev.orm.next.core.dao;

import ru.saidgadjiev.orm.next.core.criteria.impl.SelectStatement;
import ru.saidgadjiev.orm.next.core.stament_executor.GenericResults;
import ru.saidgadjiev.orm.next.core.stament_executor.result_mapper.ResultsMapper;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface BaseDao<T, ID> {

    int create(Collection<T> objects) throws SQLException;

    /**
     * Метод сохраняет объект в базе
     * @param object создаваемый объект
     * @return количество созданных объектов. В данном случае 1
     * @throws SQLException если произошла ошибка при выполнении запроса
     */
    int create(T object) throws SQLException;

    boolean createTable(boolean ifNotExists) throws SQLException;

    /**
     * Метод получает объект по id
     * @param id целевой id объекта
     * @return возвращает объект с заданной id или null
     * @throws SQLException если произошла ошибка при выполнении запроса
     */
    T queryForId(ID id) throws SQLException;

    /**
     * Метод получает все объекты из таблицы T
     * @return все записи из таблицы типа T
     * @throws SQLException если произошла ошибка при выполнении запроса
     */
    List<T> queryForAll() throws SQLException;

    /**
     * Метод обновляет объект в базе
     * @param object обновляемый объект
     * @return количество обновленных объектов. В данном случае 1
     * @throws SQLException если произошла ошибка при выполнении запроса
     */
    int update(T object) throws SQLException;

    /**
     * Метод удаляет запись из базы
     * @param object удаляемый объект
     * @return количество удаленных объектов. В данном случае 1
     * @throws SQLException если произошла ошибка при выполнении запроса
     */
    int delete(T object) throws SQLException;

    int deleteById(ID id) throws SQLException;

    boolean dropTable(boolean ifExists) throws SQLException;

    void createIndexes() throws SQLException;

    void dropIndexes() throws SQLException;

    long countOff() throws SQLException;

    <R> GenericResults<R> query(String query, ResultsMapper<R> resultsMapper) throws SQLException;

    List<T> query(SelectStatement<T> statement) throws SQLException;

    List<T> query(SelectStatement<T> statement, ResultsMapper<T> resultsMapper) throws SQLException;
}
