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
import com.lidennis.plat.common.service.IBaseService;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigProcMapping;
import com.lidennis.plat.config.web.form.ConfigProcMappingForm;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.lidennis.plat.system.constant.SystemConstants;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigProcMappingAction extends AbstractBaseAction implements ModelDriven<ConfigProcMappingForm>
{
    private static final long serialVersionUID = -246674422934611243L;
    private static final Logger log = Logger.getLogger( ConfigProcMappingAction.class );
    private ConfigProcMappingForm form = new ConfigProcMappingForm();
    protected IBaseService configProcMappingService;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( form.getTypeCode() != null && !form.getTypeCode().trim().equals("") )
            paramMap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, form.getTypeCode().trim() );
        if( form.getBeanName() != null && !form.getBeanName().trim().equals("") )
            paramMap.put( "beanName", form.getBeanName().trim() );
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
                OrderByModel ordering = new OrderByModel( "ORDERNUM" );
                Map<String,Object> paramMap = this.findRequestParamsMap();
                if( limit > 0 )
                {
                    this.checkUserOperatePower( ConfigProcMappingAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configProcMappingService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configProcMappingService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configProcMappingService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configProcMappingService.findByParams( ucontext, paramMap, ordering );
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
        this.checkUserOperatePower( ConfigProcMappingAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigProcMapping dataObj = configProcMappingService.findById( ucontext, form.getItemId() );
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
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigProcMapping.class, form );
        if( ucontext != null && baseModel != null )
        {
            try
            {
                ConfigProcMapping dataObj = (ConfigProcMapping)baseModel;
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                {
                    form.setItemCode( dataObj.getItemCode() );
                    form.setTypeCode( dataObj.getTypeCode() );
                    BeanUtils.copyProperties( dataObj, form );
                    returnMsg = configProcMappingService.update( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                        this.registerLog( "处理映射["+dataObj.getItemName()+"]修改成功", "Process mapping["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_PROC_MAPPING, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                }
                else
                {
                    returnMsg = configProcMappingService.insert( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                        this.registerLog( "处理映射["+dataObj.getItemName()+"]新增成功", "Process mapping["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_PROC_MAPPING, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
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
        this.checkUserOperatePower( ConfigProcMappingAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configProcMappingService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    this.registerLog( "处理映射删除成功, 数量: "+returnMsg.getResultCount(), "Process mapping delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_PROC_MAPPING, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() );
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

    public ConfigProcMappingForm getModel()
    {
        return form;
    }
    public void setConfigProcMappingService(IBaseService configProcMappingService)
    {
        this.configProcMappingService = configProcMappingService;
    }
}
