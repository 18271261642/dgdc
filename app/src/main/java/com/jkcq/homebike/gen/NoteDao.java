package com.jkcq.homebike.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.jkcq.homebike.db.NoteType;
import com.jkcq.homebike.db.NoteTypeConverter;

import com.jkcq.homebike.db.Note;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOTE".
*/
public class NoteDao extends AbstractDao<Note, Long> {

    public static final String TABLENAME = "NOTE";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Text = new Property(1, String.class, "text", false, "TEXT");
        public final static Property Comment = new Property(2, String.class, "comment", false, "COMMENT");
        public final static Property Date = new Property(3, java.util.Date.class, "date", false, "DATE");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
    }

    private final NoteTypeConverter typeConverter = new NoteTypeConverter();

    public NoteDao(DaoConfig config) {
        super(config);
    }
    
    public NoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TEXT\" TEXT NOT NULL ," + // 1: text
                "\"COMMENT\" TEXT," + // 2: comment
                "\"DATE\" INTEGER," + // 3: date
                "\"TYPE\" TEXT);"); // 4: type
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_NOTE_TEXT_DATE_DESC ON \"NOTE\"" +
                " (\"TEXT\" ASC,\"DATE\" DESC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getText());
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(3, comment);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date.getTime());
        }
 
        NoteType type = entity.getType();
        if (type != null) {
            stmt.bindString(5, typeConverter.convertToDatabaseValue(type));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getText());
 
        String comment = entity.getComment();
        if (comment != null) {
            stmt.bindString(3, comment);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date.getTime());
        }
 
        NoteType type = entity.getType();
        if (type != null) {
            stmt.bindString(5, typeConverter.convertToDatabaseValue(type));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Note readEntity(Cursor cursor, int offset) {
        Note entity = new Note( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // text
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // comment
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // date
            cursor.isNull(offset + 4) ? null : typeConverter.convertToEntityProperty(cursor.getString(offset + 4)) // type
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Note entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setText(cursor.getString(offset + 1));
        entity.setComment(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setType(cursor.isNull(offset + 4) ? null : typeConverter.convertToEntityProperty(cursor.getString(offset + 4)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Note entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Note entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Note entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
