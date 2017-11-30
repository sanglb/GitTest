package com.lidennis.plat.config.web.action;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

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
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigCronJob;
import com.lidennis.plat.config.service.IConfigCronJobService;
import com.lidennis.plat.config.web.form.ConfigCronJobForm;
import com.lidennis.plat.common.util.DataOperateUtils;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.lidennis.plat.system.constant.SystemConstants;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigCronJobAction extends AbstractBaseAction implements ModelDriven<ConfigCronJobForm>
{
    private static final long serialVersionUID = 3723115734927657381L;
    private static final Logger log = Logger.getLogger( ConfigCronJobAction.class );
    private ConfigCronJobForm form = new ConfigCronJobForm();
    protected IConfigCronJobService configCronJobService;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( StringUtils.checkNotEmpty( form.getProcCode() ) )
            paramMap.put( "procCode", "%" + form.getProcCode() + "%" );
        if( StringUtils.checkNotEmpty( form.getProcStatus() ) )
            paramMap.put( "procStatus", form.getProcStatus() );
        if( StringUtils.checkNotEmpty( form.getProcParam() ) )
            paramMap.put( "procParam", "%" + form.getProcParam() + "%" );
        if( StringUtils.checkNotEmpty( form.getTriggerStatus() ) )
            paramMap.put( "triggerStatus", form.getTriggerStatus() );
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
                    this.checkUserOperatePower( ConfigCronJobAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configCronJobService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configCronJobService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                    if( start == 0 )
                    {
                        if( !this.checkFindOperateRecord( ConfigConstants.MODULE_NAME_CRON_JOB ) )
                            this.registerLog( "查询定时任务信息, 总数: "+totalRows, "Query timing task, total: "+totalRows, LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_CRON_JOB, OperateTypeEnum.QUERY, OperateFlagEnum.SUCCESS, resultList.size(), form.toString() ); 
                    }
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configCronJobService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configCronJobService.findByParams( ucontext, paramMap, ordering );
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
        this.checkUserOperatePower( ConfigCronJobAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigCronJob dataObj = configCronJobService.findById( ucontext, form.getItemId() );
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
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigCronJob.class, form );
        if( ucontext != null && baseModel != null )
        {
            try
            {
                ConfigCronJob dataObj = (ConfigCronJob)baseModel;
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                {
                    if( dataObj.getItemCode() != null && !dataObj.getItemCode().trim().equals("") )
                        form.setItemCode( dataObj.getItemCode() );
                    if( dataObj.getUuidCode() != null && !dataObj.getUuidCode().trim().equals("") )
                        form.setUuidCode( dataObj.getUuidCode() );
                    form.setExecCount( dataObj.getExecCount() );
                    form.setExecStatus( dataObj.getExecStatus() );
                    form.setStartExecTime( dataObj.getStartExecTime() );
                    form.setEndExecTime( dataObj.getEndExecTime() );
                    form.setLastExecTime( dataObj.getLastExecTime() );
                    form.setNextExecTime( dataObj.getNextExecTime() );
                    form.setTriggerStatus( dataObj.getTriggerStatus() );
                    form.setProcStatus( dataObj.getProcStatus() );
                    form.setErrorLastTime( dataObj.getErrorLastTime() );
                    form.setErrorLogTime( dataObj.getErrorLogTime() );
                    form.setErrorCount( dataObj.getErrorCount() );
                    form.setRunLogTime( dataObj.getRunLogTime() );
                    BeanUtils.copyProperties( dataObj, form );
                    returnMsg = configCronJobService.update( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                        this.registerLog( "定时任务["+dataObj.getItemName()+"]修改成功", "Timing task["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_CRON_JOB, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                }
                else
                {
                    dataObj.setUuidCode( DataOperateUtils.buildUuidCodeStr( true ) );
                    dataObj.setExecCount( 0 );
                    dataObj.setErrorCount( 0 );
                    dataObj.setProcStatus( ConfigConstants.PROC_RUN_STATUS_INIT );
                    returnMsg = configCronJobService.insert( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                        this.registerLog( "定时任务["+dataObj.getItemName()+"]新增成功", "Timing task["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_CRON_JOB, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
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
        this.checkUserOperatePower( ConfigCronJobAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configCronJobService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    this.registerLog( "定时任务删除成功, 数量: "+returnMsg.getResultCount(), "Timing task delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_CRON_JOB, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() );
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
    
    public String jobStartup() throws Exception
    {
        UserContext ucontext = this.findUserContext();
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        try
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                ConfigCronJob dataObj = configCronJobService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    returnMsg = configCronJobService.execJobRequest( ucontext, dataObj, ConfigConstants.JOB_EXEC_OPERATE_RUN );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "定时任务实例启动成功("+dataObj.getItemName()+")", "Timing task instance startup success("+dataObj.getItemName()+")", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_CRON_JOB, OperateTypeEnum.STARTUP, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName()+", response: "+returnMsg.getResultDesc() );
                    }
                }
            }
        }
        catch( Exception err )
        {
            returnMsg.setSuccess( false );
            returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
            returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
            log.error( "jobStartup() error, "+err.getMessage() );
        }
        return SUCCESS;
    }
    
    public String jobStop() throws Exception
    {
        UserContext ucontext = this.findUserContext();
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        try
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                ConfigCronJob dataObj = configCronJobService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    returnMsg = configCronJobService.execJobRequest( ucontext, dataObj, ConfigConstants.JOB_EXEC_OPERATE_STOP );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "定时任务实例停止成功("+dataObj.getItemName()+")", "Timing task instance stop success("+dataObj.getItemName()+")", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_CRON_JOB, OperateTypeEnum.STOP, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName()+", response: "+returnMsg.getResultDesc() );
                    }
                }
            }
        }
        catch( Exception err )
        {
            returnMsg.setSuccess( false );
            returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
            returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
            log.error( "jobStop() error, "+err.getMessage() );
        }
        return SUCCESS;
    }
    
    public String jobDelete() throws Exception
    {
        UserContext ucontext = this.findUserContext();
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        try
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                ConfigCronJob dataObj = configCronJobService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    returnMsg = configCronJobService.execJobRequest( ucontext, dataObj, ConfigConstants.JOB_EXEC_OPERATE_DELETE );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "定时任务实例删除成功("+dataObj.getItemName()+")", "Timing task instance delete success("+dataObj.getItemName()+")", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_CRON_JOB, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName()+", response: "+returnMsg.getResultDesc() );
                    }
                }
            }
        }
        catch( Exception err )
        {
            returnMsg.setSuccess( false );
            returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
            returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
            log.error( "jobDelete() error, "+err.getMessage() );
        }
        return SUCCESS;
    }
    
    public ConfigCronJobForm getModel()
    {
        return form;
    }
    public void setConfigCronJobService(IConfigCronJobService configCronJobService)
    {
        this.configCronJobService = configCronJobService;
    }
    
}
