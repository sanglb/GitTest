package com.lidennis.plat.config.model;
import com.lidennis.plat.common.model.AbstractBaseModel;

public class ConfigRptMainType extends AbstractBaseModel
{
    private static final long serialVersionUID = 3188168710782266200L;
    
    protected Long    classifyId;           
    protected String  classifyName;
    protected String  classifyValue;
    protected String  appIdStr;             
    protected String  appInfo;             
    protected String  bizMode;              
    protected String  ctradeCode;           
    protected String  statType;             
    protected String  typeCode;             
    protected Integer exdbFlag;             
    protected String  dataTempPath;         
    protected String  dataTempName;         
    protected Long    showTempId;           
    protected String  colTypeCode;          
    protected String  colSrcCode;           
    protected String  dataSrcCode;          
    protected String  exprocCode;           
    protected String  exprocParam;          
    protected Integer crossMatchFlag;       
    protected Integer rankingFlag;
    protected String  showTypeStr;          
    protected String  showTypeParam;        
    protected String  condPanelUrl;         
    protected String  condFieldCode;        
    protected String  condFieldStr;         
    protected String  orderByFieldStr;      
    
    
    public Long getClassifyId()
    {
        return classifyId;
    }
    public void setClassifyId(Long classifyId)
    {
        this.classifyId = classifyId;
    }
    public String getClassifyName()
    {
        return classifyName;
    }
    public void setClassifyName(String classifyName)
    {
        this.classifyName = classifyName;
    }
    public String getClassifyValue()
    {
        return classifyValue;
    }
    public void setClassifyValue(String classifyValue)
    {
        this.classifyValue = classifyValue;
    }
    public String getAppIdStr()
    {
        return appIdStr;
    }
    public void setAppIdStr(String appIdStr)
    {
        this.appIdStr = appIdStr;
    }
    public String getAppInfo()
    {
        return appInfo;
    }
    public void setAppInfo(String appInfo)
    {
        this.appInfo = appInfo;
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
    public String getCtradeCode()
    {
        return ctradeCode;
    }
    public void setCtradeCode(String ctradeCode)
    {
        this.ctradeCode = ctradeCode;
    }
    public String getStatType()
    {
        return statType;
    }
    public void setStatType(String statType)
    {
        this.statType = statType;
    }
    public String getDataTempPath()
    {
        return dataTempPath;
    }
    public void setDataTempPath(String dataTempPath)
    {
        this.dataTempPath = dataTempPath;
    }
    public String getDataTempName()
    {
        return dataTempName;
    }
    public void setDataTempName(String dataTempName)
    {
        this.dataTempName = dataTempName;
    }
    public Long getShowTempId()
    {
        return showTempId;
    }
    public void setShowTempId(Long showTempId)
    {
        this.showTempId = showTempId;
    }
    public Integer getExdbFlag()
    {
        return exdbFlag;
    }
    public void setExdbFlag(Integer exdbFlag)
    {
        this.exdbFlag = exdbFlag;
    }
    public String getColTypeCode()
    {
        return colTypeCode;
    }
    public void setColTypeCode(String colTypeCode)
    {
        this.colTypeCode = colTypeCode;
    }
    public String getColSrcCode()
    {
        return colSrcCode;
    }
    public void setColSrcCode(String colSrcCode)
    {
        this.colSrcCode = colSrcCode;
    }
    public String getDataSrcCode()
    {
        return dataSrcCode;
    }
    public void setDataSrcCode(String dataSrcCode)
    {
        this.dataSrcCode = dataSrcCode;
    }
    public String getExprocCode()
    {
        return exprocCode;
    }
    public void setExprocCode(String exprocCode)
    {
        this.exprocCode = exprocCode;
    }
    public String getExprocParam()
    {
        return exprocParam;
    }
    public void setExprocParam(String exprocParam)
    {
        this.exprocParam = exprocParam;
    }
    public String getShowTypeStr()
    {
        return showTypeStr;
    }
    public void setShowTypeStr(String showTypeStr)
    {
        this.showTypeStr = showTypeStr;
    }
    public String getShowTypeParam()
    {
        return showTypeParam;
    }
    public void setShowTypeParam(String showTypeParam)
    {
        this.showTypeParam = showTypeParam;
    }
    public Integer getCrossMatchFlag()
    {
        return crossMatchFlag;
    }
    public void setCrossMatchFlag(Integer crossMatchFlag)
    {
        this.crossMatchFlag = crossMatchFlag;
    }
    public Integer getRankingFlag()
    {
        return rankingFlag;
    }
    public void setRankingFlag(Integer rankingFlag)
    {
        this.rankingFlag = rankingFlag;
    }
    public String getCondPanelUrl()
    {
        return condPanelUrl;
    }
    public void setCondPanelUrl(String condPanelUrl)
    {
        this.condPanelUrl = condPanelUrl;
    }
    public String getCondFieldCode()
    {
        return condFieldCode;
    }
    public void setCondFieldCode(String condFieldCode)
    {
        this.condFieldCode = condFieldCode;
    }
    public String getCondFieldStr()
    {
        return condFieldStr;
    }
    public void setCondFieldStr(String condFieldStr)
    {
        this.condFieldStr = condFieldStr;
    }
    public String getOrderByFieldStr()
    {
        return orderByFieldStr;
    }
    public void setOrderByFieldStr(String orderByFieldStr)
    {
        this.orderByFieldStr = orderByFieldStr;
    }
}
