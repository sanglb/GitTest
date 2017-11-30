package com.lidennis.plat.config.web.form;
import com.lidennis.plat.config.model.ConfigDictDetail;

public class ConfigDictDetailForm extends ConfigDictDetail
{
    private static final long serialVersionUID = 4570293804551391822L;

    public String toString()
    {
        StringBuffer str = new StringBuffer("");
        str.append( "Form [id=" ).append( getItemId() );
        str.append( ", code=" ).append( getItemCode() );
        str.append( ", name=" ).append( getItemName() );
        str.append( ", enabled=" ).append( getEnableFlag() );
        str.append( ", typeId=" ).append( this.getDictTypeId() );
        str.append( ", typeCode=" ).append( this.getDictTypeCode() );
        str.append( "]" );
        return str.toString();
    }
}
