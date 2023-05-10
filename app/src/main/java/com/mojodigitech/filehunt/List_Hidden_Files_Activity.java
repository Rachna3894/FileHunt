package com.mojodigitech.filehunt;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mojodigitech.filehunt.Adapter.MultiSelectAdapter_Locker;
import com.mojodigitech.filehunt.AddsUtility.SharedPreferenceUtil;
import com.mojodigitech.filehunt.AsyncTasks.UnhideAsynscTask;
import com.mojodigitech.filehunt.AsyncTasks.decryptAsynscTask;
import com.mojodigitech.filehunt.Class.Constants;
import com.mojodigitech.filehunt.Model.Grid_Model;
import com.mojodigitech.filehunt.Model.Model_Locker;
import com.mojodigitech.filehunt.Utils.AlertDialogHelper;
import com.mojodigitech.filehunt.Utils.CustomProgressDialog;
import com.mojodigitech.filehunt.Utils.RecyclerItemClickListener;
import com.mojodigitech.filehunt.Utils.Utility;
import com.mojodigitech.filehunt.Utils.UtilityStorage;

import java.io.File;
import java.util.ArrayList;


public class List_Hidden_Files_Activity extends AppCompatActivity implements AlertDialogHelper.AlertDialogListener, MultiSelectAdapter_Locker.fileSelectListener , decryptAsynscTask.decryptListener, UnhideAsynscTask.unhideListener {

    RecyclerView recyclerView;
    MultiSelectAdapter_Locker multiSelectAdapter;

    Context mContext;
    ImageView blankIndicator;
    int media_Type;
    ArrayList<Model_Locker> fileList = new ArrayList<Model_Locker>();
    ArrayList<Model_Locker> multiselect_list = new ArrayList<>();
    File fileToDelete;
    SharedPreferenceUtil addprefs;


    ActionMode mActionMode;
    Menu context_menu;
    boolean isMultiSelect = false;
    static List_Hidden_Files_Activity instance;
    private boolean isUnseleAllEnabled=false;

    Model_Locker modelLocker ;

