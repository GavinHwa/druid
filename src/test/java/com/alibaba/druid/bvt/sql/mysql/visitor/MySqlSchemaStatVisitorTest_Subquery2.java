package com.alibaba.druid.bvt.sql.mysql.visitor;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Column;

public class MySqlSchemaStatVisitorTest_Subquery2 extends TestCase {

    public void test_0() throws Exception {
        String sql = "SELECT a.id, a.name, b.name groupName FROM (select id, name, groupId from users limit 10) a inner join groups b on a.groupId = b.id";

        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement statemen = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statemen.accept(visitor);

        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());

        Assert.assertEquals(2, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("users"));
        Assert.assertEquals(true, visitor.containsTable("groups"));

        Assert.assertEquals(5, visitor.getColumns().size());
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "id")));
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "groupId")));
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("users", "name")));
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("groups", "id")));
        Assert.assertEquals(true, visitor.getColumns().contains(new Column("groups", "name")));

    }

}
