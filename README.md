Reproducer of an HSQLDB issue
==========

The reproducer can be run with `mvn clean test`

It yields:

````
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
````

Environment:

* HSQLDB 2.4.0
* Oracle JDK 1.8.0_121 (Mac OS X)
