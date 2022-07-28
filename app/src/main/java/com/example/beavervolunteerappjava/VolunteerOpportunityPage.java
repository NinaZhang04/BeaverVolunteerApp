package com.example.beavervolunteerappjava;

import static android.content.ContentValues.TAG;

import static org.apache.poi.hssf.record.FormulaSpecialCachedValue.STRING;



import static java.sql.Types.NUMERIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


public class VolunteerOpportunityPage extends AppCompatActivity {
    XSSFWorkbook workbook = new XSSFWorkbook();
    Sheet sheet = null;


    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_opportunity_page);
        Log.w(TAG,"WHAT IS HAPPENING?????????????????????????????????????");
        downloadVolunteerData();


    }

    public void downloadVolunteerData(){
        // Create a storage reference from our app
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference pathReference = storageRef.child("volunteerOpportunityList.xlsx");


        // Currently downloading the stored google sheet into memory, so keep in mind that the
        // Google sheet must not be too large, containing maximum, say, 100 opportunities?

        File localFile = null;

        try {
            localFile = File.createTempFile("tempOpportunities", "xlsx");

        } catch (IOException e) {
            e.printStackTrace();
            showToast("!volunteer opportunities not found. Check if connected to Wifi");
        }
        File newTempFile = localFile;
        if(localFile != null) {
            pathReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    // Read the entire sheet or whatever file and put it into a list of opportunity objects
                    createVolunteerOpportunityList(newTempFile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    }

    public VolunteerOpportunity[] createVolunteerOpportunityList(File tempVolunteerFile){
        //File file = new File(VolunteerOpportunityPage.this.getExternalFilesDir(null), "tempOpportunities.xlsx");
        // create input stream
        FileInputStream fileInputStream = null;

        Boolean fileExist = false;
        Boolean workbookExist = false;

        // if input stream can get the file, switch on the first confirmation switch
        try {
            fileInputStream = new FileInputStream(tempVolunteerFile);
            fileExist = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToast("~volunteer opportunities not found. Check if connected to Wifi");
        }

        try {
            if(fileInputStream != null) {
                workbook = new XSSFWorkbook(fileInputStream);
                workbookExist = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            showToast("\"volunteer opportunities not found. Check if connected to Wifi\"");
        }

        if(fileExist && workbookExist){
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


        //file.delete();

        return null;
    }


    private void showToast(String text){
        Toast.makeText(VolunteerOpportunityPage.this, text, Toast.LENGTH_LONG).show();
    }
}