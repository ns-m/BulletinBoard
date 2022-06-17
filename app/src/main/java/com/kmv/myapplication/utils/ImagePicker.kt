package com.kmv.myapplication.utils

import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.act.EditAdsAct
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ImagePicker {
    const val MAX_IMAGE_COUNT = 5
    /*const val REQUEST_CODE_GET_IMAGE = 500
    const val REQUEST_CODE_EDIT_IMAGE = 505*/
    private fun getOptions(imageCounter: Int): Options{
        val options = Options().apply{
            //ratio = Ratio.RATIO_AUTO                                    //Image/video capture ratio
            count = imageCounter                                        //Number of images to restrict selection count
            //spanCount = 4                                               //Number for columns in grid
            path = "Pix/Camera"                                         //Custom Path For media Storage
            isFrontFacing = false                                       //Front Facing camera on start
            //videoDurationLimitInSeconds = 10                            //Duration for video recording
            mode = Mode.Picture                                          //Option to select only pictures or videos or both
            //flash = Flash.Auto                                          //Option to select flash type
            //preSelectedUrls = ArrayList<Uri>()                          //Pre selected Image Urls
        }
        return options
    }

    fun launcher(edAct:EditAdsAct, launcher: ActivityResultLauncher<Intent>?, imageCounter: Int){
        edAct.addPixToActivity(R.id.placeHolder, getOptions(imageCounter)){
            when(it.status){
                PixEventCallback.Status.SUCCESS -> {

                }
                PixEventCallback.Status.BACK_PRESSED -> {

                }
            }
        }
    }

    fun launcherOld(editAA: EditAdsAct, launcher: ActivityResultLauncher<Intent>?,
                    imageCounter: Int){
        PermUtil.checkForCamaraWritePermissions(editAA){
            val intent = Intent(editAA, Pix::class.java).apply {
                putExtra("options", getOptions(imageCounter))
                }
            launcher?.launch(intent)
        }
    }

    fun getLauncherForMultiSelectImages(editAA: EditAdsAct): ActivityResultLauncher<Intent>{
        return editAA.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val returnValues = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    if (returnValues?.size!! > 1 && editAA.chooseImageFragment == null) {
                        editAA.openChooseImageFragment(returnValues)
                    } else if (returnValues.size == 1 && editAA.chooseImageFragment == null) {
                        //imageAdapter.update(returnValues)
                        //val tempList = ImageManager.getImageSize(returnValues[0])
                        CoroutineScope(Dispatchers.Main).launch{
                            editAA.binding.progressBarEditAds.visibility = View.VISIBLE
                            val bitmapArray = ImageManager.imageResize(returnValues) as ArrayList<Bitmap>
                            editAA.binding.progressBarEditAds.visibility = View.GONE
                            editAA.imageAdapter.update(bitmapArray)
                        }
                    }else if (editAA.chooseImageFragment != null) {
                        editAA.chooseImageFragment?.updateAdapter(returnValues)
                    }
                }
            }
        }
    }

    fun getLauncherForSingleSelectImage(editAA: EditAdsAct): ActivityResultLauncher<Intent> {
        return editAA.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val uriValue = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    editAA.chooseImageFragment?.setSingleImage(
                        uriValue?.get(0)!!,
                        editAA.editImagePositions)
                }
            }
        }
    }

}