package com.up.habit.kit;

import com.up.habit.expand.listener.HabitListener;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO:Excel操作类
 *
 * @author 王剑洪 on 2020/11/17 11:15
 */
public class ExcelKit {
    public static void parsing(File file, HabitListener listener) {
        List<List<String>> list = new ArrayList<>();
        try {
            Workbook workbook = Workbook.getWorkbook(file);
            for (Sheet sheet : workbook.getSheets()) {
                /**行,列数*/
                int rows = sheet.getRows();
                int columns = sheet.getColumns();
                for (int i = 0; i < rows; i++) {
                    List<String> rowList = new ArrayList<>();
                    for (int j = 0; j < columns; j++) {
                        Cell cell = sheet.getCell(j, i);
                        //获取行列所对应的内容
                        String contents = cell.getContents();
                        listener.onEvent("content", i, j, contents);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            /*数据解析完毕,删除文件*/
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
