package th.ipc.provider.presenter;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import th.how.app.BuildConfig;
import th.how.db.DbHelper;
import th.how.db.NewsEntityDao;

/**
 * Created by me_touch on 17-10-9.
 * @author me_touch
 */
@SuppressWarnings("ConstantConditions")
public class NewsContentProvider extends ContentProvider {

    private final static String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    public final static String BASE_CONTENT_URI = "content://" + AUTHORITY;

    private final static String MIME_TYPE_ITEM = "vnd.android.cursor.item/";
    private final static String MIME_TYPE_DIR = "vnd.android.cursor.dir/";

    private final static int URI_TYPE_NEWS_ENTITY = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private DbHelper helper;

    static {
        URI_MATCHER.addURI(AUTHORITY, NewsEntityDao.TABLENAME, URI_TYPE_NEWS_ENTITY);
    }

    @Override
    public boolean onCreate() {
        helper = DbHelper.getInstance(getContext());
        return false;
    }

    /**
     * @param uri           用来查询的Uri,必须是一个完整的Uri,不能为空.如果Uri的结尾为具体的数字,则到指定的_id处查询.
     * @param projection    添加到Cursor中的 columns , 如果为空,则添加所有的 columns
     * @param selection     筛选条件, 如果为空, 则返回所有的rows
     * @param selectionArgs 当 @param selection 中包含 ? 时,用来匹配对应的 ? 值
     * @param sortOrder     排序方式
     * @return cursor 查询结果
     */

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String table = getTableName(uri);
        if (table == null) {
            return null;
        }
        Cursor cursor = helper.getSQLiteOpenHelper().getReadableDatabase().query(table,
                projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int code = URI_MATCHER.match(uri);
        switch (code) {
            case URI_TYPE_NEWS_ENTITY:
                return MIME_TYPE_DIR + NewsEntityDao.TABLENAME;
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String table = getTableName(uri);
        if (table == null) {
            return null;
        }
        long id = helper.getSQLiteOpenHelper().getWritableDatabase().insertOrThrow(table, null, values);
        notify(uri);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            return 0;
        }
        int id = helper.getSQLiteOpenHelper().getWritableDatabase().delete(table, selection, selectionArgs);
        notify(uri);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            return 0;
        }
        int id = helper.getSQLiteOpenHelper().getWritableDatabase().update(table, values, selection, selectionArgs);
        notify(uri);
        return id;
    }

    private String getTableName(Uri uri) {
        int code = URI_MATCHER.match(uri);
        switch (code) {
            case URI_TYPE_NEWS_ENTITY:
                return NewsEntityDao.TABLENAME;
            default:
                return null;
        }
    }

    private void notify(Uri uri) {
        ContentResolver resolver = getContext().getContentResolver();
        if (resolver != null) {
            resolver.notifyChange(uri, null);
        }
    }
}
