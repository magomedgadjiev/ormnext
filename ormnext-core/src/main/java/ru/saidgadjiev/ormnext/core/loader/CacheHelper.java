package ru.saidgadjiev.ormnext.core.loader;

import ru.saidgadjiev.ormnext.core.cache.CacheHolder;
import ru.saidgadjiev.ormnext.core.cache.ObjectCache;
import ru.saidgadjiev.ormnext.core.field.fieldtype.DatabaseColumnType;
import ru.saidgadjiev.ormnext.core.table.internal.metamodel.MetaModel;

import java.util.Optional;

/**
 * Cache helper.
 *
 * @author Said Gadjiev
 */
public class CacheHelper {

    /**
     * Session cache holder.
     */
    private CacheHolder sessionFactoryCacheHolder;

    /**
     * Meta model.
     */
    private MetaModel metaModel;

    /**
     * Create a new helper.
     *
     * @param sessionFactoryCacheHolder session factory cache holder
     * @param metaModel                 target meta model
     */
    public CacheHelper(CacheHolder sessionFactoryCacheHolder, MetaModel metaModel) {
        this.sessionFactoryCacheHolder = sessionFactoryCacheHolder;
        this.metaModel = metaModel;
    }

    /**
     * Save objects to cache.
     *
     * @param objects     target objects
     * @param objectClass target object class
     */
    public void saveToCache(Object[] objects, Class<?> objectClass) {
        if (sessionFactoryCacheHolder.isCaching(objectClass)) {
            DatabaseColumnType primaryKeyColumnType = metaModel
                    .getPersister(objectClass)
                    .getMetadata()
                    .getPrimaryKeyColumnType();

            ObjectCache objectCache = sessionFactoryCacheHolder.getObjectCache();

            for (Object object : objects) {
                objectCache.put(object.getClass(), primaryKeyColumnType.access(object), object);
            }
        }
    }

    /**
     * Save object to cache.
     *
     * @param id     object id
     * @param object object instance
     */
    public void saveToCache(Object id, Object object) {
        if (sessionFactoryCacheHolder.isCaching(object.getClass())) {
            ObjectCache objectCache = sessionFactoryCacheHolder.getObjectCache();
            if (id != null) {
                objectCache.put(object.getClass(), id, object);
            } else {
                DatabaseColumnType primaryKeyColumnType = metaModel
                        .getPersister(object.getClass())
                        .getMetadata()
                        .getPrimaryKeyColumnType();

                objectCache.put(object.getClass(), primaryKeyColumnType.access(object), object);
            }
        }
    }

    /**
     * Remove object from cache.
     *
     * @param objectClass object class
     * @param id          object id
     */
    public void delete(Class<?> objectClass, Object id) {
        if (sessionFactoryCacheHolder.isCaching(objectClass)) {
            sessionFactoryCacheHolder.getObjectCache().invalidate(objectClass, id);
        }
    }

    /**
     * Retrieve object instance from cache.
     *
     * @param objectClass object class
     * @param id          object id
     * @return cached instance
     */
    public Optional<Object> get(Class<?> objectClass, Object id) {
        if (sessionFactoryCacheHolder.isCaching(objectClass)) {
            ObjectCache objectCache = sessionFactoryCacheHolder.getObjectCache();

            return Optional.ofNullable(objectCache.get(objectClass, id));
        }

        return Optional.empty();
    }
}