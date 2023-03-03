package com.jkcq.homebike.ali

import com.jkcq.base.base.BaseRepository
import com.jkcq.base.net.bean.BaseResponse
import com.jkcq.homebike.net.MainRetrofitHelper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 *  Created by BeyondWorlds
 *  on 2020/7/29
 */
class AliyunRepository : BaseRepository() {

    suspend fun getAliToken(): BaseResponse<OssBean> {
        return MainRetrofitHelper.mService.getAliToken()
    }

    suspend fun upgradeHead(mUserId:String,file: File): BaseResponse<UpdatePhotoBean> {

        /**
         * RequestBody requestFile =
        RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        String descriptionString = "file";
        RequestBody description =
        RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
         */

        var type = "multipart/form-data".toMediaTypeOrNull()

        var requestFile: RequestBody? =
            RequestBody.create(type, file)

        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile!!)

        val descriptionString = "file"
        val description =
            RequestBody.create(type, descriptionString)
        return MainRetrofitHelper.mService.uploadFile(mUserId,description,body)
    }


}