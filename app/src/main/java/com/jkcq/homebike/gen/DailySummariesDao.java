package com.jkcq.homebike.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.jkcq.homebike.db.DailySummaries;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DAILY_SUMMARIES".
*/
public class DailySummariesDao extends AbstractDao<DailySummaries, Long> {

    public static final String TABLENAME = "DAILY_SUMMARIES";

    /**
     * Properties of entity DailySummaries.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SummaryType = new Property(1, String.class, "summaryType", false, "SUMMARY_TYPE");
        public final static Property DeviceType = new Property(2, String.class, "deviceType", false, "DEVICE_TYPE");
        public final static Property Day = new Property(3, String.class, "day", false, "DAY");
        public final static Property UserId = new Property(4, String.class, "userId", false, "USER_ID");
        public final static Property ExerciseDay = new Property(5, String.class, "exerciseDay", false, "EXERCISE_DAY");
        public final static Property TotalDistance = new Property(6, String.class, "totalDistance", false, "TOTAL_DISTANCE");
        public final static Property TotalCalorie = new Property(7, String.class, "totalCalorie", false, "TOTAL_CALORIE");
        public final static Property Times = new Property(8, String.class, "times", false, "TIMES");
    }


    public DailySummariesDao(DaoConfig config) {
        super(config);
    }
    
    public DailySummariesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DAILY_SUMMARIES\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"SUMMARY_TYPE\" TEXT," + // 1: summaryType
                "\"DEVICE_TYPE\" TEXT," + // 2: deviceType
                "\"DAY\" TEXT," + // 3: day
                "\"USER_ID\" TEXT," + // 4: userId
                "\"EXERCISE_DAY\" TEXT," + // 5: exerciseDay
                "\"TOTAL_DISTANCE\" TEXT," + // 6: totalDistance
                "\"TOTAL_CALORIE\" TEXT," + // 7: totalCalorie
                "\"TIMES\" TEXT);"); // 8: times
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DAILY_SUMMARIES\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DailySummaries entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String summaryType = entity.getSummaryType();
        if (summaryType != null) {
            stmt.bindString(2, summaryType);
        }
 
        String deviceType = entity.getDeviceType();
        if (deviceType != null) {
            stmt.bindString(3, deviceType);
        }
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(5, userId);
        }
 
        String exerciseDay = entity.getExerciseDay();
        if (exerciseDay != null) {
            stmt.bindString(6, exerciseDay);
        }
 
        String totalDistance = entity.getTotalDistance();
        if (totalDistance != null) {
            stmt.bindString(7, totalDistance);
        }
 
        String totalCalorie = entity.getTotalCalorie();
        if (totalCalorie != null) {
            stmt.bindString(8, totalCalorie);
        }
 
        String times = entity.getTimes();
        if (times != null) {
            stmt.bindString(9, times);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DailySummaries entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String summaryType = entity.getSummaryType();
        if (summaryType != null) {
            stmt.bindString(2, summaryType);
        }
 
        String deviceType = entity.getDeviceType();
        if (deviceType != null) {
            stmt.bindString(3, deviceType);
        }
 
        String day = entity.getDay();
        if (day != null) {
            stmt.bindString(4, day);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(5, userId);
        }
 
        String exerciseDay = entity.getExerciseDay();
        if (exerciseDay != null) {
            stmt.bindString(6, exerciseDay);
        }
 
        String totalDistance = entity.getTotalDistance();
        if (totalDistance != null) {
            stmt.bindString(7, totalDistance);
        }
 
        String totalCalorie = entity.getTotalCalorie();
        if (totalCalorie != null) {
            stmt.bindString(8, totalCalorie);
        }
 
        String times = entity.getTimes();
        if (times != null) {
            stmt.bindString(9, times);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DailySummaries readEntity(Cursor cursor, int offset) {
        DailySummaries entity = new DailySummaries( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // summaryType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // deviceType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // day
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // userId
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // exerciseDay
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // totalDistance
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // totalCalorie
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // times
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DailySummaries entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSummaryType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDeviceType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDay(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUserId(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setExerciseDay(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTotalDistance(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTotalCalorie(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTimes(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DailySummaries entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DailySummaries entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DailySummaries entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
