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
import com.lidennis.plat.config.model.ConfigRptShowType;
import com.lidennis.plat.config.web.form.ConfigRptShowTypeForm;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.service.IDataDetectionProcess;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigRptShowTypeAction extends AbstractBaseAction implements ModelDriven<ConfigRptShowTypeForm>
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigRptShowTypeAction.class );
    private ConfigRptShowTypeForm form = new ConfigRptShowTypeForm();
    protected ICacheService configRptShowTypeService;
    protected ICacheService configDictDetailService;
    protected IDataDetectionProcess dataDetectionProcess;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
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
                    this.checkUserOperatePower( ConfigRptShowTypeAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configRptShowTypeService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configRptShowTypeService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configRptShowTypeService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configRptShowTypeService.findByParams( ucontext, paramMap, ordering );
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
        this.checkUserOperatePower( ConfigRptShowTypeAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigRptShowType dataObj = configRptShowTypeService.findById( ucontext, form.getItemId() );
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
                ConfigRptShowType dataObj = (ConfigRptShowType)dataList.get(i);
                this.matchObjRelateData( ucontext, dataObj );
            }
        }
    }
    
    private void matchObjRelateData( UserContext ucontext, ConfigRptShowType dataObj )
    {
        if( dataObj != null )
        {
            
        }
    }
    
    public ConfigRptShowTypeForm getModel()
    {
        return form;
    }
    public void setConfigRptShowTypeService(ICacheService configRptShowTypeService)
    {
        this.configRptShowTypeService = configRptShowTypeService;
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
