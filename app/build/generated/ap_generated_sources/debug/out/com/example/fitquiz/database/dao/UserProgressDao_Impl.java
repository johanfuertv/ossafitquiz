package com.example.fitquiz.database.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.fitquiz.database.Converters;
import com.example.fitquiz.database.entities.UserProgress;
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
public final class UserProgressDao_Impl implements UserProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserProgress> __insertionAdapterOfUserProgress;

  private final EntityDeletionOrUpdateAdapter<UserProgress> __updateAdapterOfUserProgress;

  public UserProgressDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserProgress = new EntityInsertionAdapter<UserProgress>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `user_progress` (`id`,`date`,`dailyScore`,`quizCompleted`,`challengeCompleted`,`streak`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserProgress value) {
        stmt.bindLong(1, value.id);
        final Long _tmp = Converters.dateToTimestamp(value.date);
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        stmt.bindLong(3, value.dailyScore);
        final int _tmp_1 = value.quizCompleted ? 1 : 0;
        stmt.bindLong(4, _tmp_1);
        final int _tmp_2 = value.challengeCompleted ? 1 : 0;
        stmt.bindLong(5, _tmp_2);
        stmt.bindLong(6, value.streak);
      }
    };
    this.__updateAdapterOfUserProgress = new EntityDeletionOrUpdateAdapter<UserProgress>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `user_progress` SET `id` = ?,`date` = ?,`dailyScore` = ?,`quizCompleted` = ?,`challengeCompleted` = ?,`streak` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserProgress value) {
        stmt.bindLong(1, value.id);
        final Long _tmp = Converters.dateToTimestamp(value.date);
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        stmt.bindLong(3, value.dailyScore);
        final int _tmp_1 = value.quizCompleted ? 1 : 0;
        stmt.bindLong(4, _tmp_1);
        final int _tmp_2 = value.challengeCompleted ? 1 : 0;
        stmt.bindLong(5, _tmp_2);
        stmt.bindLong(6, value.streak);
        stmt.bindLong(7, value.id);
      }
    };
  }

  @Override
  public void insert(final UserProgress userProgress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserProgress.insert(userProgress);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final UserProgress userProgress) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUserProgress.handle(userProgress);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<UserProgress> getAllProgress() {
    final String _sql = "SELECT * FROM user_progress ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDailyScore = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyScore");
      final int _cursorIndexOfQuizCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "quizCompleted");
      final int _cursorIndexOfChallengeCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeCompleted");
      final int _cursorIndexOfStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "streak");
      final List<UserProgress> _result = new ArrayList<UserProgress>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final UserProgress _item;
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp);
        final int _tmpDailyScore;
        _tmpDailyScore = _cursor.getInt(_cursorIndexOfDailyScore);
        final boolean _tmpQuizCompleted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfQuizCompleted);
        _tmpQuizCompleted = _tmp_1 != 0;
        final boolean _tmpChallengeCompleted;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfChallengeCompleted);
        _tmpChallengeCompleted = _tmp_2 != 0;
        final int _tmpStreak;
        _tmpStreak = _cursor.getInt(_cursorIndexOfStreak);
        _item = new UserProgress(_tmpDate,_tmpDailyScore,_tmpQuizCompleted,_tmpChallengeCompleted,_tmpStreak);
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
  public List<UserProgress> getProgressFromDate(final long startDate) {
    final String _sql = "SELECT * FROM user_progress WHERE date >= ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDailyScore = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyScore");
      final int _cursorIndexOfQuizCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "quizCompleted");
      final int _cursorIndexOfChallengeCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeCompleted");
      final int _cursorIndexOfStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "streak");
      final List<UserProgress> _result = new ArrayList<UserProgress>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final UserProgress _item;
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp);
        final int _tmpDailyScore;
        _tmpDailyScore = _cursor.getInt(_cursorIndexOfDailyScore);
        final boolean _tmpQuizCompleted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfQuizCompleted);
        _tmpQuizCompleted = _tmp_1 != 0;
        final boolean _tmpChallengeCompleted;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfChallengeCompleted);
        _tmpChallengeCompleted = _tmp_2 != 0;
        final int _tmpStreak;
        _tmpStreak = _cursor.getInt(_cursorIndexOfStreak);
        _item = new UserProgress(_tmpDate,_tmpDailyScore,_tmpQuizCompleted,_tmpChallengeCompleted,_tmpStreak);
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
  public UserProgress getProgressForDate(final long date) {
    final String _sql = "SELECT * FROM user_progress WHERE date = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, date);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDailyScore = CursorUtil.getColumnIndexOrThrow(_cursor, "dailyScore");
      final int _cursorIndexOfQuizCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "quizCompleted");
      final int _cursorIndexOfChallengeCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "challengeCompleted");
      final int _cursorIndexOfStreak = CursorUtil.getColumnIndexOrThrow(_cursor, "streak");
      final UserProgress _result;
      if(_cursor.moveToFirst()) {
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp);
        final int _tmpDailyScore;
        _tmpDailyScore = _cursor.getInt(_cursorIndexOfDailyScore);
        final boolean _tmpQuizCompleted;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfQuizCompleted);
        _tmpQuizCompleted = _tmp_1 != 0;
        final boolean _tmpChallengeCompleted;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfChallengeCompleted);
        _tmpChallengeCompleted = _tmp_2 != 0;
        final int _tmpStreak;
        _tmpStreak = _cursor.getInt(_cursorIndexOfStreak);
        _result = new UserProgress(_tmpDate,_tmpDailyScore,_tmpQuizCompleted,_tmpChallengeCompleted,_tmpStreak);
        _result.id = _cursor.getInt(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int getMaxStreak() {
    final String _sql = "SELECT MAX(streak) FROM user_progress";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
