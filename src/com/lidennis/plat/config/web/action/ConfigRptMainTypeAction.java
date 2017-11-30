package com.lidennis.plat.config.web.action;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.constant.LogLevelEnum;
import com.lidennis.plat.common.constant.LogTypeEnum;
import com.lidennis.plat.common.constant.OperateFlagEnum;
import com.lidennis.plat.common.constant.OperateTypeEnum;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigDictDetail;
import com.lidennis.plat.config.model.ConfigRptMainType;
import com.lidennis.plat.config.web.form.ConfigRptMainTypeForm;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.service.IDataDetectionProcess;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigRptMainTypeAction extends AbstractBaseAction implements ModelDriven<ConfigRptMainTypeForm>
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigRptMainTypeAction.class );
    private ConfigRptMainTypeForm form = new ConfigRptMainTypeForm();
    protected ICacheService configRptMainTypeService;
    protected ICacheService configDictDetailService;
    protected IDataDetectionProcess dataDetectionProcess;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( form.getClassifyId() != null )
            paramMap.put( "classifyId", form.getClassifyId() );
        if( form.getAppId() != null )
            paramMap.put( "appIdStr", "%,"+form.getAppId()+",%" );
        else if( form.getOperateType() == null || !form.getOperateType().trim().equals("manage") )
            paramMap.put( "appIdStr", ",-1," );
        if( StringUtils.checkNotEmpty( form.getBizMode() ) )
            paramMap.put( "bizMode", form.getBizMode().trim() );
        if( StringUtils.checkNotEmpty( form.getStatType() ) )
            paramMap.put( "statType", form.getStatType().trim() );
        if( StringUtils.checkNotEmpty( form.getTypeCode() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, form.getTypeCode().trim() );
        return paramMap;
    }
    
    @SuppressWarnings("unchecked")
    public String findmain()
    {
        try
        {
            UserContext ucontext = this.findUserContext();
            if( ucontext != null )
            {
                OrderByModel ordering = this.findRequestOrderByModel( "" );
                Map<String,Object> paramMap = this.findRequestParamsMap();
                if( limit > 0 )
                {
                    this.checkUserOperatePower( ConfigRptMainTypeAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configRptMainTypeService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configRptMainTypeService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configRptMainTypeService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configRptMainTypeService.findByParams( ucontext, paramMap, ordering );
                    if( resultList == null )
                        resultList = new ArrayList();
                }
                
                this.matchObjRelateData( ucontext, resultList );
                if( start <= 0 && SystemConstants.LOG_SWITCH_SEARCH_DEBUG_ON )
                {
                    if( StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                        log.info( "--> find result number: " + resultList.size() + ", start: " + start + ", limit: " + limit + ", totalRows: " + totalRows + ", req ip: " + this.findRequestIpAddr() + ", params: " + JSONObject.fromObject( paramMap ).toString() );
                    else
                        log.info( "--> find result number: " + resultList.size() + ", by: " + ucontext.getUserAcct() + ", " + ucontext.getUserInfo() + ", start: " + start + ", limit: " + limit + ", totalRows: " + totalRows  + ", req ip: " + this.findRequestIpAddr() + ", params: " + JSONObject.fromObject( paramMap ).toString() );
                }
            }
            
        }
        catch( Exception err )
        {
            log.error( "findmain() error, " + err.toString() + ", " + form.toString() );
        }
        
        return SUCCESS;
    }
    
    @SuppressWarnings("unchecked")
    public String findById()
    {
        resultList = new ArrayList();
        this.checkUserOperatePower( ConfigRptMainTypeAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigRptMainType dataObj = configRptMainTypeService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    if( StringUtils.checkEquals( dataObj.getCzCode(), ucontext.getCzCode() ) || DataTypeUtils.checkEquals( ucontext.getCzId(), dataObj.getCzId() ) )
                    {
                        this.matchObjRelateData( ucontext, dataObj );
                        resultList.add( dataObj );
                    }
                }
            }
        }
        return SUCCESS;
    }
    
    public String save() throws Exception
    { 
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        return SUCCESS;
    }
    
    public String delete() throws Exception
    {
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        return SUCCESS;
    }
    
    @SuppressWarnings("unchecked")
    private void matchObjRelateData( UserContext ucontext, List dataList )
    {
        if( dataList != null && dataList.size() > 0 )
        {
            for( int i=0; i<dataList.size(); i++ )
            {
                ConfigRptMainType dataObj = (ConfigRptMainType)dataList.get(i);
                this.matchObjRelateData( ucontext, dataObj );
            }
        }
    }
    
    private void matchObjRelateData( UserContext ucontext, ConfigRptMainType dataObj )
    {
        if( dataObj != null )
        {
            dataObj.setCzCode( "" );
            if( dataObj.getClassifyId() != null )
            {
                ConfigDictDetail dictObj = configDictDetailService.findCacheById( ucontext, dataObj.getClassifyId() ); 
                if( dictObj != null )
                {
                    dataObj.setClassifyName( dictObj.getItemName() );
                }
            }
        }
    }
    
    public ConfigRptMainTypeForm getModel()
    {
        return form;
    }
    public void setConfigRptMainTypeService(ICacheService configRptMainTypeService)
    {
        this.configRptMainTypeService = configRptMainTypeService;
    }
    public void setConfigDictDetailService(ICacheService configDictDetailService)
    {
        this.configDictDetailService = configDictDetailService;
    }
    public void setDataDetectionProcess(IDataDetectionProcess dataDetectionProcess)
    {
        this.dataDetectionProcess = dataDetectionProcess;
    }
    
}
