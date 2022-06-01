package com.kmv.myapplication.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.kmv.myapplication.act.EditAdsAct
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio

object ImagePicker {
    const val MAX_IMAGE_COUNT = 5
    const val REQUEST_CODE_GET_IMAGE = 555
    fun getImages(context: AppCompatActivity, imageCounter: Int): Options{
        val options = Options().apply{
            ratio = Ratio.RATIO_AUTO                                    //Image/video capture ratio
            count = imageCounter                                        //Number of images to restrict selection count
            spanCount = 4                                               //Number for columns in grid
            path = "Pix/Camera"                                         //Custom Path For media Storage
            isFrontFacing = false                                       //Front Facing camera on start
            //videoDurationLimitInSeconds = 10                            //Duration for video recording
            mode = Mode.Picture                                          //Option to select only pictures or videos or both
            flash = Flash.Auto                                          //Option to select flash type
            preSelectedUrls = ArrayList<Uri>()                          //Pre selected Image Urls
        }
        return options
    }
/*    fun launcher(edAct:EditAdsAct, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int){
        edAct.addPixToActivity()
    }*/
}