package com.wedding.serviceapi.boards.dto.weddinghall;

import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class HusbandWifeNameDto {
    private String gender;
    private String name;
}
