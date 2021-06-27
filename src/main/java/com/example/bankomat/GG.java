package com.example.bankomat;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;

public class GG {
    public static void main(String[] args) {
        LocalDate localDate=LocalDate.now();
        LocalDate christmas = LocalDate.of(2016, 12, 25);
        if (localDate.isAfter(christmas)){
            System.out.println("ha");
        }
    }
}
