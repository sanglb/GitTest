package com.lidennis.plat.config.service.impl;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.lidennis.plat.common.constant.Constants;
import com.lidennis.plat.common.model.OrderByModel;
import com.lidennis.plat.common.model.ReturnMsg;
import com.lidennis.plat.common.model.UserContext;
import com.lidennis.plat.common.service.AbstractBaseService;
import com.lidennis.plat.common.util.DataOperateUtils;
import com.lidennis.plat.common.util.DatetimeUtils;
import com.lidennis.plat.common.util.StringUtils;
import com.lidennis.plat.config.model.ConfigDataTemp;
import com.lidennis.plat.config.service.IConfigDataTempService;
import com.lidennis.plat.system.constant.SystemConstants;

public class ConfigDataTempServiceImpl extends AbstractBaseService implements IConfigDataTempService
{
    private static final Logger log = Logger.getLogger( ConfigDataTempServiceImpl.class );
    protected Map<String,Object> globalAppConfigBaseMap;
    
    @Override
    public File buildAndUpdateTmpTempFile( UserContext ucontext, ConfigDataTemp dataObj, String dataContent )
    {
        File tmpTempFile = null;       
        if( ucontext != null && dataObj != null && dataContent != null )
        {
            BufferedOutputStream bouts = null;
            try
            {
                dataObj.setCharEncode( Constants.CHAR_ENCODEING_UTF_8 );
                if( StringUtils.checkIsEmpty( dataObj.getUuidCode() ) )
                    dataObj.setUuidCode( DataOperateUtils.buildUuidCodeStr( true ) );
                dataObj.setTempFileName( dataObj.getUuidCode() );
                String tempFullPath = "";
                String tempRelaPath = dataObj.getTempRelaPath();
                if( tempRelaPath == null || tempRelaPath.trim().equals("") )
                {
                    if( StringUtils.checkNotEmpty( dataObj.getTypeCode() ) )
                        tempRelaPath = "/data_template/" + dataObj.getTypeCode().trim() + "/" + DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis(), "yyyyMM" ) + "/"; 
                    else
                        tempRelaPath = "/data_template/" + DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis(), "yyyyMM" ) + "/"; 
                    dataObj.setTempMainPath( "" );
                    dataObj.setTempRelaPath( tempRelaPath );   
                }
                tempFullPath = Constants.APP_EXFILE_PATH  + tempRelaPath;
                tempFullPath = tempFullPath.replaceAll( "//", "/" );
                File dirObj = new File( tempFullPath );
                if( !dirObj.exists() || !dirObj.isDirectory() )
                {
                    dirObj.mkdirs();
                    log.info( "buildAndUpdateTempFile(), create template dir: "+ tempFullPath );
                }
                
                tmpTempFile = new File( tempFullPath, dataObj.getUuidCode() + "_TMP.ftl" );    
                bouts = new BufferedOutputStream( new FileOutputStream( tmpTempFile ) );
                bouts.write( dataContent.getBytes( Constants.CHAR_ENCODEING_UTF_8 ) );
                bouts.flush();
                bouts.close();
            }
            catch( Exception err )
            {
                log.error( "buildAndUpdateTempFile error, " + err.getMessage() );
                try
                {
                    if( bouts != null )
                        bouts.close();
                }
                catch( Exception e )
                {
                }
            }
        }
        
        return tmpTempFile;
    }
    
    @Override
    public File buildAndUpdateTmpTempFile( UserContext ucontext, ConfigDataTemp dataObj, File uploadFile, String uploadFileName )
    {
        File tmpTempFile = null;       
        if( ucontext != null && dataObj != null && uploadFile != null && uploadFile.exists() )
        {
            FileOutputStream fout = null;
            FileInputStream  fin  = null;
            try
            {
                if( StringUtils.checkIsEmpty( dataObj.getUuidCode() ) )
                    dataObj.setUuidCode( DataOperateUtils.buildUuidCodeStr( true ) ); 
                if( StringUtils.checkNotEmpty( uploadFileName ) )
                    dataObj.setTempFileName( uploadFileName );
                else
                    dataObj.setTempFileName( uploadFile.getName() );
                String tempFullPath = "";
                String tempRelaPath = dataObj.getTempRelaPath();
                if( tempRelaPath == null || tempRelaPath.trim().equals("") )
                {
                    if( StringUtils.checkNotEmpty( dataObj.getTypeCode() ) )
                        tempRelaPath = "/data_template/" + dataObj.getTypeCode().trim() + "/" + DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis(), "yyyyMM" ) + "/"; 
                    else
                        tempRelaPath = "/data_template/" + DatetimeUtils.convertToDatetimeStr( System.currentTimeMillis(), "yyyyMM" ) + "/"; 
                    dataObj.setTempMainPath( "" );
                    dataObj.setTempRelaPath( tempRelaPath );   
                }
                tempFullPath = Constants.APP_EXFILE_PATH  + tempRelaPath;
                tempFullPath = tempFullPath.replaceAll( "//", "/" );
                File dirObj = new File( tempFullPath );
                if( !dirObj.exists() || !dirObj.isDirectory() )
                {
                    dirObj.mkdirs();
                    log.info( "buildAndUpdateTempFile(), create template dir: "+ tempFullPath );
                }
                
                tmpTempFile = new File( tempFullPath, dataObj.getUuidCode() + "_TMP.ftl" );   
                fout = new FileOutputStream( tmpTempFile ); 
                fin = new FileInputStream( uploadFile );
                byte[] buffer = new byte[4096];
                int len = 0;       
                while((len = fin.read(buffer)) > 0)
                {
                    fout.write(buffer,0,len);
                }
                fin.close();
                fout.close();
            }
            catch( Exception err )
            {
                log.error( "buildAndUpdateTempFile error, " + err.getMessage() );
                try
                {
                    if( fin != null )
                        fin.close();
                    if( fout != null )
                        fout.close();
                }
                catch( Exception e )
                {
                }
            }
        }
        
        return tmpTempFile;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void reloadCacheData()
    {
        if( globalAppConfigBaseMap != null )
        {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            OrderByModel ordering = new OrderByModel( "ORDERNUM" );
            List<ConfigDataTemp> resultList = baseIbatisDao.findByParams( ConfigDataTemp.class.getSimpleName(), paramMap, ordering );
            synchronized( globalAppConfigBaseMap )
            {
                if( globalAppConfigBaseMap.containsKey( ConfigDataTemp.class.getSimpleName() ) )
                    globalAppConfigBaseMap.remove( ConfigDataTemp.class.getSimpleName() );
                Map<String,Object> dataMap = new ConcurrentHashMap<String,Object>();
                if( resultList != null && resultList.size() > 0 )
                {
                    for( int i=0; i<resultList.size(); i++ )
                    {
                        ConfigDataTemp dataObj = resultList.get(i);
                        if( dataObj.getCzId() != null && dataObj.getCzId().longValue() > 0 )
                        {
                            dataMap.put( dataObj.getCzId() + "_" + dataObj.getItemId().toString(), dataObj );
                        }
                        else
                        {
                            dataMap.put( dataObj.getItemId().toString(), dataObj );
                            dataMap.put( dataObj.getUuidCode(), dataObj );
                        }
                    }
                    dataMap.put( "all_result_data_list", resultList );
                }
                
                globalAppConfigBaseMap.put( ConfigDataTemp.class.getSimpleName(), dataMap );
                if( SystemConstants.LOG_SWITCH_PROC_DEBUG_ON && SystemConstants.LOG_SWITCH_PACKAGE_INFO.length() >= 15 && SystemConstants.LOG_SWITCH_PACKAGE_INFO.indexOf( this.getClass().getPackage().getName() ) >= 0 )
                    log.info( "--> load data template config to cache, total: " + dataMap.size() );
            }
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheById( UserContext ucontext, Long itemId )
    {
        ConfigDataTemp dataObj = null;
        if( globalAppConfigBaseMap.containsKey( ConfigDataTemp.class.getSimpleName() ) )
        {
            Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( ConfigDataTemp.class.getSimpleName() );
            if( ucontext != null && itemId != null )
            {
                if( ucontext.getUserAcct() != null && ucontext.getUserAcct().equals(Constants.COMMON_MODEL_CODE_SENIOR) )
                {
                    if( dataMap.containsKey( itemId.toString() ) )
                    {
                        dataObj = (ConfigDataTemp)dataMap.get( itemId.toString() );
                    }
                    else
                    {
                        
                    }
                }
                else if( ucontext.getCzCode() != null && ucontext.getCzCode().equals(Constants.COMMON_CODE_SYSTEM) )
                {
                    if( dataMap.containsKey( itemId.toString() ) )
                    {
                        dataObj = (ConfigDataTemp)dataMap.get( itemId.toString() );
                    }
                    else
                    {
                        if( dataMap.containsKey( ucontext.getCzId()+"_"+itemId.toString() ) )
                            dataObj = (ConfigDataTemp)dataMap.get( ucontext.getCzId()+"_"+itemId.toString() );
                    }
                }
                else
                {
                    if( ucontext.getCzId() != null && ucontext.getCzId().longValue() > 0 )
                    {
                        if( dataMap.containsKey( ucontext.getCzId() + "_" + itemId.toString() ) )
                            dataObj = (ConfigDataTemp)dataMap.get( ucontext.getCzId()+"_"+itemId.toString() );
                    }
                }
            }
        }
        
        if( dataObj != null )
            return (T)dataObj;
        else
            return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findCacheByCode( UserContext ucontext, String itemCode )
    {
        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findCacheListByParams( UserContext ucontext, Map<String, Object> paramMap )
    {
        List resultList = new ArrayList();
        if( ucontext != null && ucontext.getCzCode() != null && !ucontext.getCzCode().equals("") )
        {
            if( globalAppConfigBaseMap.containsKey( ConfigDataTemp.class.getSimpleName() ) )
            {
                Map<String,Object> dataMap = (Map<String,Object>)globalAppConfigBaseMap.get( ConfigDataTemp.class.getSimpleName() );
                String keyStr = "all_result_data_list";
                resultList = (List)dataMap.get( keyStr ); 
            }
        }
        return resultList;
    }
    
    @Override
    protected <T> ReturnMsg checkBySave( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkBySave( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ConfigDataTemp dataObj = (ConfigDataTemp)model;
            if( dataObj.getItemName() == null || dataObj.getItemName().trim().equals("") )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( Constants.MSG_NAME_NO_EMPTY );
                returnMsg.setEnMessage( Constants.MSG_NAME_NO_EMPTY_EN );
                return returnMsg;
            }
            else
            {
                dataObj.setItemName( dataObj.getItemName().trim() );
            }
            
            if( dataObj.getTypeCode() == null || dataObj.getTypeCode().trim().equals("") )
            {
                returnMsg.setSuccess( false );
                returnMsg.setMessage( "类型不能为空" );
                returnMsg.setEnMessage( "the type can not be empty." );
                return returnMsg;
            }
            
            if( StringUtils.checkNotEmpty( dataObj.getBizCode() ) )
            {
                Map<String,Object> pmap = new HashMap<String,Object>();
                pmap.put( "bizCode", dataObj.getBizCode().trim() );
                if( dataObj.getItemId() != null && dataObj.getItemId().longValue() > 0 )
                    pmap.put( "itemId_notexist", dataObj.getItemId() );
                if( !StringUtils.checkEquals( ucontext.getUserAcct(), Constants.COMMON_MODEL_CODE_SENIOR ) )
                    pmap.put( Constants.COMMON_MODEL_CODE_CZ_ID, ucontext.getCzId() );
                int num = baseIbatisDao.findCountByParams( ConfigDataTemp.class.getSimpleName(), pmap );
                if( num > 0 )
                {
                    returnMsg.setSuccess( false );
                    returnMsg.setMessage( Constants.MSG_CODE_ALREADY_EXIST );
                    returnMsg.setEnMessage( Constants.MSG_CODE_ALREADY_EXIST_EN );
                    return returnMsg;
                }
            }
        }
        
        return returnMsg;
    }
    
    @Override
    protected <T> ReturnMsg checkByDelete( UserContext ucontext, final T model, Map<String, Object> exparamMap ) throws Exception
    {
        ReturnMsg returnMsg = super.checkByDelete( ucontext, model, exparamMap );
        if( returnMsg.isSuccess() )
        {
            ConfigDataTemp dataObj = (ConfigDataTemp)model;
            Map<String,Object> params01Map = new HashMap<String,Object>();
            params01Map.put( "relateTempId", dataObj.getItemId() );
        }
        
        return returnMsg;
    }

    public void setGlobalAppConfigBaseMap(Map<String, Object> globalAppConfigBaseMap)
    {
        this.globalAppConfigBaseMap = globalAppConfigBaseMap;
    }
}
