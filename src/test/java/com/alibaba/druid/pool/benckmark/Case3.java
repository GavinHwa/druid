package com.alibaba.druid.pool.benckmark;

import java.sql.Connection;
import java.text.NumberFormat;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.apache.commons.dbcp.BasicDataSource;

import com.alibaba.druid.TestUtil;
import com.alibaba.druid.pool.DruidDataSource;

public class Case3 extends TestCase {

    private String  jdbcUrl;
    private String  user;
    private String  password;
    private String  driverClass;
    private int     maxIdle         = 40;
    private int     maxActive       = 50;
    private int     maxWait         = 500;
    private String  validationQuery = "SELECT 1";
    private int     threadCount     = 14;
    private int     loopCount       = 3;
    final int       LOOP_COUNT      = 1000 * 100;
    private boolean testOnBorrow    = true;

    protected void setUp() throws Exception {
        jdbcUrl = "jdbc:fake:dragoon_v25masterdb";
        user = "dragoon25";
        password = "dragoon25";
        driverClass = "com.alibaba.druid.mock.MockDriver";
    }

    public void test_0() throws Exception {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setMaxActive(maxActive);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(jdbcUrl);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnBorrow(testOnBorrow);

        for (int i = 0; i < loopCount; ++i) {
            p0(dataSource, "druid", threadCount);
        }
        System.out.println();
    }

    public void test_1() throws Exception {
        final BasicDataSource dataSource = new BasicDataSource();

        dataSource.setMaxActive(maxActive);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(jdbcUrl);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(testOnBorrow);

        for (int i = 0; i < loopCount; ++i) {
            p0(dataSource, "dbcp", threadCount);
        }
        System.out.println();
    }

    private void p0(final DataSource dataSource, String name, int threadCount) throws Exception {

        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(1);
        for (int i = 0; i < threadCount; ++i) {
            Thread thread = new Thread() {

                public void run() {
                    try {
                        startLatch.await();

                        for (int i = 0; i < LOOP_COUNT; ++i) {
                            Connection conn = dataSource.getConnection();
                            conn.close();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    endLatch.countDown();
                }
            };
            thread.start();
        }
        long startMillis = System.currentTimeMillis();
        long startYGC = TestUtil.getYoungGC();
        long startFullGC = TestUtil.getFullGC();
        startLatch.countDown();
        endLatch.await();

        long millis = System.currentTimeMillis() - startMillis;
        long ygc = TestUtil.getYoungGC() - startYGC;
        long fullGC = TestUtil.getFullGC() - startFullGC;

        System.out.println("thread " + threadCount + " " + name + " millis : " + NumberFormat.getInstance().format(millis) + ", YGC " + ygc + " FGC " + fullGC);
    }
}