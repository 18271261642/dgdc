package com.jkcq.platform.umeng

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.facebook.*
import com.facebook.GraphRequest.newMeRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jkcq.base.app.BaseApp
import com.jkcq.platform.R
import com.jkcq.util.PackageUtil
import com.jkcq.util.ktx.ToastUtil
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

/**
 *  Created by BeyondWorlds
 *  on 2020/7/31
 */
object PlatformLoginManager {

    lateinit private var mCallbackManager: CallbackManager
    private const val RC_SIGN_IN = 9001
    lateinit var mGoogleSignInClient: GoogleSignInClient

    const val LOGIN_BY_WECHAT = 1
    const val LOGIN_BY_QQ = 2
    const val LOGIN_BY_FACEBOOK = 3
    const val LOGIN_BY_TWITTER = 4
    const val LOGIN_BY_GOOGLE = 5
    var mLoginListener: LoginListener? = null

    /**
     * 微信登录
     */
    fun loginByWeChat(activity: Activity, loginListener: LoginListener?) {
        if (!PackageUtil.isWxInstall(PackageUtil.weichatPakage)) {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton,
                BaseApp.sApplicaton.getString(R.string.please_install_wechat)
            )
            return
        }
        mLoginListener = loginListener
        val mShareAPI: UMShareAPI = UMShareAPI.get(BaseApp.sApplicaton)
        mShareAPI.getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, object : UMAuthListener {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            override fun onStart(platform: SHARE_MEDIA) {}

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param
             */
            override fun onComplete(
                platform: SHARE_MEDIA,
                action: Int,
                map: Map<String, String>
            ) {
//                openid:openid
//
//                unionid:（6.2以前用unionid）用户id
//
//                accesstoken: accessToken （6.2以前用access_token）
//
//                refreshtoken: refreshtoken: （6.2以前用refresh_token）
//
//                过期时间：expiration （6.2以前用expires_in）
//
//                name：name（6.2以前用screen_name）
//
//                城市：city
//
//                省份：prvinice
//
//                国家：country
//
//                性别：gender
//
//                头像：iconurl（6.2以前用profile_image_url）
                val uid = map["uid"]
                val openid = map["openid"] //微博没有
                val unionid = map["unionid"] //微博没有
                val access_token = map["access_token"]
                val refresh_token = map["refresh_token"] //微信,qq,微博都没有获取到
                val expires_in = map["expires_in"]
                val name = map["name"]
                val gender = map["gender"]
                val iconurl = map["iconurl"]
                mLoginListener?.onSuccess(map)
            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            override fun onError(
                platform: SHARE_MEDIA,
                action: Int,
                t: Throwable
            ) {
                mLoginListener?.onFailed(BaseApp.sApplicaton.getString(R.string.login_failed))
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            override fun onCancel(platform: SHARE_MEDIA, action: Int) {
                mLoginListener?.onFailed(BaseApp.sApplicaton.getString(R.string.login_cancel))
            }
        })
    }

