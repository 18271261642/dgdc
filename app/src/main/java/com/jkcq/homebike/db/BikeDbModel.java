package com.jkcq.homebike.db;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jkcq.homebike.ble.bike.reponsebean.DailybriefBean;
import com.jkcq.homebike.gen.DailyBriefDao;
import com.jkcq.homebike.gen.DaoMaster;
import com.jkcq.homebike.gen.DaoSession;
import com.jkcq.homebike.gen.SummaryDao;
import com.jkcq.util.DateUtil;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import rx.android.schedulers.AndroidSchedulers;


public class BikeDbModel {
    private static DaoSession daoSession;
    private RxDao<UpgradeBikeBean, Long> bikeDao;
    private RxQuery<UpgradeBikeBean> bikeQuery;


    private static RxDao<DailyBrief, Long> dailyBriefLongRxDao;
    private RxQuery<DailyBrief> dailyBriefRxQuery;


    private static RxDao<DailySummaries, Long> dailySummariesLongRxDao;
    private static RxQuery<DailySummaries> dailySummariesRxQuery;

    private static RxDao<Summary, Long> summaryLongRxDao;
    private static RxQuery<Summary> summaryRxQuery;


    public static void init(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "notes-db");
        Database db = helper.getWritableDb();

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    static BikeDbModel managerInstance;

    public static synchronized BikeDbModel getInstance() {
        if (managerInstance == null) {

            managerInstance = new BikeDbModel();
        }
        return managerInstance;
    }

    private BikeDbModel() {
        bikeDao = daoSession.getUpgradeBikeBeanDao().rx();
        // bikeQuery = daoSession.getUpgradeBikeBeanDao().queryBuilder().rx();

        dailyBriefLongRxDao = daoSession.getDailyBriefDao().rx();
        // dailyBriefRxQuery = daoSession.getDailyBriefDao().queryBuilder().rx();


        dailySummariesLongRxDao = daoSession.getDailySummariesDao().rx();
        // dailySummariesRxQuery = daoSession.getDailySummariesDao().queryBuilder().rx();


        summaryLongRxDao = daoSession.getSummaryDao().rx();
        // summaryRxQuery = daoSession.getSummaryDao().queryBuilder().rx();

        // sceneBeanRxQuery = daoSession.getSceneBeanDao().queryBuilder().rx();
    }


    public void addDailyBrief(DailybriefBean bean, String deviceType, String mUserId) {
        if (TextUtils.isEmpty(deviceType)) {
            return;
        }
        DailyBrief dailyBrief = new DailyBrief();
        dailyBrief.setCalorie(bean.getCalorie());
        dailyBrief.setDistance(bean.getDistance());
        dailyBrief.setDuration(bean.getDuration());
        dailyBrief.setExerciseTime(bean.getExerciseTime());
        dailyBrief.setExerciseType(bean.getExerciseType());
        dailyBrief.setReportId(bean.getId());
        dailyBrief.setPowerGeneration(bean.getPowerGeneration());
        dailyBrief.setScenario(JsonUtils.getInstance().toJSON(bean.getScenario()));
        dailyBrief.setPkInfo(JsonUtils.getInstance().toJSON(bean.getPkInfo()));
        dailyBrief.setStrDate(DateUtil.getYyyyMm(Long.parseLong(bean.getExerciseTime())));
        dailyBrief.setDeviceType(deviceType);
        dailyBrief.setUserId(mUserId);
        dailyBriefRxQuery = daoSession.getDailyBriefDao().queryBuilder().where(DailyBriefDao.Properties.ExerciseTime.eq(bean.getExerciseTime())).rx();
        dailyBriefRxQuery.list().observeOn(AndroidSchedulers.mainThread()).subscribe(note1 -> {
                    if (note1.size() > 0) {
                        dailyBrief.setId(note1.get(0).getId());
                    }
                    // Log.e("dailyBriefRxQuery", "Inserted new note, ID: " + note1.size());

                    dailyBriefLongRxDao.insertOrReplace(dailyBrief).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(saveben -> {
                                Log.e("addDailyBrief", "Inserted new note, ID: " + saveben.getId());
                                //Log.d("DaoExample", "Inserted new note, ID: " + note1.getId());
                                //updateNotes();
                            });
                }

        );


    }

    public void addSummary(Summary summary, String day,
                           String deviceType,
                           String summaryType,
                           String mUserId) {
        summary.setDeviceType(deviceType);
        summary.setSummaryType(summaryType);
        summary.setDay(day);
        summary.setUserId(mUserId);
        if (TextUtils.isEmpty(deviceType)) {
            return;
        }
        summaryRxQuery = daoSession.getSummaryDao().queryBuilder().where(SummaryDao.Properties.Day.eq(day), SummaryDao.Properties.DeviceType.eq(deviceType)
                , SummaryDao.Properties.SummaryType.eq(summaryType), SummaryDao.Properties.UserId.eq(mUserId)).rx();
        summaryRxQuery.list().observeOn(AndroidSchedulers.mainThread()).subscribe(not1 -> {
            Log.e("addSummary ", "summaryRxQuery " + not1 + "note1");

            if (not1.size() > 0) {
                summary.setId(not1.get(0).getId());
            }
            summaryLongRxDao.insertOrReplace(summary).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(note1 -> {
                        Log.e("addSummary", "Inserted new note, ID: " + note1.getId() + "note1" + note1);
                        //Log.d("DaoExample", "Inserted new note, ID: " + note1.getId());
                        //updateNotes();
                    });
        });


    }

    public void addDailySummarise(DailySummaries dailySummaries, String day, String deviceType, String summaryType, String mUserId) {
        if (TextUtils.isEmpty(deviceType) || TextUtils.isEmpty(mUserId)) {
            return;
        }
        dailySummaries.setDeviceType(deviceType);
        dailySummaries.setUserId(mUserId);
        dailySummaries.setSummaryType(summaryType);
        dailySummaries.setDay(day);


        dailyBriefRxQuery = daoSession.getDailyBriefDao().queryBuilder().where(SummaryDao.Properties.Day.eq(day), SummaryDao.Properties.DeviceType.eq(deviceType)
                , SummaryDao.Properties.SummaryType.eq(summaryType), SummaryDao.Properties.UserId.eq(mUserId)).rx();

        summaryRxQuery.list().observeOn(AndroidSchedulers.mainThread()).subscribe(not1 -> {
            Log.e("addDailySummarise ", "summaryRxQuery " + not1 + "note1");

            if (not1.size() > 0) {
                dailySummaries.setId(not1.get(0).getId());
            }
            dailySummariesLongRxDao.insertOrReplace(dailySummaries).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(note1 -> {
                        Log.e("addDailySummarise", "Inserted new note, ID: " + note1.getId());
                        //Log.d("DaoExample", "Inserted new note, ID: " + note1.getId());
                        //updateNotes();
                    });
        });


    }


    public void addNote(UpgradeBikeBean bikeBean) {
        bikeDao.insertOrReplace(bikeBean)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(note1 -> {
                    Log.e("addNote", "Inserted new note, ID: " + note1.getId());
                    //Log.d("DaoExample", "Inserted new note, ID: " + note1.getId());
                    //updateNotes();
                });
    }

}
