package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRequestDto {

    @Schema(description = SchemaDescriptionUtils.Company.NAME, example = "Nkock" , maxLength = 20)
    private String companyName;

    @Schema(description = SchemaDescriptionUtils.Company.ADDRESS, example = "경기도 OO시" , maxLength = 100)
    private String address;

    @Schema(description = SchemaDescriptionUtils.Company.TEL, example = "02-XXX-XXXX" , maxLength = 20)
    private String companyTel;

    @Schema(description = SchemaDescriptionUtils.Company.PRESIDENT, example = "홍길동" , maxLength = 20)
    private String president;

    @Schema(description = SchemaDescriptionUtils.Company.BUSINESS_NUM, example = "XXX-XX-XXXXX" , maxLength = 20)
    private String businessNum;

    @Schema(description = SchemaDescriptionUtils.EMAIL, example = "test@xxx.xxx" , maxLength = 50)
    private String companyEmail;
}
