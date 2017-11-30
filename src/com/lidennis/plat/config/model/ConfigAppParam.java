package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigAppParam extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected String  appMode;         
    protected String  bizCode;
    protected String  itemType;        
    protected String  itemValue;       
    protected String  splitSymbol;
    
    
    public String getAppMode()
    {
        return appMode;
    }
    public void setAppMode(String appMode)
    {
        this.appMode = appMode;
    }
    public String getBizCode()
    {
        return bizCode;
    }
    public void setBizCode(String bizCode)
    {
        this.bizCode = bizCode;
    }
    public String getItemType()
    {
        return itemType;
    }
    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }
    public String getItemValue()
    {
        return itemValue;
    }
    public void setItemValue(String itemValue)
    {
        this.itemValue = itemValue;
    }
    public String getSplitSymbol()
    {
        return splitSymbol;
    }
    public void setSplitSymbol(String splitSymbol)
    {
        this.splitSymbol = splitSymbol;
    }
    
}
