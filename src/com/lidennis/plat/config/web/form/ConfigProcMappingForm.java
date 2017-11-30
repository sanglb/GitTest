package com.lidennis.plat.config.web.form;
import com.lidennis.plat.config.model.ConfigProcMapping;

public class ConfigProcMappingForm extends ConfigProcMapping
{
    private static final long serialVersionUID = -1402853979730629751L;

    public String toString()
    {
        StringBuffer str = new StringBuffer("");
        str.append( "Form [id=" ).append( getItemId() );
        str.append( ", code=" ).append( getItemCode() );
        str.append( ", name=" ).append( getItemName() );
        str.append( ", enabled=" ).append( getEnableFlag() );
        str.append( "]" );
        return str.toString();
    }
}
