package com.zzerp.core;

public interface IOperationLogger
{

    public static final int LOG_LEVEL_SEARCH = 0;
    public static final int LOG_LEVEL_UPDATE = 1;
    public static final int LOG_LEVEL_ADD    = 2;
    public static final int LOG_LEVEL_REMOVE = 3;

    /**
     * 记录"新增"操作 日志级别1
     * 
     * @param category
     */
    public void tellAdding(String category, String content);

    public void tellAdding(String category, String content, String memo);

    /**
     * 记录"查询"操作 日志级别0
     * 
     * @param category
     */
    public void tellSearching(String category, String content);

    public void tellSearching(String category, String content, String memo);

    /**
     * 记录"修改"操作 日志级别2
     * 
     * @param category
     */
    public void tellUpdating(String category, String content);

    public void tellUpdating(String category, String content, String memo);

    /**
     * 记录"删除"操作 日志级别3
     * 
     * @param category
     */
    public void tellRemoving(String category, String content);

    public void tellRemoving(String category, String content, String memo);

    /**
     * 可自定义级别的日志 更高级别得操作使用大于3的数值;更低级别的操作使用负数.
     * 
     * @param level
     * @param category
     * @param content
     * @param memo
     */
    public void tell(Integer level, String category, String content);

    public void tell(Integer level, String category, String content, String memo);

    /**
     * 默认级别为0
     * 
     * @param category
     * @param content
     * @param memo
     */
    public void tell(String category, String content);

    public void tell(String category, String content, String memo);

}
