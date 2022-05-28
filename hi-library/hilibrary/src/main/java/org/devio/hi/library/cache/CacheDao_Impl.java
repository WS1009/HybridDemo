package org.devio.hi.library.cache;

import android.database.Cursor;

import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CacheDao_Impl implements CacheDao {
    private final RoomDatabase __db;

    private final EntityInsertionAdapter<Cache> __insertionAdapterOfCache;

    private final EntityDeletionOrUpdateAdapter<Cache> __deletionAdapterOfCache;

    public CacheDao_Impl(RoomDatabase __db) {
        this.__db = __db;
        this.__insertionAdapterOfCache = new EntityInsertionAdapter<Cache>(__db) {
            @Override
            public String createQuery() {
                return "INSERT OR REPLACE INTO `cache` (`key`,`data`) VALUES (?,?)";
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, Cache value) {
                if (value.getKey() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.getKey());
                }
                if (value.getData() == null) {
                    stmt.bindNull(2);
                } else {
                    stmt.bindBlob(2, value.getData());
                }
            }
        };
        this.__deletionAdapterOfCache = new EntityDeletionOrUpdateAdapter<Cache>(__db) {
            @Override
            public String createQuery() {
                return "DELETE FROM `cache` WHERE `key` = ?";
            }

            @Override
            public void bind(SupportSQLiteStatement stmt, Cache value) {
                if (value.getKey() == null) {
                    stmt.bindNull(1);
                } else {
                    stmt.bindString(1, value.getKey());
                }
            }
        };
    }

    @Override
    public long saveCache(final Cache cache) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            long _result = __insertionAdapterOfCache.insertAndReturnId(cache);
            __db.setTransactionSuccessful();
            return _result;
        } finally {
            __db.endTransaction();
        }
    }

    @Override
    public void deleteCache(final Cache cache) {
        __db.assertNotSuspendingTransaction();
        __db.beginTransaction();
        try {
            __deletionAdapterOfCache.handle(cache);
            __db.setTransactionSuccessful();
        } finally {
            __db.endTransaction();
        }
    }

    @Override
    public Cache getCache(final String key) {
        final String _sql = "select * from cache where `key`=:key";
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
        int _argIndex = 1;
        if (key == null) {
            _statement.bindNull(_argIndex);
        } else {
            _statement.bindString(_argIndex, key);
        }
        __db.assertNotSuspendingTransaction();
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
            final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
            final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
            final Cache _result;
            if (_cursor.moveToFirst()) {
                _result = new Cache();
                final String _tmpKey;
                _tmpKey = _cursor.getString(_cursorIndexOfKey);
                _result.setKey(_tmpKey);
                final byte[] _tmpData;
                _tmpData = _cursor.getBlob(_cursorIndexOfData);
                _result.setData(_tmpData);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _cursor.close();
            _statement.release();
        }
    }
}
