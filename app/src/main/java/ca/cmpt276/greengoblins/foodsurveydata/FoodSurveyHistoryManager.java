package ca.cmpt276.greengoblins.foodsurveydata;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FoodSurveyHistoryManager {

    private final String[] TAGS = {"SAVE_EXCEPTION", "LOAD_EXCEPTION"};
    private ArrayList<String> tableDates;
    private ArrayList<ConsumptionTable> tableHistory;

    public FoodSurveyHistoryManager() {
        tableHistory = new ArrayList<ConsumptionTable>();
        tableDates = new ArrayList<String>();
    }

    public ArrayList<ConsumptionTable> getTableHistory() {
        return tableHistory;
    }

    public ArrayList<String> getTableDates(){
        return tableDates;
    }

    public int getNumberOfTables(){
        return tableHistory.size();
    }

    public boolean saveTableByDate(Context activityContext, ConsumptionTable tableToSave) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yy-MM-dd");
        String fileName = mdformat.format(calendar.getTime());
        //DateFormat df = DateFormat.getDateInstance();
        //String fileName = df.format(new Date());
        try {
            FileOutputStream fos = activityContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(tableToSave);
            os.close();
            fos.close();
            Log.d(TAGS[0], "saved: " + fileName );
        } catch (FileNotFoundException exception) {
            Log.d(TAGS[0], exception.getMessage());
            return false;
        } catch (IOException exception) {
            Log.d(TAGS[0], exception.getMessage());
            return false;
        }
        return true;
    }

    public void deleteTableByDate(Context activityContext, String formattedDate){
        String[] fileList = activityContext.fileList();
        for (String fileName : fileList) {
            Log.d(TAGS[1], "file: " + fileName);
            if (fileName.equals(formattedDate)) {

                tableHistory.remove(tableDates.indexOf(formattedDate));
                tableDates.remove(formattedDate);
                File file = new File(activityContext.getFilesDir(), fileName);
                file.delete();
            }
        }
    }

    public ConsumptionTable loadTableByDate(Context activityContext, String formattedDate) {
        ConsumptionTable loaded = null;
        try {
            FileInputStream fis = activityContext.openFileInput(formattedDate);
            ObjectInputStream is = new ObjectInputStream(fis);
            loaded = (ConsumptionTable) is.readObject();
            is.close();
            fis.close();
            Log.d(TAGS[1], "loaded: " + formattedDate );
        } catch (FileNotFoundException exception) {
            Log.d(TAGS[1], exception.getMessage());
        } catch (IOException exception) {
            Log.d(TAGS[1], exception.getMessage());
        } catch (ClassNotFoundException exception) {
            Log.d(TAGS[1], exception.getMessage());
        }
        return loaded;
    }

    public void loadTablesByMonth(Context activityContext, int monthIndex) {
        if (!tableHistory.isEmpty())
            tableHistory.clear();
        String[] fileList = activityContext.fileList();
        for (String fileName : fileList) {
            String month = fileName.substring(fileName.length()-5, fileName.length()-3);
            int monthValue = 0;
            try{
                monthValue = Integer.parseInt(month);
                if(monthValue == monthIndex){
                    tableDates.add(fileName);
                    tableHistory.add( loadTableByDate( activityContext, fileName ));
                }
            }catch (NumberFormatException exception){
                Log.d(TAGS[1], exception.getMessage());
            }
        }
    }

    public void loadAllSavedTables(Context activityContext) {
        if (!tableHistory.isEmpty())
            tableHistory.clear();
        String[] fileList = activityContext.fileList();
        Pattern dateFormatPattern = Pattern.compile("\\d{2}-\\d{2}");
        for (String fileName : fileList) {
            if (fileName.matches(dateFormatPattern.pattern())) {
                tableDates.add(fileName);
                tableHistory.add(loadTableByDate(activityContext, fileName));
            }
        }
    }
}
