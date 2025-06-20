package com.example.fitquiz.database.dao;

import android.database.Cursor;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.fitquiz.database.Converters;
import com.example.fitquiz.database.entities.QuizResult;
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
public final class QuizResultDao_Impl implements QuizResultDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<QuizResult> __insertionAdapterOfQuizResult;

  public QuizResultDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfQuizResult = new EntityInsertionAdapter<QuizResult>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `quiz_results` (`id`,`score`,`totalQuestions`,`date`,`timeSpent`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, QuizResult value) {
        stmt.bindLong(1, value.id);
        stmt.bindLong(2, value.score);
        stmt.bindLong(3, value.totalQuestions);
        final Long _tmp = Converters.dateToTimestamp(value.date);
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        stmt.bindLong(5, value.timeSpent);
      }
    };
  }

  @Override
  public void insert(final QuizResult quizResult) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfQuizResult.insert(quizResult);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<QuizResult> getAllResults() {
    final String _sql = "SELECT * FROM quiz_results ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
      final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSpent");
      final List<QuizResult> _result = new ArrayList<QuizResult>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final QuizResult _item;
        final int _tmpScore;
        _tmpScore = _cursor.getInt(_cursorIndexOfScore);
        final int _tmpTotalQuestions;
        _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp);
        final long _tmpTimeSpent;
        _tmpTimeSpent = _cursor.getLong(_cursorIndexOfTimeSpent);
        _item = new QuizResult(_tmpScore,_tmpTotalQuestions,_tmpDate,_tmpTimeSpent);
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
  public List<QuizResult> getResultsFromDate(final long startDate) {
    final String _sql = "SELECT * FROM quiz_results WHERE date >= ? ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
      final int _cursorIndexOfTotalQuestions = CursorUtil.getColumnIndexOrThrow(_cursor, "totalQuestions");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfTimeSpent = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSpent");
      final List<QuizResult> _result = new ArrayList<QuizResult>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final QuizResult _item;
        final int _tmpScore;
        _tmpScore = _cursor.getInt(_cursorIndexOfScore);
        final int _tmpTotalQuestions;
        _tmpTotalQuestions = _cursor.getInt(_cursorIndexOfTotalQuestions);
        final Date _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converters.fromTimestamp(_tmp);
        final long _tmpTimeSpent;
        _tmpTimeSpent = _cursor.getLong(_cursorIndexOfTimeSpent);
        _item = new QuizResult(_tmpScore,_tmpTotalQuestions,_tmpDate,_tmpTimeSpent);
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
  public double getAverageScore(final long startDate) {
    final String _sql = "SELECT AVG(score) FROM quiz_results WHERE date >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startDate);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final double _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getDouble(0);
      } else {
        _result = 0.0;
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
