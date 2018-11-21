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
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

public class FoodSurveyHistoryManager {

    private File saveFolder;
    private final String[] TAGS = {"SAVE_EXCEPTION", "LOAD_EXCEPTION"};
    private ArrayList<String> tableDates;
    private ArrayList<ConsumptionTable> tableHistory;

    public FoodSurveyHistoryManager() {
        tableHistory = new ArrayList<ConsumptionTable>();
        tableDates = new ArrayList<String>();
    }

    public boolean createSaveFolder(Context activityContext){
        if( saveFolder != null ) return true;

        File path = new File(activityContext.getFilesDir(), "foodsurveydata");
        if ( !path.exists() ) {
            path.mkdirs();
        }

        saveFolder = path;

        if ( saveFolder == null ) {
            return false;
        }
        return true;
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
        createSaveFolder(activityContext);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdFormat = new SimpleDateFormat("yy-MM-dd");
        String fileName = mdFormat.format(calendar.getTime());
        try {
            File newSave = new File(saveFolder, fileName);
            FileOutputStream fos = new FileOutputStream(newSave);
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
        createSaveFolder(activityContext);

        File[] fileList = saveFolder.listFiles();
        for( File listedFile : fileList ){
            if( listedFile.getName().equals(formattedDate) ){
                tableHistory.remove(tableDates.indexOf(formattedDate));
                tableDates.remove(formattedDate);
                listedFile.delete();
            }
        }
    }

    public ConsumptionTable loadTableByDate(Context activityContext, String formattedDate) {
        createSaveFolder(activityContext);

        ConsumptionTable loaded = null;
        try {
            File newLoad = new File( saveFolder, formattedDate );
            FileInputStream fis = new FileInputStream(newLoad);
            ObjectInputStream is = new ObjectInputStream(fis);
            loaded = (ConsumptionTable) is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException exception) {
            Log.d(TAGS[1], exception.getMessage());
        } catch (IOException exception) {
            Log.d(TAGS[1], exception.getMessage());
        } catch (ClassNotFoundException exception) {
            Log.d(TAGS[1], exception.getMessage());
        }
        return loaded;
    }

    public boolean loadTablesByMonth(Context activityContext, int monthIndex) {
        createSaveFolder(activityContext);

        if (!tableHistory.isEmpty())
            tableHistory.clear();
        if (!tableDates.isEmpty())
            tableDates.clear();
        String[] fileList = saveFolder.list();
        if (fileList != null && fileList.length > 1){
            Arrays.sort(fileList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
        }

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
        return true;
    }

    public boolean loadAllSavedTables(Context activityContext) {
        createSaveFolder(activityContext);

        if( saveFolder == null ) return false;
        if (!tableHistory.isEmpty())
            tableHistory.clear();
        if (!tableDates.isEmpty())
            tableDates.clear();
        String[] fileList = activityContext.getDir(saveFolder.getName(), Context.MODE_PRIVATE).list();

        if (fileList != null && fileList.length > 1){
            Arrays.sort(fileList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
        }

        Pattern dateFormatPattern = Pattern.compile("\\d{2}-\\d{2}");
        for (String fileName : fileList) {
            if (fileName.matches(dateFormatPattern.pattern())) {
                tableDates.add(fileName);
                tableHistory.add(loadTableByDate(activityContext, fileName));
            }
        }
        return true;
    }
}
