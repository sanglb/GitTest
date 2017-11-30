package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigReportsItem extends AbstractBaseModel
{
    private static final long serialVersionUID = 1L;
    
    protected Long    reportsId;            
    protected String  reportsName;
    protected String  bizMode;              
    protected String  typeCode;             
    protected String  rptTypeCode;          
    protected String  rptTypeName;
    protected String  rptShowCode;          
    protected String  rptShowName;
    
    protected String  uuidCode;
    protected String  itemParam;
    protected String  formCond;             
    protected String  queryCond;            
    
    protected Integer fillRowFlag;          
    protected Integer showColIndex;         
    protected Integer showRowIndex;         
    protected Integer showWinWidth;         
    protected Integer showWinHeight;
    protected Integer showChartWidth;       
    protected Integer showChartHeight;
    
    protected Integer exint01;
    protected Integer exint02;
    protected Long    exlong01;
    protected Long    exlong02;
    
    
    public Long getReportsId()
    {
        return reportsId;
    }
    public void setReportsId(Long reportsId)
    {
        this.reportsId = reportsId;
    }
    public String getReportsName()
    {
        return reportsName;
    }
    public void setReportsName(String reportsName)
    {
        this.reportsName = reportsName;
    }
    public String getBizMode()
    {
        return bizMode;
    }
    public void setBizMode(String bizMode)
    {
        this.bizMode = bizMode;
    }
    public String getTypeCode()
    {
        return typeCode;
    }
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }
    public String getRptTypeCode()
    {
        return rptTypeCode;
    }
    public void setRptTypeCode(String rptTypeCode)
    {
        this.rptTypeCode = rptTypeCode;
    }
    public String getRptTypeName()
    {
        return rptTypeName;
    }
    public void setRptTypeName(String rptTypeName)
    {
        this.rptTypeName = rptTypeName;
    }
    public String getRptShowCode()
    {
        return rptShowCode;
    }
    public void setRptShowCode(String rptShowCode)
    {
        this.rptShowCode = rptShowCode;
    }
    public String getRptShowName()
    {
        return rptShowName;
    }
    public void setRptShowName(String rptShowName)
    {
        this.rptShowName = rptShowName;
    }
    public String getItemParam()
    {
        return itemParam;
    }
    public void setItemParam(String itemParam)
    {
        this.itemParam = itemParam;
    }
    public String getUuidCode()
    {
        return uuidCode;
    }
    public void setUuidCode(String uuidCode)
    {
        this.uuidCode = uuidCode;
    }
    public String getFormCond()
    {
        return formCond;
    }
    public void setFormCond(String formCond)
    {
        this.formCond = formCond;
    }
    public String getQueryCond()
    {
        return queryCond;
    }
    public void setQueryCond(String queryCond)
    {
        this.queryCond = queryCond;
    }
    public Integer getFillRowFlag()
    {
        return fillRowFlag;
    }
    public void setFillRowFlag(Integer fillRowFlag)
    {
        this.fillRowFlag = fillRowFlag;
    }
    public Integer getShowColIndex()
    {
        return showColIndex;
    }
    public void setShowColIndex(Integer showColIndex)
    {
        this.showColIndex = showColIndex;
    }
    public Integer getShowRowIndex()
    {
        return showRowIndex;
    }
    public void setShowRowIndex(Integer showRowIndex)
    {
        this.showRowIndex = showRowIndex;
    }
    public Integer getShowWinWidth()
    {
        return showWinWidth;
    }
    public void setShowWinWidth(Integer showWinWidth)
    {
        this.showWinWidth = showWinWidth;
    }
    public Integer getShowWinHeight()
    {
        return showWinHeight;
    }
    public void setShowWinHeight(Integer showWinHeight)
    {
        this.showWinHeight = showWinHeight;
    }
    public Integer getShowChartWidth()
    {
        return showChartWidth;
    }
    public void setShowChartWidth(Integer showChartWidth)
    {
        this.showChartWidth = showChartWidth;
    }
    public Integer getShowChartHeight()
    {
        return showChartHeight;
    }
    public void setShowChartHeight(Integer showChartHeight)
    {
        this.showChartHeight = showChartHeight;
    }
    public Integer getExint01()
    {
        return exint01;
    }
    public void setExint01(Integer exint01)
    {
        this.exint01 = exint01;
    }
    public Integer getExint02()
    {
        return exint02;
    }
    public void setExint02(Integer exint02)
    {
        this.exint02 = exint02;
    }
    public Long getExlong01()
    {
        return exlong01;
    }
    public void setExlong01(Long exlong01)
    {
        this.exlong01 = exlong01;
    }
    public Long getExlong02()
    {
        return exlong02;
    }
    public void setExlong02(Long exlong02)
    {
        this.exlong02 = exlong02;
    }
}
