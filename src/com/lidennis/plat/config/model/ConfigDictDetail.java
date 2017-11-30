package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractLevelModel;

public class ConfigDictDetail extends AbstractLevelModel
{
    private static final long serialVersionUID = 1L;
    
    protected Long    dictTypeId;
    protected String  dictTypeCode;
    protected String  dictTypeName;
    protected String  bizCode;              
    protected String  itemValue;
    protected String  exval01;              
    protected String  exval02;              
    protected String  exval03;              
    protected String  exval04;
    protected String  exval05;
    protected String  startValue1;          
    protected String  endValue1;
    protected String  itemParam;            
    protected String  indexInfo;            
    
    public Long getDictTypeId()
    {
        return dictTypeId;
    }
    public void setDictTypeId(Long dictTypeId)
    {
        this.dictTypeId = dictTypeId;
    }
    public String getDictTypeCode()
    {
        return dictTypeCode;
    }
    public void setDictTypeCode(String dictTypeCode)
    {
        this.dictTypeCode = dictTypeCode;
    }
    public String getDictTypeName()
    {
        return dictTypeName;
    }
    public void setDictTypeName(String dictTypeName)
    {
        this.dictTypeName = dictTypeName;
    }
    public String getItemValue()
    {
        return itemValue;
    }
    public void setItemValue(String itemValue)
    {
        this.itemValue = itemValue;
    }
    public String getBizCode()
    {
        return bizCode;
    }
    public void setBizCode(String bizCode)
    {
        this.bizCode = bizCode;
    }
    public String getExval01()
    {
        return exval01;
    }
    public void setExval01(String exval01)
    {
        this.exval01 = exval01;
    }
    public String getExval02()
    {
        return exval02;
    }
    public void setExval02(String exval02)
    {
        this.exval02 = exval02;
    }
    public String getExval03()
    {
        return exval03;
    }
    public void setExval03(String exval03)
    {
        this.exval03 = exval03;
    }
    public String getExval04()
    {
        return exval04;
    }
    public void setExval04(String exval04)
    {
        this.exval04 = exval04;
    }
    public String getExval05()
    {
        return exval05;
    }
    public void setExval05(String exval05)
    {
        this.exval05 = exval05;
    }
    public String getStartValue1()
    {
        return startValue1;
    }
    public void setStartValue1(String startValue1)
    {
        this.startValue1 = startValue1;
    }
    public String getEndValue1()
    {
        return endValue1;
    }
    public void setEndValue1(String endValue1)
    {
        this.endValue1 = endValue1;
    }
    public String getIndexInfo()
    {
        return indexInfo;
    }
    public void setIndexInfo(String indexInfo)
    {
        this.indexInfo = indexInfo;
    }
    public String getItemParam()
    {
        return itemParam;
    }
    public void setItemParam(String itemParam)
    {
        this.itemParam = itemParam;
    }
    
}
