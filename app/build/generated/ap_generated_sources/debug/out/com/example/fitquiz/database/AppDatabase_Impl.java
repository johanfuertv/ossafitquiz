package com.example.fitquiz.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.example.fitquiz.database.dao.ChallengeResultDao;
import com.example.fitquiz.database.dao.ChallengeResultDao_Impl;
import com.example.fitquiz.database.dao.QuizResultDao;
import com.example.fitquiz.database.dao.QuizResultDao_Impl;
import com.example.fitquiz.database.dao.UserProgressDao;
import com.example.fitquiz.database.dao.UserProgressDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile QuizResultDao _quizResultDao;

  private volatile ChallengeResultDao _challengeResultDao;

  private volatile UserProgressDao _userProgressDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `quiz_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `score` INTEGER NOT NULL, `totalQuestions` INTEGER NOT NULL, `date` INTEGER, `timeSpent` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `challenge_results` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `challengeType` TEXT, `targetReps` INTEGER NOT NULL, `completedReps` INTEGER NOT NULL, `completed` INTEGER NOT NULL, `date` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user_progress` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `date` INTEGER, `dailyScore` INTEGER NOT NULL, `quizCompleted` INTEGER NOT NULL, `challengeCompleted` INTEGER NOT NULL, `streak` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1d3e8bf0afafd7bef1256d5a7d47567a')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `quiz_results`");
        _db.execSQL("DROP TABLE IF EXISTS `challenge_results`");
        _db.execSQL("DROP TABLE IF EXISTS `user_progress`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsQuizResults = new HashMap<String, TableInfo.Column>(5);
        _columnsQuizResults.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("totalQuestions", new TableInfo.Column("totalQuestions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("date", new TableInfo.Column("date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuizResults.put("timeSpent", new TableInfo.Column("timeSpent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysQuizResults = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesQuizResults = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoQuizResults = new TableInfo("quiz_results", _columnsQuizResults, _foreignKeysQuizResults, _indicesQuizResults);
        final TableInfo _existingQuizResults = TableInfo.read(_db, "quiz_results");
        if (! _infoQuizResults.equals(_existingQuizResults)) {
          return new RoomOpenHelper.ValidationResult(false, "quiz_results(com.example.fitquiz.database.entities.QuizResult).\n"
                  + " Expected:\n" + _infoQuizResults + "\n"
                  + " Found:\n" + _existingQuizResults);
        }
        final HashMap<String, TableInfo.Column> _columnsChallengeResults = new HashMap<String, TableInfo.Column>(6);
        _columnsChallengeResults.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeResults.put("challengeType", new TableInfo.Column("challengeType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeResults.put("targetReps", new TableInfo.Column("targetReps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeResults.put("completedReps", new TableInfo.Column("completedReps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeResults.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChallengeResults.put("date", new TableInfo.Column("date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChallengeResults = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChallengeResults = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChallengeResults = new TableInfo("challenge_results", _columnsChallengeResults, _foreignKeysChallengeResults, _indicesChallengeResults);
        final TableInfo _existingChallengeResults = TableInfo.read(_db, "challenge_results");
        if (! _infoChallengeResults.equals(_existingChallengeResults)) {
          return new RoomOpenHelper.ValidationResult(false, "challenge_results(com.example.fitquiz.database.entities.ChallengeResult).\n"
                  + " Expected:\n" + _infoChallengeResults + "\n"
                  + " Found:\n" + _existingChallengeResults);
        }
        final HashMap<String, TableInfo.Column> _columnsUserProgress = new HashMap<String, TableInfo.Column>(6);
        _columnsUserProgress.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("date", new TableInfo.Column("date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("dailyScore", new TableInfo.Column("dailyScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("quizCompleted", new TableInfo.Column("quizCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("challengeCompleted", new TableInfo.Column("challengeCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserProgress.put("streak", new TableInfo.Column("streak", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserProgress = new TableInfo("user_progress", _columnsUserProgress, _foreignKeysUserProgress, _indicesUserProgress);
        final TableInfo _existingUserProgress = TableInfo.read(_db, "user_progress");
        if (! _infoUserProgress.equals(_existingUserProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "user_progress(com.example.fitquiz.database.entities.UserProgress).\n"
                  + " Expected:\n" + _infoUserProgress + "\n"
                  + " Found:\n" + _existingUserProgress);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1d3e8bf0afafd7bef1256d5a7d47567a", "a20024f54f7c4d22b3608bc18f74e92a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "quiz_results","challenge_results","user_progress");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `quiz_results`");
      _db.execSQL("DELETE FROM `challenge_results`");
      _db.execSQL("DELETE FROM `user_progress`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(QuizResultDao.class, QuizResultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ChallengeResultDao.class, ChallengeResultDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserProgressDao.class, UserProgressDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public QuizResultDao quizResultDao() {
    if (_quizResultDao != null) {
      return _quizResultDao;
    } else {
      synchronized(this) {
        if(_quizResultDao == null) {
          _quizResultDao = new QuizResultDao_Impl(this);
        }
        return _quizResultDao;
      }
    }
  }

  @Override
  public ChallengeResultDao challengeResultDao() {
    if (_challengeResultDao != null) {
      return _challengeResultDao;
    } else {
      synchronized(this) {
        if(_challengeResultDao == null) {
          _challengeResultDao = new ChallengeResultDao_Impl(this);
        }
        return _challengeResultDao;
      }
    }
  }

  @Override
  public UserProgressDao userProgressDao() {
    if (_userProgressDao != null) {
      return _userProgressDao;
    } else {
      synchronized(this) {
        if(_userProgressDao == null) {
          _userProgressDao = new UserProgressDao_Impl(this);
        }
        return _userProgressDao;
      }
    }
  }
}
