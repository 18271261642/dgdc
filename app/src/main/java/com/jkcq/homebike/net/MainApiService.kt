package com.jkcq.homebike.net

import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.base.net.bean.UserInfo
import com.jkcq.homebike.ali.OssBean
import com.jkcq.homebike.ali.UpdatePhotoBean
import com.jkcq.homebike.ble.bean.ResultRankingBean
import com.jkcq.homebike.ble.bike.reponsebean.DailybriefBean
import com.jkcq.homebike.ble.bike.reponsebean.DetailUrlBean
import com.jkcq.homebike.db.DailySummaries
import com.jkcq.homebike.db.SceneBean
import com.jkcq.homebike.db.Summary
import com.jkcq.homebike.ota.DeviceUpgradeBean
import com.jkcq.homebike.ride.pk.bean.*
import com.jkcq.homebike.ride.sceneriding.bean.PraiseBean
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
interface MainApiService {

    /**
     * 登录
     */
    @POST("app/spinning-consumer/basic/customer/loginByMobile")
    suspend fun loginByMobile(@Body map: Map<String, String>): BaseResponse<UserInfo>

    @POST("app/spinning-consumer/basic/customer/authorizedLanding")
    suspend fun loginByThirdPlatform(@Body map: Map<String, String>): BaseResponse<UserInfo>

    @POST("app/spinning-consumer/basic/verify/{mobile}/{type}")
    suspend fun getVerifyCodeByMobile(
        @Path("mobile") mobile: String,
        @Path("type") type: Int
    ): BaseResponse<String>

    @POST("app/spinning-consumer/basic/verify/{mobile}/{type}/{language}")
    suspend fun getVerifyCodeByEmail(
        @Path("mobile") mobile: String,
        @Path("type") type: Int,
        @Path("language") language: String
    ): BaseResponse<String>


    /**
     * 编辑用户信息
     */
    @POST("app/spinning-consumer/basic/customer/editBasicInfo")
    suspend fun editUserInfo(@Body map: Map<String, String>): BaseResponse<String>


    /**
     * 单车数据上传
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-records
     */
    @POST("app/spinning-consumer/exercise-records")
    suspend fun cyclingrecords(@Body map: Map<String, String>): BaseResponse<String>


    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-record-statistics/summary?day=2020-12-03&deviceType=83003&summaryType=ALL&userId=4
     * 查询概要数据
     * 按日周月统计跳绳运动总数
     *
     */
    @GET("app/spinning-consumer/exercise-record-statistics/summary")
    suspend fun summary(
        @Query("day") day: String,
        @Query("deviceType") deviceType: String,
        @Query("summaryType") summaryType: String,
        @Query("userId") userId: String,
        @Query("deviceCategoryCode") deviceCategoryCode: String
    ): BaseResponse<Summary>

    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-record-statistics/exercise-days-in-month?deviceType=83003&month=2020-12&userId=4
     * 查询指定月份用户运动的日期
     */

    @GET("app/spinning-consumer/exercise-record-statistics/exercise-days-in-month")
    suspend fun exercise_days_in_month(
        @Query("deviceType") deviceType: String,
        @Query("month") month: String,
        @Query("userId") userId: String,
        @Query("deviceCategoryCode") deviceCategoryCode: String
    ): BaseResponse<List<String>>

    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-record-statistics/daily-summaries?day=2020-12-03&deviceType=83003&summaryType=WEEK&userId=4
     * 按周、月统计每天运动总数
     */
    @GET("app/spinning-consumer/exercise-record-statistics/daily-summaries")
    suspend fun daily_summaries(
        @Query("day") day: String,
        @Query("deviceType") deviceType: String,
        @Query("summaryType") summaryType: String,
        @Query("userId") userId: String,
        @Query("deviceCategoryCode") deviceCategoryCode: String
    ): BaseResponse<List<DailySummaries>>

    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-record-statistics/daily-brief?day=2020-12-03&deviceType=83003&userId=4
     */
    @GET("app/spinning-consumer/exercise-record-statistics/daily-brief")
    suspend fun daily_brief(
        @Query("day") day: String,
        @Query("deviceType") deviceType: String,
        @Query("userId") userId: String,
        @Query("deviceCategoryCode") deviceCategoryCode: String
    ): BaseResponse<List<DailybriefBean>>

    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-records/url
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-records/url
     */
    @GET("app/spinning-consumer/exercise-records/url")
    suspend fun getUrl(): BaseResponse<DetailUrlBean>

    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/basic/customer/getBasicInfo
     */
    @POST("app/spinning-consumer/basic/customer/getBasicInfo")

