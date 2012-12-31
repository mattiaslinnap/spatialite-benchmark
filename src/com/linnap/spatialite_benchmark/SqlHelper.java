package com.linnap.spatialite_benchmark;

import android.content.Context;
import android.util.Log;
import jsqlite.*;

import java.io.File;

public class SqlHelper {
    public static final String TAG = "Spatialite";

    public static File dbfile(Context context) {
        return new File(context.getExternalFilesDir(null), "test.db");
    }

    public static long size(Context context) {
        File file = dbfile(context);
        if (file.exists())
            return file.length();
        else
            return -1;
    }

    public static Database open(Context context) {
        Database db = new Database();
        try {
            db.open(dbfile(context).getAbsolutePath(), Constants.SQLITE_OPEN_READWRITE | Constants.SQLITE_OPEN_CREATE);

            Stmt create = db.prepare("CREATE TABLE IF NOT EXISTS log (" +
                    "counter_obj INTEGER PRIMARY KEY, " +
                    "counter_boot INTEGER, " +
                    "time_system INTEGER, " +
                    "time_realtime INTEGER, " +
                    "time_uptime INTEGER, " +
                    "tag TEXT NOT NULL, " +
                    "data BLOB);");
            create.step();
            create.close();
            return db;
        } catch (jsqlite.Exception e) {
            Log.e(TAG, "SQL error", e);
            return null;
        }
    }

    public static void append(Database db, long counter_obj, long counter_boot, long time_system, long time_realtime, long time_uptime, String tag, String data) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }

        try {
            Stmt insert = db.prepare("INSERT INTO log VALUES (?,?,?,?,?,?,?);");
            insert.bind(1, counter_obj);
            insert.bind(2, counter_boot);
            insert.bind(3, time_system);
            insert.bind(4, time_realtime);
            insert.bind(5, time_uptime);
            insert.bind(6, tag);
            insert.bind(7, data);
            insert.step();
        } catch (jsqlite.Exception e) {
            Log.e(TAG, "SQL error", e);
        }
    }

    public static void truncate(Database db) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }

        try {
            Stmt delete = db.prepare("DELETE FROM log;");
            delete.step();
        } catch (jsqlite.Exception e) {
            Log.e(TAG, "SQL error", e);
        }
    }

    public static long count(Database db) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return -1;
        }

        try {
            Stmt count = db.prepare("SELECT COUNT(*) FROM log;");
            count.step();
            return count.column_long(0);
        } catch (jsqlite.Exception e) {
            Log.e(TAG, "SQL error", e);
            return -1;
        }
    }

    public static void close(Database db) {
        if (db == null) {
            Log.e(TAG, "DB is null");
            return;
        }

        try {
            db.close();
        } catch (jsqlite.Exception e) {
            Log.e(TAG, "SQL error", e);
        }
    }
}
