package repro;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReproducerTest {

    private static final String CREATE_TABLE =
            "DECLARE LOCAL TEMPORARY TABLE SOME_TABLE (\n" +
                    "  SOME_COL          BIGINT PRIMARY KEY)\n";

    @Before
    public void setUp() throws Exception {
        FileUtils.forceMkdir(new File("./work"));
        FileUtils.cleanDirectory(new File("./work"));
    }

    @Test
    public void closeConnectionThrowsSoManyExceptions() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:hsqldb:file:./work/work_db;hsqldb.result_max_memory_rows=1000");
        c.setAutoCommit(false);

        try (CallableStatement s = c.prepareCall(CREATE_TABLE)) {
            s.execute();
        }

        PreparedStatement ps = c.prepareStatement("INSERT INTO SOME_TABLE (SOME_COL) VALUES (?)");

        // max_memory_rows exceeded by 1
        for (int i = 0; i < 1001; i++) {
            ps.setLong(1, i);
            ps.executeUpdate();
        }

        ps.close();

        c.rollback(); // so many exceptions occur...

        /*
2018-02-21T19:56:45.238+0900  WARNING  failed to read a byte array
java.io.EOFException
	at org.hsqldb.persist.RAFile.read(Unknown Source)
	at org.hsqldb.persist.RAFile.readInt(Unknown Source)
	at org.hsqldb.persist.DataFileCache.readObject(Unknown Source)
	at org.hsqldb.persist.DataFileCache.getFromFile(Unknown Source)
	at org.hsqldb.persist.DataFileCache.get(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybrid.get(Unknown Source)
	at org.hsqldb.index.NodeAVLDisk.findNode(Unknown Source)
	at org.hsqldb.index.NodeAVLDisk.getRight(Unknown Source)
	at org.hsqldb.index.IndexAVL.findNode(Unknown Source)
	at org.hsqldb.index.IndexAVL.findFirstRow(Unknown Source)
	at org.hsqldb.Table.getDeleteRowFromLog(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybridExtended.delete(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybrid.rollbackRow(Unknown Source)
	at org.hsqldb.TransactionManager2PL.rollbackPartial(Unknown Source)
	at org.hsqldb.TransactionManager2PL.rollback(Unknown Source)
	at org.hsqldb.Session.rollbackNoCheck(Unknown Source)
	at org.hsqldb.Session.rollback(Unknown Source)
	at org.hsqldb.Session.close(Unknown Source)
	at org.hsqldb.jdbc.JDBCConnection.close(Unknown Source)
	at repro.MyTest2.name(MyTest2.java:44)

	...

	java.lang.NullPointerException
	at org.hsqldb.persist.DataFileCache.get(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybrid.get(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybrid.getAccessor(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybridExtended.getAccessor(Unknown Source)
	at org.hsqldb.index.IndexAVL.getAccessor(Unknown Source)
	at org.hsqldb.index.IndexAVL.findNode(Unknown Source)
	at org.hsqldb.index.IndexAVL.findFirstRow(Unknown Source)
	at org.hsqldb.Table.getDeleteRowFromLog(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybridExtended.delete(Unknown Source)
	at org.hsqldb.persist.RowStoreAVLHybrid.rollbackRow(Unknown Source)
	at org.hsqldb.TransactionManager2PL.rollbackPartial(Unknown Source)
	at org.hsqldb.TransactionManager2PL.rollback(Unknown Source)
	at org.hsqldb.Session.rollbackNoCheck(Unknown Source)
	at org.hsqldb.Session.rollback(Unknown Source)
	at org.hsqldb.jdbc.JDBCConnection.rollback(Unknown Source)
         */

        c.close();
    }
}
