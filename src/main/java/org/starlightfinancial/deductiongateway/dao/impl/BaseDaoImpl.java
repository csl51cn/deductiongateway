package org.starlightfinancial.deductiongateway.dao.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.dao.BaseDao;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseDaoImpl<T> extends HibernateDaoSupport implements BaseDao<T> {
    public final Class<T> modelClass;

    public final String HQL_QUERY;

    public final String HQL_COUNT;

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        super();
        modelClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        HQL_QUERY = "from " + modelClass.getSimpleName()
                + " t where 1 = 1 {0} ";
        HQL_COUNT = "select cast(count(*) as int) from "
                + modelClass.getSimpleName() + " t where 1 = 1 {0} ";
    }

    @Autowired
    @Qualifier("sessionFactory")
    public void setSessionFactory0(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public T load(Integer id) {
        T t = null;
        try {
            t = (T) getHibernateTemplate().get(modelClass, id);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public T save(T obj) {
        try {

            Map<String, Object> properties = PropertyUtils.describe(obj);

            for (Map.Entry<String, Object> e : properties.entrySet()) {
                Object value = e.getValue();

                if (value instanceof BigDecimal) {
                    BigDecimal decimal = (BigDecimal) value;
                    PropertyUtils.setProperty(obj, e.getKey(),
                            decimal.setScale(2, RoundingMode.HALF_UP));
                }
            }

            getHibernateTemplate().save(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED)
    public T update(T obj) {
        try {

            Map<String, Object> properties = PropertyUtils.describe(obj);

            for (Map.Entry<String, Object> e : properties.entrySet()) {
                Object value = e.getValue();

                if (value instanceof BigDecimal) {
                    BigDecimal decimal = (BigDecimal) value;
                    PropertyUtils.setProperty(obj, e.getKey(),
                            decimal.setScale(2, RoundingMode.HALF_UP));
                }
            }

            getHibernateTemplate().update(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return obj;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public T saveOrUpdate(T obj) {
        try {
            getHibernateTemplate().saveOrUpdate(obj);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }

        return obj;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(T obj) {
        try {
            getHibernateTemplate().delete(obj);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(Integer id) {
        try {
            delete(load(id));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<T> query(String condition) {
        List<T> t = new ArrayList<T>();
        try {
            t = (List<T>) getHibernateTemplate().find(
                    MessageFormat.format(HQL_QUERY, condition));
        } catch (Exception e) {
            e.printStackTrace();
            return t;
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public <T2> T2 queryForObject(String condition, Class<T2> requiredType) {
        //object = (T2) getSessionFactory().getCurrentSession().createQuery(MessageFormat.format("from " + requiredType.getSimpleName() + " t where {0} ", condition)).uniqueResult();
        String hql = MessageFormat.format("from {0} t where {1} ", requiredType.getSimpleName(), condition);
        List<T2> list = getSessionFactory().getCurrentSession().createQuery(hql).list();
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            logger.error(String.format("请核实数据是否不允许多条记录：%s。", hql));
        }
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<T> pageQuery(String condition, int start, int length) {
        List<T> t = new ArrayList<T>();
        try {
            Query query = getSessionFactory().getCurrentSession().createQuery(
                    MessageFormat.format(HQL_QUERY, condition));
            query.setFirstResult(start);
            query.setMaxResults(length);
            t = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public int count(String condition) {
        int count = 0;
        try {
            count = (Integer) getHibernateTemplate()
                    .find(MessageFormat.format(HQL_COUNT, condition))
                    .iterator().next();
        } catch (DataAccessException e) {
            e.printStackTrace();
            ;
        }
        return count;
    }

    public T refresh(T obj) {
        getSessionFactory().getCurrentSession().flush();
        getSessionFactory().getCurrentSession().refresh(obj);
        return obj;
    }

    public void clearSession() {
        getSessionFactory().getCurrentSession().flush();
        getSessionFactory().getCurrentSession().clear();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<Map> executeSQL(String condition) {
        return getSessionFactory().getCurrentSession().createSQLQuery(condition)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, Object> queryForMap(String condition) {
        return (Map) getSessionFactory().getCurrentSession().createSQLQuery(condition)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();

    }

    public void executeUpdate(String sql) {
        getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
    }

    public int queryForInt(String sql) {
        return (Integer) getSessionFactory().getCurrentSession().createSQLQuery(sql).uniqueResult();
    }

    public BigDecimal queryForBigDecimal(String sql) {
        return (BigDecimal) getSessionFactory().getCurrentSession().createSQLQuery(sql).uniqueResult();
    }

    /**
     * @author piggy
     */
    @SuppressWarnings("unchecked")
    public <T3> List<T3> queryForList(String sql, Class<T3> requiredType) {
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(requiredType));
        return query.list();
    }
}