    AlertDialogHelper alertDialogHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hidden_file);

        mContext=List_Hidden_Files_Activity.this;
        addprefs=new SharedPreferenceUtil(mContext);

        instance=this;

        initViews();

        Bundle extrasIntent = getIntent().getExtras();

        if (extrasIntent != null) {
            media_Type=extrasIntent.getInt("mediaKey");
        }

        switch (media_Type)
        {
            case 1:  //img

                getFilesfromAppFolder(media_Type);

                Utility.setActivityTitle2(mContext, getResources().getString(R.string.cat_Images));
                break;

            case 2:  //vdo
                getFilesfromAppFolder(media_Type);
                Utility.setActivityTitle2(mContext, getResources().getString(R.string.cat_Videos));
                break;

            case 3:  //ado
                getFilesfromAppFolder(media_Type);
                Utility.setActivityTitle2(mContext, getResources().getString(R.string.cat_Audio));
                break;

            case 4:  //docs
                getFilesfromAppFolder(media_Type);
                Utility.setActivityTitle2(mContext, getResources().getString(R.string.cat_Documents));
                break;

        }


    }



    private void initViews() {

        alertDialogHelper =new AlertDialogHelper(this);

        recyclerView =  findViewById(R.id.recycler_view);
        blankIndicator=findViewById(R.id.blankIndicator);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isMultiSelect) {
                    int pos = position;
                    if(pos!= RecyclerView.NO_POSITION)
                        multi_select(position);
                }
                else {
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiselect_list = new ArrayList<Model_Locker>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = startActionMode(mActionModeCallback);


                        Utility.hideKeyboard(List_Hidden_Files_Activity.this);
                        //isSearchModeActive = false;
                        //searchView.onActionViewCollapsed();
                        //sortView.setVisible(true);
                    }
                }
                multi_select(position);
            }
        }));

    }



    /**********************set ActionMode***************************/
    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(fileList.get(position)))
                multiselect_list.remove(fileList.get(position));
            else {
                multiselect_list.add(fileList.get(position));

                // to  rename file contain old file;
               // if(multiselect_list.size()==1) {
                    //fileTorename = fileList.get(position);
                    //renamePosition=position;
                //}
                // to  rename file contain old file;
            }

            if (multiselect_list.size() > 0) {
                mActionMode.setTitle("" + multiselect_list.size());

                //keep  the reference of file to  be renamed
                //if (fileList.contains(multiselect_list.get(0))) {
                    // renamePosition = fileList.indexOf(multiselect_list.get(0));
                    // fileTorename = multiselect_list.get(0);
              //  }
            }
            //keep  the reference of file to  be renamed
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter()
    {
        if(multiSelectAdapter !=null) {
            multiSelectAdapter.selected_LockerList = multiselect_list;
            multiSelectAdapter.LockerList = fileList;
            multiSelectAdapter.notifyDataSetChanged();

            selectMenuChnage();

            //finish action mode when user deselect files one by one ;
            if (multiselect_list.size() == 0) {
                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }
        }
    }

    private void selectAll()
    {
        if (mActionMode != null)
        {
            multiselect_list.clear();

            for(int i=0;i<fileList.size();i++)
            {
                if(!multiselect_list.contains(multiselect_list.contains(fileList.get(i))))
                {
                    multiselect_list.add(fileList.get(i));
                }
            }
            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

            //to change  the unselectAll  menu  to  selectAll
            selectMenuChnage();
            //to change  the unselectAll  menu  to  selectAll

        }
    }

    private void unSelectAll()
    {
        if (mActionMode != null)
        {
            multiselect_list.clear();

            if (multiselect_list.size() >= 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            //to change  the unselectAll  menu  to  selectAll
            selectMenuChnage();
            //to change  the unselectAll  menu  to  selectAll

            if (mActionMode != null) {
                mActionMode.finish();
            }
            refreshAdapter();
        }
    }

    private void selectMenuChnage()
    {
        if(context_menu!=null)
        {
            if(fileList.size()==multiselect_list.size()) {
                for (int i = 0; i < context_menu.size(); i++) {
                    MenuItem item = context_menu.getItem(i);
                    if (item.getTitle().toString().equalsIgnoreCase(getResources().getString(R.string.menu_selectAll))) {
                        item.setTitle(getResources().getString(R.string.menu_unselectAll));
                        isUnseleAllEnabled=true;
                    }
                }
            }
            else {
                for (int i = 0; i < context_menu.size(); i++) {
                    MenuItem item = context_menu.getItem(i);
                    if (item.getTitle().toString().equalsIgnoreCase(getResources().getString(R.string.menu_unselectAll))) {
                        item.setTitle(getResources().getString(R.string.menu_selectAll));
                        isUnseleAllEnabled=false;
                    }
                }
            }



        }
        invalidateOptionsMenu();
    }



    private int statusBarColor;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_unhide_files, menu);
            context_menu = menu;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //hold current color of status bar
                statusBarColor = getWindow().getStatusBarColor();
                //set your gray color
                getWindow().setStatusBarColor(getResources().getColor(R.color.onePlusAccentColor_device_default_dark));
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }


        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_unhide:
                    if (multiselect_list.size() >= 1) {

                        if (Utility.createOrFindAppDirectory(Constants.MEDIA_TYPE_IMG))
                        {
                            File[] f = new File[multiselect_list.size()];
                            for (int i = 0; i < multiselect_list.size(); i++) {
                                File file = new File(multiselect_list.get(i).getFilePath());
                                f[i] = file;
                            }

                            if (f.length >= 1)
                                new UnhideAsynscTask(mContext, f, instance, Constants.encryptionPassword, media_Type).execute();
                            else
                                Utility.dispToast(mContext, getResources().getString(R.string.filenotfound));
                        }
                        else
                        {
                            Utility.dispToast(mContext,getResources().getString(R.string.directorynotfound));
                        }
                    }
                    return  true;




                case R.id.action_delete:
                    if(multiselect_list.size()>=1) {
                        int mFileCount = multiselect_list.size();
                        String msgDeleteFile = mFileCount > 1 ? mFileCount + " " + getResources().getString(R.string.delfiles) : mFileCount + " " + getResources().getString(R.string.delfile);
                        alertDialogHelper.showAlertDialog("", getResources().getString(R.string.delete_file_msgs)+" ("+msgDeleteFile+")", getResources().getString(R.string.menu_item_delete), getResources().getString(R.string.cancel), 1, true);
                    }
                    return true;


                case R.id.action_select:
                    if(fileList.size()==multiselect_list.size() || isUnseleAllEnabled==true)
                        unSelectAll();
                    else
                        selectAll();
                    return  true;


                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiselect_list = new ArrayList<Model_Locker>();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //return to "old" color of status bar
                getWindow().setStatusBarColor(statusBarColor);
            }
            refreshAdapter();
        }
    };


    /********************Set ActionMode***********************************/

    private void  getFilesfromAppFolder(int cat_Type)
    {
        String folderPath= Utility.setDecryptFilePath(cat_Type);
        File fpath  = new File(folderPath);
        if(fpath.exists() && fpath.isDirectory()) {

            File[] files = fpath.listFiles();

            for(int i=0;i<files.length;i++)
            {
                File f = files[i];
                String fname = f.getName();
                System.out.print(""+fname);

                Model_Locker model=new Model_Locker();
                model.setFileName(f.getName());
                model.setFileSize(Utility.humanReadableByteCount(f.length(),true));
                model.setFilePath(f.getPath());
                model.setFileSizeCmpr(f.length());
                model.setDateCmpr(f.lastModified());
                model.setFileMDate(Utility.LongToDate(f.lastModified()));
                fileList.add(model);
            }
            if(fileList.size()!=0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
                multiSelectAdapter = new MultiSelectAdapter_Locker(this, fileList, multiselect_list, this,media_Type);
                recyclerView.setAdapter(multiSelectAdapter);
            }
            else
            {
                blankIndicator.setVisibility(View.VISIBLE);
            }

        }
        else
        {
            blankIndicator.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onfileSelectListener(Model_Locker locker_model) {
        new decryptAsynscTask(mContext, new File[]{new File(locker_model.getFilePath().toString())}, this, Constants.encryptionPassword, media_Type).execute();
    }


//    @Override
//    public void onUnhideSelectListener(Model_Locker locker_model) {
//       // modelLocker = locker_model ;
//        new UnhideAsynscTask(mContext, new File[]{new File(locker_model.getFilePath().toString())}, this, Constants.encryptionPassword, media_Type).execute();
//    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        // deletes the viewd file from memory;
        if(fileToDelete!=null && fileToDelete.exists())
            fileToDelete.delete();

    }



    @Override
    public void OnDeCryptFinish(File fileDecrypted) {

        if(fileDecrypted !=null)

            fileToDelete = fileDecrypted;  //  keeps the reference of the file  to  be deleted from the memory after  user  used it  ;

        switch (media_Type)
        {
            case 1:  //img
                Grid_Model model = new Grid_Model();
                model.setImgPath(fileDecrypted.getAbsolutePath());

                Constants.img_ArrayImgList.clear();
                Constants.img_ArrayImgList.add(model);
                Intent intentImageGallary = new Intent(mContext, Media_ImgActivity.class);
                intentImageGallary.putExtra(Constants.CUR_POS_VIEW_PAGER, 0);
                //intentImageGallary.putExtra(Constants.MEDIA_DELETE_ACTIVITY_TRACKER, Constants.RECENT);
                startActivity(intentImageGallary);

                break;



            case 2:  //vdo
                addprefs.setIntValue("position", 0);
                Intent intentVideoGallary = new Intent(mContext, Media_VdoActivity.class);
                intentVideoGallary.putExtra(Constants.selectedVdo, fileDecrypted.getAbsolutePath());
                //intentVideoGallary.putExtra(Constants.MEDIA_DELETE_ACTIVITY_TRACKER, Constants.RECENT);
                startActivity(intentVideoGallary);

                break;

            case 3:  //ado
                addprefs.setIntValue("position", 0);
                Intent intentAudioGallary = new Intent(mContext, Media_AdoActivity.class);
                intentAudioGallary.putExtra(Constants.selectedAdo, fileDecrypted.getAbsolutePath());
                //intentAudioGallary.putExtra(Constants.MEDIA_DELETE_ACTIVITY_TRACKER, Constants.STORAGE);
                startActivity(intentAudioGallary);

                break;

            case 4:  //docs

                Utility.OpenFileWithNoughtAndAll(fileDecrypted.getAbsolutePath(), mContext, getResources().getString(R.string.file_provider_authority));

                break;

        }


    }



    @Override
   // public void OnUnhideFinish(File fileUnhided) {
    public void OnUnhideFinish( ) {

        if(multiselect_list.size()>0)
        {
            for(int i=0;i<multiselect_list.size();i++)
            {
                fileList.remove(multiselect_list.get(i));
            }
        }

        multiselect_list.clear();
        multiSelectAdapter.notifyDataSetChanged();

        if(mActionMode !=null)
            mActionMode.finish();

//        if(fileUnhided !=null){
//                    //tks
//                    for(int i=0;i<fileList.size();i++)
//                    {
//                        fileList.removeAll(AddConstants.hidden_file_pathList);
//
////                        Model_Locker model1 = fileList.get(i);
////                        String hiddenFileName ="";
////
////                        for(int j=0;j<AddConstants.hidden_file_pathList.size();j++) {
////                              hiddenFileName = AddConstants.hidden_file_pathList.get(j);
////                        }
////
////                        if (model1.getFilePath().equalsIgnoreCase(hiddenFileName)) {
////                            fileList.remove(model1);
////                            Utility.dispToast(mContext, model1.getFileName()+" file restored in gallery successfully");
////                        }
//
//                    }
//
//                    multiSelectAdapter.notifyDataSetChanged();
                    //tks
            // Utility.dispToast(mContext, fileUnhided.getName()+" file restored in gallery successfully");
       // }

    }







    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiselect_list.size()>0)
            {
//                File f =new File(multiselect_list.get(0).getFilePath());
//                if(UtilityStorage.isWritableNormalOrSaf(f,mContext)) {

                    new  DeleteFileTask(multiselect_list).execute();

//                }
//                else
//                {
//                    UtilityStorage.guideDialogForLEXA(mContext,f.getParent(), Constants.FILE_DELETE_REQUEST_CODE);
//                }


                //  now this task  is being done on  postexecute of detefiletask

//                for(int i=0;i<multiselect_list.size();i++)
//                    audioList.remove(multiselect_list.get(i));
//
//                multiSelectAdapter.notifyDataSetChanged();
//
//                if (mActionMode != null) {
//                    mActionMode.finish();
//                }
            }
