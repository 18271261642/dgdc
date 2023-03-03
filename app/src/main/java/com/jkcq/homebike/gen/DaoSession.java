package com.jkcq.homebike.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.jkcq.homebike.db.DailyBrief;
import com.jkcq.homebike.db.DailySummaries;
import com.jkcq.homebike.db.Note;
import com.jkcq.homebike.db.Summary;
import com.jkcq.homebike.db.UpgradeBikeBean;

import com.jkcq.homebike.gen.DailyBriefDao;
import com.jkcq.homebike.gen.DailySummariesDao;
import com.jkcq.homebike.gen.NoteDao;
import com.jkcq.homebike.gen.SummaryDao;
import com.jkcq.homebike.gen.UpgradeBikeBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dailyBriefDaoConfig;
    private final DaoConfig dailySummariesDaoConfig;
    private final DaoConfig noteDaoConfig;
    private final DaoConfig summaryDaoConfig;
    private final DaoConfig upgradeBikeBeanDaoConfig;

    private final DailyBriefDao dailyBriefDao;
    private final DailySummariesDao dailySummariesDao;
    private final NoteDao noteDao;
    private final SummaryDao summaryDao;
    private final UpgradeBikeBeanDao upgradeBikeBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dailyBriefDaoConfig = daoConfigMap.get(DailyBriefDao.class).clone();
        dailyBriefDaoConfig.initIdentityScope(type);

        dailySummariesDaoConfig = daoConfigMap.get(DailySummariesDao.class).clone();
        dailySummariesDaoConfig.initIdentityScope(type);

        noteDaoConfig = daoConfigMap.get(NoteDao.class).clone();
        noteDaoConfig.initIdentityScope(type);

        summaryDaoConfig = daoConfigMap.get(SummaryDao.class).clone();
        summaryDaoConfig.initIdentityScope(type);

        upgradeBikeBeanDaoConfig = daoConfigMap.get(UpgradeBikeBeanDao.class).clone();
        upgradeBikeBeanDaoConfig.initIdentityScope(type);

        dailyBriefDao = new DailyBriefDao(dailyBriefDaoConfig, this);
        dailySummariesDao = new DailySummariesDao(dailySummariesDaoConfig, this);
        noteDao = new NoteDao(noteDaoConfig, this);
        summaryDao = new SummaryDao(summaryDaoConfig, this);
        upgradeBikeBeanDao = new UpgradeBikeBeanDao(upgradeBikeBeanDaoConfig, this);

        registerDao(DailyBrief.class, dailyBriefDao);
        registerDao(DailySummaries.class, dailySummariesDao);
        registerDao(Note.class, noteDao);
        registerDao(Summary.class, summaryDao);
        registerDao(UpgradeBikeBean.class, upgradeBikeBeanDao);
    }
    
    public void clear() {
        dailyBriefDaoConfig.clearIdentityScope();
        dailySummariesDaoConfig.clearIdentityScope();
        noteDaoConfig.clearIdentityScope();
        summaryDaoConfig.clearIdentityScope();
        upgradeBikeBeanDaoConfig.clearIdentityScope();
    }

    public DailyBriefDao getDailyBriefDao() {
        return dailyBriefDao;
    }

    public DailySummariesDao getDailySummariesDao() {
        return dailySummariesDao;
    }

    public NoteDao getNoteDao() {
        return noteDao;
    }

    public SummaryDao getSummaryDao() {
        return summaryDao;
    }

    public UpgradeBikeBeanDao getUpgradeBikeBeanDao() {
        return upgradeBikeBeanDao;
    }

}