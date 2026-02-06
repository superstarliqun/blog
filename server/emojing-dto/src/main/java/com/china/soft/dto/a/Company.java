package com.china.soft.dto.a;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Company {

    private Integer sid;

    private String companyName;

    private String keywords;

    public Company(Integer sid, String companyName,String keywords) {
        this.sid = sid;
        this.companyName = companyName;
        this.keywords = keywords;
    }

}
