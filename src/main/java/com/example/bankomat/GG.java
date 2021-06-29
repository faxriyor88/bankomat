package com.example.bankomat;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;

public class GG {
    public static void main(String[] args) {
        System.out.println(578_000-578_000%100_000);
        System.out.println(4_578_000%100_000-(4_578_000%100_000)%50_000);
        System.out.println((4_578_000%100_000)%50_000-((4_578_000%100_000)%50_000)%10_000);
        System.out.println(((4_578_000%100_000)%50_000)%10_000-(((4_578_000%100_000)%50_000)%10_000)%5_000);
        System.out.println((((4_578_000 % 100_000) % 50_000) % 10_000) % 5_000);

    }
}
