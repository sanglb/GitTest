package com.lidennis.plat.config.web.action;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.constant.LogLevelEnum;
import com.lidennis.plat.common.constant.LogTypeEnum;
import com.lidennis.plat.common.constant.OperateFlagEnum;
import com.lidennis.plat.common.constant.OperateTypeEnum;
import com.lidennis.plat.common.model.AbstractBaseModel;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.ICacheService;
import com.lidennis.plat.common.util.DataOperateUtils;
import com.lidennis.plat.common.util.DataTypeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigReportsItem;
import com.lidennis.plat.config.service.IConfigReportsItemService;
import com.lidennis.plat.config.web.form.ConfigReportsItemForm;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.service.IDataExportProcess;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigReportsItemAction extends AbstractBaseAction implements ModelDriven<ConfigReportsItemForm>
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigReportsItemAction.class );
    private ConfigReportsItemForm form = new ConfigReportsItemForm();
    protected IConfigReportsItemService configReportsItemService;
    protected ICacheService configDictDetailService;
    protected IDataExportProcess dataExportProcess;
    protected InputStream excelStream;
    protected String  exportFileName;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( form.getReportsId() != null )
            paramMap.put( "reportsId", form.getReportsId() );
        if( StringUtils.checkNotEmpty( form.getBizMode() ) )
            paramMap.put( "bizMode", form.getBizMode().trim() );
        if( StringUtils.checkNotEmpty( form.getTypeCode() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, form.getTypeCode().trim() );
        if( StringUtils.checkNotEmpty( form.getUuidCode() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_UUID, form.getUuidCode().trim() );
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
                    this.checkUserOperatePower( ConfigReportsItemAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configReportsItemService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configReportsItemService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                    if( start == 0 )
                    {
                        if( !this.checkFindOperateRecord( ConfigConstants.MODULE_NAME_REPORTS_ITEM ) )
                            this.registerLog( "查询报表实例信息, 总数: "+totalRows, "Query report instance info, total: "+totalRows, LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_ITEM, OperateTypeEnum.QUERY, OperateFlagEnum.SUCCESS, resultList.size(), form.toString() );
                    }
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configReportsItemService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configReportsItemService.findByParams( ucontext, paramMap, ordering );
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
        this.checkUserOperatePower( ConfigReportsItemAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigReportsItem dataObj = configReportsItemService.findById( ucontext, form.getItemId() );
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
        UserContext ucontext = this.findUserContext();
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigReportsItem.class, form );
        if( ucontext != null && baseModel != null )
        {
            try
            {
                ConfigReportsItem dataObj = (ConfigReportsItem)baseModel;
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                {
                    if( dataObj.getItemCode() != null && !dataObj.getItemCode().trim().equals("") )
                        form.setItemCode( dataObj.getItemCode() );
                    form.setUuidCode( dataObj.getUuidCode() );
                    form.setReportsId( dataObj.getReportsId() );
                    form.setRptTypeCode( dataObj.getRptTypeCode() );
                    BeanUtils.copyProperties( dataObj, form );
                    returnMsg = configReportsItemService.update( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        operateId = dataObj.getItemId();
                        this.registerLog( "报表实例["+dataObj.getItemName()+"]修改成功", "Report instance["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_ITEM, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                    }
                }
                else
                {
                    dataObj.setUuidCode( DataOperateUtils.buildUuidCodeStr( true ) );
                    returnMsg = configReportsItemService.insert( ucontext, dataObj, null );
                    if( returnMsg.isSuccess() )
                    {
                        operateId = dataObj.getItemId();
                        this.registerLog( "报表实例["+dataObj.getItemName()+"]新增成功", "Report instance["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_ITEM, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
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
        this.checkUserOperatePower( ConfigReportsItemAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configReportsItemService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    this.registerLog( "报表实例删除成功, 数量: "+returnMsg.getResultCount(), "Report instance delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_REPORTS_ITEM, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() );
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
    
    public String export() throws Exception
    {
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        UserContext ucontext = this.findUserContext();
        String requestIp = this.findRequestIpAddr();
        if( ucontext != null && ucontext.getUserId() != null && ucontext.getUserId().longValue() > 0 )
        {
            try
            {
            }
            catch( Exception err )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                log.error( "export() error, "+err.getMessage()+ ", operator: "+ucontext.getUserAcct()+", requestIp: "+requestIp+", "+form.toString(), err );
            }
        }
        
        if( !returnMsg.isSuccess() )
        {
            try
            {
                String message = returnMsg.getMessage();
                if( ucontext != null && StringUtils.checkEquals( Constants.LANG_TYPE_EN, ucontext.getLangType(), false ) )
                    message = returnMsg.getEnMessage(); 
                message = message.replaceAll( "'", " " ).replaceAll( "\"", " " ).replaceAll( "\n", " " );
                HttpServletResponse response = ServletActionContext.getResponse();
                response.setContentType("text/html; charset=GBK");
                PrintWriter out = response.getWriter();
                out.println( "<script language=\"javascript\">" );
                out.println( "alert('"+message+"');" ); 
                out.println( "</script>" );
            }
            catch( Exception e )
            {        
            }
        }
        return NONE;
    }
    
    @SuppressWarnings("unchecked")
    private void matchObjRelateData( UserContext ucontext, List dataList )
    {
        if( dataList != null && dataList.size() > 0 )
        {
            for( int i=0; i<dataList.size(); i++ )
            {
                ConfigReportsItem dataObj = (ConfigReportsItem)dataList.get(i);
                this.matchObjRelateData( ucontext, dataObj );
            }
        }
    }
    
    private void matchObjRelateData( UserContext ucontext, ConfigReportsItem dataObj )
    {
        if( dataObj != null )
        {
            dataObj.setCzCode( "" );
            dataObj.setQueryCond( "" );
        }
    }
    
    
    public ConfigReportsItemForm getModel()
    {
        return form;
    }
    
    
    public void setConfigReportsItemService(IConfigReportsItemService configReportsItemService)
    {
        this.configReportsItemService = configReportsItemService;
    }
    public void setConfigDictDetailService(ICacheService configDictDetailService)
    {
        this.configDictDetailService = configDictDetailService;
    }
    public void setDataExportProcess(IDataExportProcess dataExportProcess)
    {
        this.dataExportProcess = dataExportProcess;
    }
    public InputStream getExcelStream()
    {
        return excelStream;
    }
    public String getExportFileName()
    {
        return exportFileName;
    }
    
}
