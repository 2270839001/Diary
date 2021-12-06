package com.example.diary;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 数据共享
 */
public class TheContentprovider extends ContentProvider {
    //匹配不成功返回NO_MATCH(-1)
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int WORDS = 1;
    //这里的AUTHORITY就是我们在AndroidManifest.xml中配置的authorities
    private static final String AUTHORITY = "com.example.diarycontentprovider.provider";
    private static final int WORD = 2;
    private static final String _ID = "_id";

    private DBHelper helperDb;

    static {
        //添加我们需要匹配的uri
        matcher.addURI(AUTHORITY, "wordinformation", WORDS);
        matcher.addURI(AUTHORITY, "wordinformation/#", WORD);
    }

    @Override
    public boolean onCreate() {

        helperDb = new DBHelper(getContext(), "mydb", 1);
        return true;
    }

    /**
     * 查询
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = helperDb.getReadableDatabase();
        switch (matcher.match(uri)) {
            case WORDS:
                return db.query("wordinformation", null, null, null, null, null, null);
            case WORD:
                return db.query("wordinformation", null, "_id = ?", selectionArgs, null, null,
                        null);
            default:
                throw new IllegalArgumentException("Unrecognized Uri !");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * 插入数据
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = helperDb.getReadableDatabase();
        switch (matcher.match(uri)) {
            case WORDS:
                long rowId = db.insert("wordinformation", null, values);
                if (rowId > 0) {
                    Uri wordUri = ContentUris.withAppendedId(uri, rowId);
                    //通知数据改变
                    getContext().getContentResolver().notifyChange(wordUri, null);
                    return wordUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unrecognized Uri !");
        }
        return null;
    }

    /**
     * 删除数据
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        SQLiteDatabase db = helperDb.getReadableDatabase();
        //记录所删除的记录数目
        int num = 0;
        switch (matcher.match(uri)) {
            case WORDS:
                num = db.delete("wordinformation", null, null);
                break;
            case WORD:
                //解析处所要删除的记录Id
                long id = ContentUris.parseId(uri);
                String whereClause = _ID + "=" + id;
                num = db.delete("wordinformation", whereClause, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unrecognized Uri !");
        }
        //通知数据改变
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

    /**
     * 更新数据
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase db = helperDb.getReadableDatabase();
        //记录需要修改的记录数目
        int num = 0;
        switch (matcher.match(uri)) {
            case WORDS:
                //db.update()
                num = db.update("wordinformation", values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized Uri !");
        }
        //通知数据改变
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }
}