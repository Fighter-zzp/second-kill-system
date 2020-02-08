package com.zzp.second.kill.commons.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author 佐斯特勒
 * <p>
 * Excel的工具类
 * </p>
 * @version v1.0.0
 * @date 2020/2/8 11:41
 * @see ExcelUtils
 **/
public class ExcelUtils {
    /**
     * Excel监听器获取
     * @param consumer 需要数据的数据集合
     * @param threshold 阈值
     * @param <T> 泛型
     * @return 监听器
     */
    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer, int threshold) {
        return new AnalysisEventListener<T>() {
            private LinkedList<T> linkedList = new LinkedList<>();

            @Override
            public void invoke(T data, AnalysisContext context) {
                linkedList.add(data);
                if (linkedList.size() == threshold) {
                    consumer.accept(linkedList);
                    linkedList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                if (linkedList.size() > 0) {
                    consumer.accept(linkedList);
                }
            }

            @Override
            public void onException(Exception exception, AnalysisContext context) throws Exception {
                exception.printStackTrace();
            }
        };
    }

    /**
     * 读取文件，可以设置读取文件时的的操作
     * @param consumer 用来对文件读取时的逻辑处理
     * @param in 输入流
     * @param clazz 反射对象
     * @param headNum 行头
     */
    public static void readFile(Consumer<List<T>> consumer,InputStream in,Class<T> clazz,int headNum){
        ExcelReader excelReader = EasyExcel.read(in, clazz, getListener(consumer,10)).build();
        var readSheet = EasyExcel.readSheet(0).headRowNumber(headNum).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }

    /**
     * 读取文件，可以设置读取文件时的的操作 行头默认1
     * @param consumer 用来对文件读取时的逻辑处理
     * @param in 输入流
     * @param clazz 反射对象
     */
    public static void readFile(Consumer<List<T>> consumer,InputStream in,Class<?> clazz){
        ExcelReader excelReader = EasyExcel.read(in, clazz, getListener(consumer,10)).build();
        var readSheet = EasyExcel.readSheet(0).build();
        excelReader.read(readSheet);
        // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
    }

    /**
     * 按模板写出数据
     * @param out 数据流
     * @param clazz 反射对象
     * @param data 数据列
     * @throws Exception 异常处理
     */
    public static void writeFile(OutputStream out,Class<?> clazz,List<?> data) throws Exception{
        EasyExcel.write(out, clazz).autoCloseStream(Boolean.FALSE).sheet("模板")
                .doWrite(data);
    }
}