    suspend fun getBasicInfo(@Body map: Map<String, String>): BaseResponse<UserInfo>


    /**
     * 获取列表场景
     * https://test.gateway.spinning.fitalent.com.cn/web/spinning-web/scenario/listByDeviceCategoryCode/83003
     */

    @GET("web/spinning-web/scenario/listByDeviceCategoryCode/{deviceCategoryCode}")
    suspend fun getListByDeviceTypeId(
        @Path("deviceCategoryCode") deviceType: String,
        @Query("applyType") applyType: String
    ): BaseResponse<List<SceneBean>>

    @GET("web/spinning-web/scenario/listByDeviceCategoryCode/{deviceCategoryCode}")
    suspend fun getAllListByDeviceTypeId(
        @Path("deviceCategoryCode") deviceType: String
    ): BaseResponse<List<SceneBean>>


    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-records/rankings?praiseType=0&relevanceId=1336890049355923458
     */
    @GET("app/spinning-consumer/exercise-records/rankings")
    suspend fun getrankings(
        @Query("relevanceId") relevanceId: String,
        @Query("praiseType") praiseType: String
    ): BaseResponse<ResultRankingBean>


    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-praises
     */
    @POST("app/spinning-consumer/exercise-praises")
    suspend fun praisesToOther(@Body map: Map<String, String>): BaseResponse<PraiseBean>

    /**
     * https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/exercise-praises/1
     */
    @DELETE("app/spinning-consumer/exercise-praises/{praisesId}")
    suspend fun unPraiseToOTher(@Path("praisesId") praisesId: String): BaseResponse<PraiseBean>


    @Streaming
    @GET
    suspend fun download(@Url url: String?): Call<ResponseBody>


    /**
     * 获取阿里云OSS key、secret
     * http://192.168.10.203:8767/oss/bonlala-oss/oss/app
     */
    @GET("oss/bonlala-oss/oss/app")
    suspend fun getAliToken(): BaseResponse<OssBean>


    @Multipart
    @POST("app/spinning-consumer/basic/customer/editImmage")
    suspend fun uploadFile(
        @Query("userId") userid: String?,
        @Part("description") description: RequestBody?,
        @Part file: MultipartBody.Part?
    ): BaseResponse<UpdatePhotoBean>


    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/basic/deviceVersion?type=83003
    //获取版本号
    @GET("app/spinning-consumer/basic/deviceVersion")
    suspend fun getDeviceVersion(@Query("type") type: String): BaseResponse<DeviceUpgradeBean>


    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/basic/customer/measurement
    @PUT("app/spinning-consumer/basic/customer/measurement")
    suspend fun measurement(@Body map: Map<String, String>): BaseResponse<String>

    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/h5-urls/feedback
    @GET("app/spinning-consumer/h5-urls/feedback")
    suspend fun getFeedBackUrl(): BaseResponse<String>

    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/h5-urls/question
    @GET("app/spinning-consumer/h5-urls/question")
    suspend fun getQuestionUrl(): BaseResponse<String>

    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/basic/customer/editBackground?url=bgUrl
    @POST("app/spinning-consumer/basic/customer/editBackground")
    suspend fun editBackround(@Query("url") url: String): BaseResponse<String>


