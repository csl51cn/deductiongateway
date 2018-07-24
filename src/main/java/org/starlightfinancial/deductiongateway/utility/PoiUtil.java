package org.starlightfinancial.deductiongateway.utility;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: Senlin.Deng
 * @Description: 读取xls, xlsx扩展名的Excel 表格,转换为特定的java bean
 * @date: Created in 2018/7/18 10:40
 * @Modified By:
 */
public class PoiUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PoiUtil.class);
    private static final String XLS = "xls";
    private static final String XLSX = "xlsx";

    /**
     * 读入excel文件，解析后返回对应的Map,Map的key为sheet名,value为java bean List
     *
     * @param file                      excel文件
     * @param columnNameAndFieldNameMap excel文件列名和java bean 属性映射Map
     * @param targetClass               目标java bean 类型
     * @param <T>                       返回的java bean 类型
     * @return 返回Map , Map<sheet name ,List<java bean>>
     * @throws IOException 解析io异常
     */
    public static <T> Map<String, List<T>> readExcel(MultipartFile file, Map<String, String> columnNameAndFieldNameMap, Class<T> targetClass) throws IOException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        //检查文件
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建结果Map
         Map<String, List<T>> resultMap = new LinkedHashMap<>(16);
        ArrayList<T> list;
        if (workbook != null) {
            //遍历所有的sheet
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //创建索引和java bean字段名映射
                Map<Integer, String> columnIndexAndFieldNameMap = new HashMap<>(16);
                //列名默认在excel表格第一行
                Row firstRow = sheet.getRow(sheet.getFirstRowNum());
                //遍历excel表格第一行所有单元格,设置列索引和java bean字段名映射
                for (int i = firstRow.getFirstCellNum(); i < firstRow.getLastCellNum(); i++) {
                    //获得第一行第i个单元格
                    Cell cell = firstRow.getCell(i);
                    //如果当前单元格的类型是字符串并且在列名和java bean字段名映射(columnNameAndFieldNameMap)中包含当前单元格的内容也就是列名,
                    // 将当前单元格的索引和内容设置到列索引和java bean字段名映射中
                    if (cell.getCellTypeEnum() == CellType.STRING && columnNameAndFieldNameMap.containsKey(StringUtils.trimToEmpty(cell.getStringCellValue()))) {
                        String fieldName = columnNameAndFieldNameMap.get(StringUtils.trimToEmpty(cell.getStringCellValue()));
                        if (StringUtils.isNotBlank(fieldName)) {
                            columnIndexAndFieldNameMap.put(i, fieldName);
                        }
                    }
                }

                //创建包含excel表格转java bean 结果的list
                list = new ArrayList<T>();

                //利用反射设置java bean 属性
                getJavaBean(targetClass, list, sheet, columnIndexAndFieldNameMap);
                resultMap.put(sheet.getSheetName(), list);
            }
            workbook.close();
        }
        return resultMap;
    }

    /**
     * 利用反射从excel读取内容设置到java bean 属性中
     *
     * @param targetClass                目标 java bean 类型
     * @param list                       包含转换后java bean 的List
     * @param sheet                      excel表格的当前sheet
     * @param columnIndexAndFieldNameMap 类索引和目标java bean 的字段名映射Map
     * @param <T>                        目标java bean 类型
     * @throws InstantiationException 实例化目标类型对象异常抛出的异常
     * @throws IllegalAccessException 目标类型对象字段设置值时无访问权限时抛出的异常
     * @throws NoSuchFieldException   获取目标类型某个申明的字段对象时没有这样的字段时抛出的异常
     */
    private static <T> void getJavaBean(Class<T> targetClass, ArrayList<T> list, Sheet sheet, Map<Integer, String> columnIndexAndFieldNameMap) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        outerLoop:
        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getPhysicalNumberOfRows(); i++) {
            //获得当前sheet的第i行
            Row row = sheet.getRow(i);
            //判断当前行不为null
            if (null != row) {
                //创建T类对象
                T t = targetClass.newInstance();
                //循环获取当前行的所有单元格内容
                for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                    //获得当前行第j个单元格
                    Cell cell = row.getCell(j);
                    //判断当前单元格内容是否为空,如果为空继续执行外循环
                    if (StringUtils.isEmpty(getCellValue(cell))) {
                        continue outerLoop;
                    }
                    //判断列索引和字段名映射Map的key值是否包含当前索引值j
                    if (columnIndexAndFieldNameMap.containsKey(j)) {
                        //获取列索引对应的字段名称
                        String fieldName = columnIndexAndFieldNameMap.get(j);
                        //获得字段对象
                        Field field = targetClass.getDeclaredField(fieldName);
                        //字段对象设置为可访问
                        field.setAccessible(true);
                        //判断字段类型是否为字符串
                        if (field.getType() == String.class) {
                            String value = "";
                            //判断单元格类型是否为字符串
                            if (cell.getCellTypeEnum() == CellType.STRING) {
                                //获得单元格的内容,并去除两端空格
                                value = StringUtils.trimToEmpty(cell.getStringCellValue());
                            } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                //如果单元格类型为数字,获得单元格数字内容,并转换为字符串
                                value = String.valueOf(cell.getNumericCellValue());
                                if ("0.0".equals(value)) {
                                    value = "0";
                                }
                            }
                            //设置t对象的field 字段的值
                            field.set(t, value);
                        }
                        if (field.getType() == BigDecimal.class) {
                            //如果字段类型为BigDecimal,将单元格内容转换为BigDecimal
                            BigDecimal value = BigDecimal.valueOf(cell.getNumericCellValue());
                            //设置t对象的field 属性的值
                            field.set(t, value);
                        }
                        if (field.getType() == Date.class) {
                            //如果字段类型为Date,将单元格内容转换为Date,设置t对象的field 属性的值
                            Date dateCellValue = cell.getDateCellValue();
                            field.set(t, cell.getDateCellValue());
                        }
                    }
                }
                list.add(t);
            }
        }
    }


    private static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            LOGGER.error("文件不存在！");
            throw new FileNotFoundException("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
            LOGGER.error(fileName + "不是excel文件");
            throw new IOException(fileName + "不是excel文件");
        }
    }

    private static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith(XLS)) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(XLSX)) {
                //2007
                workbook = new XSSFWorkbook(is);

            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        return workbook;
    }

    private static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellTypeEnum()) {
            //数字
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            //字符串
            case STRING:
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            //Boolean
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            //公式
            case FORMULA:
                // 判断当前的cell是否为Date
                if (DateUtil.isCellDateFormatted(cell)) {
                    /* 如果是Date类型则转化为Data格式

                    方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    cellValue = cell.getDateCellValue().toLocaleString();*/

                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    cellValue = Utility.convertToString(date, "yyyy-MM-dd");
                } else {
                    // 如果是纯数字
                    // 取得当前Cell的数值
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            //空值
            case BLANK:
                cellValue = "";
                break;
            //故障
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


}
