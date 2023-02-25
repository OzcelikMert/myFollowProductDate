package com.example.myfpd.MyLibrary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyLibraryVariable {

    /* Clear Variable */
    public static Object ClearVar(Object Variable, String ClearType){
        Variable = (Variable != null) ? Variable : "";

        switch (ClearType){
            case "normal":
                Variable = Variable.toString().replaceAll(" ", "");
                Variable = Variable.toString().replaceAll("'", "");
                break;
            case "normal+number":
                Variable = Variable.toString().replaceAll(" ", "");
                Variable = Variable.toString().replaceAll("'", "");
                Variable = Variable.toString().replaceAll("([a-zA-Z])", "");
                Variable = (Variable.toString().equals("")) ? "0" : Variable;
                Variable = Integer.valueOf(Variable.toString());
                break;
        }

        return Variable;
    }
    /* end Clear Variable */

    public static String convertDateFormat(Date date, String format) {
        format = format == null ? "dd/MM/yyyy" : format;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static boolean isValidDateFormat(String strDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setLenient(false);
        try {
            Date date = dateFormat.parse(strDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