    //https://test.gateway.spinning.fitalent.com.cn/web/spinning-web/courses/1340856547405467650
    @GET("web/spinning-web/courses/{detailId}")
    suspend fun getCourseDetial(
        @Path("detailId") detailId: String
    ): BaseResponse<SceneBean>

    //https://test.gateway.spinning.fitalent.com.cn/web/spinning-web/courses/1340856547405467650
    @GET("web/spinning-web/courses/listByDeviceCategoryCode/{detailId}")
    suspend fun getCourseList(
        @Path("detailId") detailId: String, @Query("language") language: String
    ): BaseResponse<List<SceneBean>>

    //pk
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks
    // 创建PK
    @POST("app/spinning-consumer/cycling-pks")
    suspend fun createPks(@Body map: Map<String, String>): BaseResponse<PKRoomBean>

    //获取PK列表
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/play
    @GET("app/spinning-consumer/cycling-pks/play")
    suspend fun getPkList(): BaseResponse<List<PKListBean>>

    //添加PK
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/join
    @POST("app/spinning-consumer/cycling-pks/join")
    suspend fun joinPk(@Body map: Map<String, String>): BaseResponse<PKRoomBean>

    //退出PK
    // https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/leave
    @POST("app/spinning-consumer/cycling-pks/leave")
    suspend fun leavePk(@Body map: Map<String, String>): BaseResponse<String>

    //解散PK
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/destroy
    @POST("app/spinning-consumer/cycling-pks/destroy")
    suspend fun destroyPk(@Body map: Map<String, String>): BaseResponse<String>

    //开始PK
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/start
    @POST("app/spinning-consumer/cycling-pks/start")
    suspend fun startPk(@Body map: Map<String, String>): BaseResponse<String>

    //查询PK状态
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/join-pk
    @GET("app/spinning-consumer/cycling-pks/join-pk")
    suspend fun findJoinPk(
        @Query("pkId") pkId: String,
        @Query("userId") userId: String
    ): BaseResponse<PKStateBean>

    //重新加入PK
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/rejoin
    @POST("app/spinning-consumer/cycling-pks/rejoin")
    suspend fun reJoinPk(@Body map: Map<String, String>): BaseResponse<PKRoomBean>

    //上报PK数据
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pk-records
    @POST("app/spinning-consumer/cycling-pk-records")
    suspend fun cyclingPkRecords(@Body map: Map<String, String>): BaseResponse<String>

    //查询pk中的人数
// https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pks/pk-participant-num
    @GET(
        "app/spinning-consumer/cycling-pks/pk-participant-num"
    )
    suspend fun getPkParticipantNum(): BaseResponse<ResultPKNumberBean>

    //查询PK结果
    // https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pk-records?pkId=12356
    @GET(
        "app/spinning-consumer/cycling-pk-records"
    )
    suspend fun getCyclingPkRecords(@Query("pkId") pkId: String): BaseResponse<List<PKResultBean>>

    //最好成绩查询
    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/cycling-pk-records/best?scenarioId=12&userId=4
    @GET(
        "app/spinning-consumer/cycling-pk-records/best"
    )
    suspend fun getBestRecodes(
        @Query("scenarioId") scenarioId: String, @Query("userId") userId: String
    ): BaseResponse<BestRecodeBean>

    //查询道具
    //https://test.gateway.spinning.fitalent.com.cn/web/spinning-web/props/all
    @GET(
        "web/spinning-web/props/all"
    )
    suspend fun getPropsAll(): BaseResponse<List<PropesBean>>


    //https://test.gateway.spinning.fitalent.com.cn/web/spinning-web/cycling-backgrounds/all
    @GET("web/spinning-web/cycling-backgrounds/all")
    suspend fun getBackgrounds(): BaseResponse<List<SceneBean>>

    //https://test.gateway.spinning.fitalent.com.cn/app/spinning-consumer/h5-urls
    @GET("app/spinning-consumer/h5-urls")
    suspend fun getH5Urls(@Query("suffix") parms: String): BaseResponse<String>
}