    /**
     * qq登录
     */
    fun loginByQQ(activity: Activity, loginListener: LoginListener?) {
        if (!PackageUtil.isWxInstall(PackageUtil.qqPakage)) {
            ToastUtil.showTextToast(
                BaseApp.sApplicaton, R.string.please_install_qq
            )
            return
        }
        mLoginListener = loginListener

        val mShareAPI = UMShareAPI.get(activity)
        mShareAPI.getPlatformInfo(activity, SHARE_MEDIA.QQ, object : UMAuthListener {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            override fun onStart(platform: SHARE_MEDIA) {}

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param
             */
            override fun onComplete(
                platform: SHARE_MEDIA,
                action: Int,
                map: Map<String, String>
            ) {
//                Toast.makeText(mContext, "成功了", Toast.LENGTH_LONG).show();
//                openid:openid
//
//                unionid:（6.2以前用unionid）用户id
//
//                accesstoken: accessToken （6.2以前用access_token）
//
//                refreshtoken: refreshtoken: （6.2以前用refresh_token）
//
//                过期时间：expiration （6.2以前用expires_in）
//
//                name：name（6.2以前用screen_name）
//
//                城市：city
//
//                省份：prvinice
//
//                国家：country
//
//                性别：gender
//
//                头像：iconurl（6.2以前用profile_image_url）
                val uid = map["uid"]
                val openid = map["openid"] //微博没有
                val unionid = map["unionid"] //微博没有
                val access_token = map["access_token"]
                val refresh_token = map["refresh_token"] //微信,qq,微博都没有获取到
                val expires_in = map["expires_in"]
                val name = map["name"]
                val gender = map["gender"]
                val iconurl = map["iconurl"]
                mLoginListener?.onSuccess(map)
            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {
                mLoginListener?.onFailed(BaseApp.sApplicaton.getString(R.string.login_failed))

            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            override fun onCancel(platform: SHARE_MEDIA, action: Int) {
                mLoginListener?.onFailed(BaseApp.sApplicaton.getString(R.string.login_cancel))
            }
        })
    }


    /**
     * 设置google配置
     */
    fun setLoginOption(activity: Activity) {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    val currentProfile = Profile.getCurrentProfile()
                    val accessToken: AccessToken = AccessToken.getCurrentAccessToken()
//                   currentProfile?.let {
//
//                   }
                    if ((currentProfile != null) and (accessToken != null)) {
                        val uri: Uri = currentProfile.getProfilePictureUri(180, 180)
                        var url: String? = ""
                        if (uri != null) {
                            //Logger.myLog("currentProfile == " + uri.toString() + "uri:------" + uri.getEncodedPath());
                            // Logger.myLog("currentProfile == " + currentProfile.getLinkUri().getEncodedPath());
                            url = uri.path
                        }
                        val map = HashMap<String, String>()

                        map["openid"] = accessToken.getUserId()
                        map["name"] = currentProfile.getName()
                        map["iconurl"] = url!!
                        map["platformType"] = "" + LOGIN_BY_FACEBOOK
                    } else {
                        handleFacebookResult(loginResult)
                    }
                }

                override fun onCancel() {}
                override fun onError(error: FacebookException?) {}
            })

    }

    /**
     * facebook登录
     */
    fun loginByFaceBook(activity: Activity, loginListener: LoginListener?) {
        mLoginListener = loginListener
        LoginManager.getInstance()
            .logInWithReadPermissions(activity, Arrays.asList("public_profile"))

    }

    private fun handleFacebookResult(loginResult: LoginResult?) {
        GraphRequest.newMeRequest(
            loginResult?.accessToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(`object`: JSONObject?, response: GraphResponse) {
                    // Application code
                    try {
                        Log.i("Response", response.toString())
                        val name: String = response.getJSONObject().getString("name")
                        val id: String = response.getJSONObject().getString("id")
                        val map = HashMap<String, String>()
                        map["openid"] = id
                        map["name"] = name
                        map["iconurl"] = ""
                        map["platformType"] = "" + LOGIN_BY_FACEBOOK

                        mLoginListener?.onSuccess(map)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }).executeAsync()
    }

    /**
     * google登录
     */
    fun loginByGoogle(activity: Activity, loginListener: LoginListener?) {
        mLoginListener = loginListener
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    var index = 0

    /**
     * 处理google登录结果
     */
    private fun handleGoogleResult(activity: Activity, googleData: Task<GoogleSignInAccount>) {
        try {
            val signInAccount =
                googleData.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            if (signInAccount != null && index == 0) {
                Log.e(
                    "account", """
     si:
     ${signInAccount.email}
     """.trimIndent()
                )
                val str = """
                    ${signInAccount.email}
                    ${signInAccount.id}
                    ${signInAccount.account!!.name}
                    ${signInAccount.displayName}
                    ${signInAccount.givenName}
                    ${signInAccount.photoUrl}
                    
                    """.trimIndent()
                Log.e("account", "str:$str\n")
                val map = HashMap<String, String>()

                map["openid"] = signInAccount.id!!
                map["name"] = signInAccount.account?.name ?: ""
                map["iconurl"] = ""
                map["platformType"] = "" + LOGIN_BY_GOOGLE
                if (signInAccount.photoUrl != null) {
                    map["iconurl"] = signInAccount.photoUrl?.path ?: ""
                }

                //textView.setText(str);
            } else {
                Log.e(
                    "account", """
     si为空:
     
     """.trimIndent()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(
                "account", """
     si异常:
     
     """.trimIndent()
            )
        }
    }

    /**
     * 处理onActivityResult
     */
    fun handleActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data)
        if (mCallbackManager != null) mCallbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleResult(activity, task)
        }
    }

    interface LoginListener {
        fun onSuccess(map: Map<String, String>)
        fun onFailed(msg: String)
    }
}