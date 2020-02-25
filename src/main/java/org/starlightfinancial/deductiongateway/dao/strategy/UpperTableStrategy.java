package org.starlightfinancial.deductiongateway.dao.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/19 14:16
 * @Modified By:
 */
public class UpperTableStrategy extends PhysicalNamingStrategyStandardImpl {
    private static final long serialVersionUID = 8139154190295732626L;

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        // 将表名全部转换成大写
        String tableName = name.getText().toUpperCase();
        return Identifier.toIdentifier(tableName);
    }
}
