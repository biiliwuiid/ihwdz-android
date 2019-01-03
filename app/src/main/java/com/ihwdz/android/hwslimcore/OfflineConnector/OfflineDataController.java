package com.ihwdz.android.hwslimcore.OfflineConnector;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.ihwdz.android.hwapp.model.entity.HomePageData;
import com.ihwdz.android.hwslimcore.InjectUtil.ForApplication;
import com.ihwdz.android.hwslimcore.LogUtil.Logger;
import com.ihwdz.android.hwslimcore.SlimConnector.ISlimAuthenticator;
import com.ihwdz.android.hwslimcore.SlimConnector.SlimAPICreator;
import com.ihwdz.android.hwslimcore.Util.WifiService;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/03
 * desc :
 * version: 1.0
 * </pre>
 */
public class OfflineDataController {

    final static String TAG = "OfflineDataController";


//    @Inject OfflineSQLiteOpenHelper sqLiteOpenHelper;
    @Inject @ForApplication
    Context context;
    @Inject
    WifiService wifiService;
    @Inject
    RequestQueue queue;
    @Inject
    SlimAPICreator apiCreator;
    @Inject
    ISlimAuthenticator slimAuthenticator;

    @Inject @ForOfflineUploading
    ExecutorService uploadingExecutorService;
    @Inject @ForOfflineDatabase ExecutorService databaseExecutorService;



    public interface UpdateLocalDataFinishedHandler{
        void onUpdateLocalDataFinished();
    }
    public void updateLocalDataAsync(
//                                               final List<JobDefinition> jobDefinitions,
                                               final HomePageData jobDefinitions,
//                                               final String url,
//                                               final String cookie,
                                               final UpdateLocalDataFinishedHandler handler
    ){
        databaseExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
//                    if(url!=null){
//                        ModelOfflineSite siteItem = new Select().from(ModelOfflineSite.class)
//                                .where(ModelOfflineSite_Table.siteUrl.eq(url.toLowerCase()))
//                                .and(ModelOfflineSite_Table.cookie.eq(cookie))
//                                .querySingle();
//
//                        if(siteItem == null){
//                            siteItem = new ModelOfflineSite();
//                            siteItem.setSiteUrl(url.toLowerCase());
//                            siteItem.setCookie(cookie);
//                            siteItem.save();
//                        }
//
//                        for (JobDefinition jobDefinition : jobDefinitions) {
//                            //Select the rows with same job id and site url.
//                            ModelOfflineJobDefinition item = new Select().from(ModelOfflineJobDefinition.class)
//                                    .where(ModelOfflineJobDefinition_Table.jobId.eq(jobDefinition.id))
//                                    .and(ModelOfflineJobDefinition_Table.site_id.eq(siteItem.getID())).querySingle();
//
//                            if (item != null) {
//                                item.setJobId(jobDefinition.id);
//                                item.setJobDefinition(jobDefinition);
//                                item.setSiteUrl(siteItem);
//                                item.update();
//                            } else {
//                                ModelOfflineJobDefinition offlineJobDefinition = new ModelOfflineJobDefinition();
//                                offlineJobDefinition.setJobId(jobDefinition.id);
//                                offlineJobDefinition.setJobDefinition(jobDefinition);
//                                offlineJobDefinition.setSiteUrl(siteItem);
//                                offlineJobDefinition.save();
//                            }
//                        }








//                    }

                    handler.onUpdateLocalDataFinished();
                }catch (Exception e){
                    Logger.e(TAG, "updateLocalDataAsync", e);

                    //Ignore error
                    handler.onUpdateLocalDataFinished();
                }
            }
        });
    }
}
