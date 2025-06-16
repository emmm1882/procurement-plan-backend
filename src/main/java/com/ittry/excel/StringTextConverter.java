package com.ittry.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;

public class StringTextConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    public WriteCellData<String> convertToExcelData(String value, ExcelContentProperty contentProperty) {
        WriteCellData<String> cellData = new WriteCellData<>(value);
        cellData.setType(CellDataTypeEnum.STRING);
        return cellData;
    }
} 