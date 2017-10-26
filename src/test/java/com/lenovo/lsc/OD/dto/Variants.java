package com.lenovo.lsc.OD.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Variants {
    private String varTab;
    private String sequence;
    private String charName;
    private String charValue;
}
