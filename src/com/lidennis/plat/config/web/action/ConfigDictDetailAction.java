package com.lidennis.plat.config.web.action;
import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.constant.LogLevelEnum;
import com.lidennis.plat.common.constant.LogTypeEnum;
import com.lidennis.plat.common.constant.OperateFlagEnum;
import com.lidennis.plat.common.constant.OperateTypeEnum;
import com.lidennis.plat.common.model.AbstractBaseModel;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigDictDetail;
import com.lidennis.plat.config.web.form.ConfigDictDetailForm;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.service.IDataDetectionProcess;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigDictDetailAction extends AbstractBaseAction implements ModelDriven<ConfigDictDetailForm>
{
    private static final long serialVersionUID = 4749135463179976090L;
    private static final Logger log = Logger.getLogger( ConfigDictDetailAction.class );
    private ConfigDictDetailForm form = new ConfigDictDetailForm();
    protected ICacheService configDictDetailService;
    protected IDataDetectionProcess dataDetectionProcess;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( form.getDictTypeId() != null )
            paramMap.put( "dictTypeId", form.getDictTypeId() );
        if( form.getDictTypeCode() != null && !form.getDictTypeCode().trim().equals("") )
            paramMap.put( "dictTypeCode", form.getDictTypeCode() );
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
                OrderByModel ordering = new OrderByModel();
                ordering.add( "DICTTYPECODE" );
                ordering.add( "ORDERNUM" );
                Map<String,Object> paramMap = this.findRequestParamsMap();
                if( limit > 0 )
                {
                    this.checkUserOperatePower( ConfigDictDetailAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configDictDetailService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configDictDetailService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                    if( start == 0 )
                    {
                        if( !this.checkFindOperateRecord( ConfigConstants.MODULE_NAME_DICT_DETAIL ) )
                            this.registerLog( "查询数据字典信息, 总数: "+totalRows, "Query data dictionary info, total: "+totalRows, LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DICT_DETAIL, OperateTypeEnum.QUERY, OperateFlagEnum.SUCCESS, resultList.size(), form.toString() );
                    }
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configDictDetailService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configDictDetailService.findByParams( ucontext, paramMap, ordering );
                    if( resultList == null )
                        resultList = new ArrayList();
                }
                
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
        this.checkUserOperatePower( ConfigDictDetailAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigDictDetail dataObj = configDictDetailService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    if( StringUtils.checkEquals( dataObj.getCzCode(), ucontext.getCzCode() ) || DataTypeUtils.checkEquals( ucontext.getCzId(), dataObj.getCzId() ) )
                    {
                        resultList.add( dataObj );
                    }
                }
            }
        }
        return SUCCESS;
    }
    
    public String save() throws Exception
    {
        UserContext ucontext = this.findUserContext();
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigDictDetail.class, form );
        if( ucontext != null && baseModel != null )
        {
            try
            {
                ConfigDictDetail dataObj = (ConfigDictDetail)baseModel;
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                {
                    form.setDictTypeId( dataObj.getDictTypeId() );
                    form.setDictTypeCode( dataObj.getDictTypeCode() );
                    BeanUtils.copyProperties( dataObj, form );
                    returnMsg = configDictDetailService.update( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "数据字典["+dataObj.getItemName()+"]修改成功", "Data dictionary["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DICT_DETAIL, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                        try {
                            dataDetectionProcess.updateNotify( ucontext, ConfigDictDetail.class.getSimpleName(), "configDictDetailService" );
                        }
                        catch( Exception e ){
                        }
                    }
                }
                else
                {
                    returnMsg = configDictDetailService.insert( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "数据字典["+dataObj.getItemName()+"]新增成功", "Data dictionary["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DICT_DETAIL, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                        try {
                            dataDetectionProcess.updateNotify( ucontext, ConfigDictDetail.class.getSimpleName(), "configDictDetailService" );
                        }
                        catch( Exception e ){
                        }
                    }   
                }
            }
            catch( Exception err )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                log.error( "save() error, " + err.toString() + ", " + form.toString() );
            }
        }
        
        return SUCCESS;
    }
    
    public String delete() throws Exception
    {
        this.checkUserOperatePower( ConfigDictDetailAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configDictDetailService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    this.registerLog( "数据字典删除成功, 数量: "+returnMsg.getResultCount(), "Data dictionary delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DICT_DETAIL, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() );
                    try {
                        dataDetectionProcess.updateNotify( ucontext, ConfigDictDetail.class.getSimpleName(), "configDictDetailService" );
                    }
                    catch( Exception e ){
                    }
                } 
            }
            catch( Exception err )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                log.error( "delete() error, " + err.toString() + ", " + form.toString() );
            }
        }
        return SUCCESS;
    }

    public ConfigDictDetailForm getModel()
    {
        return form;
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
