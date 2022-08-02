package com.example.beavervolunteerappjava;

//import static android.content.ContentValues.TAG;

//import static org.apache.poi.hssf.record.FormulaSpecialCachedValue.STRING;



import static android.content.ContentValues.TAG;
import static java.sql.Types.NUMERIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

//import com.alibaba.easyexcel.test.util.TestFileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.DefaultConverterLoader;
//import com.alibaba.excel.enums.CellExtraTypeEnum;
//import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
//import com.alibaba.excel.util.ListUtils;
//import com.alibaba.fastjson.JSON;

public class VolunteerOpportunityPage extends AppCompatActivity implements SelectListener{
    Workbook workbook;
    Sheet sheet = null;
    File localFile;
    final int opportunityPropertiesAmount = 9;



    RecyclerView recyclerView;
    //this is the list that gets passed into recycler view, which is the display list
    VolunteerOpportunity[] classListOfOpportunityList;
    CustomAdapter customAdapter;

    //this will be used in detailed opportunity page so that class knows what opportunity should be displayed
    public static VolunteerOpportunity currentlyViewingOpportunity;


    //new// private final static String TAG="VolunteerOpportunityPage";

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    //
    @Override
    public void onItemClicked(VolunteerOpportunity opportunity) {
        currentlyViewingOpportunity = opportunity;
        startActivity(new Intent(VolunteerOpportunityPage.this, DetailedOpportunityPage.class));

    }

    //@Getter
    //@Setter
    //@EqualsAndHashCode
    static public class DemoData {
        private String string;
        //private Date date;
        private Double doubleData;
    }

    class DemoDataListener{
        int j;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"WHAT IS  asdf  HAPPENING?????????????????????????????????????");
       // jgkj
       // try{
          //  workbook= new XSSFWorkbook();
       // }catch(Exception e) {
         //   Log.w(TAG,"XSSFWorkbook got exception",e);
       // }

        setContentView(R.layout.activity_opportunity_list);
        //new// Log.e(TAG,"WHAT IS HAPPENING?????????????????????????????????????");
        downloadVolunteerData();
        classListOfOpportunityList = createOpportunityList();

