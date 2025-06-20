package com.example.fitquiz.database.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.fitquiz.database.Converters;
import com.example.fitquiz.database.entities.ChallengeResult;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ChallengeResultDao_Impl implements ChallengeResultDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ChallengeResult> __insertionAdapterOfChallengeResult;

  public ChallengeResultDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChallengeResult = new EntityInsertionAdapter<ChallengeResult>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `challenge_results` (`id`,`challengeType`,`targetReps`,`completedReps`,`completed`,`date`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChallengeResult value) {
        stmt.bindLong(1, value.id);
        if (value.challengeType == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.challengeType);
        }
        stmt.bindLong(3, value.targetReps);
        stmt.bindLong(4, value.completedReps);
        final int _tmp = value.completed ? 1 : 0;
        stmt.bindLong(5, _tmp);
        final Long _tmp_1 = Converters.dateToTimestamp(value.date);
        if (_tmp_1 == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, _tmp_1);
        }
      }
    };
  }

  @Override
  public void insert(final ChallengeResult challengeResult) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfChallengeResult.insert(challengeResult);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ChallengeResult> getAllResults() {
    final String _sql = "SELECT * FROM challenge_results ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
      final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
      final int _cursorIndexOfCompletedReps = CursorUtil.getColumnIndexOrThrow(_cursor, "completedReps");
      final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final List<ChallengeResult> _result = new ArrayList<ChallengeResult>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChallengeResult _item;
        final String _tmpChallengeType;
        if (_cursor.isNull(_cursorIndexOfChallengeType)) {
          _tmpChallengeType = null;
        } else {
          _tmpChallengeType = _cursor.getString(_cursorIndexOfChallengeType);
        }
        final int _tmpTargetReps;
        _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
        final int _tmpCompletedReps;
        _tmpCompletedReps = _cursor.getInt(_cursorIndexOfCompletedReps);
        final boolean _tmpCompleted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfCompleted);
        _tmpCompleted = _tmp != 0;
        final Date _tmpDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp_1);
        _item = new ChallengeResult(_tmpChallengeType,_tmpTargetReps,_tmpCompletedReps,_tmpCompleted,_tmpDate);
        _item.id = _cursor.getInt(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<ChallengeResult> getResultsFromDate(final long startDate) {
    final String _sql = "SELECT * FROM challenge_results WHERE date >= ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfChallengeType = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeType");
      final int _cursorIndexOfTargetReps = CursorUtil.getColumnIndexOrThrow(_cursor, "targetReps");
      final int _cursorIndexOfCompletedReps = CursorUtil.getColumnIndexOrThrow(_cursor, "completedReps");
      final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final List<ChallengeResult> _result = new ArrayList<ChallengeResult>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ChallengeResult _item;
        final String _tmpChallengeType;
        if (_cursor.isNull(_cursorIndexOfChallengeType)) {
          _tmpChallengeType = null;
        } else {
          _tmpChallengeType = _cursor.getString(_cursorIndexOfChallengeType);
        }
        final int _tmpTargetReps;
        _tmpTargetReps = _cursor.getInt(_cursorIndexOfTargetReps);
        final int _tmpCompletedReps;
        _tmpCompletedReps = _cursor.getInt(_cursorIndexOfCompletedReps);
        final boolean _tmpCompleted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfCompleted);
        _tmpCompleted = _tmp != 0;
        final Date _tmpDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp_1);
        _item = new ChallengeResult(_tmpChallengeType,_tmpTargetReps,_tmpCompletedReps,_tmpCompleted,_tmpDate);
        _item.id = _cursor.getInt(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getCompletedChallengesCount(final long startDate) {
    final String _sql = "SELECT COUNT(*) FROM challenge_results WHERE completed = 1 AND date >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
