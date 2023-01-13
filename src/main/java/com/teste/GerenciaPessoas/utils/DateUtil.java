package com.teste.GerenciaPessoas.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static String pattern = "dd/MM/yyyy";
    private static SimpleDateFormat dateFormat= new SimpleDateFormat(pattern);
    public static Date dateFromString(String dataString) throws ParseException {
        Date data= null;

        data = (dateFormat.parse(dataString));

        return data;
    }

    public static String stringFromDate(Date data) {
        String dataString=dateFormat.format(data);
        return dataString;
    }
}