        if(classListOfOpportunityList != null){
            displayItems();
        }
        else{
            showToast("!Cannot display opportunities. You may be offline or our server document is updating.");
        }

    }

    private void displayItems() {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        customAdapter = new CustomAdapter(this, classListOfOpportunityList, this);
        recyclerView.setAdapter(customAdapter);
    }

    public void downloadVolunteerData(){
        // Create a storage reference from our app
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference pathReference = storageRef.child("Book3.xls");


        // Currently downloading the stored google sheet into memory, so keep in mind that the
        // Google sheet must not be too large, containing maximum, say, 100 opportunities?


//new//
        try {
           // localFile = File.createTempFile("tempOpportunities", ".xlsx");
            //String path =  Environment.getExternalStorageDirectory();

            //new// File path1 =  Environment.getExternalStorageDirectory();
            //new//Log.e(TAG, "Environment.getExternalStorageDirectory(); IS " + path1.getAbsolutePath());
            //new//localFile = new File(path1.getAbsolutePath()+"/wenjie.xlsx");

        } catch (Exception e) {
            e.printStackTrace();
            showToast("!volunteer opportunities not found. Check if connected to Wifi");
        }





        localFile = new File(this.getCacheDir(), "opportunityListOnPhone.xls");
        Log.d(TAG, "++++"+ localFile.getAbsolutePath());




        pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                // Read the entire sheet or whatever file and put it into a list of opportunity objects


                classListOfOpportunityList = createOpportunityList();

                //new// createVolunteerOpportunityList(newTempFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "BRUH WHAT WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
            }
        });


    }


    public VolunteerOpportunity[] createOpportunityList(){

        FileInputStream fileInputStream = null;

        File file = localFile;
        Log.d(TAG,"=========:   "+ file.getAbsolutePath());
        try {
            fileInputStream = new FileInputStream(file);
            try {
                workbook = new HSSFWorkbook(fileInputStream);

            } catch (IOException e) {
                Log.d(TAG, "OMG just work already");
            }

            //Get the first worksheet, because the second is not going to be searched
            sheet = workbook.getSheetAt(0);

            int rowCountOfSheet = 0;
            for (Row row : sheet){
                if (row.getRowNum() > 0){
                    rowCountOfSheet++;
                }
            }

            // Initialize the VolunteerOpportunity list here and add things in in the for loop below
            classListOfOpportunityList = new VolunteerOpportunity[rowCountOfSheet];

            //This number keeps track of which index the for loop produced object should be put in
            int indexOfReturnThisList = 0;

            // Iterate through each row
            for (Row row : sheet) {
                Log.d(TAG, "Initialize list and count");
                String[] opportunityDescriptions = new String[opportunityPropertiesAmount];
                int opportunityCount = 0;
                if (row.getRowNum() > 0) {
                    // Iterate through all the cells in a row (Excluding header row)
                    Iterator<Cell> cellIterator = row.cellIterator();
                    Log.d(TAG, "Creating one new object");

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        // Check cell type and format accordingly
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                // Print cell value
                                Log.d(TAG, "----------" + cell.getNumericCellValue() + "----------");
                                break;

                            case STRING:
                                Log.d(TAG,  opportunityCount + "++++++++++" + cell.getStringCellValue() + "++++++++++");
                                opportunityDescriptions[opportunityCount] = cell.getStringCellValue();
                                break;
                        }
                        opportunityCount++;
                    }



                    //Create the object using the information obtained in one row
                    classListOfOpportunityList[indexOfReturnThisList] =
                            new VolunteerOpportunity(
                                    opportunityDescriptions[0], opportunityDescriptions[1],
                                    opportunityDescriptions[2], opportunityDescriptions[3],
                                    opportunityDescriptions[4], opportunityDescriptions[5],
                                    opportunityDescriptions[6], opportunityDescriptions[7],
                                    opportunityDescriptions[8]);
                    Log.d(TAG, classListOfOpportunityList[indexOfReturnThisList].toString());
                    indexOfReturnThisList++;
                }





            }

            Log.d(TAG, "the list stored in this class is updated------------------------\n");
            return classListOfOpportunityList;

        } catch (FileNotFoundException e) {
            Log.d(TAG, "====================lol u didn't find file==================");
            showToast("Volunteer Opportunities cannot be found. This could be server error or you are offline.");
            return null;
        }

        // If file is not found, return null, and then another function that sees this function return null will know
        // that the file did not exist

    }




    /** //new//
    public VolunteerOpportunity[] createVolunteerOpportunityList(File tempVolunteerFile){
        //File file = new File(VolunteerOpportunityPage.this.getExternalFilesDir(null), "tempOpportunities.xlsx");
        // create input stream
        FileInputStream fileInputStream = null;

        Boolean fileExist = false;
        Boolean workbookExist = false;


        XSSFWorkbook  workbook = null;
        // if input stream can get the file, switch on the first confirmation switch
        try {
            String filename = tempVolunteerFile.getAbsolutePath();
            Log.e(TAG,"WENJIE filename IS " + filename);
            fileInputStream = new FileInputStream(tempVolunteerFile);
            fileExist = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToast("~volunteer opportunities not found. Check if connected to Wifi");
        }
        if(true) {
            try {
                if (fileInputStream != null) {
                    workbook = new XSSFWorkbook(fileInputStream);
                    workbookExist = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                showToast("\"volunteer opportunities not found. Check if connected to Wifi\"");
            } catch( Exception al) {
                al.printStackTrace();
                showToast("\"volunteer opportunities not found. Check if connected to Wifi asdf\"");
            }
            Sheet sheet = null;
            if (fileExist && workbookExist) {
                sheet = workbook.getSheetAt(0);
                // Iterate through each row
                for (Row row : sheet) {
                    if (row.getRowNum() > 0) {
                        // Iterate through all the cells in a row (Excluding header row)
                        Iterator<Cell> cellIterator = row.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();

                            // Check cell type and format accordingly
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    // Print cell value
                                    // Is this supposed to happen?
                                    Log.d(TAG, "++++::::::: " + cell.getNumericCellValue());
                                    break;

                                case STRING:
                                    Log.d(TAG, (cell.getStringCellValue()));

                                    break;
                            }
                        }
                    }
                }
            }
        }
     **/
        /**else {

            //EasyExcel.read("aaa", DemoData.class, new DemoDataListener()).sheet().doRead();
            String fileName= "aaa";
            EasyExcel.read(fileName, DemoData.class, new ReadListener<DemoData>(dataList -> {
                @Override
                public void invoke(DemoData data, AnalysisContext context) {}
                Log.d(TAG,"READ LINE");
               // for (DemoData demoData : dataList) {
              //     // log.info("读取到一条数据{}", JSON.toJSONString(demoData));
               // }
            })).sheet().doRead();
        }

        //file.delete();

        return null;
    }
    **/

    private void showToast(String text){
        Toast.makeText(VolunteerOpportunityPage.this, text, Toast.LENGTH_LONG).show();
    }
}