//            else
//            {
//                blankIndicator.setVisibility(View.VISIBLE);
//            }
        }
        else if(from==2)
        {
            if (mActionMode != null) {
                mActionMode.finish();
            }

            //this yet to be implemented
//            Model_Audio mImg = new Model_Audio();
//            mImg.setImgPath("");
//            audioList.add(mImg);
            multiSelectAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }


    private class DeleteFileTask extends AsyncTask<Void,Void,Integer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CustomProgressDialog.show(mContext,getResources().getString(R.string.deleting_file));
        }

        ArrayList<Model_Locker> multiselect_list;

        DeleteFileTask( ArrayList<Model_Locker> multiselect_list)
        {
            this.multiselect_list = multiselect_list;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            return deleteFile(multiselect_list);
        }

        @Override
        protected void onPostExecute(Integer FileCount) {
            super.onPostExecute(FileCount);
            if(FileCount>0)
            {
                for (int i = 0; i < multiselect_list.size(); i++) {
                    fileList.remove(multiselect_list.get(i));
                    Utility.removeFileFromCopyList(multiselect_list.get(i).getFilePath());
                }

                multiSelectAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }

            String msg=FileCount>1 ? FileCount+" "+getResources().getString(R.string.delmsg1) : FileCount+" "+getResources().getString(R.string.delmsg2);
            Utility.dispToast(mContext, msg);

            CustomProgressDialog.dismiss();
        }
    }


    private int deleteFile(ArrayList<Model_Locker> delete_list)
    {
        int count=0;
        for(int i=0;i<delete_list.size();i++)
        {
            File f=new File(String.valueOf(delete_list.get(i).getFilePath()));
            if(f.exists()) {
                if (f.delete()) {
                    count++;
                    sendBroadcast(f);
                }

                //new
                else {
                    boolean st = UtilityStorage.isWritableNormalOrSaf(f, mContext);
                    System.out.println("" + st);
                    if (st) {
                        boolean status = UtilityStorage.deleteWithAccesFramework(mContext, f);
                        if (status) {
                            count++;
                            Utility.RunMediaScan(mContext, f);
                        }
                    }
                    else {
                        //  UtilityStorage.triggerStorageAccessFramework(mcontext);
                    }
                }
                //new
            }
        }

        Constants.DELETED_AUDIO_FILES=count;

        return count;
    }





    private void sendBroadcast(File outputFile)
    {
        //  https://stackoverflow.com/questions/4430888/android-file-delete-leaves-empty-placeholder-in-gallery
        //this broadcast clear the deleted images from  android file system
        //it makes the MediaScanner service run again that keep  track of files in android
        // to  run it a permission  in manifest file has been given
        // <protected-broadcast android:name="android.intent.action.MEDIA_MOUNTED" />


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(outputFile);
            scanIntent.setData(contentUri);
            sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
        }

    }
}
