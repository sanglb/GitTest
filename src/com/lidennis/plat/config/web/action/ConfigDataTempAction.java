package com.lidennis.plat.config.web.action;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lidennis.plat.common.util.FileUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.constant.ConfigConstants;
import com.lidennis.plat.config.model.ConfigDataTemp;
import com.lidennis.plat.config.service.IConfigDataTempService;
import com.lidennis.plat.config.web.form.ConfigDataTempForm;
import com.lidennis.plat.system.constant.SystemConstants;
import com.lidennis.plat.system.service.IDataDetectionProcess;
import com.lidennis.plat.common.web.base.AbstractBaseAction;
import com.opensymphony.xwork2.ModelDriven;

public class ConfigDataTempAction extends AbstractBaseAction implements ModelDriven<ConfigDataTempForm>
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger( ConfigDataTempAction.class );
    private ConfigDataTempForm form = new ConfigDataTempForm();
    protected IConfigDataTempService configDataTempService;
    protected ICacheService configDictDetailService;
    protected IDataDetectionProcess dataDetectionProcess;
    protected String  dataContent;
    protected File    upload01;
    protected String  upload01FileName;
    
    public Map<String,Object> findRequestParamsMap() throws Exception
    {
        Map<String,Object> paramMap = super.findRequestParamsMap( form );
        if( StringUtils.checkNotEmpty( form.getTypeCode() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_TYPE_CODE, form.getTypeCode().trim() );
        if( StringUtils.checkNotEmpty( form.getBizCode() ) )
            paramMap.put( "bizCode", form.getBizCode().trim() );
        if( StringUtils.checkNotEmpty( form.getRelateType() ) )
            paramMap.put( Constants.COMMON_MODEL_CODE_RELATE_TYPE, form.getRelateType().trim() );
        if( form.getRelateId() != null )
            paramMap.put( Constants.COMMON_MODEL_CODE_RELATE_ID, form.getRelateId() );
        if( form.getClfyId() != null )
            paramMap.put( "clfyId", form.getClfyId() );
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
                    this.checkUserOperatePower( ConfigDataTempAction.class, "find" );
                    if( returnMsg.isSuccess() ) 
                    {
                        totalRows = configDataTempService.findCountByParams( ucontext, paramMap );
                        if( totalRows > 0 )
                            resultList = configDataTempService.findByParams( ucontext, start, limit, paramMap, ordering );
                    }
                    if( resultList == null )
                        resultList = new ArrayList();
                    if( start == 0 )
                    {
                        if( !this.checkFindOperateRecord( ConfigConstants.MODULE_NAME_DATA_TEMP ) )
                            this.registerLog( "查询数据模板信息, 总数: "+totalRows, "Query data template, total: "+totalRows, LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DATA_TEMP, OperateTypeEnum.QUERY, OperateFlagEnum.SUCCESS, resultList.size(), form.toString() ); 
                    }
                }
                else
                {
                    if( simpleFindFlag.intValue() == Constants.BOOLEAN_VALUE_TRUE )
                        resultList = configDataTempService.findSimpleByParams( ucontext, paramMap, ordering );
                    else
                        resultList = configDataTempService.findByParams( ucontext, paramMap, ordering );
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
        this.checkUserOperatePower( ConfigDataTempAction.class, "find" );
        if( returnMsg.isSuccess() ) 
        {
            if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                UserContext ucontext = this.findUserContext();
                ConfigDataTemp dataObj = configDataTempService.findById( ucontext, form.getItemId() );
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
    
    @SuppressWarnings("unchecked")
    public String viewContent() throws Exception
    {
        returnMsg = new ReturnMsg( false, Constants.MSG_OPERATE_FAILED, Constants.MSG_OPERATE_FAILED_EN );
        UserContext ucontext = this.findUserContext();
        if( ucontext != null )
        {
            ConfigDataTemp dataObj = null;
            if( form.getUuidCode() != null && !form.getUuidCode().trim().equals("") )
            {
                Map<String, Object> paramMap = new HashMap<String, Object>(); 
                if( !StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                    paramMap.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                paramMap.put( Constants.COMMON_MODEL_CODE_UUID, form.getUuidCode().trim() );
                List<ConfigDataTemp> dataList = configDataTempService.findByParams( ucontext, paramMap, null );
                if( dataList != null && dataList.size() == 1 )
                    dataObj = dataList.get( 0 );
            }
            else if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                dataObj = configDataTempService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    if( !StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                    {
                        if( !DataTypeUtils.checkEquals( ucontext.getCzId(), dataObj.getCzId() ) )
                            dataObj = null;
                    }
                }
            }
            
            if( dataObj != null )
            {
                String fileFullPath = Constants.APP_EXFILE_PATH + dataObj.getTempRelaPath() + dataObj.getUuidCode() + ".ftl";
                File tempFile = new File( fileFullPath );
                if( tempFile.exists() && tempFile.isFile() )
                {
                    InputStream inputStream = null;
                    BufferedReader reader = null;
                    try
                    {
                        String charEncode = Constants.CHAR_ENCODEING_UTF_8;
                        if( dataObj.getCharEncode() != null && !dataObj.getCharEncode().trim().equals("") )
                            charEncode = dataObj.getCharEncode().trim();
                        HttpServletResponse response = ServletActionContext.getResponse();
                        response.setContentType( "text/html; charset="+charEncode );
                        PrintWriter out = response.getWriter();
                        inputStream = new FileInputStream( tempFile );
                        reader = new BufferedReader( new InputStreamReader( inputStream, charEncode ) );
                        String line = reader.readLine();
                        while( line != null )
                        {
                            String outInfo = line;
                            out.write( outInfo );
                            line = reader.readLine();
                        }
                        
                        out.flush();
                        reader.close();
                        inputStream.close();
                        return null;
                    }
                    catch( Exception err )
                    {
                        returnMsg.setSuccess( false );
                        returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                        returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                        try {
                            if( reader != null )
                                reader.close();
                            if( inputStream != null )
                                inputStream.close();
                        }
                        catch( Exception e ) {
                        }
                    }
                }
                else
                {
                    log.error( "viewContent() error, associated template file does not exist, "+fileFullPath );
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "关联的模板文件不存在." );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "associated template file does not exist." );
                }
            }
        }
        
        String responseMsg = "";
        if( ucontext != null && StringUtils.checkEquals( Constants.LANG_TYPE_EN, ucontext.getLangType(), false ) )
            responseMsg = returnMsg.getEnMessage().replaceAll( "'", " " ).replaceAll( "\"", " " ).replaceAll( "\n", " " );
        else
            responseMsg = returnMsg.getMessage().replaceAll( "'", " " ).replaceAll( "\"", " " ).replaceAll( "\n", " " );
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html; charset=utf-8"); 
        PrintWriter out = response.getWriter();
        out.println( "<script language=\"javascript\">" );
        out.println( "alert('"+ responseMsg +"');" );
        out.println( "</script>" );    
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public String findContent() throws Exception
    {
        dataContent = "";
        UserContext ucontext = this.findUserContext();
        if( ucontext != null )
        {
            ConfigDataTemp dataObj = null;
            if( form.getUuidCode() != null && !form.getUuidCode().trim().equals("") )
            {
                Map<String, Object> paramMap = new HashMap<String, Object>(); 
                if( !StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                    paramMap.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                paramMap.put( Constants.COMMON_MODEL_CODE_UUID, form.getUuidCode().trim() );
                List<ConfigDataTemp> dataList = configDataTempService.findByParams( ucontext, paramMap, null );
                if( dataList != null && dataList.size() == 1 )
                    dataObj = dataList.get( 0 );
            }
            else if( form.getItemId() != null && form.getItemId().longValue() > 0 )
            {
                dataObj = configDataTempService.findById( ucontext, form.getItemId() );
                if( dataObj != null )
                {
                    if( !StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                    {
                        if( !DataTypeUtils.checkEquals( ucontext.getCzId(), dataObj.getCzId() ) )
                            dataObj = null;
                    }
                }
            }
            
            if( dataObj != null )
            {
                String fileFullPath = Constants.APP_EXFILE_PATH + dataObj.getTempRelaPath() + dataObj.getUuidCode() + ".ftl";
                File tempFile = new File( fileFullPath );
                if( tempFile.exists() && tempFile.isFile() )
                {
                    BufferedInputStream inputStream = null;
                    ByteArrayOutputStream outputStream = null;
                    try
                    {
                        String charEncode = Constants.CHAR_ENCODEING_UTF_8;
                        if( dataObj.getCharEncode() != null && !dataObj.getCharEncode().trim().equals("") ) 
                            charEncode = dataObj.getCharEncode().trim();
                        inputStream = new BufferedInputStream( new FileInputStream( tempFile ) );  
                        outputStream = new ByteArrayOutputStream(4096);
                        byte[] tmpArray = new byte[4096]; 
                        int size = 0; 
                        while( (size = inputStream.read(tmpArray)) > 0 )
                        {        
                            outputStream.write( tmpArray, 0, size ); 
                        }        
                        inputStream.close();
                        byte[] contentArray = outputStream.toByteArray();
                        dataContent = new String( contentArray, charEncode );
                        outputStream.close();
                    }
                    catch( Exception err )
                    {
                        try {
                            if( inputStream != null )
                                inputStream.close();
                            if( outputStream != null )
                                outputStream.close();
                        }
                        catch( Exception e ) {
                        }
                    }
                }
                else
                {
                    log.error( "findContent() error, associated template file does not exist, "+fileFullPath );
                }
            }
        }
        
        return SUCCESS;
    }
    
    public String uploadSave() throws Exception
    {
        UserContext ucontext = this.findUserContext();
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigDataTemp.class, form );
        if( ucontext != null && baseModel != null )
        {
            File tmpTempFile = null;
            try
            {
                ConfigDataTemp dataObj = (ConfigDataTemp)baseModel;
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                {
                    if( StringUtils.checkNotEmpty( dataObj.getItemCode() ) )
                        form.setItemCode( dataObj.getItemCode() );
                    if( StringUtils.checkNotEmpty( dataObj.getBizCode() ) )
                        form.setBizCode( dataObj.getBizCode() );
                    form.setTypeCode( dataObj.getTypeCode() );
                    form.setUuidCode( dataObj.getUuidCode() );
                    form.setTempMainPath( dataObj.getTempMainPath() );
                    form.setTempRelaPath( dataObj.getTempRelaPath() );
                    form.setTempFileName( dataObj.getTempFileName() );
                    BeanUtils.copyProperties( dataObj, form );
                }
                else
                {
                    if( StringUtils.checkIsEmpty( dataObj.getUuidCode() ) )
                        dataObj.setUuidCode( DataOperateUtils.buildUuidCodeStr( true ) );
                }
                
                boolean bflag = true;        
                if( upload01 != null && StringUtils.checkNotEmpty( upload01FileName ) )
                {
                    tmpTempFile = configDataTempService.buildAndUpdateTmpTempFile( ucontext, dataObj, upload01, upload01FileName ); 
                    if( tmpTempFile != null && tmpTempFile.exists() && tmpTempFile.isFile() ) 
                        bflag = true;
                    else
                        bflag = false;
                }
                if( bflag )
                {
                    if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                    {
                        returnMsg = configDataTempService.update( ucontext, dataObj, null );
                        if( returnMsg.isSuccess() )
                        {
                            this.registerLog( "数据模板["+dataObj.getItemName()+"]修改成功", "Data template["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DATA_TEMP, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName()+", type: "+dataObj.getTypeCode()+", file: "+dataObj.getTempFileName() );
                            try {
                                dataDetectionProcess.updateNotify( ucontext, ConfigDataTemp.class.getSimpleName(), "configDataTempService" );
                            }
                            catch( Exception e ){
                            }
                            if( tmpTempFile != null && tmpTempFile.exists() )
                            {
                                if( !FileUtils.moveFile( tmpTempFile, Constants.APP_EXFILE_PATH+dataObj.getTempRelaPath(), dataObj.getUuidCode()+".ftl" ) )
                                {
                                    log.error( "uploadSave moveFile(...) failed, temp name: " + dataObj.getItemName() + ", file path: " + tmpTempFile.getAbsolutePath() );
                                    returnMsg.setSuccess( false );
                                    returnMsg.setMessage( "数据保存成功但模板文件保存失败, 请重试" );
                                    returnMsg.setEnMessage( "Data saved success but template file save failed, please try again." );
                                }
                                else
                                {
                                    tmpTempFile = null;
                                }
                                tmpTempFile = null;
                            }
                        }
                        else
                        {
                            if( tmpTempFile != null && tmpTempFile.exists() )
                                tmpTempFile.delete();
                        }
                    }
                    else
                    {
                        if( tmpTempFile != null && tmpTempFile.exists() )
                        {
                            returnMsg = configDataTempService.insert( ucontext, dataObj, null );
                            if( returnMsg.isSuccess() )
                            {
                                this.registerLog( "数据模板["+dataObj.getItemName()+"]新增成功", "Data template["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DATA_TEMP, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName()+", type: "+dataObj.getTypeCode()+", file: "+dataObj.getTempFileName() );
                                try {
                                    dataDetectionProcess.updateNotify( ucontext, ConfigDataTemp.class.getSimpleName(), "configDataTempService" );
                                }
                                catch( Exception e ){
                                }
                                if( !FileUtils.moveFile( tmpTempFile, Constants.APP_EXFILE_PATH+dataObj.getTempRelaPath(), dataObj.getUuidCode()+".ftl" ) )
                                {
                                    log.error( "uploadSave moveFile(...) failed, temp name: " + dataObj.getItemName() + ", file path: " + tmpTempFile.getAbsolutePath() );
                                    returnMsg.setSuccess( false );
                                    returnMsg.setMessage( "数据保存成功但模板文件保存失败, 请重试" );
                                    returnMsg.setEnMessage( "Data saved success but template file save failed, please try again." );
                                }
                                else
                                {
                                    tmpTempFile = null;
                                }
                            }
                            else
                            {
                                tmpTempFile.delete();
                            }
                        }
                        else
                        {
                            returnMsg.setSuccess( false );
                            returnMsg.setMessage( Constants.MSG_OPERATE_FAILED_PREFIX + "首次创建时必须上传一个模板文件." );
                            returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_PREFIX_EN + "when first created must upload a template file." );
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
                if( tmpTempFile != null && tmpTempFile.exists() )
                    tmpTempFile.delete();
            }
        }
        
        String responseMsg = "";
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html; charset=utf-8"); 
        if( ucontext != null && StringUtils.checkEquals( Constants.LANG_TYPE_EN, ucontext.getLangType(), false ) )
            responseMsg = returnMsg.getEnMessage().replaceAll( "'", " " ).replaceAll( "\"", " " ).replaceAll( "\n", " " );
        else
            responseMsg = returnMsg.getMessage().replaceAll( "'", " " ).replaceAll( "\"", " " ).replaceAll( "\n", " " );
        if( returnMsg.isSuccess() )
        {
            response.getWriter().write( "{\"success\":\"true\", \"message\":\"" + responseMsg + "\"}" );
        }
        else
        {
            response.getWriter().write( "{\"success\":\"flase\", \"message\":\"" + responseMsg + "\"}" );
        }
        response.getWriter().flush();
        return NONE;
    }
    
    public String save() throws Exception
    { 
        UserContext ucontext = this.findUserContext();
        AbstractBaseModel baseModel = this.checkDataBySave( ucontext, ConfigDataTemp.class, form );
        if( ucontext != null && baseModel != null )
        {
            File tmpTempFile = null;
            try
            {
                if( dataContent != null && dataContent.length() > 5 )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_OPERATE_FAILED );
                    returnMsg.setEnMessage( Constants.MSG_OPERATE_FAILED_EN );
                    ConfigDataTemp dataObj = (ConfigDataTemp)baseModel;
                    tmpTempFile = configDataTempService.buildAndUpdateTmpTempFile( ucontext, dataObj, dataContent );
                    if( tmpTempFile != null && tmpTempFile.exists() && tmpTempFile.isFile() )
                    {
                        if( dataObj.getTypeCode() != null )
                        {
                            if( dataObj.getTypeCode().equals( SystemConstants.INACTIVE_MODE_CODE_SMS ) )
                                form.setTempContent( dataContent );      
                        }
                        
                        if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                        {
                            if( StringUtils.checkNotEmpty( dataObj.getItemCode() ) )
                                form.setItemCode( dataObj.getItemCode() );
                            if( StringUtils.checkNotEmpty( dataObj.getBizCode() ) )
                                form.setBizCode( dataObj.getBizCode() );
                            form.setTypeCode( dataObj.getTypeCode() );
                            form.setUuidCode( dataObj.getUuidCode() );
                            form.setTempMainPath( dataObj.getTempMainPath() );
                            form.setTempRelaPath( dataObj.getTempRelaPath() );
                            form.setTempFileName( dataObj.getTempFileName() );
                            BeanUtils.copyProperties( dataObj, form );
                            returnMsg = configDataTempService.update( ucontext, dataObj, null );
                            if( returnMsg.isSuccess() )
                            {
                                this.registerLog( "数据模板["+dataObj.getItemName()+"]修改成功", "Data template["+dataObj.getItemName()+"] update success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DATA_TEMP, OperateTypeEnum.UPDATE, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                                try {
                                    dataDetectionProcess.updateNotify( ucontext, ConfigDataTemp.class.getSimpleName(), "configDataTempService" );
                                }
                                catch( Exception e ){
                                }
                                
                                if( !FileUtils.moveFile( tmpTempFile, Constants.APP_EXFILE_PATH+dataObj.getTempRelaPath(), dataObj.getUuidCode()+".ftl" ) )
                                {
                                    log.error( "save moveFile(...) failed, temp name: " + dataObj.getItemName() + ", file path: " + tmpTempFile.getAbsolutePath() );
                                    returnMsg.setSuccess( false );
                                    returnMsg.setMessage( "数据保存成功但模板文件保存失败, 请重试" );
                                    returnMsg.setEnMessage( "Data saved success but template file save failed, please try again." );
                                }
                                else
                                {
                                    tmpTempFile = null;
                                }
                            }
                            else
                            {
                                if( tmpTempFile != null && tmpTempFile.exists() )
                                    tmpTempFile.delete();
                            }
                        }
                        else
                        {
                            if( StringUtils.checkIsEmpty( dataObj.getUuidCode() ) )
                                dataObj.setUuidCode( DataOperateUtils.buildUuidCodeStr( true ) );
                            returnMsg = configDataTempService.insert( ucontext, dataObj, null );
                            if( returnMsg.isSuccess() )
                            {
                                this.registerLog( "数据模板["+dataObj.getItemName()+"]新增成功", "Data template["+dataObj.getItemName()+"] create success", LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DATA_TEMP, OperateTypeEnum.INSERT, OperateFlagEnum.SUCCESS, 1, "id: "+dataObj.getItemId()+", name: "+dataObj.getItemName() );
                                try {
                                    dataDetectionProcess.updateNotify( ucontext, ConfigDataTemp.class.getSimpleName(), "configDataTempService" );
                                }
                                catch( Exception e ){
                                }
                                
                                if( !FileUtils.moveFile( tmpTempFile, Constants.APP_EXFILE_PATH+dataObj.getTempRelaPath(), dataObj.getUuidCode()+".ftl" ) )
                                {
                                    log.error( "save moveFile(...) failed, temp name: " + dataObj.getItemName() + ", file path: " + tmpTempFile.getAbsolutePath() );
                                    returnMsg.setSuccess( false );
                                    returnMsg.setMessage( "数据保存成功但模板文件保存失败, 请重试" );
                                    returnMsg.setEnMessage( "Data saved success but template file save failed, please try again." );
                                }
                                else
                                {
                                    tmpTempFile = null;
                                }
                            }
                            else
                            {
                                if( tmpTempFile != null && tmpTempFile.exists() )
                                    tmpTempFile.delete();
                            }
                        }
                    }
                    else
                    {
                        log.error( "save() error, temporary template file creation failed. " );
                    }
                }
                else
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( "模板内容不能为空." );
                    returnMsg.setEnMessage( "The template content can not be empty." );
                }
            }
            catch( Exception err )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_OPERATE_ERROR_PREFIX + err.getMessage() );
                returnMsg.setEnMessage( Constants.MSG_OPERATE_ERROR_PREFIX_EN + err.getMessage() );
                log.error( "save() error, " + err.toString() + ", " + form.toString() );
                try
                {
                    if( tmpTempFile != null && tmpTempFile.exists() )
                        tmpTempFile.delete();
                }
                catch( Exception e )
                {
                }
            }
        }
        
        return SUCCESS;
    }
    
    public String delete() throws Exception
    {
        this.checkUserOperatePower( ConfigDataTempAction.class, "delete" );
        if( returnMsg.isSuccess() )
        {
            try
            {
                UserContext ucontext = this.findUserContext();
                returnMsg = configDataTempService.deleteBatch( ucontext, selIdStr, null );
                if( returnMsg.isSuccess() )
                {
                    try {
                        dataDetectionProcess.updateNotify( ucontext, ConfigDataTemp.class.getSimpleName(), "configDataTempService" );
                    }
                    catch( Exception e ){
                    }
                    this.registerLog( "数据模板删除成功, 数量: "+returnMsg.getResultCount(), "Data template delete success, total: "+returnMsg.getResultCount(), LogTypeEnum.OPERATE, LogLevelEnum.INFO, ConfigConstants.MODULE_NAME_DATA_TEMP, OperateTypeEnum.DELETE, OperateFlagEnum.SUCCESS, returnMsg.getResultCount(), "delete id: "+selIdStr+"  detail: "+returnMsg.getResultDesc() );
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
    
    @SuppressWarnings("unchecked")
    private void matchObjRelateData( UserContext ucontext, List dataList )
    {
        if( dataList != null && dataList.size() > 0 )
        {
            for( int i=0; i<dataList.size(); i++ )
            {
                ConfigDataTemp dataObj = (ConfigDataTemp)dataList.get(i);
                this.matchObjRelateData( ucontext, dataObj );
            }
        }
    }
    
    private void matchObjRelateData( UserContext ucontext, ConfigDataTemp dataObj )
    {
        if( dataObj != null )
        {
            
        }
    }
    
    public ConfigDataTempForm getModel()
    {
        return form;
    }
    public void setConfigDataTempService(IConfigDataTempService configDataTempService)
    {
        this.configDataTempService = configDataTempService;
    }
    public void setConfigDictDetailService(ICacheService configDictDetailService)
    {
        this.configDictDetailService = configDictDetailService;
    }
    public void setDataDetectionProcess(IDataDetectionProcess dataDetectionProcess)
    {
        this.dataDetectionProcess = dataDetectionProcess;
    }
    public String getDataContent()
    {
        return dataContent;
    }
    public void setDataContent(String dataContent)
    {
        this.dataContent = dataContent;
    }
    public File getUpload01()
    {
        return upload01;
    }
    public void setUpload01(File upload01)
    {
        this.upload01 = upload01;
    }
    public String getUpload01FileName()
    {
        return upload01FileName;
    }
    public void setUpload01FileName(String upload01FileName)
    {
        this.upload01FileName = upload01FileName;
    }
    
}
