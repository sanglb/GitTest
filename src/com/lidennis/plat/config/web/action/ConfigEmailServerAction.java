package com.lidennis.plat.config.web.action;
import java.util.ArrayList;
import java.util.List;
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
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.IBaseService;
import com.lidennis.plat.common.util.DataSecurityUtils;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigEmailServer;
import com.lidennis.plat.config.web.form.ConfigEmailServerForm;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.service.IDataDetectionProcess;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigEmailServerAction extends AbstractBaseAction implements ModelDriven<ConfigEmailServerForm>
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigEmailServerAction.class );
    private ConfigEmailServerForm form = new ConfigEmailServerForm();
    protected IBaseService configEmailServerService;
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
                OrderByModel ordering = new OrderByModel( "ORDERNUM" );
                Map<String,Object> paramMap = this.findRequestParamsMap();
                if( limit > 0 )
                {
                    this.checkUserOperatePower( ConfigEmailServerAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configEmailServerService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configEmailServerService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                    if( start == 0 )
                    {
                        if( !this.checkFindOperateRecord( ConfigConstants.MODULE_NAME_EMAIL_SERVER ) )
                            this.registerLog( "查询邮件服务器信息, 总数: "+totalRows, "Query E-mail server info, total: "+totalRows, LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_EMAIL_SERVER, OperateTypeEnum.QUERY, OperateFlagEnum.SUCCESS, resultList.size(), form.toString() );
                    }
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configEmailServerService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configEmailServerService.findByParams( ucontext, paramMap, ordering );
                    if( resultList == null )
                        resultList = new ArrayList();
                }
                
                matchObjRelateData( ucontext, resultList );
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
        this.checkUserOperatePower( ConfigEmailServerAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigEmailServer dobj = configEmailServerService.findById( ucontext, form.getItemId() );
                if( dobj != null )
                {
                    if( DataTypeUtils.checkEquals( ucontext.getCzId(), dobj.getCzId() ) )
                    {
                        matchObjRelateData( ucontext, dobj );
                        resultList.add( dobj );
                    }
                }
            }
        }
        return SUCCESS;
    }
    
    public String save() throws Exception
    { 
        UserContext ucontext = this.findUserContext();
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigEmailServer.class, form );
        if( ucontext != null && baseModel != null )
        {
            try
            {
                ConfigEmailServer dobj = (ConfigEmailServer)baseModel;
                if( dobj.getItemId() != null && dobj.getItemId().longValue() > 0 )
                {
                    if( dobj.getItemCode() != null && !dobj.getItemCode().trim().equals("") )
                        form.setItemCode( dobj.getItemCode() );
                    form.setStatDateStr( dobj.getStatDateStr() );
                    form.setStatCount( dobj.getStatCount() );
                    BeanUtils.copyProperties( dobj, form );
                    dobj.setItemPwd( DataSecurityUtils.encodeByChars( form.getItemPwd(), ConfigEmailServer.class ) );
                    returnMsg = configEmailServerService.update( ucontext, dobj, null );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "邮件服务器["+dobj.getItemName()+"]修改成功", "E-mail server["+dobj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_EMAIL_SERVER, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dobj.getItemId()+", name: "+dobj.getItemName() );
                        try {
                            dataDetectionProcess.updateNotify( ucontext, ConfigEmailServer.class.getSimpleName(), "configEmailServerService" );
                        }
                        catch( Exception e ){
                        }
                    }
                }
                else
                {
                    dobj.setItemPwd( DataSecurityUtils.encodeByChars( form.getItemPwd(), ConfigEmailServer.class ) );
                    returnMsg = configEmailServerService.insert( ucontext, dobj, null );
                    if( returnMsg.isSuccess() )
                    {
                        this.registerLog( "邮件服务器["+dobj.getItemName()+"]新增成功", "E-mail server["+dobj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_EMAIL_SERVER, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dobj.getItemId()+", name: "+dobj.getItemName() );
                        try {
                            dataDetectionProcess.updateNotify( ucontext, ConfigEmailServer.class.getSimpleName(), "configEmailServerService" );
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
        this.checkUserOperatePower( ConfigEmailServerAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configEmailServerService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    this.registerLog( "邮件服务器删除成功, 数量: "+returnMsg.getResultCount(), "E-mail server delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_EMAIL_SERVER, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() ); 
                    try {
                        dataDetectionProcess.updateNotify( ucontext, ConfigEmailServer.class.getSimpleName(), "configEmailServerService" );
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
    
    public String connTest()
    {
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        UserContext ucontext = this.findUserContext();
        String requestIp = this.findRequestIpAddr();
        if( form.getItemId() != null && form.getItemId() > 0 && ucontext != null )
        {
            ConfigEmailServer dobj = configEmailServerService.findById( ucontext, form.getItemId() );
            try
            {
                returnMsg = configEmailServerService.execExtend( ucontext, dobj, null );
                log.info( "connTest, requestIp: "+requestIp + ", operator: "+ucontext.getUserAcct()+", code: "+dobj.getItemCode()+", returnMsg: "+returnMsg.getEnMessage() );
            }
            catch( Exception err )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                log.error( "connTest() error, requestIp: " + requestIp + ", operator: " + ucontext.getUserAcct() + ", code: " + dobj.getItemCode() + ", " + err.toString() );
            }
        }
        return SUCCESS;
    }
    
    @SuppressWarnings("unchecked")
    private void matchObjRelateData( UserContext ucontext, List dataList )
    {
        if( dataList != null && dataList.size() > 0 )
        {
            for( int i=0; i<dataList.size(); i++ )
            {
                ConfigEmailServer dobj = (ConfigEmailServer)dataList.get(i);
                this.matchObjRelateData( ucontext, dobj );
            }
        }
    }
    
    private void matchObjRelateData( UserContext ucontext, ConfigEmailServer dobj )
    {
        if( dobj != null )
        {
            dobj.setItemPwd( DataSecurityUtils.decodeByChars( dobj.getItemPwd(), ConfigEmailServer.class ) );
        }
    }
    
    public ConfigEmailServerForm getModel()
    {
        return form;
    }
    public void setConfigEmailServerService(IBaseService configEmailServerService)
    {
        this.configEmailServerService = configEmailServerService;
    }
    public void setDataDetectionProcess(IDataDetectionProcess dataDetectionProcess)
    {
        this.dataDetectionProcess = dataDetectionProcess;
    }
    
}
