package ru.said.orm.next.core.stament_executor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.said.orm.next.core.cache.LRUObjectCache;
import ru.said.orm.next.core.cache.ObjectCache;
import ru.said.orm.next.core.field.DBField;
import ru.said.orm.next.core.stament_executor.object.DataBaseObject;
import ru.said.orm.next.core.table.TableInfo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.*;

public class CachedStatementExecutorTest {

    private static TableInfo<TestClazz> tableInfo;

    @Before
    public void setUp() throws Exception {
        tableInfo = TableInfo.TableInfoCache.build(TestClazz.class);
    }

    @Test
    public void create() throws Exception {
        DataBaseObject<TestClazz> dataBaseObject = createDBObject();
        ObjectCache objectCache = dataBaseObject.getObjectCache().get();
        IStatementExecutor<TestClazz, Integer> statementExecutor = Mockito.mock(IStatementExecutor.class);
        TestClazz clazz = new TestClazz();

        clazz.id = 1;
        Mockito.when(statementExecutor.create(null, clazz)).thenReturn(1);
        CachedStatementExecutor<TestClazz, Integer> cachedStatementExecutor = new CachedStatementExecutor<>(dataBaseObject, statementExecutor);

        cachedStatementExecutor.create(null, clazz);
        Assert.assertEquals(1, objectCache.size(TestClazz.class));
        Assert.assertEquals(1, objectCache.sizeAll());
        Assert.assertEquals(1, objectCache.get(TestClazz.class, 1).id);
    }

    @Test
    public void update() throws Exception {
        DataBaseObject<TestClazz> dataBaseObject = createDBObject();
        ObjectCache objectCache = dataBaseObject.getObjectCache().get();
        IStatementExecutor<TestClazz, Integer> statementExecutor = Mockito.mock(IStatementExecutor.class);
        CachedStatementExecutor<TestClazz, Integer> cachedStatementExecutor = new CachedStatementExecutor<>(dataBaseObject, statementExecutor);
        TestClazz clazz = createTestClazz(1, "Said");
        TestClazz updatedClazz = createTestClazz(1, "SaidTest");

        Mockito.when(statementExecutor.create(null, clazz)).thenReturn(1);
        Mockito.when(statementExecutor.update(null, updatedClazz)).thenReturn(1);

        cachedStatementExecutor.create(null, clazz);
        Assert.assertEquals(1, objectCache.get(TestClazz.class, 1).id);
        Assert.assertEquals("Said", objectCache.get(TestClazz.class, 1).name);
        cachedStatementExecutor.update(null, updatedClazz);
        Assert.assertEquals(1, objectCache.get(TestClazz.class, 1).id);
        Assert.assertEquals("SaidTest", objectCache.get(TestClazz.class, 1).name);
    }

    @Test
    public void delete() throws Exception {
        DataBaseObject<TestClazz> dataBaseObject = createDBObject();
        ObjectCache objectCache = dataBaseObject.getObjectCache().get();
        IStatementExecutor<TestClazz, Integer> statementExecutor = Mockito.mock(IStatementExecutor.class);
        CachedStatementExecutor<TestClazz, Integer> cachedStatementExecutor = new CachedStatementExecutor<>(dataBaseObject, statementExecutor);
        TestClazz clazz = createTestClazz(1, "Said");
        TestClazz updatedClazz = createTestClazz(1, "SaidTest");

        Mockito.when(statementExecutor.create(null, clazz)).thenReturn(1);
        Mockito.when(statementExecutor.delete(null, updatedClazz)).thenReturn(1);

        cachedStatementExecutor.create(null, clazz);
        Assert.assertEquals(1, objectCache.get(TestClazz.class, 1).id);
        Assert.assertEquals("Said", objectCache.get(TestClazz.class, 1).name);
        Assert.assertEquals(1, objectCache.size(TestClazz.class));
        Assert.assertEquals(1, objectCache.sizeAll());
        cachedStatementExecutor.delete(null, updatedClazz);
        Assert.assertNull(objectCache.get(TestClazz.class, 1));
        Assert.assertEquals(0, objectCache.size(TestClazz.class));
        Assert.assertEquals(0, objectCache.sizeAll());
    }

    @Test
    public void queryForId() throws Exception {
        DataBaseObject<TestClazz> dataBaseObject = createDBObject();
        ObjectCache objectCache = dataBaseObject.getObjectCache().get();
        IStatementExecutor<TestClazz, Integer> statementExecutor = Mockito.mock(IStatementExecutor.class);
        CachedStatementExecutor<TestClazz, Integer> cachedStatementExecutor = new CachedStatementExecutor<>(dataBaseObject, statementExecutor);
        TestClazz clazz = createTestClazz(1, "Said");

        Mockito.when(statementExecutor.queryForId(null, 1)).thenReturn(clazz);

        TestClazz queryForIdClazz1 = cachedStatementExecutor.queryForId(null, 1);

        Assert.assertEquals(1, objectCache.get(TestClazz.class, 1).id);
        Assert.assertEquals("Said", objectCache.get(TestClazz.class, 1).name);
        Assert.assertEquals(1, objectCache.size(TestClazz.class));
        Assert.assertEquals(1, objectCache.sizeAll());

        Assert.assertEquals(1, queryForIdClazz1.id);
        Assert.assertEquals("Said", queryForIdClazz1.name);

        TestClazz queryForIdClazz2 = cachedStatementExecutor.queryForId(null, 1);

        Assert.assertEquals(1, queryForIdClazz2.id);
        Assert.assertEquals("Said", queryForIdClazz2.name);

        Mockito.verify(statementExecutor, Mockito.times(1)).queryForId(null, 1);
    }

    @Test
    public void queryForAll() throws Exception {
    }

    private DataBaseObject<TestClazz> createDBObject() {
        ObjectCache objectCache = new LRUObjectCache(16);

        objectCache.registerClass(TestClazz.class);

        return new DataBaseObject<>(null, tableInfo).caching(true).objectCache(objectCache);
    }

    private TestClazz createTestClazz(Object ... args) throws IllegalAccessException {
        TestClazz clazz = new TestClazz();
        Field [] fields = TestClazz.class.getDeclaredFields();

        for (int i = 0; i < fields.length; ++i) {
            fields[i].setAccessible(true);
            fields[i].set(clazz, args[i]);
            fields[i].setAccessible(false);
        }

        return clazz;
    }

    private static class TestClazz {
        @DBField(id = true)
        private int id;

        @DBField
        private String name;
    }
}