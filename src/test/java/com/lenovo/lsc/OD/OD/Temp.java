package com.lenovo.lsc.OD.OD;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Temp {

    private String tempCell = "";
    private String temVT = "";
}
